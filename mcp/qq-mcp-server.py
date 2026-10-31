#!/usr/bin/env python3
"""QQ MCP Server — 纯标准库实现，零第三方依赖（除 websockets）。

WS 连接 NapCat OneBot (127.0.0.1:3001)
HTTP 暴露 MCP JSON-RPC (双栈 8899,可选 token 鉴权)
"""

import asyncio
import json
import os
import socket
import threading
import time
import uuid
import urllib.request
from collections import deque
from http.server import HTTPServer, BaseHTTPRequestHandler

import websockets

NAPCAT_WS = "ws://127.0.0.1:3001"
MCP_HOST = os.environ.get("MCP_HOST", "::")   # 双栈监听(密决:域名仅 AAAA)
MCP_PORT = int(os.environ.get("MCP_PORT", "8899"))
MCP_TOKEN = os.environ.get("MCP_TOKEN", "")   # 非空时要求 X-MCP-Token 头或 ?token=
WEBUI_BASE = "http://127.0.0.1:6099"
PLUGIN_KEY = "x524980520"
MSG_BUF = deque(maxlen=200)

_loop = None       # WS 线程的事件循环
_ws = None         # WS 连接
_self_id = ""
_pending = {}      # echo → Future
_ready = threading.Event()


def _extract_text(message):
    if isinstance(message, str):
        return message
    return "".join(
        seg.get("data", {}).get("text", "")
        for seg in message if seg.get("type") == "text"
    )


# ── WS 线程 ──────────────────────────────────────────────────

def _ws_thread():
    global _loop, _ws, _self_id
    loop = asyncio.new_event_loop()
    _loop = loop
    asyncio.set_event_loop(loop)

    async def _run():
        global _ws, _self_id
        while True:
            try:
                async with websockets.connect(NAPCAT_WS) as sock:
                    _ws = sock
                    _ready.set()
                    print(f"[WS] connected", flush=True)
                    async for raw in sock:
                        try:
                            data = json.loads(raw)
                        except json.JSONDecodeError:
                            continue
                        echo = data.get("echo")
                        if echo and echo in _pending:
                            fut = _pending.pop(echo)
                            if not fut.done():
                                fut.set_result(data)
                            continue
                        pt = data.get("post_type", "")
                        if pt in ("message", "message_sent"):
                            MSG_BUF.append({
                                "time": data.get("time", 0),
                                "type": data.get("message_type", ""),
                                "user_id": data.get("sender", {}).get("user_id", 0),
                                "nickname": data.get("sender", {}).get("nickname", ""),
                                "group_id": data.get("group_id"),
                                "text": _extract_text(data.get("message", [])),
                            })
                        elif pt in ("notice", "request"):
                            EVENT_BUF.append({"time": data.get("time", 0), "type": pt})
                        sid = data.get("self_id")
                        if sid:
                            _self_id = str(sid)
            except Exception as exc:
                print(f"[WS] error: {exc}, retry in 5s", flush=True)
                _ws = None
                await asyncio.sleep(5)

    loop.run_until_complete(_run())


def _call_action(action, params, timeout=10):
    """从 HTTP 线程线程安全地向 WS 事件循环投递 OneBot action。"""
    if _loop is None or _ws is None:
        raise RuntimeError("WS not connected")
    echo = f"mcp-{uuid.uuid4().hex[:12]}"

    async def _do():
        fut = asyncio.get_event_loop().create_future()
        _pending[echo] = fut
        await _ws.send(json.dumps({"action": action, "params": params, "echo": echo}))
        return await asyncio.wait_for(fut, timeout=timeout)

    future = asyncio.run_coroutine_threadsafe(_do(), _loop)
    result = future.result(timeout=timeout + 2)
    if result.get("status") != "ok":
        raise RuntimeError(f"OneBot '{action}' failed: {result.get('message', '')}")
    return result.get("data", {})


def _plugin_api(path, method="GET", body=None):
    """调用 WebUI 插件 NoAuth API（account / autoreply，key 与 WebUI token 相同）。"""
    sep = "&" if "?" in path else "?"
    url = f"{WEBUI_BASE}{path}{sep}key={PLUGIN_KEY}"
    data = json.dumps(body, ensure_ascii=False).encode("utf-8") if body is not None else None
    req = urllib.request.Request(url, data=data, method=method,
                                 headers={"Content-Type": "application/json"})
    with urllib.request.urlopen(req, timeout=10) as resp:
        return json.loads(resp.read().decode("utf-8"))


def _parse_rules_text(keywords_text):
    """多行文本转规则数组，格式与插件 textToKeywords 一致：匹配词|类型|回复。
    类型 prefix/contains（缺省 contains），回复中可含 |，坏行跳过。
    POST /config 是浅合并且 keywords 数组为规则唯一权威，必须整组回写。
    """
    rules, skipped = [], []
    for line in keywords_text.splitlines():
        line = line.strip()
        if not line:
            continue
        parts = line.split("|")
        match = (parts[0] or "").strip()
        match_type = "prefix" if (parts[1].strip() if len(parts) > 1 else "") == "prefix" else "contains"
        reply = "|".join(parts[2:]).strip()
        if not match or not reply:
            skipped.append(line)
            continue
        rules.append({"match": match, "matchType": match_type, "reply": reply})
    return rules, skipped


# ── MCP 工具调度 ──────────────────────────────────────────────

def _dispatch(name, args):
    if name == "qq_send_private_msg":
        return _call_action("send_private_msg", {"user_id": args["user_id"], "message": [{"type": "text", "data": {"text": args["message"]}}]})
    if name == "qq_send_group_msg":
        return _call_action("send_group_msg", {"group_id": args["group_id"], "message": [{"type": "text", "data": {"text": args["message"]}}]})
    if name == "qq_get_group_list":
        return _call_action("get_group_list", {})
    if name == "qq_get_group_members":
        return _call_action("get_group_member_list", {"group_id": args["group_id"]})
    if name == "qq_get_group_info":
        return _call_action("get_group_info", {"group_id": args["group_id"]})
    if name == "qq_set_group_ban":
        return _call_action("set_group_ban", {"group_id": args["group_id"], "user_id": args["user_id"], "duration": args.get("duration", 600)})
    if name == "qq_set_group_whole_ban":
        return _call_action("set_group_whole_ban", {"group_id": args["group_id"], "enable": args.get("enable", True)})
    if name == "qq_set_group_kick":
        return _call_action("set_group_kick", {"group_id": args["group_id"], "user_id": args["user_id"], "reject_add_request": False})
    if name == "qq_set_group_card":
        return _call_action("set_group_card", {"group_id": args["group_id"], "user_id": args["user_id"], "card": args.get("card", "")})
    if name == "qq_set_group_name":
        return _call_action("set_group_name", {"group_id": args["group_id"], "name": args.get("name", "")})
    if name == "qq_get_login_info":
        return _call_action("get_login_info", {})
    if name == "qq_get_friend_list":
        return _call_action("get_friend_list", {})
    if name == "qq_get_recent_messages":
        count = min(args.get("count", 20), 200)
        gid = args.get("group_id", 0)
        msgs = list(MSG_BUF)
        if gid:
            msgs = [m for m in msgs if m.get("group_id") == gid]
        return msgs[-count:]
    # ── 插件扩展工具（account / autoreply 插件 API）──
    if name == "qq_list_accounts":
        return _plugin_api("/plugin/napcat-plugin-account/api/status")
    if name == "qq_switch_account":
        return _plugin_api("/plugin/napcat-plugin-account/api/switch", "POST",
                           {"uin": str(args["uin"])})
    if name == "qq_get_rules":
        return _plugin_api("/plugin/napcat-plugin-autoreply/api/config")
    if name == "qq_set_rules":
        rules, skipped = _parse_rules_text(args["keywordsText"])
        result = _plugin_api("/plugin/napcat-plugin-autoreply/api/config", "POST",
                             {"keywords": rules, "keywordsText": args["keywordsText"]})
        result["applied"] = {"count": len(rules), "skipped": skipped}
        return result
    if name == "qq_groups":
        return _plugin_api("/plugin/napcat-plugin-autoreply/api/groups")
    # ── 多实例管理（account 插件）──
    if name == "qq_instances":
        return _plugin_api("/plugin/napcat-plugin-account/api/instances")
    if name == "qq_instance_add":
        return _plugin_api("/plugin/napcat-plugin-account/api/instance-add", "POST", {})
    if name == "qq_instance_start":
        return _plugin_api("/plugin/napcat-plugin-account/api/instance-start", "POST", {"name": str(args["name"])})
    if name == "qq_instance_stop":
        return _plugin_api("/plugin/napcat-plugin-account/api/instance-stop", "POST", {"name": str(args["name"])})
    if name == "qq_instance_delete":
        return _plugin_api("/plugin/napcat-plugin-account/api/instance-delete", "POST", {"name": str(args["name"])})
    if name == "qq_instance_qr":
        return _plugin_api("/plugin/napcat-plugin-account/api/instance-qr", "POST", {"name": str(args["name"])})
    raise ValueError(f"Unknown tool: {name}")


MCP_TOOLS = [
    {"name": "qq_send_private_msg", "description": "发送私聊消息", "inputSchema": {"type": "object", "properties": {"user_id": {"type": "integer"}, "message": {"type": "string"}}, "required": ["user_id", "message"]}},
    {"name": "qq_send_group_msg", "description": "发送群聊消息", "inputSchema": {"type": "object", "properties": {"group_id": {"type": "integer"}, "message": {"type": "string"}}, "required": ["group_id", "message"]}},
    {"name": "qq_get_recent_messages", "description": "获取最近收到的消息", "inputSchema": {"type": "object", "properties": {"count": {"type": "integer"}, "group_id": {"type": "integer"}}, "required": []}},
    {"name": "qq_get_group_list", "description": "获取群列表", "inputSchema": {"type": "object", "properties": {}, "required": []}},
    {"name": "qq_get_group_members", "description": "获取群成员列表", "inputSchema": {"type": "object", "properties": {"group_id": {"type": "integer"}}, "required": ["group_id"]}},
    {"name": "qq_get_group_info", "description": "获取群信息", "inputSchema": {"type": "object", "properties": {"group_id": {"type": "integer"}}, "required": ["group_id"]}},
    {"name": "qq_set_group_ban", "description": "禁言群成员", "inputSchema": {"type": "object", "properties": {"group_id": {"type": "integer"}, "user_id": {"type": "integer"}, "duration": {"type": "integer"}}, "required": ["group_id", "user_id"]}},
    {"name": "qq_set_group_whole_ban", "description": "全员禁言", "inputSchema": {"type": "object", "properties": {"group_id": {"type": "integer"}, "enable": {"type": "boolean"}}, "required": ["group_id", "enable"]}},
    {"name": "qq_set_group_kick", "description": "踢人", "inputSchema": {"type": "object", "properties": {"group_id": {"type": "integer"}, "user_id": {"type": "integer"}}, "required": ["group_id", "user_id"]}},
    {"name": "qq_set_group_card", "description": "设置群名片", "inputSchema": {"type": "object", "properties": {"group_id": {"type": "integer"}, "user_id": {"type": "integer"}, "card": {"type": "string"}}, "required": ["group_id", "user_id", "card"]}},
    {"name": "qq_set_group_name", "description": "修改群名", "inputSchema": {"type": "object", "properties": {"group_id": {"type": "integer"}, "name": {"type": "string"}}, "required": ["group_id", "name"]}},
    {"name": "qq_get_login_info", "description": "获取登录状态", "inputSchema": {"type": "object", "properties": {}, "required": []}},
    {"name": "qq_get_friend_list", "description": "获取好友列表", "inputSchema": {"type": "object", "properties": {}, "required": []}},
    {"name": "qq_list_accounts", "description": "获取登录状态、当前账号、历史账号与自动登录配置", "inputSchema": {"type": "object", "properties": {}, "required": []}},
    {"name": "qq_switch_account", "description": "切换 QQ 账号（会重启 NapCat 实例，几分钟后才恢复在线）", "inputSchema": {"type": "object", "properties": {"uin": {"type": "integer", "description": "要切换到的 QQ 号"}}, "required": ["uin"]}},
    {"name": "qq_get_rules", "description": "获取自动回复插件配置（关键词规则、私聊/群聊开关、黑白名单）", "inputSchema": {"type": "object", "properties": {}, "required": []}},
    {"name": "qq_set_rules", "description": "设置自动回复关键词规则，每行一条：匹配词|类型|回复（类型 prefix=前缀匹配 / contains=包含匹配，可省略，回复中可含 |）", "inputSchema": {"type": "object", "properties": {"keywordsText": {"type": "string"}}, "required": ["keywordsText"]}},
    {"name": "qq_groups", "description": "获取群列表及各群自动回复开关状态", "inputSchema": {"type": "object", "properties": {}, "required": []}},
    {"name": "qq_instances", "description": "列出全部 QQ 实例(主实例+多账号实例):绑定账号/昵称/运行状态/登录状态/端口", "inputSchema": {"type": "object", "properties": {}, "required": []}},
    {"name": "qq_instance_add", "description": "创建新的 QQ 实例(独立数据与端口),创建后自动启动并等待扫码登录", "inputSchema": {"type": "object", "properties": {}, "required": []}},
    {"name": "qq_instance_start", "description": "启动指定 QQ 实例", "inputSchema": {"type": "object", "properties": {"name": {"type": "string", "description": "实例名,如 acc-6101"}}, "required": ["name"]}},
    {"name": "qq_instance_stop", "description": "停止指定 QQ 实例", "inputSchema": {"type": "object", "properties": {"name": {"type": "string"}}, "required": ["name"]}},
    {"name": "qq_instance_delete", "description": "删除指定 QQ 实例(不可恢复,会先停止进程再删除数据目录;主实例不可删)", "inputSchema": {"type": "object", "properties": {"name": {"type": "string"}}, "required": ["name"]}},
    {"name": "qq_instance_qr", "description": "手动刷新指定实例的登录二维码(会请求 QQ 接口,仅在需要扫码时调用)", "inputSchema": {"type": "object", "properties": {"name": {"type": "string"}}, "required": ["name"]}},
]


# ── HTTP 服务 ────────────────────────────────────────────────

class MCPHandler(BaseHTTPRequestHandler):
    def _authorized(self):
        if not MCP_TOKEN:
            return True
        if self.headers.get("X-MCP-Token", "") == MCP_TOKEN:
            return True
        if "token=" in self.path and self.path.split("token=", 1)[1].split("&")[0] == MCP_TOKEN:
            return True
        return False

    def do_POST(self):
        if not self._authorized():
            self._send_json(401, {"error": "unauthorized"})
            return
        if self.path.split("?")[0] != "/mcp":
            self.send_response(404)
            self.end_headers()
            return
        length = int(self.headers.get("Content-Length", 0))
        try:
            req = json.loads(self.rfile.read(length))
        except json.JSONDecodeError:
            self._send_json(400, {"error": "bad JSON"})
            return
        method = req.get("method", "")
        req_id = req.get("id")
        params = req.get("params", {})

        if method == "initialize":
            self._send_json(200, self._rpc(req_id, {"protocolVersion": "2025-03-26", "capabilities": {"tools": {}}, "serverInfo": {"name": "qq-bot", "version": "1.0"}}))
        elif method == "notifications/initialized":
            self._send_json(202, {})
        elif method == "tools/list":
            self._send_json(200, self._rpc(req_id, {"tools": MCP_TOOLS}))
        elif method == "tools/call":
            name = params.get("name", "")
            args = params.get("arguments", {})
            try:
                result = _dispatch(name, args)
                text = json.dumps(result, ensure_ascii=False) if not isinstance(result, str) else result
                self._send_json(200, self._rpc(req_id, {"content": [{"type": "text", "text": text}]}))
            except Exception as exc:
                self._send_json(200, self._rpc(req_id, {"content": [{"type": "text", "text": f"Error: {exc}"}], "isError": True}))
        else:
            self._send_json(200, self._rpc(req_id, {}))

    def _rpc(self, req_id, result):
        return {"jsonrpc": "2.0", "id": req_id, "result": result}

    def _send_json(self, code, body):
        raw = json.dumps(body, ensure_ascii=False).encode("utf-8")
        self.send_response(code)
        self.send_header("Content-Type", "application/json")
        self.send_header("Content-Length", str(len(raw)))
        self.end_headers()
        self.wfile.write(raw)

    def log_message(self, format, *args):
        pass


class MCPHTTPServer(HTTPServer):
    allow_reuse_address = True
    daemon_threads = True


if __name__ == "__main__":
    print("QQ MCP Server starting", flush=True)
    t = threading.Thread(target=_ws_thread, daemon=True)
    t.start()
    _ready.wait(timeout=10)
    if ":" in MCP_HOST:
        class MCPHTTPServer6(MCPHTTPServer):
            address_family = socket.AF_INET6
        server = MCPHTTPServer6((MCP_HOST, MCP_PORT), MCPHandler)
    else:
        server = MCPHTTPServer((MCP_HOST, MCP_PORT), MCPHandler)
    print(f"MCP HTTP listening on {MCP_HOST}:{MCP_PORT}", flush=True)
    server.serve_forever()

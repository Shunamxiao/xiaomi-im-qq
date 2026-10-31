#!/usr/bin/env python3
"""QQ MCP Server — 纯标准库实现，零第三方依赖（除 websockets）。

WS 连接 NapCat OneBot (127.0.0.1:3001)
HTTP 暴露 MCP JSON-RPC (0.0.0.0:8000)
"""

import asyncio
import json
import threading
import time
import uuid
from collections import deque
from http.server import HTTPServer, BaseHTTPRequestHandler

import websockets

NAPCAT_WS = "ws://127.0.0.1:3001"
MCP_HOST = "0.0.0.0"
MCP_PORT = 8000
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
]


# ── HTTP 服务 ────────────────────────────────────────────────

class MCPHandler(BaseHTTPRequestHandler):
    def do_POST(self):
        if self.path != "/mcp":
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
    server = MCPHTTPServer((MCP_HOST, MCP_PORT), MCPHandler)
    print(f"MCP HTTP listening on {MCP_HOST}:{MCP_PORT}", flush=True)
    server.serve_forever()

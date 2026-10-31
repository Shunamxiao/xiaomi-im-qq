# MCP Server 与 AI Agent 框架接入指南

> mi8 上的 QQ MCP Server 把 NapCat（OneBot 11）能力以 MCP 工具暴露，
> 供 OpenClaw、OMP、Claude Code 等任意 MCP 客户端操作 QQ。
> 本文档面向接入方与运维。日期：2026-10-31。

## 1. 服务概览与端点

- 实现：`mcp/qq-mcp-server.py`（本地仓库为主，mi8 为部署目标），纯 Python 标准库 + websockets，
  依赖仅 `pip install websockets`（requirements-mcp.txt）。
- 传输：MCP JSON-RPC over HTTP，无状态，单端点 `POST /mcp`（支持 `initialize` / `tools/list` / `tools/call`，
  协议版本 2025-03-26）。
- 内部依赖：启动时连接 NapCat OneBot WS `ws://127.0.0.1:3001`，并调用本机 WebUI `http://127.0.0.1:6099`
  的插件 API（account / autoreply），因此**必须部署在 mi8 本机**。
- 端点（外网经路由转发）：`http://mi.theworld.codes:8000/mcp`；
  mi8 本机/局域网：`http://127.0.0.1:8000/mcp` 或 `http://<mi8局域网IP>:8000/mcp`。

## 2. 监听合规性（重要，当前不合规）

mi8 对外服务的约定：**bind `::`（IPv6/双栈）+ 8800 系端口**（参考 nginx 8800：同时监听
`0.0.0.0:8800` 与 `:::8800`）。检查结果（2026-10-31 实测）：

```text
tcp  0.0.0.0:8000  LISTEN  python3  qq-mcp-server.py   ← 仅 IPv4，非 8800 系，不合规
tcp  0.0.0.0:8800  LISTEN  nginx（双栈，合规参照）
tcp6 :::8800      LISTEN  nginx
```

> 本次按约定**未改 mi8 运行态**，以下调整步骤留待下次部署时执行：

1. 改 `mcp/qq-mcp-server.py`：`MCP_HOST = "::"`、`MCP_PORT = 8810`（示例），
   并让 server 使用 IPv6 socket（python http.server 默认 AF_INET，直接传 "::" 会报 AddressFamily 错）：

   ```python
   import socket
   class MCPHTTPServer(HTTPServer):
       address_family = socket.AF_INET6
       allow_reuse_address = True
       daemon_threads = True
   ```

2. 如需从公网访问，同步路由器端口转发与防火墙（放行 TCP 8810）；
3. 重启进程（见第 6 节），`netstat -tlnp | grep 8810` 应看到 `:::8810`。

## 3. 工具清单（18 个）

### 消息（OneBot 透传）

| 工具 | 参数 | 用途 |
| --- | --- | --- |
| `qq_send_private_msg` | `user_id:int`, `message:str` | 发私聊消息 |
| `qq_send_group_msg` | `group_id:int`, `message:str` | 发群消息 |
| `qq_get_recent_messages` | `count?:int(≤200)`, `group_id?:int` | 取最近收到的消息（服务端内存缓存 200 条，重启即空） |

### 群信息与群管理（OneBot 透传）

| 工具 | 参数 | 用途 |
| --- | --- | --- |
| `qq_get_group_list` | — | 群列表 |
| `qq_get_group_members` | `group_id` | 群成员列表 |
| `qq_get_group_info` | `group_id` | 群信息 |
| `qq_set_group_ban` | `group_id`, `user_id`, `duration?:int(默认600秒)` | 禁言群成员 |
| `qq_set_group_whole_ban` | `group_id`, `enable:bool` | 全员禁言 |
| `qq_set_group_kick` | `group_id`, `user_id` | 踢人 |
| `qq_set_group_card` | `group_id`, `user_id`, `card:str` | 改群名片 |
| `qq_set_group_name` | `group_id`, `name:str` | 改群名 |

### 账号与配置（account / autoreply 插件，2026-10-31 新增）

| 工具 | 参数 | 用途 |
| --- | --- | --- |
| `qq_get_login_info` | — | OneBot 登录信息（当前账号） |
| `qq_get_friend_list` | — | 好友列表 |
| `qq_list_accounts` | — | 登录状态 + 当前账号 + 历史账号 + autoLoginAccount |
| `qq_switch_account` | `uin:int` | **切换账号（会重启 NapCat 实例，几分钟内所有工具不可用）** |
| `qq_get_rules` | — | 自动回复插件完整配置（关键词规则/私聊群聊开关/黑白名单/群配置） |
| `qq_set_rules` | `keywordsText:str` | 整体替换关键词规则，每行 `匹配词\|类型\|回复`（类型 prefix/contains，可省略；回复可含 `\|`；空行与坏行跳过） |
| `qq_groups` | — | 群列表 + 各群自动回复开关（member_count/enabled） |

注意：`qq_set_rules` 是**全量替换**关键词（对齐插件的规则数据模型），改规则前先 `qq_get_rules`
取回 `keywordsText`，在其基础上修改后整体回写，不要凭空生成。

## 4. Agent 框架接入配置示例

### OpenClaw（`mcp.servers`，支持 Streamable HTTP）

```json
{
  "mcp": {
    "servers": {
      "qq": {
        "type": "streamable-http",
        "url": "http://mi.theworld.codes:8000/mcp"
      }
    }
  }
}
```

### Claude Code / 通用 MCP 客户端（CLI 方式）

```bash
claude mcp add --transport http qq http://mi.theworld.codes:8000/mcp
```

### OMP 等：任意支持 "HTTP MCP / Streamable HTTP" 的框架

通用参数：URL = `http://mi.theworld.codes:8000/mcp`，方法 POST，
body 为标准 MCP JSON-RPC（`tools/list`、`tools/call`）。当前服务无鉴权头要求，
鉴权字段留空即可（建议尽快加，见第 5 节）。

手动验证：

```bash
curl -s http://mi.theworld.codes:8000/mcp -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":1,"method":"tools/list"}' | head -c 400
```

## 5. 安全状态与建议

- **当前状态：8000 端口无任何鉴权**。拿到地址即可发消息、踢人、改规则、切账号。
- 实测（2026-10-31）：从外网连接 `mi.theworld.codes:8000` 被拒绝——路由器只转发了
  6099 等已配置端口，8000 目前**公网不可达**，实际暴露面 = mi8 本机 + 局域网。
  这属于"侥幸安全"，不要依赖它：路由器配置一变即裸奔。
- 建议（按优先级）：
  1. 给 MCPHandler 加 token 校验：`tools/call` 检查 `Authorization: Bearer <token>`，
     不匹配直接 401；接入框架侧配置同 token（OpenClaw 支持 header 鉴权）；
  2. 或只走内网：不转发端口，Agent 框架部署在 mi8 本机或经 frp/内网隧道访问；
  3. 若必须公网，走 nginx 8800 反代并加 TLS + basic auth，不要直连 8000。

## 6. 部署与重启（mi8，未在本次执行）

本地仓库 `mcp/qq-mcp-server.py` 为唯一真源；部署 = 覆盖 + 重启进程：

```bash
# 1. 覆盖 mi8 上的 server 文件
scp mcp/qq-mcp-server.py mi8:/data/data/com.termux/files/home/qq-mcp/qq-mcp-server.py

# 2. 重启进程（root 常驻；首次部署先装依赖：
#    ssh mi8 "pip3 install -r /data/data/com.termux/files/home/qq-mcp/requirements-mcp.txt"）
ssh mi8 'pkill -f qq-mcp-server.py; sleep 1; \
  cd /data/data/com.termux/files/home/qq-mcp && \
  nohup python3 qq-mcp-server.py >> mcp-server.log 2>&1 &'

# 3. 验证（应看到 WS connected + listening，且 13+5=18 个工具可 tools/list）
ssh mi8 'tail -5 /data/data/com.termux/files/home/qq-mcp/mcp-server.log; netstat -tlnp | grep 8000'
```

> 状态备注：截至 2026-10-31，mi8 上运行的仍是旧版（13 工具）。
> 新版（+5 工具）需要按上面命令部署一次才生效。重启 MCP server 不影响 QQ 本体。

## 7. 常用任务配方

### 查状态

```text
qq_get_login_info          → 当前 OneBot 登录的账号
qq_list_accounts           → 登录阶段 / 当前账号 / 历史账号 / 自动登录配置
qq_groups                  → 在哪些群、各群自动回复开关
```

### 改自动回复规则

```text
1. qq_get_rules            → 拿到 keywordsText 原文
2. 在原文基础上增删改行（每行: 匹配词|类型|回复）
3. qq_set_rules(keywordsText=改后全文)
   → 返回 applied.count 成功条数与 applied.skipped 被跳过的坏行
```

### 切换账号

```text
1. qq_list_accounts        → 确认目标 uin 在 quickList（不在则需扫码添加，走 WebUI 管理页）
2. qq_switch_account(uin)  → 触发 NapCat 重启，本工具立即返回
3. 等待 1~3 分钟后 qq_get_login_info 验证新账号；期间所有 OneBot 工具报 "WS not connected" 属预期
```

### 发消息

```text
群聊: qq_get_group_list 找 group_id → qq_send_group_msg(group_id, message)
私聊: qq_get_friend_list 找 user_id → qq_send_private_msg(user_id, message)
```

## 8. 已知边界

- `qq_get_recent_messages` 只读进程内存（200 条上限），MCP server 重启即清空，无历史回溯；
- 消息发送仅支持纯文本段（富文本/图片未封装，需要时在 server 侧扩展 OneBot message 段）；
- NapCat 无频道发送能力（4.18.42），频道发布需走官方 QQ 机器人开放平台（见 docs/qq-cloud-agent-mcp.md）；
- MCP server 与 QQ 生命周期解耦：QQ 重启期间 WS 断开并自动重连，HTTP 端点始终可用。


## 14. MCP 监听与工具更新（2026-11-01）

- 监听地址:`::`(IPv6 双栈),端口 **8899**(原 8000/8801 已停用;8801 被 transmission 占用)。
- 鉴权:可选,设置环境变量 `MCP_TOKEN` 后要求请求头 `X-MCP-Token` 或 `?token=`。
- 启动:`cd /data/data/com.termux/files/home/qq-mcp && nohup python3 qq-mcp-server.py > server.log 2>&1 &`(注意用 nohup+disown,`setsid` 组合在该 shell 下会被回收)。
- 工具 24 个,新增 6 个实例管理:qq_instances / qq_instance_add / qq_instance_start /
  qq_instance_stop / qq_instance_delete / qq_instance_qr。
- 接入示例(openclaw/omp 等):MCP HTTP/JSON-RPC endpoint `http://<host>:8899/mcp`,
  IPv6 直连 `http://[2408:8256:1475:2258:4a2c:a0ff:fe63:d831]:8899/mcp` 或域名
  `http://mi.theworld.codes:8899/mcp`。

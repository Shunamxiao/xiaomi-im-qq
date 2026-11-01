# MCP Server 与 AI Agent 框架接入指南

> mi8 上的 QQ MCP Server 把 NapCat（OneBot 11）能力以 MCP 工具暴露，
> 供 OpenClaw、OMP、Claude Code 等任意 MCP 客户端操作 QQ。
> 本文档面向接入方与运维。日期：2026-11-01。

## 1. 服务概览与端点

- 实现：`mcp/qq-mcp-server.py`（本地仓库为主，mi8 为部署目标），纯 Python 标准库 + websockets，
  依赖仅 `pip install websockets`。
- 传输：MCP JSON-RPC over HTTP，无状态，单端点 `POST /mcp`（支持 `initialize` / `tools/list` / `tools/call`，
  协议版本 2025-03-26）。
- 内部依赖：启动时连接 NapCat OneBot WS `ws://127.0.0.1:3001`，并调用本机 WebUI `http://127.0.0.1:6099`
  的插件 API（account / autoreply），因此**必须部署在 mi8 本机**。
- 默认监听：`::`（IPv6 双栈）`8899`。
- 端点：
  - 本机：`http://127.0.0.1:8899/mcp`
  - 局域网：`http://<mi8局域网IP>:8899/mcp`
  - 域名：`http://mi.theworld.codes:8899/mcp`
  - IPv6 直连：`http://[2408:8256:1475:2258:4a2c:a0ff:fe63:d831]:8899/mcp`
- 鉴权：可选。设置环境变量 `MCP_TOKEN` 后，请求必须带 `X-MCP-Token: <token>` 或 `?token=<token>`。

## 2. 监听、鉴权与二维码主机

当前进程监听结果（2026-11-01 实测）：

```text
LISTEN 0 5 *:8899 *:*     python3 qq-mcp-server.py
```

- `MCP_HOST` 默认 `::`，`MCP_PORT` 默认 `8899`。
- 设置 `MCP_TOKEN` 后，`/mcp` 所有请求都会校验 token；未设置时保持原有“无鉴权”行为。
- 二维码 URL 的主机由 `WEBUI_PUBLIC_HOST` 控制：
  - 只填主机名，不要带 `http://` 和端口，例如 `mi.theworld.codes`；
  - 未设置时取本次 MCP 请求的 `Host` 头并去掉端口；
  - 仍为空时兜底 `127.0.0.1`。
- 主实例 WebUI 固定为 `6099`，页面/API key 为 `x524980520`；二维码图片由主实例 WebUI 提供，
  子实例的二维码也会通过主实例代理返回。

## 3. 工具清单（28 个）

> 现有 24 个工具中已经包含 `qq_instance_qr`。本次新增 4 个
> （`qq_send_image` / `qq_get_stats` / `qq_account_config` / `qq_set_account_config`），
> 增强 1 个（`qq_instance_qr` 额外返回 `qr_url`），所以总数是 **28**，不是 29。

### 消息（4）

| 工具 | 参数 | 用途 |
| --- | --- | --- |
| `qq_send_private_msg` | `user_id:int`, `message:str` | 发私聊文本 |
| `qq_send_group_msg` | `group_id:int`, `message:str` | 发群文本 |
| `qq_send_image` | `target_type:"private"\|"group"`, `user_id?` / `group_id?`, `image:str`, `text?:str` | 发图片，可混发文字；文字段在图片前 |
| `qq_get_recent_messages` | `count?:int(≤200)`, `group_id?:int` | 取最近收到的消息（内存缓存 200 条，重启即空） |

`qq_send_image` 的 `image` 支持：

- `https://...` / `http://...` URL；
- `base64://...`；
- NapCat **容器内可见**的本地路径，例如 `/root/pic.png`。

本地路径不是 Termux 侧路径；NapCat 在 proot 容器里运行，通常只能读 `/root/...`。

### 群信息与群管理（8）

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

### 账号与配置（10）

| 工具 | 参数 | 用途 |
| --- | --- | --- |
| `qq_get_login_info` | — | OneBot 登录信息（当前账号） |
| `qq_get_friend_list` | — | 好友列表 |
| `qq_list_accounts` | — | 登录状态 + 当前账号 + 历史账号 + autoLoginAccount |
| `qq_switch_account` | `uin:int` | **切换账号（会重启 NapCat 实例，几分钟内所有工具不可用）** |
| `qq_get_rules` | — | 主实例自动回复完整配置 |
| `qq_set_rules` | `keywordsText:str` | 整体替换主实例关键词规则，每行 `匹配词\|类型\|回复`（类型 prefix/contains，可省略；回复可含 `\|`；空行与坏行跳过） |
| `qq_groups` | — | 群列表 + 各群自动回复开关（member_count/enabled） |
| `qq_get_stats` | — | autoreply 运行统计：`stats.processed` / `stats.todayProcessed`，附运行时长与启用状态 |
| `qq_account_config` | `name:str` | 读取指定实例的完整 autoreply 配置（跨实例，`main` 或 `acc-*`） |
| `qq_set_account_config` | `name:str`, `config:object` | 写入指定实例的完整配置（跨实例）；建议先读后改再整包回写 |

说明：

- `qq_set_rules` 是**全量替换**关键词（对齐插件的数据模型）。改规则前先 `qq_get_rules`
  取回 `keywordsText`，在其基础上修改后整体回写，不要凭空生成。
- `qq_set_account_config` 调用 account 插件 `/api/account-rules`。插件 POST `/config` 是浅合并，
  `private` / `group` 再合并一层，但 `keywords` 数组一旦提交会整体替换；因此同样建议
  `qq_account_config` → 修改 → `qq_set_account_config` 整包回写，避免误删字段。
- `qq_get_stats` 当前只返回插件真实统计字段；自动回复插件目前**没有单独的“回复数”计数**，
  不要把它当成已回复条数。

### 多实例管理（6）

| 工具 | 参数 | 用途 |
| --- | --- | --- |
| `qq_instances` | — | 列出全部实例：绑定账号/昵称/运行状态/登录状态/端口 |
| `qq_instance_add` | — | 创建新实例（独立数据与端口），创建后自动启动并等待扫码 |
| `qq_instance_start` | `name:str` | 启动指定实例 |
| `qq_instance_stop` | `name:str` | 停止指定实例 |
| `qq_instance_delete` | `name:str` | 删除指定实例（不可恢复；主实例不可删） |
| `qq_instance_qr` | `name:str` | 刷新指定实例二维码（会请求 QQ 接口），并返回 `qr_url` |

`qq_instance_qr` 返回示例：

```json
{
  "ok": true,
  "name": "acc-6102",
  "qr_url": "http://mi.theworld.codes:6099/plugin/napcat-plugin-account/api/qr?key=x524980520&name=acc-6102"
}
```

刷新成功不代表二维码图片一定能取到：实例未启动、尚未生成二维码或已登录时，`qr_url` 可能返回 404。

## 4. Agent 框架接入配置示例

### OpenClaw（`mcp.servers`，支持 Streamable HTTP）

```json
{
  "mcp": {
    "servers": {
      "qq": {
        "type": "streamable-http",
        "url": "http://mi.theworld.codes:8899/mcp"
      }
    }
  }
}
```

若服务端设置了 `MCP_TOKEN`，在客户端 headers 配置中加入 `X-MCP-Token: <token>`。

### Claude Code / 通用 MCP 客户端（CLI 方式）

```bash
claude mcp add --transport http qq http://mi.theworld.codes:8899/mcp
```

### OMP 等：任意支持 “HTTP MCP / Streamable HTTP” 的框架

通用参数：URL = `http://mi.theworld.codes:8899/mcp`，方法 POST，
body 为标准 MCP JSON-RPC（`tools/list`、`tools/call`）。未设置 `MCP_TOKEN` 时无需鉴权头。

手动验证：

```bash
curl -s http://127.0.0.1:8899/mcp -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":1,"method":"tools/list"}'
```

设置 token 后：

```bash
curl -s http://127.0.0.1:8899/mcp \
  -H "Content-Type: application/json" \
  -H "X-MCP-Token: $MCP_TOKEN" \
  -d '{"jsonrpc":"2.0","id":1,"method":"tools/list"}'
```

## 5. 安全状态与建议

- `MCP_TOKEN` 为可选。**未设置时（当前 mi8 进程即此状态），拿到地址即可发消息、踢人、改规则、切账号。**
- WebUI 6099 的 key 固化为 `x524980520`，且对公网 IPv6 开放；这只是访问密钥，不是强鉴权。
- 建议按优先级处理：
  1. 设置 `MCP_TOKEN`，并在 Agent 客户端配置 `X-MCP-Token`；
  2. 只走内网/隧道，不直接转发 8899；
  3. 若必须公网，走 nginx + TLS，并加 IP 白名单或额外认证。
- 任何写操作前先读取当前配置并留档，尤其是 `qq_set_rules` / `qq_set_account_config`。

## 6. 部署、重启与验证

本地仓库 `mcp/qq-mcp-server.py` 为唯一真源。先做语法检查，再覆盖 mi8 上的文件：

```bash
python3 -m py_compile mcp/qq-mcp-server.py
scp mcp/qq-mcp-server.py mi8:/data/data/com.termux/files/home/qq-mcp/qq-mcp-server.py
```

重启（**不要用 setsid**；用锚定的 `pkill` 避免误杀执行重启命令的 shell）：

```bash
ssh mi8 "pkill -f '^python3 qq-mcp-server.py$'; sleep 1; \
  cd /data/data/com.termux/files/home/qq-mcp && \
  nohup python3 qq-mcp-server.py > server.log 2>&1 & disown"
```

> 直接 `pkill -f qq-mcp-server.py` 会连当前 ssh 命令行一起匹配，可能把重启 shell 杀掉；
> 使用 `^python3 qq-mcp-server.py$` 只匹配真正的 Python 进程。

验证：

```bash
ssh mi8 "ss -tln | grep 8899; pgrep -af '^python3 qq-mcp-server.py$'; tail -5 /data/data/com.termux/files/home/qq-mcp/server.log"

ssh mi8 "curl -s -X POST http://127.0.0.1:8899/mcp \
  -H 'Content-Type: application/json' \
  -d '{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"tools/list\"}'"
```

重启 MCP server 不影响 QQ 本体；QQ 重启期间 WS 会断开并自动重连。

## 7. 常用任务配方

### 查状态

```text
qq_get_login_info          → 当前 OneBot 登录的账号
qq_list_accounts           → 登录阶段 / 当前账号 / 历史账号 / 自动登录配置
qq_groups                  → 在哪些群、各群自动回复开关
qq_get_stats               → 已处理 / 今日处理 / 运行时长
```

### 改主实例自动回复规则

```text
1. qq_get_rules            → 拿到 keywordsText 原文
2. 在原文基础上增删改行（每行: 匹配词|类型|回复）
3. qq_set_rules(keywordsText=改后全文)
   → 返回 applied.count 成功条数与 applied.skipped 被跳过的坏行
```

### 跨实例读写规则

```text
1. qq_account_config(name="acc-6102")        → 拿到完整 config
2. 修改 config.keywords / config.private 等字段
3. qq_set_account_config(name="acc-6102", config=改后的完整对象)
```

### 切换账号

```text
1. qq_list_accounts        → 确认目标 uin 在 quickList（不在则需扫码添加，走 WebUI 管理页）
2. qq_switch_account(uin)  → 触发 NapCat 重启，本工具立即返回
3. 等待 1~3 分钟后 qq_get_login_info 验证新账号；期间所有 OneBot 工具报 "WS not connected" 属预期
```

### 发消息 / 发图片

```text
群聊文本: qq_get_group_list 找 group_id → qq_send_group_msg(group_id, message)
私聊文本: qq_get_friend_list 找 user_id → qq_send_private_msg(user_id, message)

群聊图片: qq_send_image(target_type="group", group_id=..., image="https://...", text="说明")
私聊图片: qq_send_image(target_type="private", user_id=..., image="base64://...")
本地图片: qq_send_image(target_type="group", group_id=..., image="/root/pic.png")
```

### 扫码登录子实例

```text
1. qq_instances                    → 确认实例 running 且 login.isLogin=false
2. qq_instance_qr(name="acc-6102") → 刷新二维码并拿到 qr_url
3. 把 qr_url 交给手机 QQ 扫码；成功后 qq_instances 应显示绑定的 uin
```

## 8. 已知边界

- `qq_get_recent_messages` 只读进程内存（200 条上限），MCP server 重启即清空，无历史回溯；
- `qq_send_image` 的本地路径必须是 NapCat 容器内可见路径；Termux 侧路径不可用；
- `qq_get_stats` 只提供 autoreply 插件实际统计字段，当前没有“回复条数”；
- `qq_instance_qr` 的刷新动作会请求 QQ 接口，仅在需要扫码时调用；`qr_url` 依赖主实例 6099 可访问；
- NapCat 无频道发送能力（4.18.42），频道发布需走官方 QQ 机器人开放平台（见 docs/qq-cloud-agent-mcp.md）；
- MCP server 与 QQ 生命周期解耦：QQ 重启期间 WS 断开并自动重连，HTTP 端点始终可用。

## 9. 变更记录（2026-11-01）

- 新增 `qq_send_image`、`qq_get_stats`、`qq_account_config`、`qq_set_account_config`；
- 增强 `qq_instance_qr`：保留刷新动作，额外返回主实例 6099 的二维码 `qr_url`；
- 工具总数 24 → 28；监听端口统一为 `8899`，文档同步 token 与 `WEBUI_PUBLIC_HOST` 配置；
- 修复 WS 线程 `EVENT_BUF` 未定义导致的 notice/request 断线重连。
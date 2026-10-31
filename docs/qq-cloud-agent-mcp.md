# 调研：把 mi8 上的 QQ 变成云端 Agent（MCP 接入 OMP / OpenClaw）

- 日期：2026-10-31
- 结论先行：**协议端维持 NapCat（即 Linux 协议登录，已实现且与手机 QQ 共存）；
  mi8 上新增反向隧道，云端部署 qq-gateway（规则引擎 + 收件箱队列 + MCP Server）；
  QQ 频道发布因 NapCat 已无频道发送能力，改走官方 QQ 机器人开放平台。**

## 1. 需求与问题定义

目标功能：

1. 私聊回复（现有关键词规则，主用）；
2. 群聊回复（被 @ 按规则回复 + 正常群消息），群管理（禁言/踢人/名片等）；
3. QQ 频道内容发布；
4. 以上能力以 MCP 工具暴露，供 OMP、OpenClaw 等 Agent 框架操作；
5. 登录方式不能影响其他设备（手机）正常登录。

## 2. 已确认事实（源码 / 官方文档 / 本仓库实测）

### 2.1 协议登录

| 事实 | 依据 |
| --- | --- |
| NapCat 4.18.42 为当前最新 release（2026-10-30 发布），维护活跃 | GitHub Releases API 实查 |
| 本机 NapCat 即「Linux 底层协议登录」：NTQQ Linux arm64，登录为独立电脑设备（ZAIDU-5466），与手机 QQ 多端共存，已实测 | docs/napcat-deploy.md、README 运行状态 |
| 旧式 iPad 协议（go-cqhttp/mirai 时代）签名已死，无可维护的开源实现；ImPad 平板伪装被后台 ANR 阻塞且与本需求无关（NapCat 已解决多端问题），不重启 | docs/impad-tablet-spoof.md、社区现状 |
| 风控定性：NapCat 官方承认社交风控存在（临时限制为主）；本仓库降险措施已就位（版本锁定、127.0.0.1 绑定、设备名伪装、低频触发） | docs/napcat-deploy.md 第 5 节 |

### 2.2 NapCat 4.18.42 能力矩阵（读源码树 packages/napcat-onebot/action 确认）

| 能力 | 状态 | 对应 action |
| --- | --- | --- |
| 私聊/群聊/群临时会话收发 | 可用 | SendMsg / SendPrivateMsg / SendGroupMsg |
| 被艾特判断 | 可用 | 消息段 type=at data.qq，qq==self_id 即被 @ |
| 单人禁言 / 全员禁言 / 踢人 | 可用 | SetGroupBan / SetGroupWholeBan / SetGroupKick |
| 群名片 / 群管理员 / 退群 / 改群名 | 可用 | SetGroupCard / SetGroupAdmin / SetGroupLeave / SetGroupName |
| 加群审批 | 可用 | SetGroupAddRequest、GetGroupAddRequest |
| 精华消息 / 专属头衔 / 戳一戳 / 群签到 | 可用 | SetEssenceMsg / SetSpecialTitle / SendPoke / SetGroupSign |
| 群成员与禁言列表查询 | 可用 | GetGroupMemberList / GetGroupMemberInfo / GetGroupShutList |
| 撤回 / 表情回应 / 消息取回 | 可用 | DeleteMsg / SetMsgEmojiLike / GetMsg |
| QQ 频道（guild）发送 | 不可用 | guild 目录仅剩 GetGuildList / GetGuildProfile 只读接口；send_msg 的 createContext 无 guild 路径（SendMsg.ts 实读）。旧版 send_guild_channel_msg 在当前版本已不存在 |

### 2.3 MCP / Agent 框架接入

| 事实 | 依据 |
| --- | --- |
| OpenClaw 支持接入第三方 MCP server：Streamable HTTP / SSE / Stdio 三种传输，配置在 mcp.servers，支持 header 鉴权 | openclaw/openclaw 仓库 docs/tools/mcp.md（2026-10-31 实读，393k stars） |
| 已有 NapCat→MCP 开源项目：OmniQQ-MCP（74 工具，2026-10-28 仍在更新）、ptrel1/napcat_mcp、Xadeus-QQ-MCP 等 | GitHub search 实查 |
| 现成项目均为工具透传型，不含规则引擎与消息分流，无法满足「规则+Agent 混合」需求 | 项目 README 抽查 |
| MCP 2026 规范支持 Streamable HTTP 远程传输 + Bearer/header 鉴权，适合云端多客户端接入 | MCP 官方规范 |

## 3. 方案选择

### 3.1 协议端：维持 NapCat（决策：不变更）

- NapCat 已经是 Linux 协议登录且与手机共存，需求 5 天然满足；
- 换 Lagrange（纯协议）要重新登录、换设备指纹、重冒风控风险，收益仅是去 QQ 客户端依赖，不值；
- iPad 协议无可用实现，ImPad 路线已验证被阻塞且不解决本需求。

### 3.2 频道发布：改走官方 QQ 机器人开放平台（决策：推翻「NapCat 个人号」初选）

调研中发现硬缺口：NapCat 4.18.42 已无频道发送能力（见 2.2），个人号发频道不可行。
替代对比：

| 选项 | 结论 |
| --- | --- |
| 降级旧版 NapCat 用 send_guild_channel_msg | 不采纳：旧版有已知风控问题（#1728 类），为单一功能引入版本风险 |
| Lagrange 补频道 | 不采纳：Lagrange 与 NapCat 同为电脑端登录，同端互踢，不能共存 |
| 官方 QQ 机器人开放平台（bot.q.qq.com） | 采纳：频道 API 官方稳定，独立机器人身份不占设备登录位，与个人号共存；缺点是频道里显示为机器人身份 |

对 Agent 侧透明：MCP 网关封装为统一工具 qq_channel_publish，Agent 不感知背后是官方 API。

### 3.3 部署拓扑：mi8 协议端 + 隧道 + 云端网关（用户已确认）

mi8 保留 NapCat（家宽真实 IP、已通过快速登录稳定运行），云端跑网关与 Agent。
NapCat 与网关之间用反向隧道（autossh -R 或 frp），OneBot WS 不直接暴露公网。

    手机QQ（不受影响）
       │
    mi8 (root, Termux+proot 容器 napcat)
       ├─ NapCat 4.18.42 ── OneBot11 WS 127.0.0.1:3001
       └─ autossh/frp 反向隧道（token 鉴权）
       ▼
    云端服务器 (Docker)
       └─ qq-gateway (Node.js)
           ├─ OneBot WS 客户端（经隧道）
           ├─ 规则引擎（复用 bot/autoreply.js 的 matchReply）
           ├─ 收件箱队列（需 Agent 处理的被@/私聊消息）
           ├─ MCP Server（Streamable HTTP + token）
           └─ 频道发布模块（官方机器人 API）
       ▼
    OMP / OpenClaw / Claude Code 等任意 MCP 客户端

### 3.4 消息分流：规则 + Agent 混合（用户已确认）

- 规则引擎网关本地毫秒级回复（关键词/前缀/白名单，沿用现有规则文件与热重载）；
- 未命中规则且需要智能处理的消息（私聊、被 @）进入收件箱队列，Agent 通过 MCP 拉取处理；
- 选用 poll_inbox 轮询工具而非 MCP notification 推送：OpenClaw 等框架对 server 主动推送支持不一，轮询语义简单、全框架通用，v1 不做推送。

## 4. 实施步骤（待批准后执行）

1. qq-gateway 服务（Node.js，云端 Docker）：
   - OneBot WS 客户端重构自 bot/autoreply.js，规则引擎零改动复用（tests/test-matchreply.js 继续有效）；
   - 新增：@ 段解析与被@判断、私聊/群聊收件箱队列（环形缓冲 + 读取游标）、群管理 action 转发。
2. MCP Server 层（@modelcontextprotocol/sdk，Streamable HTTP + Bearer token）：
   - 工具集：qq_send_private / qq_send_group / qq_poll_inbox / qq_recent_messages /
     qq_group_ban / qq_group_kick / qq_group_card / qq_group_approve / qq_group_info /
     qq_channel_publish / qq_self_info；
   - 每个工具 = OneBot action 的薄封装 + 参数校验，不做业务逻辑。
3. 隧道：mi8 起 autossh 反向隧道（service.d 加一条自启），云端网关监听 wss + token。
4. 频道发布模块：注册 QQ 机器人开放平台拿到 appid/secret，独立 qq-channel 模块调官方 API，
   以 MCP 工具暴露；与 OneBot 链路互不依赖。
5. 测试与验收：
   - 单元：@ 解析、收件箱游标、规则引擎（现有用例不回归）；
   - E2E（小号）：私聊规则回复、群被@进队列并被 Agent 处理、禁言/踢人、频道发布、手机 QQ 同时在线不受影响；
   - 风控观察期：灰度一周低频运行，观察掉线/异常（沿用 docs/napcat-deploy.md 措施）。

## 5. 风险与边界

- 风控：云端 Agent 生成的内容经个人号发出，刷屏式回复会放大风控面；网关需带发送频率限制（每会话 N 条/分钟）与内容长度上限；
- 单点：mi8 断电/断网 = 协议端掉线；Agent 框架需容忍 poll_inbox 超时；恢复后 NapCat -q 快速登录自愈（已具备）；
- 账号边界：官方频道机器人与个人号是两个身份，频道内不可冒充个人号发言——如需个人号发频道只能人工，属腾讯侧限制，无技术绕行；
- 合规：协议端与自动化回复违反 QQ 用户协议，自担风险（现状已接受）。

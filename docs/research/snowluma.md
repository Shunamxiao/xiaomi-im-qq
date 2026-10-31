# 调研：SnowLuma（NapCat 后继生态）

- 日期：2026-10-31
- 仓库：github.com/SnowLuma/SnowLuma（★3293，TypeScript，2025-06 创建，活跃）
- 版本：v2.2.1（2026-10-27），发版频率约每周 1-2 次

## 1. 是什么

SnowLuma 是 NapCat 后继生态的「Next Remote Protocol Framework」：
TypeScript 互操作运行时，把官方 QQ 客户端进程的原生会话桥接为
OneBot v11 动作与事件，并通过 WebSocket / HTTP / WebUI / SDK / MCP
提供统一入口。项目参考了 LagrangeV2 的协议定义与 NapCatQQ 的实现思路，
社区与 NapCat 同源（Telegram 群同为 t.me/napcatqq）。

核心机制与 NapCat 相同：注入 hook 官方 QQ 客户端进程
（官方文档原话：「核心要求都一样：能跑 QQ、能向 QQ 进程注入 hook、能扫码登录」），
不是独立协议实现。

## 2. 相对 NapCat 的优势

| 项 | SnowLuma | NapCat 4.18.42（现部署） |
| --- | --- | --- |
| 内置 MCP | 官方 @snowluma/mcp（docs/read/write 三档，npm 分发） | 需配 OmniQQ-MCP 等第三方 |
| 内置 WebUI | 账号状态/实时日志/连接配置/动作调试/存储管理（:5099） | 较简（:6099） |
| 多账号 | 各账号独立会话/身份映射/存储/网络配置 | 支持 |
| SDK | @snowluma/sdk（TypeScript） | 无 |
| linux-arm64 发行包 | 完整版 49.7MB（含运行时）/ lite 7.3MB（需 Node 24+） | 有 |
| 发版节奏 | 高频（10 月已发 5+ 版） | NapCat 本体转维护模式，社区分支续命 |

MCP 三档模式：docs（只读目录+JSON Schema，默认）、read（只读 action 可调）、
write（invoke_action 全量写操作）。连 OneBot HTTP 端点即启用执行，
支持 SNOWLUMA_MCP_TOKEN Bearer 鉴权。

## 3. 对 mi8 部署的硬伤

1. proot 注入风险：官方部署文档明确警告——「注入在受限环境（如 proot）
   下可能不可用」「手机（Android）部署：注入可能受限」。我们的 NapCat 在
   proot 下可用，但 SnowLuma 的注入机制不同，能否在 proot+arm64 下工作未验证。
2. 许可：源码可见非商业许可，非 OSI 开源。个人自托管/私下修改可以；
   商业使用或公开发布衍生版需书面授权。
3. QQ 频道不支持：官方动作目录（docs/api）中无任何 guild/channel action
   （send_* 全集已核对：msg/image/record/forward/ark/poke/notice/sign/qzone 等，
   无频道）。

## 4. 结论

- 自动回复/Agent 化主链路维持 NapCat + OmniQQ-MCP 不变：
  proot 下已验证、频道外能力全覆盖、无许可风险。
- SnowLuma 的价值在内置 MCP/WebUI/多账号——若未来 NapCat 停止维护
  或需要多账号矩阵，可在 x86 服务器上用 Docker 跑 SnowLuma 作为替代基座
  （官方最推荐 Docker，mi8 的 proot 路径不推荐）。
- SnowLuma 与 NapCat 的 OneBot 接口同构，上层（autoreply.js / MCP 工具层）
  迁移成本低。

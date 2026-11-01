# 交接文档 — mi8 QQ 云端 Agent（给联合开发 Agent）

> 版本：2026-11-01 ｜ 目标：把 mi8 上的 QQ 变成可被 Agent 驱动/扩展的云端机器人

## 1. 项目目标与现状

**目标**：在小米8（root）上跑多个 QQ 账号，具备自动回复、账号/实例管理、MCP 工具化能力，最终接入 openclaw / omp 等 Agent 框架。

**已实现**：
- 多 QQ 实例（主 + acc-*），独立数据/端口/WebUI，扫码添加、启停、删除
- 自动回复插件：关键词（包含/前缀/正则）、图片回复、私聊/群聊开关、仅@回复、
  冷却、随机延迟、黑白名单、群级规则（按群覆盖）、跨账号同群防重复
- WebUI：设置按钮直达自定义页、扩展页面、账号配置中心、二维码手动刷新
- MCP Server：29+ 工具（消息、群管理、账号、实例、规则、图片）
- 调试插件：WS 8998 热重载

## 2. 架构

```
Windows(ZAIDU-5466) ──ssh──► mi8 (Android root, Termux)
                               │
                        proot 容器 napcat (Debian arm64)
                               │
        ┌──────────────────────┼───────────────────────┐
        │                      │                       │
   主实例 QQ(HOME=/root)   子实例 acc-*(HOME=隔离)   Xvfb per instance
        │                      │
  NapCat 4.18.42 + 插件      同左(独立 workdir/config)
        │
  OneBot WS 3001/3003+   WebUI 6099/6102+
        │
  MCP Server(:8899) ◄── Agent 框架(openclaw/omp)
        │
  共享 /root/qq-accounts/shared/claims (跨实例防重复)
```

## 3. 代码地图

| 路径 | 职责 |
| --- | --- |
| `plugin/`(fork: Shunamxiao/napcat-plugin-template) | 自动回复插件源码(TS/Vite) |
| `plugin/src/index.ts` | 生命周期、配置钩子、内嵌配置页(SETTINGS_PAGE_HTML) |
| `plugin/src/config.ts` | 默认配置、配置 schema、文本↔结构转换 |
| `plugin/src/handlers/message-handler.ts` | 匹配/冷却/延迟/防重复/图片/发送 |
| `plugin/src/core/state.ts` | 状态、配置清洗、冷却 Map、定时器 |
| `plugins/account-manager/` | 账号/实例管理插件(index.mjs 页面+API,manager.mjs 编排库) |
| `deploy/` | 启动脚本、宿主桥、OneBot 配置、fakehost |
| `mcp/qq-mcp-server.py` | MCP Server(标准库,工具 29+) |
| `docs/` | 部署史、运维手册、插件开发经验、AI 接入、多实例 |

## 4. 数据流

**消息回复**：QQ 消息 → NapCat → 插件 `plugin_onmessage` → 过滤(自己/黑名单/开关)
→ 群级规则优先 → 匹配(contains/prefix/regex) → 冷却 → 跨实例 claim → 延迟 →
`send_msg`(text+image 段)。

**配置**：WebUI/API → `plugin_set_config` → sanitize → 写 config.json → 热生效。

**多账号**：account 插件 → manager.js 注册表 → 宿主 bridge 拉起 proot 实例 →
实例 WebUI(独立 token) → 扫码/自动登录 → pollBinding 写 OneBot 配置并回填 uin。

## 5. 扩展指南

### 5.1 增加回复能力(字段/规则)
1. `types.ts` 加字段(如 `replies?: string[]`)
2. `config.ts`：DEFAULT_CONFIG + schema + 文本转换(若有)
3. `state.ts`：sanitize 保留新字段
4. `message-handler.ts`：使用新字段
5. `index.ts` 页面：加输入项
6. 构建 → 部署 → 验证

### 5.2 增加 MCP 工具
1. `mcp/qq-mcp-server.py`：`_dispatch` 加分支 + `MCP_TOOLS` 加声明
2. `py_compile` → 部署 → `tools/list` 核对 → 文档更新

### 5.3 增加新插件
- 用 `plugin/` 模板(官方 fork)新建目录；package.json 唯一 `name`；
  `napcat.settingsPage` 声明自定义设置页；plugins.json 启用。

### 5.4 新页面
- 用 `ctx.router.page({path,title,htmlFile})` + init 时 `fs.writeFileSync(pluginPath/x.html)`；
- 页面在 iframe 沙箱：禁用 alert/confirm，用内联 toast + 二次确认；
- **必须**提取内嵌 JS 做 `node --check` 后再部署。

## 6. 开发约定(重要)

1. 主实例不能被搞挂；重启用 `bash /data/adb/bin/mi8-napcat-start.sh`(前台)。
2. 改路由/页面必须完整重启；只改逻辑可热重载(reloadPlugin)。
3. 任何内嵌 HTML/JS 修改先本地语法自检(服务端 200 ≠ 页面可用)。
4. 插件 package.json 的 `name` 必须与 `plugins.json` 键一致。
5. 多实例共享 `/root`，但 HOME/NAPCAT_WORKDIR 隔离；跨实例状态用 `/root/qq-accounts/shared/`。
6. 图片本地路径必须在 `/root` 下且 ≤5MB。
7. 每次功能变更：构建 → 部署 → 验证(Loaded/API/页面) → 提交(git)。
8. 提交分两仓库：主仓库 `D:\xiaomi-im-qq`、插件 fork `D:\xiaomi-im-qq\plugin`。

## 7. 已知限制 / 待办

- NapCat 升级会覆盖 `napcat.mjs` 与静态 bundle 补丁(设置按钮跳转、缓存头)，需重打。
- 子实例创建后首次可能需停止再启动一次才加载插件(已自动补齐配置，仍建议验证)。
- MCP 默认无 token；公网暴露前设置 MCP_TOKEN。
- QQ 频道发布未实现(官方接口受限)，见 docs/napcat-deploy.md 第 9 节。
- 图片只支持单图/规则一张；多图池可扩展。

## 8. 快速上手(新 Agent 第一步)

1. 读 `docs/ops-runbook.md`(运维)与 `docs/plugin-dev-notes.md`(插件经验)
2. `ssh mi8` 健康检查(见 runbook 第 2 节)
3. 打开 WebUI(`http://<ip>:6099/webui?token=x524980520`)确认 3 插件、账号在线
4. 只读优先：先调 `/api/config`、`/api/instances` 熟悉数据结构
5. 任何写操作前先备份配置(GET 保存 JSON)

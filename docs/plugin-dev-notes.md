# NapCat 插件开发经验笔记

> 本文档汇总 mi8 QQ 云端 Agent 项目中**已实测验证**的插件开发经验，
> 面向后续开发者 / Agent。所有结论均来自本仓库源码实读与 mi8 实机验证，
> 不是官方文档转述。日期：2026-10-31，NapCat 4.18.42。

## 1. 插件工程形态

项目里两种并存形态，按需选择：

| 形态 | 代表 | 适用 |
| --- | --- | --- |
| TS 模板 fork（Vite 构建） | `plugin/`（napcat-plugin-autoreply） | 需要 WebUI React 面板、复杂逻辑 |
| 单文件 ESM | `plugins/account-manager/index.mjs` | 轻量插件，纯 API + 简单页面 |

- 模板 fork 的 `package.json` 的 **name 必须与 NapCat `config/plugins.json` 里的启用键完全一致**，
  否则构建产物部署后 `Loaded 0 plugins`（模板默认 name 是 napcat-plugin-template，fork 后必须改）。
- NapCat 通过 `plugins.json` 显式启用：`{"napcat-plugin-autoreply": true}`，缺失即 disabled。

## 2. 插件生命周期钩子

模块导出以下函数（均可选，除 init 外）：

| 导出 | 时机 | 要点 |
| --- | --- | --- |
| `plugin_init(ctx)` | 插件加载 | 注册路由、读配置、起定时器；失败会阻止加载 |
| `plugin_onmessage(ctx, event)` | 每条消息 | 私聊/群聊都在这里分流（按 `message_type`） |
| `plugin_onevent(ctx, event)` | 通知/请求类事件 | 加群申请、戳一戳等 |
| `plugin_cleanup(ctx)` | 卸载/热重载前 | **必须**清定时器、断连接，否则热重载后泄漏 |
| `plugin_get_config()` | WebUI 读配置 | 返回完整配置对象（可附加派生字段如 keywordsText） |
| `plugin_set_config(ctx, config)` | WebUI 保存配置 | **完整替换**语义；前端提交的文本字段在此解析回结构化数据 |
| `plugin_config_ui(ctx)` | 返回配置面板 schema | **必须是数组**，见下 |
| `plugin_on_config_change(ctx, ui, key, value, config)` | 单项变更 | 仅标记 `reactive: true` 的控件触发 |

### 大坑：plugin_config_ui 必须是数组

NapCat 内部判断 `!!module.plugin_config_ui` 后**当数组直接使用**。
导出一个返回数组的**函数**无效（truthy 但不可迭代）→ WebUI 报
"No configuration schema available"。正确写法：

```js
let plugin_config_ui = [];
function plugin_init(ctx) { plugin_config_ui = buildSchema(ctx); }
export { plugin_config_ui };
```

## 3. 路由与 HTTP 层大坑

### req 是包装对象，没有 url

插件 router 的 handler 收到的 `req` 不是原生 IncomingMessage，而是：

```js
{ path, method, query, body, headers, params, raw }
```

- 取查询参数用 `req.query`，**没有 `req.url`**；
- `req.raw` 才是原生对象（需要流式读取时才用）。

### res 没有 end，只有 send / json

`res` 可用方法：`status()`、`json()`、`send()`、`setHeader()`、
`sendFile()`、`redirect()`、`raw`。**调用 `res.end()` 会直接 TypeError**。
返回 JSON 一律：

```js
res.json({ code: 0, data: ... });
```

### 路由前缀与鉴权

| 类型 | 注册方法 | 实际前缀 |
| --- | --- | --- |
| 免鉴权 API | `router.getNoAuth / postNoAuth` | `/plugin/<plugin-id>/api/...` |
| 需 WebUI 鉴权 API | `router.get / post` | `/api/Plugin/ext/<plugin-id>/...` |
| 静态文件 | `router.static(path, dir)` | `/plugin/<plugin-id>/files/<path>` |
| 内存文件 | `router.staticOnMem` | `/plugin/<plugin-id>/mem/<path>` |
| 页面 | `router.page({path,title,htmlFile})` | `/plugin/<plugin-id>/page/<path>` |

插件自带的页面走 NoAuth 即可（页面本身在 WebUI 内嵌展示）；
NoAuth 接口建议自带 accessKey 参数校验（account 插件做法：`?key=` 与 WebUI token 同值）。

### WebUI 响应统一包装

所有 WebUI/插件 API 响应为 `{code, data, message}`，`code !== 0` 为失败。
例外：插件自己 res.json 的对象原样返回（account 的 /status 就没有 code 包装）——
**对接前先用 curl 实测响应形状，不要假设**。

## 4. 配置系统

- 配置数据以 `config/<plugin-id>.json` 持久化，读改都经 `plugin_get_config/set_config`。
- WebUI 的 HTTP 保存接口 `POST /plugin/<id>/api/config` 走 `pluginState.updateConfig(body)`
  = **浅合并**；而 `plugin_set_config` 钩子是**完整替换**（会 sanitize）。
  两条路径语义不同，外部系统改配置前先 GET 再合并，永远回写完整字段。
- 结构化数组 + 派生文本双字段模式（autoreply 的 keywords / keywordsText）：
  规则权威是数组，文本只是编辑视图；在 `plugin_set_config` 里做双向转换，
  文本为空时从数组反推。外部 API（如 MCP）应直接写数组并同步文本。

## 5. 构建与部署闭环

### 构建环境（pnpm 11）

- **pnpm 11 不再读 package.json 的 `pnpm` 字段**；构建脚本白名单写
  `pnpm-workspace.yaml` 的 `allowBuilds: { esbuild: true }`。
  主插件与 webui 子项目**各需一份** pnpm-workspace.yaml。
- 构建命令：`pnpm build`（Vite），产物 `plugin/dist/`：
  `index.mjs` + **构建生成的精简 package.json** + `webui/`（dashboard 静态资源）。

### 大坑：dist/package.json 会覆盖身份信息

`dist/package.json` 由构建从根 package.json 生成。若 fork 后没改根 package.json 的
name，部署后插件名与 plugins.json 启用键失配 → 插件消失（Loaded 数变少）。
改 fork 身份信息要在**根 package.json** 改，不是 dist。

### 部署必须整包

dashboard 的 `htmlFile` 从**插件目录**解析，只拷 index.mjs 会报
"HTML file not found"、侧边栏空白。整包 = `index.mjs + package.json + webui/`。

### 标准闭环

```bash
# 1. 本地构建
cd plugin && pnpm build

# 2. scp 到 mi8（termux home 中转）
scp plugin/dist/index.mjs mi8:/data/data/com.termux/files/home/

# 3. cp 进容器插件目录（proot 容器路径）
ssh mi8 '/data/adb/bin/astermux proot-distro login napcat -- cp \
  /data/data/com.termux/files/home/index.mjs /root/<插件目录>/

# 4a. 小改（无路由变化）：热重载，不用重启 QQ
#     通过 napcat-plugin-debug 的 reloadPlugin（见第 6 节）

# 4b. 改了路由/页面：完整重启
ssh mi8 'pkill -f "opt/QQ/qq"; /data/adb/bin/mi8-napcat-start.sh'

# 5. 验证加载成功
ssh mi8 'grep -aE "Initialized plugin|Loaded . plugins" <napcat日志>'
# 期望看到: Initialized plugin: napcat-plugin-xxx vN.N.N + Loaded N plugins
```

### 大坑：热重载会丢路由

`reloadPlugin` 后**路由不会重新注册**（复现：reload 后访问 /api/* 全 404，
日志出现 no registered pages）。约定：**任何路由/页面/schema 改动必须完整重启验证**，
热重载只用于纯逻辑小改。

## 6. 调试服务 napcat-plugin-debug

商店安装的官方调试插件，**JSON-RPC over WebSocket**：`ws://127.0.0.1:8998`。

已验证能力：

| 方法 | 作用 | 限制 |
| --- | --- | --- |
| `reloadPlugin` | 热重载指定插件 | 丢路由（见上），仅限纯逻辑改动 |
| `setPluginStatus` | 启用/禁用插件 | — |
| `writeFile` | 直接写文件到插件目录 | **限定插件目录内**，可绕过 scp→容器 cp |

用途：热部署闭环（writeFile 写 index.mjs → reloadPlugin），
但改路由后仍须走完整重启。

## 7. WebUI API 鉴权（供程序化调用）

1. `POST /api/auth/login`，body `{ "hash": sha256(token + ".napcat") }`
   （token 为 WebUI 访问令牌，本项目当前为 x524980520）；
2. 响应 `data.Credential` 即凭证；
3. 后续请求带 `Authorization: Bearer <Credential>`。

插件经 HTTP 自调 WebUI 时用同一套（account 插件即如此实现 RestartNapCat 等内部调用）。

## 8. 单实例单账号约束

- NapCat 是**单实例单账号**架构。WebUI 的"添加账号"实为**切换账号**（退出当前登录）。
- 本项目用 `webui.json.autoLoginAccount` 控制自动登录（启动脚本不再传 `-q`），
  account 插件的 /switch 就是写该字段 + RestartNapCat。
- 多号同时在线必须多实例：独立数据目录 + 独立 OneBot/WebUI 端口，
  内存允许时再做（mi8 上每实例约数百 MB）。

## 9. 风控注意（官方口径）

- NapCat 官方承认存在社交风控，以临时限制为主；
- **同 IP 多账号登录有连带风控风险**（官方 README 口径）——多实例方案评估时必须计入；
- 本项目已就位的降险措施：版本锁定 4.18.42、OneBot 仅绑 127.0.0.1、
  登录设备名伪装（ZAIDU-5466，deploy/fakehost.c）、低频触发自动回复。

## 10. 排障速查

| 症状 | 原因 | 处置 |
| --- | --- | --- |
| Loaded 0 plugins | plugins.json 缺失/未列出 | 补 `{"<插件名>": true}` |
| 插件突然消失（Loaded 变少） | dist/package.json name 与启用键失配 | 改根 package.json name 后重建重部署 |
| No configuration schema available | plugin_config_ui 不是数组 | 改为数组导出 |
| 调路由 404 / no registered pages | reloadPlugin 丢路由 | 完整重启 |
| HTML file not found / 侧边栏空白 | 只拷了 index.mjs | 整包部署（含 webui/） |
| res.end 报错 | 插件 res 无 end | 用 res.json / res.send |
| QQ 起不来且日志不刷新 | 孤儿 Xvfb/proot 容器占位 | 清孤儿进程后重跑启动脚本 |
| napcat.out 无新时间戳 | QQ 根本没重启 | 先查进程再查日志 |


# QQ 多账号多实例方案（2026-10-31，Agent A 交付）

## 结论
proot 容器内多 QQ 实例**可行**，已落地为 account-manager 插件的多实例管理能力。

## PoC 关键发现
1. **HOME 隔离 QQ 数据**：HOME=/root/qq-accounts/<name> 时 QQ 数据落在 <HOME>/.config/QQ，与主实例 /root 完全隔离。
2. **NapCat 配置不随 HOME 走，随 NAPCAT_WORKDIR 走**：napcat.mjs 中 configPath = NAPCAT_WORKDIR 存在时取 (WORKDIR)/config，否则固定用安装目录 config（两实例会共享）。实测落盘：(WORKDIR)/{config,logs,cache}。
3. **WebUI 端口**：NAPCAT_WEBUI_PREFERRED_PORT 运行时优先于 webui.json 的 port 字段（webui.json 仍写 6099，实际监听 env 指定端口）。新实例 webui.json 自动生成随机 token，须从文件读取后调用 API。
4. **启动通道**：容器内 setsid 拉起的进程会随 proot 会话退出被杀；常驻实例必须由宿主侧 `proot-distro login napcat -- bash <script>` 拉起（与 mi8-napcat-start.sh 同方式）。
5. **插件真实加载目录是 napcat/plugins/（安装根目录下），不是 config/plugins/**（后者为历史遗留副本，改动无效）。部署必须 cp 到 napcat/plugins/。
6. **进程识别**：proot 内可遍历 /proc 读 environ，按 HOME 精确区分实例并 kill，不误伤主实例。
7. **资源**：每实例约 150-300MB；设备可用约 2.1G + swap，manager 上限设 3 个从实例。
8. **OneBot 配置**：扫码前 uin 未知；登录后 workdir/config/ 出现 napcat_<uin>.json，manager 轮询识别 uin → 写 onebot11_<uin>.json（分配 3002+）→ 重启实例生效，并设置 autoLoginAccount。

## 组件
- 容器内 /root/qq-accounts/manager.js：注册表(registry.json) + 编排库，插件动态 import；注册表 key 为实例名 acc-<webuiPort>，扫码后绑定 uin。
- 宿主 /data/adb/bin/mi8-account-bridge.sh：root 常驻轮询 (termux home)/qq-accounts/cmd/<name>.start 哨兵 → proot-distro 拉起实例；mi8-napcat-start.sh 末尾已加自启块（手机重启自动恢复）。
- 插件 plugins/account-manager/index.mjs：保留主实例 status/switch/add/set-auto，新增 GET /instances、POST /instance-add、/instance-start、/instance-stop；管理页含多实例卡片（状态/启停/扫码二维码）。

## 运维
- 添加账号：管理页「添加新账号（独立实例）」→ 出二维码 → 手机扫码 → 自动绑定 uin、写 OneBot 配置并重启该实例（OneBot 3002+ 上线），主实例不受影响。
- 停止/启动：管理页实例卡片按钮（停止只杀对应 HOME 的进程；主实例禁停）。
- registry：容器内 /root/qq-accounts/registry.json；端口分配见 next.onebot/webui/display。
- 已知现象：instance-add 偶发 {code:-1}，多为请求体被 shell 转义损坏（客户端问题），重试即可；插件日志无异常。
## 13. 多账号配置中心与 WebUI 设置页二开（2026-11-01）

### WebUI「设置」按钮二开

- 插件列表 API(/api/Plugin/List)新增字段 `settingsPage`(读 `package.json.napcat.settingsPage`)。
- 前端 bundle `static/assets/plugin-*.js` 已 patch:插件声明 settingsPage 时,「设置」直接跳转
  `/plugin/<id>/page/<settingsPage>`;未声明时回退原表单面板。
- 当前:account 与 autoreply 均声明 `settingsPage: "dashboard"`。
- 备份:napcat.mjs.bak-settingspage、plugin-*.js.bak-settingspage(NapCat 升级会被覆盖,需重打)。

### 账号配置中心(account 插件)

- `GET /plugin/napcat-plugin-account/api/account-rules?name=<实例名>&key=` —— 代理读取该实例
  autoreply 配置(主实例用插件配置 token,子实例读 workdir/config/webui.json)。
- `POST /plugin/napcat-plugin-account/api/account-rules {name, config, key}` —— 整份回写。
- 页面:每个运行中且已登录的实例卡片有「配置」按钮,内联编辑:私聊开关/兜底、
  关键词规则表(增删行)、群聊开关、仅@、冷却、随机延迟、群级规则 JSON。

### 群级规则(autoreply)

- 数据模型:`groupRules[groupId] = { enabled?, atOnly?, keywords? }`。
- 语义:未配置→全局行为;enabled=false→整群跳过;keywords 非空→仅用群规则;
  atOnly 群级覆盖全局。
- 编辑载体 `groupRulesText`(JSON)由结构化规则派生,非法 JSON 忽略不覆盖。

### 实例删除与二维码

- `POST /instance-delete {name}`:停止进程→清 registry→删数据目录;main 受保护。
- `POST /instance-qr {name}`:手动刷新二维码(唯一会请求 QQ 接口的操作)。
- 页面轮询 12s;实例状态 8s TTL 缓存;二维码图片走本地 PNG 代理
  (`/api/qr?name=`),不再持续请求 QQ。

### MCP

- 监听 `::`(IPv6 双栈):8899(CONFIG:~/.config 见 docs/ai-integration.md),可选 `MCP_TOKEN`。
- 工具 24 个:新增 qq_instances / qq_instance_add / qq_instance_start /
  qq_instance_stop / qq_instance_delete / qq_instance_qr。

## 15. 自动回复配置专页 + 正则匹配（2026-11-01）

### 新增能力

- 关键词规则 matchType 新增 **regex**（另含 contains/prefix），全局规则与群级规则都支持；
  正则超过 300 字符跳过，编译失败按不命中处理，忽略大小写。
- autoreply 新增独立配置专页：`/plugin/napcat-plugin-autoreply/page/settings`，
  出现在 WebUI「扩展页面」列表（QQ自动回复 / 自动回复配置）。
  页面含：总开关、私聊开关+兜底、关键词规则表（含正则）、群聊开关/仅@/冷却/延迟、
  群级规则 JSON。读写本插件 /api/config。
- 插件 settingsPage 改为 settings：插件管理里点「插件配置」直接进入该专页。
- 账号配置中心（account 插件）规则类型下拉同步支持「正则」。

### 注意

- 配置专页 HTML 由插件 init 时写盘（settings.html）并注册 page；
  页面 JS 采用事件委托、内联 toast（iframe 沙箱无 alert/confirm）。
- 部署后验证：page 200 + 内嵌 JS node --check 通过 + extensionPages 含 settings。

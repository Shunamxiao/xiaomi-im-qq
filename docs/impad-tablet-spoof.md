# 调研：让小米8（手机）运行平板版 QQ

- 日期：2026-10-31
- 设备：mi8（MI 8，Android 10 / MIUI 12.5，Magisk 28.1，root，arm64）
- 状态：调研完成，未实施

## 0. 实施进展（2026-10-31）

- 已完成：Zygisk 开启、LSPosed v2.2.1 安装（模块目录 `/data/adb/modules/zygisk_lsposed`）、
  ImPad 安装并注入 QQ `com.tencent.mobileqq:MSF`、QQ 升级到 9.3.70（16410，
  SHA-256 与官方候选一致）。
- 阻塞：ImPad 首次语义解析在 `MSF` 主线程同步读取 QQ APK 的 DEX 快照
  （376MB，Inflater+SHA-256），超过后台 ANR 窗口（`bg anr`，约 36-60 秒），
  `MSF` 被杀并无限重启（logcat 确认 4 次以上，间隔恒定 60 秒）。
  已修复两处前置问题：Android 10 的 `getPackageArchiveInfo` 不回填
  `signingInfo`（已在反编译层回退 legacy `signatures`，签名校验语义不变）。
- 临时处置：`modules_state.enabled=0` 停用 ImPad，QQ 以普通手机形态运行；
  重新启用需改回 `/data/adb/lspd/config/modules_config.db`。
- 剩余工程：需要把语义运行时移出主线程（上游级修复，需处理 libxposed
  拦截链的 continuation 语义）或离线生成 plan cache 预置
  （`/data/data/io.github.nku100.impad/files/impad/semantic-plan-cache/`，
  key 由 `SemanticPlanCacheKey.create()` 计算）。
- 自动回复改走 NapCat（与本机平板伪装互不依赖），见
  [qq-autoreply-napcat.md](qq-autoreply-napcat.md)。

## 1. 结论

**推荐方案：LSPosed（v2.2.1+，Zygisk）+ ImPad 模块 + QQ 9.3.70（16410）。**

ImPad 是一个开源 LSPosed 模块，通过 hook QQ 内部的设备类型判定
（`PadUtil` 相关方法，返回含 `TABLET` 的枚举），让 QQ 把本机识别为平板，
从而走平板登录通道（平板 AppID）并获得 iPad 风格的双栏布局。
该模块同时适配微信（平板登录）和企业微信。

这是目前唯一同时满足“不重打包 QQ、保留手机版其余功能、按构建精确验证兼容性”的方案。

## 2. 设备现状（2026-10-31 实测）

| 项 | 值 |
| --- | --- |
| Android / MIUI | 10 / V12.5.2.0.QEACNXM |
| Magisk | 28.1（`/data/adb/magisk/magisk`） |
| Zygisk | 未启用（magisk.db 无 zygisk 设置项） |
| LSPosed | 未安装 |
| 已装模块 | 无（`/data/adb/modules` 为空） |
| QQ | `com.tencent.mobileqq` 8.8.90（2828），旧版非 NT 架构 |
| 屏幕 | 1080x2248 @ 440dpi，smallestWidth ≈ 392dp |
| `ro.build.characteristics` | `nosdcard` |

## 3. 方案对比

### 3.1 ImPad + LSPosed（推荐）

- 原理：LSPosed 在 QQ 进程内 hook 设备类型判定，DexKit 语义定位，fail-closed。
- 已验证：QQ 9.3.1（14378）基线验收 + QQ 9.3.70（16410）徽章绿色“已兼容”。
- 效果：QQ 以平板 AppID（537360606）与服务器通信，可平板在线；UI 为平板布局。
- 成本：需开启 Zygisk、安装 LSPosed 与 ImPad、升级 QQ 到已验证版本。
- 限制见第 5 节。

### 3.2 App Settings Reborn（LSPosed，per-app DPI）

- 原理：单独修改 QQ 的 density / smallestWidth，让 `sw >= 600dp` 触发部分平板布局。
- 缺点：只骗过 UI 布局层，QQ 服务端仍按手机设备登录，无法获得“平板在线”/iPad 级交互；
  且 QQ 大量界面非资源限定符驱动，效果零散。不推荐作为主方案。

### 3.3 QQ HD（`com.tencent.qqhd`）

- 独立的旧版平板 QQ，早已停更（v5.x 时代）。2026 年登录通道能否使用无可靠证据，
  且无法获得 QQ9 功能。不推荐。

### 3.4 `resetprop ro.build.characteristics=tablet`

- QQ 判定平板不走系统 characteristics（源码证据：ImPad 定位的是 QQ 自有
  `PadUtil` 枚举判定），此方案对 QQ 无效，且全局副作用大。排除。

### 3.5 开发者选项全局最小宽度 ≥ 600

- 全局改密度，整个系统 UI 变成平板比例，影响 mi8 服务器日常使用（状态页、
  Termux 等）。可作临时体验，不作为交付方案。

## 4. 实施路径（待批准后执行）

1. Magisk 应用中开启 Zygisk，重启。
2. 安装 LSPosed v2.2.1（7912）Zygisk 版。本机可从 ImPad 发布页获取，
   SHA-256 `ed37de2d2097aa6ba871377e7bf4ac719b708484f9468928e6abae10ab311e74`。
   安装时需要联网请求 license（离线机需按提示走 bot 离线流程）。
3. 安装 ImPad v1.1.2（`impad-1.1.2-release.apk`，2026-10-30 发布）。
4. LSPosed 管理器启用 ImPad，作用域勾选 `com.tencent.mobileqq`
   （模块 `staticScope=true`，作用域固定为 QQ/微信/企微）。
5. 升级 QQ 到已验证构建：
   - 首选 9.3.70（16410），SHA-256
     `e8c28713b740c18246989d47916d9fbbad06d440778a5aab3f4e108a2423e55c`；
   - 或 9.3.1（14378），官方直链
     `https://downv6.qq.com/qqweb/QQ_1/android_apk/9.3.1_877e277185fb829c.apk`，
     SHA-256 `ccf9714d268662ee8311add537705e66dee3367d45a20eabbf57b0070949595e`。
   - 现装 8.8.90 不受支持，必须升级；覆盖升级保留聊天数据。
6. 强行停止 QQ 后重开，验证登录界面/布局是否为平板形态，账号是否“平板在线”。
7. 在 QQ 设置中关闭应用内自动更新，避免升到未验证构建。

## 5. 风险与限制

- ImPad 官方验收是**无账号**运行验证（ReDroid + 固定回归）。作者明确声明：
  不证明真实账号登录、服务端接受或双设备会话共存。真实账号效果需实测。
- 模块 fail-closed：QQ 版本变化导致规则不匹配时，不安装 hook，QQ 恢复手机形态，
  不会崩溃；此时需回退 QQ 版本或等待 ImPad 更新规则。
- QQ 热更新（Tinker）可能改变目标方法；ImPad 对 QQ 以 APK/分包为输入重新解析，
  但不承诺覆盖任意热补丁。
- LSPosed v2.x 为新世代框架，安装需 license 请求步骤；本机有网，问题不大。
- 登录态：QQ 升级为覆盖安装，保留数据；但换设备类型登录可能触发安全验证
  （需重新验证设备），属预期行为。
- 微信若也想平板登录，同一模块作用域加 `com.tencent.mm` 即可
  （已验证微信 8.0.69/8.0.79）。

## 6. 证据来源

| 证据 | 位置 |
| --- | --- |
| ImPad 仓库与 README | github.com/NKU100/ImPad（MIT 之外为 GPL-3.0） |
| QQ 适配徽章 | `compatibility-badge-data/qq.json`：9.3.70 · 已兼容 |
| 基线验收记录 | ImPad `docs/support.md`（QQ 9.3.1 run 39555436990） |
| hook 原理 | ImPad `compat-rules/.../qq/QqDeviceTypeRule.kt`（PadUtil 锚点 + TABLET 枚举） |
| 框架要求 | ImPad `module.prop`：minApiVersion=102；LSPosed v2.2.1 zip：Android 9~17、Magisk 27+、Zygisk |
| 本机状态 | magisk.db、`/data/adb/modules`、`pm list packages`（2026-10-31 实测） |
| 本地克隆 | `work/ImPad/`（含源码与验证脚本） |

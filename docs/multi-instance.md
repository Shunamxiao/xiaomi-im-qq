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
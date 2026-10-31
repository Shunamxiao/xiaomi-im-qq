# 自动回复：NapCat 方案（已部署框架）

- 日期：2026-10-31
- 设备：mi8（Termux + proot-distro Debian 容器 `napcat`）

## 1. 选型

QQ 9.x（NT 架构）没有可用的 Xposed 自动回复模块（QAuxiliary 只有开放的功能请求，
XAutoDaily 是签到向）。选择 **NapCat 4.18.42**（基于 NTQQ 的协议端实现，
OneBot 11 接口）：官方支持 Termux（proot-distro Debian + xvfb + QQ Linux arm64），
与 ImPad 平板伪装互不依赖——NapCat 以独立设备登录，手机 QQ 不受影响。

## 2. 已完成

1. `astermux` 身份下执行官方 `install.termux.sh`：
   proot 容器 `napcat`（debian:bookworm-slim）+ QQ Linux arm64 + NapCat Shell。
2. 绕过 `xvfb-run` 在 proot 下的 `expected absolute path` bug：
   手动 `Xvfb :0 &` + `DISPLAY=:0 qq --no-sandbox`。
3. 以 Termux 侧 `screen -dmS napcat` 脱管启动（SSH 断开不影响）。
4. WebUI 已确认：`http://[::]:6099/webui?token=b97e257658fe`。

### 审计修复（2026-10-31 第二轮）

审计发现 Termux 用户身份的 NapCat 进程被 MIUI 静默清理（playbook 红线）。
已改为 **root 常驻** 并解决三连问题：

1. root 下 Chromium 的 `/dev/shm`（bind 到 Termux 属主目录）访问被拒 →
   `chmod 1777` shm 目录 + `--disable-dev-shm-usage`。
2. root(magisk 域) 对 app_data_file 的 SELinux 拒绝 →
   `magiskpolicy --live` 注入 magisk 域对 app_data_file 的读写执行规则
   （启动脚本内幂等重放）。
3. `pgrep -f` 自匹配导致启动脚本假阳性跳过 → 方括号技巧 `[o]pt`。

持久化：`/data/adb/service.d/mi8-napcat.sh`（开机自启，调
`/data/adb/bin/mi8-napcat-start.sh`）。启动参数已带
`--disable-dev-shm-usage`。验证：进程存活、`:6099` 监听、WebUI 返回 301。

## 3. 登录与设备伪装（已完成）

- 扫码登录成功（账号 1457278554），Quick Login 已写入启动参数
  （`-q 1457278554`），重启免扫码。
- 设备名伪装：容器内 proot 把 uname nodename 硬编码为 "localhost"，
  已用容器内自编译的 LD_PRELOAD 库（`/usr/lib/fakehost.so`，hook
  gethostname/uname）伪装为 **ZAIDU-5466**（本机电脑名）。
  QQ 登录设备管理里应显示为 ZAIDU-5466（可在手机QQ 设置→账号安全 里核对）。

## 4. 自动回复机器人（已部署，默认关闭）

- OneBot 正向 WS：`127.0.0.1:3001`（仅本机，已写入
  `onebot11_1457278554.json`）。
- 机器人：容器内 `/root/autoreply.js`（OneBot 11 WS 客户端，nodejs），
  规则文件 `/root/autoreply-rules.json`，日志 `/root/autoreply.log`。
- **默认 enabled=false**（部署时账号正在人工聊天，避免误回复）。
  开启：把 rules 里 `enabled` 改为 `true`，机器人热重载规则，无需重启。
- 规则结构：`keywords`（关键词→回复）、`private.defaultReply`（私聊兜底）、
  `private/group.whitelist`（白名单，空=全部）、`group.enabled`（群聊开关）。

## 5. 风险

- NapCat 属协议端机器人，账号存在被风控/限制的可能（官方文档亦有提示）；
  建议用小号验证后再上主力号。
- 与手机 QQ 同时在线：QQ 允许手机+平板/电脑多端；NapCat 登录后注意
  多端消息同步行为。

## 6. 关键路径

| 项 | 值 |
| --- | --- |
| 容器 | `proot-distro login napcat` |
| NapCat 日志 | 容器内 `/root/napcat.out` |
| WebUI | `http://[::]:6099/webui` token `b97e257658fe` |
| 启动脚本 | `/data/adb/bin/mi8-napcat-start.sh`（root 常驻 + 机器人自启） |
| 开机自启 | `/data/adb/service.d/mi8-napcat.sh` |
| 机器人日志 | 容器内 `/root/autoreply.log` |
| 配置目录 | 容器内 `/root/Napcat/opt/QQ/resources/app/app_launcher/napcat/config` |

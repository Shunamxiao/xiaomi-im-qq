# mi8 QQ 云端 Agent — 运维手册（Runbook）

> 适用设备：小米8 root（MIUI）+ Termux + proot Debian 容器 `napcat`
> 最后更新：2026-11-01

## 1. 拓扑与端口

| 组件 | 位置 | 端口 | 说明 |
| --- | --- | --- | --- |
| 主 QQ 实例 | 容器内 HOME=/root | OneBot 3001 / WebUI 6099 | 账号 1457278554 |
| 子实例 | /root/qq-accounts/acc-* | OneBot 3003+ / WebUI 6102+ | 独立数据目录 |
| 插件调试服务 | napcat-plugin-debug | 127.0.0.1:8998 | JSON-RPC 热重载 |
| MCP Server | Termux qq-mcp/ | ::8899 | Agent 接入，可选 MCP_TOKEN |
| 状态页 | Termux 侧 | 8800 | http://mi.theworld.codes:8800/ |

统一入口（主实例 WebUI）：`http://<局域网IP>:6099/webui?token=x524980520`

## 2. 日常运维命令

```bash
# 健康检查（进程 / 端口 / 插件加载 / 登录状态）
ssh mi8 "ps -eo pid,args | grep 'Napcat/opt/QQ/qq' | grep -v grep | wc -l"
ssh mi8 "ss -tln | grep -E ':6099|:3001|:8899'"
ssh mi8 "grep -aE 'Loaded|快速登录成功' /data/data/com.termux/files/usr/var/lib/proot-distro/containers/napcat/rootfs/root/napcat.out | tail -3"

# 重启主实例（脚本自带 setsid，必须前台执行）
ssh mi8 "pkill -f 'Napcat/opt/QQ/qq'; sleep 2; bash /data/adb/bin/mi8-napcat-start.sh"

# 热重载一个插件（改了代码但不改路由时）
ssh mi8 "/data/adb/bin/astermux proot-distro login napcat -- node /root/reload-plugin.js napcat-plugin-autoreply"
```

注意：`pkill -f 'Napcat/opt/QQ/qq'` 会同时杀掉子实例；重启后需要用 API 恢复：

```bash
curl -s -X POST 'http://127.0.0.1:6099/plugin/napcat-plugin-account/api/instance-start?key=x524980520' \
  -H 'Content-Type: application/json' -d '{"name":"acc-6102"}'
```

## 3. 插件部署流程

```bash
# 1) 构建（本地仓库 D:\xiaomi-im-qq\plugin）
cd D:\xiaomi-im-qq\plugin && pnpm build

# 2) 上传 + 容器内覆盖
scp dist/index.mjs dist/package.json mi8:/data/data/com.termux/files/home/autoreply-dist/
ssh mi8 "/data/adb/bin/astermux proot-distro login napcat -- sh -c 'cp /data/data/com.termux/files/home/autoreply-dist/index.mjs /root/Napcat/opt/QQ/resources/app/app_launcher/napcat/plugins/napcat-plugin-autoreply/'"

# 3) 重启
ssh mi8 "pkill -f 'Napcat/opt/QQ/qq'; sleep 2; bash /data/adb/bin/mi8-napcat-start.sh"
```

插件目录（真实加载路径）：`/root/Napcat/opt/QQ/resources/app/app_launcher/napcat/plugins/<plugin-id>/`
启用开关：同级 `config/plugins.json`（`{"napcat-plugin-autoreply":true}`）。

## 4. 配置管理

- 自动回复：WebUI → 扩展页面 → 「自动回复配置」，或 `POST /plugin/napcat-plugin-autoreply/api/config`
- 多账号规则：账号管理页每个实例的「配置」按钮（跨实例读写）
- 账号自动登录：`config/webui.json` 的 `autoLoginAccount`
- WebUI 密钥：`config/webui.json` 的 `token`（固定 x524980520）

## 5. 账号与实例

| 操作 | 方式 |
| --- | --- |
| 添加账号 | 账号管理页「+ 添加新账号实例」→ 扫卡片上的二维码 |
| 停止 / 启动 | 卡片按钮（停止需二次确认） |
| 删除实例 | 卡片「删除」（不可恢复；main 不可删） |
| 刷新二维码 | 卡片「刷新二维码」（唯一会请求 QQ 接口的操作） |
| 切换主实例账号 | 历史账号「切换」（会重启主实例） |

实例注册表：容器内 `/root/qq-accounts/registry.json`；宿主桥：`/data/adb/bin/mi8-account-bridge.sh`（轮询哨兵拉起实例，开机自启）。

## 6. 故障排查速查

| 现象 | 常见原因 | 处理 |
| --- | --- | --- |
| QQ 进程为 0，日志不刷新 | 启动失败/残留 Xvfb 容器 | 清理孤儿 proot 后重跑启动脚本 |
| 插件 Scanned N, Loaded 0 | `plugins.json` 缺启用键 / package.json name 不匹配 | 修正启用键与 name |
| 页面白屏 | 插件页面内嵌 JS 语法错误 | 提取 <script> 跑 node --check |
| 页面仍是旧版 | 浏览器缓存入口 HTML | Ctrl+F5 强刷（服务端已加 no-cache） |
| 设置按钮不跳转 | bundle 补丁被 NapCat 升级覆盖 | 重新打 patch（备份 .bak-settingspage） |
| 子实例起不来 | GPU 进程崩溃 | instance.sh 已带 --disable-gpu；检查日志 |
| 子实例插件不加载 | workdir 缺 plugins.json/plugins 目录 | 启动前已自动补齐 |
| 扫码后页面仍显示未登录 | 实例重启后凭证失效 | 已自动重登录；若仍异常重启插件 |
| 无法写 qq-accounts | 目录属主是宿主席 root | `ssh mi8 "chown -R 10290:10290 .../rootfs/root/qq-accounts"` |
| 8801 被占 | transmission-daemon | MCP 使用 8899 |
| MCP 起不来 | setsid 被回收 | 用 nohup ... & disown |
| 两个号同群双回复 | 防重复关闭/claim 目录不可写 | 确认 crossAccountDedup=true 且 /root/qq-accounts/shared 可写 |

日志位置：
- 主实例：容器内 `/root/napcat.out`（宿主 rootfs 对应路径）
- 子实例：`/root/qq-accounts/<name>/instance.log`
- MCP：Termux `/data/data/com.termux/files/home/qq-mcp/server.log`

## 7. 备份与恢复

```bash
# 备份关键数据（配置 + 实例注册表 + 插件配置）
ssh mi8 "tar czf /data/data/com.termux/files/home/mi8-qq-backup-$(date +%F).tgz \
  /data/data/com.termux/files/usr/var/lib/proot-distro/containers/napcat/rootfs/root/Napcat/opt/QQ/resources/app/app_launcher/napcat/config \
  /data/data/com.termux/files/usr/var/lib/proot-distro/containers/napcat/rootfs/root/qq-accounts/registry.json"
```

QQ 登录态在 `/root/.config/QQ`，迁移/重装前建议一并备份（体积大，按需）。

## 8. 安全与风控

- WebUI 6099 对公网 IPv6 开放，密钥固定为 x524980520，建议加 IP 白名单（webui.json `ipWhitelist`）。
- MCP 8899 支持 `MCP_TOKEN` 环境变量鉴权，公网暴露前务必开启。
- 自动回复：冷却 + 随机延迟 + 同群防重复已默认开启；避免高频发图（建议 ≤5 张/分钟）。
- 同 IP 多账号有连带风控风险（NapCat 官方口径），重要账号建议与常用号分开设备/IP。

## 9. 常用 API（本机）

- 插件配置：`GET/POST /plugin/<plugin-id>/api/config`
- 账号状态：`GET /plugin/napcat-plugin-account/api/instances?key=...`
- 跨实例规则：`GET/POST /plugin/napcat-plugin-account/api/account-rules`
- 二维码图：`GET /plugin/napcat-plugin-account/api/qr?key=...&name=<实例>`

## 10. 在 mi8 上直接开发（异地 Agent 用）

本机电脑不可用时，全部操作可在 mi8 上完成（容器内已具备 node18/git/npm/pnpm9）。

### 仓库位置（容器内）

- 主仓库：`/root/dev/xiaomi-im-qq`
- 插件仓库：`/root/dev/plugin`

首次准备（已做过，重建时参考）：

```bash
/data/adb/bin/astermux proot-distro login napcat -- bash -c \
  'mkdir -p /root/dev && cd /root/dev && \
   git clone --depth 1 https://github.com/Shunamxiao/xiaomi-im-qq.git && \
   git clone --depth 1 https://github.com/Shunamxiao/napcat-plugin-template.git plugin && \
   npm i -g pnpm@9'
```

### 构建与部署（全部在容器内）

```bash
# 安装依赖(首次或 package.json 变化后)
/data/adb/bin/astermux proot-distro login napcat -- bash -c 'cd /root/dev/plugin && pnpm install'

# 构建
/data/adb/bin/astermux proot-distro login napcat -- bash -c 'cd /root/dev/plugin && pnpm build'

# 部署到运行中的插件目录
/data/adb/bin/astermux proot-distro login napcat -- bash -c \
  'cp /root/dev/plugin/dist/index.mjs /root/dev/plugin/dist/package.json \
   /root/Napcat/opt/QQ/resources/app/app_launcher/napcat/plugins/napcat-plugin-autoreply/'

# 重启(宿主侧)
ssh mi8 "pkill -f 'Napcat/opt/QQ/qq'; sleep 2; bash /data/adb/bin/mi8-napcat-start.sh"
```

注意：`pnpm-workspace.yaml` 必须包含 `packages: ["."]`（pnpm9 要求）；两处
（plugin 与 src/webui）都已配置。

### 提交与推送

- 容器内可 `git add/commit`；`git push` 需要 GitHub 凭据（PAT），由负责人提供或代推。
- 提交前必须通过：构建成功 + 部署验证（Loaded 3 plugins / 页面 200 / JS 语法检查）。

### 从异地设备（只连 mi8）的最短上手

1. `ssh mi8`
2. `/data/adb/bin/astermux proot-distro login napcat -- bash -c 'ls /root/dev'`
3. 读 `/root/dev/xiaomi-im-qq/docs/handoff.md` 与 `ops-runbook.md`
4. 按上面命令改码、构建、部署

## 11. 2026-11-08 故障记录：inst1=unreachable / device=down

### 现象

- 公网 /health=200，服务=active，但 device=down、inst1=unreachable
- mi8 上 QQ 进程数 0，3001/6099 均未监听

### 已处理

1. **inst1 恢复**：NapCat 完全未运行；执行
   `bash /data/adb/bin/mi8-napcat-start.sh`（通过 account-bridge 哨兵拉起 acc-main），
   之后 3001/6099 监听、QQ 1457278554 自动登录、Loaded 3 plugins。
   - 该脚本 2026-11-08 已改为「只拉 acc-main」，主实例即 `acc-main`（registry 中
     onebotPort=3001 / webuiPort=6099）。bridge 会把哨兵 `main.start` 映射到 `acc-main`。
2. **公网链路确认**：本机通过公网 IPv6 `ssh -p 8022 u0_a290@[2408:8256:1480:4c41:...]`
   成功；`mi.theworld.codes` 的 AAAA 已指向该地址。
3. **容器 DNS**：原 `/etc/resolv.conf` 为 8.8.8.8/8.8.4.4（UDP 53 被拒），
   已改为 `192.168.1.1 / 223.5.5.5 / 240c::6666`。

### 待服务端处理

- `device=down`：服务端（疑似 `eu-dev@vmi3675503` VPS）SSH 到 mi8 失败。
  排查：`getent ahosts mi.theworld.codes`、`ssh -v -p 8022 u0_a290@mi.theworld.codes`。
  若是写死旧 IPv6（2408:8256:1475:... 已变为 1480:4c41:...），改为域名并重启服务。

### 已知环境限制（2026-11-08）

- 容器内 node/curl 直连公网域名（含 198.18.0.4 fake-ip、公共 DNS、Cloudflare IP）
  均超时，仅 QQ 与 cloudflared 的既存通道可用；宿主 Termux 侧 curl 正常（200）。
  影响：autoreply 插件与 qqbot.theworld.codes 的云端同步/上报可能失败，
  群内自动回复不受影响。
- 若要恢复插件云同步：给容器内 node 增加可用代理通道，或让服务端改为推送模式。

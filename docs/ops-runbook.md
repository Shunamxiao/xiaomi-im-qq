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

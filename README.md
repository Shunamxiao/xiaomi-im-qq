# xiaomi-im-qq

小米8（mi8）上的 QQ 机器人栈：NapCat（OneBot 11）+ 自动回复机器人，
以及 QQ 平板版（ImPad）伪装的调研与半成品修复。

## 结构

| 路径 | 内容 | 设备上位置（容器内） |
| --- | --- | --- |
| `bot/autoreply.js` | 自动回复机器人（OneBot 11 WS 客户端） | `/root/autoreply.js` |
| `bot/autoreply-rules.json` | 回复规则（总开关/关键词/白名单） | `/root/autoreply-rules.json` |
| `deploy/mi8-napcat-start.sh` | root 常驻启动脚本（QQ+机器人，幂等） | `/data/adb/bin/mi8-napcat-start.sh` |
| `deploy/mi8-napcat-service.sh` | Magisk service.d 开机自启包装 | `/data/adb/service.d/mi8-napcat.sh` |
| `deploy/fakehost.c` | 容器内主机名伪装 LD_PRELOAD 源码 | `/usr/lib/fakehost.so`（容器内编译） |
| `deploy/onebot11-config.json` | OneBot 正向 WS 配置（仅 127.0.0.1:3001） | `napcat/config/onebot11_1457278554.json` |
| `tests/` | 单元测试 + 设备冒烟测试 | — |
| `docs/` | 部署文档与 ImPad 调研 | — |

## 运行状态（2026-10-31）

- NapCat 4.18.42 在线（账号 1457278554，快速登录 `-q`，重启免扫码）。
- 登录设备名伪装为 **ZAIDU-5466**（容器内 LD_PRELOAD hook gethostname/uname）。
- 自动回复机器人已连接 OneBot，**默认 enabled=false**（避免误回复）；
  开启：`bot/autoreply-rules.json` 里 `enabled` 改 `true`，热重载立即生效。
- QQ 平板伪装（ImPad）受阻于后台 ANR，模块已停用，详见 docs。

## 测试

```bash
# 单元测试（任意有 node 的机器）
node tests/test-matchreply.js

# 设备冒烟测试（在 mi8 上执行）
ssh mi8 'bash -s' < tests/smoke-napcat.sh
```

## 机器人规则

`autoreply-rules.json`：

```json
{
  "enabled": false,
  "keywords": [{ "match": "在吗", "reply": "在的，有事请留言～" }],
  "private": { "enabled": true, "defaultReply": "【自动回复】...", "whitelist": [] },
  "group": { "enabled": false, "whitelist": [] }
}
```

- `enabled`：总开关，false 时只记录不回复；
- `keywords`：包含匹配，命中即回复（优先于兜底）；
- `private.defaultReply`：私聊未命中关键词时的兜底回复；
- `whitelist`：QQ 号白名单，空数组 = 对所有人生效；
- 群聊需 `group.enabled=true` 且命中关键词或白名单。

修改后机器人热重载，无需重启。

## 部署同步（repo → mi8）

```bash
scp bot/autoreply.js bot/autoreply-rules.json mi8:/data/local/tmp/
ssh mi8 'cp /data/local/tmp/autoreply.js /data/local/tmp/autoreply-rules.json /data/data/com.termux/files/home/'
ssh mi8 '/data/adb/bin/astermux proot-distro login napcat -- cp /data/data/com.termux/files/home/autoreply.js /data/data/com.termux/files/home/autoreply-rules.json /root/'
ssh mi8 '/system/bin/am force-stop com.tencent.mobileqq; pkill -f "[o]pt/QQ/qq"; /data/adb/bin/mi8-napcat-start.sh'
```

## 风险

- NapCat 属协议端机器人，账号有被风控可能，建议小号先行。
- 修改包/机器人登录违反 QQ 服务条款，自担风险。

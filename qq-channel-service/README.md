# qq-channel-service

QQ 频道帖子发布服务（逆向还原）。

## 架构

```
POST /publish (HTTP, :8901)
        │
        ├─ via=web   → pd.qq.com gotrpc 网关直发（不依赖真机）
        │              凭证: credentials-web.json（浏览器扫码登录的 p_skey，约6天有效）
        ├─ via=native → WSL python bridge (stdio JSON 行协议)
        │              → Frida 16.5.9 注入 mi8 QQ 主进程
        │              → kernel feedService.publishFeed()（主线程调用+两阶段回调）
        └─ via=auto   → web 凭证有效优先走 web，否则 native
```

## API

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | /status | native 通道就绪状态（{ok, ready, pskeyLen}） |
| GET | /credentials | native 通道凭证（pskey/uin/tinyid），本地联调用 |
| POST | /publish | 发帖。body: `{guildId, channelId, text, posterId?, markdown?, images?, channel?('auto'\|'web'\|'native')}` |

- `markdown: true` — text 按 Markdown 解析转为频道富文本段落（标题/列表/代码块/引用降级排版）
- `images` — jsonFeed images 数组（图片上传链路见下）
- 返回 `{ok, via, idd}`，idd 为服务端分配的帖子 ID

## 风控审计结论（2026-11-15 对比真实客户端样本）

native 通道请求现已对齐真实客户端指纹：

- clientkey 格式 `GuildNativeMain_<ms>_<uuid4>`（原 NodeSvc_ 前缀为可检测异常）
- extInfo 补齐 11 条（sgrp_* 来源埋点、feed_source_json、sec_flag_v1 行为特征串）
- req.isSquare=false（与真实客户端一致，jsonFeed 内保持 is_square=true）
- 服务端限频：最小发布间隔 10s
- poster.id 默认本人 tinyid，不要传他人 id（冒充发帖会被风控/封禁）

## 协议关键结论

- 发布协议 `trpc.qchannel.commwriter.ComWriter.PublishFeed`；安卓端走 MSF 长连接 SSO 隧道，
  web 端走 pd.qq.com gotrpc 网关（JSON body）
- 鉴权：web=cookie p_skey(pd.qq.com)+uin=o<tinyid>；native=pskey+appid 537395438，签名/加密全在 native 内核
- **Android 端 pskey 与 web 端 p_skey 为不同登录态签发，不可互用**（实测 Android pskey 走 web 网关返回 retcode=4003）
- 发布成功两要素：主线程调用（native）+ 等待第二阶段回调拿 feed.idd
- `GProStRichText` 含 isMarkdown/sourceMarkdown 字段，频道原生支持 Markdown（通道待验证）

## web 凭证获取（通道 B 部署）

1. 浏览器打开 https://pd.qq.com 扫码登录
2. 登录后导出 cookie `p_skey` 与 tinyid，写入 `credentials-web.json`：
   ```json
   {"pskey":"...", "tinyid":"144115...", "updatedAt":"2026-11-15T20:00:00+08:00"}
   ```
3. pskey 过期（约 6 天）重新扫码更新；`/publish` 返回过期提示时服务不中断 native 通道

## 目录

- `server.js` — HTTP 服务（:8901）
- `bridge/bridge.py` — WSL frida 桥宿主（stdio JSON 行协议）
- `bridge/agent.js` — Frida 注入 agent（多 ClassLoader、回调单例、风控对齐）
- `lib/gateway.js` — web 网关直发模块
- `lib/markdown.js` — Markdown → 频道段落转换器
- `../tools/frida-client/` — 抓包/探针/手动 invoke 诊断脚本（invoke_publish.js 为参照实现）
- `../tools/jadx-driver/` — jadx-core 批量反编译驱动及产物

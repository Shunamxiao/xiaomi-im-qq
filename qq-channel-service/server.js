'use strict';
/**
 * QQ 频道发帖服务 — Node.js HTTP API
 *
 * POST /publish  {guildId, channelId, text, posterId?}  -> {ok, idd?}
 * GET  /status   -> {ok, ready, pskeyLen}
 *
 * 依赖: WSL 内 frida 客户端 (bridge/bridge.py) + mi8 上运行的 QQ。
 * 桥进程通过 stdio JSON 行协议与 Node 通信。
 */
const { spawn } = require('child_process');
const http = require('http');
const path = require('path');
const { publishFeed: webPublish, loadCredentials } = require('./lib/gateway');
const { mdToParagraphs } = require('./lib/markdown');
const { publishViaBrowser } = require('./lib/webui');

const PORT = 8901;
const POSTER_ID = process.env.QQ_POSTER_ID || '144115219939370567'; // 账号在频道内的 tinyid

// ---------- bridge ----------
const bridge = spawn('wsl.exe', ['-e', 'python3', '/mnt/d/xiaomi-im-qq/qq-channel-service/bridge/bridge.py'], {
  stdio: ['pipe', 'pipe', 'pipe']
});
let nextId = 1;
const pending = new Map();

bridge.stdout.setEncoding('utf8');
let buf = '';
bridge.stdout.on('data', (d) => {
  buf += d;
  let idx;
  while ((idx = buf.indexOf('\n')) >= 0) {
    const line = buf.slice(0, idx).trim();
    buf = buf.slice(idx + 1);
    if (!line) continue;
    let msg;
    try { msg = JSON.parse(line); } catch { continue; }
    if (msg.event) { console.log('[bridge]', msg.data); continue; }
    if (msg.id !== undefined && pending.has(msg.id)) {
      pending.get(msg.id)(msg);
      pending.delete(msg.id);
    }
  }
});
bridge.stderr.setEncoding('utf8');
bridge.stderr.on('data', (d) => console.error('[bridge-err]', d.trim()));

function callBridge(action, args, timeoutMs = 65000) {
  const id = nextId++;
  return new Promise((resolve, reject) => {
    pending.set(id, (msg) => {
      if (msg.ok) resolve(msg.result);
      else reject(new Error(msg.error));
    });
    bridge.stdin.write(JSON.stringify({ id, action, args }) + '\n');
    setTimeout(() => {
      if (pending.has(id)) { pending.delete(id); reject(new Error('bridge timeout')); }
    }, timeoutMs).unref();
  });
}

// ---------- http ----------
const server = http.createServer(async (req, res) => {
  const url = new URL(req.url, 'http://localhost');
  res.setHeader('Content-Type', 'application/json; charset=utf-8');
  try {
    if (req.method === 'GET' && url.pathname === '/status') {
      const r = await callBridge('status', []);
      res.end(JSON.stringify({ ok: true, ...r }));
      return;
    }
    if (req.method === 'GET' && url.pathname === '/credentials') {
      // 本地联调用：返回 web 网关直发所需凭证（pskey/uin/tinyid）
      const r = await callBridge('credentials', []);
      res.end(JSON.stringify({ ok: true, ...r }));
      return;
    }
    if (req.method === 'POST' && url.pathname === '/publish') {
      const body = await readBody(req);
      const p = JSON.parse(body);
      if (!p.guildId || !p.channelId || !p.text) {
        res.statusCode = 400;
        res.end(JSON.stringify({ ok: false, error: 'guildId, channelId, text are required' }));
        return;
      }
      // 通道选择: auto(默认)=native 优先(全能力:中文/Markdown/图片结构),
      // channel='browser'=浏览器 UI 代理(不依赖真机,ASCII 文本+图片文件),
      // channel='web'=网关直发(需 credentials-web.json)
      const channel = p.channel || 'auto';
      if (channel === 'browser') {
        const r = publishViaBrowser({
          guildSlug: p.guildSlug || 'apksccbox9527',
          text: p.text,
          images: p.imageFiles || []
        });
        res.end(JSON.stringify(r));
        return;
      }
      let result;
      if (channel === 'web' || channel === 'auto') {
        const cred = loadCredentials();
        if (channel === 'web' || (cred && cred.pskey && !cred.stale)) {
          const extra = {};
          if (p.markdown) {
            extra.content_with_style = { paragraphs: mdToParagraphs(p.text) };
          }
          result = await webPublish({
            guildId: p.guildId, channelId: p.channelId, text: p.text,
            tinyid: p.posterId, images: p.images, extraFeedFields: extra
          });
          if (result.ok) { res.end(JSON.stringify({ via: 'web', ...result })); return; }
          if (channel === 'web') {
            res.statusCode = 502;
            res.end(JSON.stringify({ ok: false, via: 'web', ...result }));
            return;
          }
          // auto: web 失败回退 native
        }
      }
      // native 通道同样支持 markdown：段落转换在 Node 侧完成，agent 只负责组装
      try {
        const paragraphs = p.markdown ? mdToParagraphs(p.text) : null;
        const r = await callBridge('publish', [p.guildId, p.channelId, p.text, p.posterId || POSTER_ID, p.images || [], paragraphs]);
        const published = r && r.idd;
        if (published) {
          res.end(JSON.stringify({ ok: true, via: 'native', ...r }));
          return;
        }
        if (channel === 'native') {
          res.statusCode = 502;
          res.end(JSON.stringify({ ok: false, via: 'native', ...r }));
          return;
        }
        // auto: native 失败（如真机离线）→ browser UI 兜底
        res.end(JSON.stringify({
          ok: true, via: 'browser-ui', nativeAttempt: r,
          ...publishViaBrowser({ guildSlug: p.guildSlug || 'apksccbox9527', text: p.text, images: p.imageFiles || [] })
        }));
      } catch (nativeErr) {
        if (channel === 'native') throw nativeErr;
        res.end(JSON.stringify({
          ok: true, via: 'browser-ui', nativeError: String(nativeErr.message || nativeErr),
          ...publishViaBrowser({ guildSlug: p.guildSlug || 'apksccbox9527', text: p.text, images: p.imageFiles || [] })
        }));
      }
      return;
    }
    res.statusCode = 404;
    res.end(JSON.stringify({ ok: false, error: 'not found' }));
  } catch (e) {
    res.statusCode = 500;
    res.end(JSON.stringify({ ok: false, error: String(e.message || e) }));
  }
});

function readBody(req) {
  return new Promise((resolve, reject) => {
    let b = '';
    req.on('data', (c) => { b += c; });
    req.on('end', () => resolve(b));
    req.on('error', reject);
  });
}

server.listen(PORT, () => console.log(`qq-channel-service listening on :${PORT}`));

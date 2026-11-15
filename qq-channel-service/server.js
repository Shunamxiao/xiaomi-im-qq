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
    if (req.method === 'POST' && url.pathname === '/publish') {
      const body = await readBody(req);
      const p = JSON.parse(body);
      if (!p.guildId || !p.channelId || !p.text) {
        res.statusCode = 400;
        res.end(JSON.stringify({ ok: false, error: 'guildId, channelId, text are required' }));
        return;
      }
      const r = await callBridge('publish', [p.guildId, p.channelId, p.text, p.posterId || POSTER_ID]);
      const published = r && r.idd;
      res.statusCode = published ? 200 : 502;
      res.end(JSON.stringify({ ok: !!published, ...r }));
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

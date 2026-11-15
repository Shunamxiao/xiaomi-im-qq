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
const { spawn, execFileSync } = require('child_process');
const http = require('http');
const fs = require('fs');
const path = require('path');
const { publishFeed: webPublish, loadCredentials } = require('./lib/gateway');
const { mdToParagraphs } = require('./lib/markdown');
const { publishViaBrowser } = require('./lib/webui');

const PORT = 8901;
const POSTER_ID = process.env.QQ_POSTER_ID || '144115219939370567'; // 账号在频道内的 tinyid

// ---------- bridge ----------
// wsl.exe 里的 bridge.py 可能随 WSL VM 回收/frida 异常而退出——监听 exit 自动重启，
// 否则请求会写入死管道，一直挂到 65s 超时。
let bridge = null;
let bridgeReady = false;
let nextId = 1;
const pending = new Map();

function failPending(reason) {
  for (const cb of pending.values()) cb({ ok: false, error: reason });
  pending.clear();
}

function startBridge() {
  bridgeReady = false;
  bridge = spawn('wsl.exe', ['-e', 'python3', '/mnt/d/xiaomi-im-qq/qq-channel-service/bridge/bridge.py'], {
    stdio: ['pipe', 'pipe', 'pipe']
  });
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
      if (msg.event) {
        if (msg.data && msg.data.indexOf('attached') === 0) bridgeReady = true;
        console.log('[bridge]', msg.data);
        continue;
      }
      if (msg.id !== undefined && pending.has(msg.id)) {
        pending.get(msg.id)(msg);
        pending.delete(msg.id);
      }
    }
  });
  bridge.stderr.setEncoding('utf8');
  bridge.stderr.on('data', (d) => console.error('[bridge-err]', d.trim()));
  bridge.on('exit', (code) => {
    console.error('[bridge] exited code=' + code + ' — respawn in 3s');
    failPending('bridge exited');
    bridgeReady = false;
    setTimeout(() => { if (!bridgeReady) startBridge(); }, 3000);
  });
}
startBridge();

async function waitBridgeReady(timeoutMs) {
  const t0 = Date.now();
  while (!bridgeReady) {
    if (Date.now() - t0 > timeoutMs) throw new Error('bridge not ready (wsl/frida attach failed)');
    await new Promise((r) => setTimeout(r, 300));
  }
}

async function callBridge(action, args, timeoutMs = 65000) {
  await waitBridgeReady(timeoutMs);
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
    if (req.method === 'POST' && url.pathname === '/upload-image') {
      // body: {fileName, width?, height?, dataBase64}
      // 流程: 写本地临时文件 → adb push 到手机 → kernel guildFileTransferService 上传
      // 返回 {ok, taskId, url, svrFileId, md5} 供 /publish images 使用
      const p2 = JSON.parse(await readBody(req));
      if (!p2.dataBase64 || !p2.fileName) {
        res.statusCode = 400;
        res.end(JSON.stringify({ ok: false, error: 'fileName, dataBase64 are required' }));
        return;
      }
      // 自动探测装有 QQ 的设备（USB/网络 adb 可能换口），env QQ_ADB_DEVICE 可强制指定
      const ADB_DEV = process.env.QQ_ADB_DEVICE || resolveAdbDevice();
      const localPath = path.join(__dirname, 'upload-tmp-' + Date.now() + '-' + p2.fileName.replace(/[^\w.\-]/g, '_'));
      fs.writeFileSync(localPath, Buffer.from(p2.dataBase64, 'base64'));
      let devPath = '/sdcard/Android/data/com.tencent.mobileqq/cache/rea_up/' + path.basename(localPath);
      if (p2.devPathOverride) {
        // 直接使用设备上已有文件（调试用），跳过 push
        devPath = p2.devPathOverride;
      } else {
        execFileSync('adb', ['-s', ADB_DEV, 'shell', 'mkdir', '-p', '/sdcard/Android/data/com.tencent.mobileqq/cache/rea_up'], { timeout: 15000 });
        execFileSync('adb', ['-s', ADB_DEV, 'push', localPath, devPath], { timeout: 60000 });
        try { execFileSync('adb', ['-s', ADB_DEV, 'shell', 'chmod', '666', devPath], { timeout: 15000 }); } catch {} // adb push 以 root 落盘，QQ uid 读不了
        fs.unlinkSync(localPath);
      }
      const r = await callBridge('upload_image', [devPath, p2.devPathOverride ? '' : p2.fileName, p2.width || 0, p2.height || 0, p2.size || 0, p2.guildId || p.guildId || '']);
      res.end(JSON.stringify({ ok: !!r.ok, ...r }));
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
      // native 通道同样支持 markdown：段落转换在 Node 侧完成，agent 只负责组装；
      // 同时在 jsonFeed.contents 里带原生 isMarkdown/sourceMarkdown（渲染器认则原生渲染，段落兜底）
      try {
        const paragraphs = p.markdown ? mdToParagraphs(p.text) : null;
        const r = await callBridge('publish', [p.guildId, p.channelId, p.text, p.posterId || POSTER_ID, p.images || [], paragraphs, p.markdown ? 1 : 0]);
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

// 在 adb devices 里找装了 com.tencent.mobileqq 的那台（QQ 所在机可能从 USB 换到网络 adb）
let adbDeviceCache = null;
function resolveAdbDevice() {
  if (adbDeviceCache) return adbDeviceCache;
  try {
    const list = execFileSync('adb', ['devices'], { encoding: 'utf8', timeout: 10000 });
    const serials = list.split('\n').slice(1)
      .map((l) => l.trim().split(/\s+/)[0])
      .filter((s) => s && s !== 'offline');
    for (const serial of serials) {
      try {
        execFileSync('adb', ['-s', serial, 'shell', 'pm', 'path', 'com.tencent.mobileqq'], { timeout: 10000 });
        adbDeviceCache = serial;
        console.log('[adb] resolved QQ device:', serial);
        return serial;
      } catch {} // 这台没装 QQ，看下一台
    }
  } catch (e) {
    console.error('[adb] device probe failed:', e.message);
  }
  return 'a0d80dd7'; // 探测失败时的兜底（历史 mi8 serial）
}

server.listen(PORT, () => console.log(`qq-channel-service listening on :${PORT}`));

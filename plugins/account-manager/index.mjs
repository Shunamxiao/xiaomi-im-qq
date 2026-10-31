import fs from 'fs';
import http from 'http';
import crypto from 'crypto';

const PLUGIN_ID = 'napcat-plugin-account';
const DEFAULT_CONFIG = { webuiPort: 6099, webuiToken: 'x524980520', accessKey: 'x524980520' };

const state = { ctx: null, config: { ...DEFAULT_CONFIG }, credential: null, credTime: 0 };

function sha256hex(s) { return crypto.createHash('sha256').update(s).digest('hex'); }

function configDir() {
  return state.ctx?.core?.context?.pathWrapper?.configPath || '/root/Napcat/opt/QQ/resources/app/app_launcher/napcat/config';
}

function readWebuiConfig() {
  try { return JSON.parse(fs.readFileSync(configDir() + '/webui.json', 'utf8')); } catch { return {}; }
}

function writeAutoLoginAccount(uin) {
  const p = configDir() + '/webui.json';
  const j = JSON.parse(fs.readFileSync(p, 'utf8'));
  j.autoLoginAccount = uin;
  fs.writeFileSync(p, JSON.stringify(j, null, 4));
  return true;
}

function rawRequest(path, body, headers) {
  return new Promise((resolve, reject) => {
    const data = body ? JSON.stringify(body) : null;
    const req = http.request({ host: '127.0.0.1', port: state.config.webuiPort, path, method: body ? 'POST' : 'GET',
      headers: { 'Content-Type': 'application/json', ...(data ? { 'Content-Length': Buffer.byteLength(data) } : {}), ...(headers || {}) } },
      res => { let d = ''; res.on('data', c => d += c); res.on('end', () => { try { resolve(JSON.parse(d)); } catch { resolve({ raw: d }); } }); });
    req.on('error', reject);
    if (data) req.write(data);
    req.end();
    setTimeout(() => { req.destroy(new Error('timeout')); }, 8000);
  });
}

async function webuiCall(path, body) {
  if (!state.credential || Date.now() - state.credTime > 50 * 60 * 1000) {
    const r = await rawRequest('/api/auth/login', { hash: sha256hex(state.config.webuiToken + '.napcat') });
    const cred = (r.data && r.data.Credential) || r.Credential;
    if (r.code !== 0 || !cred) throw new Error('WebUI auth failed: ' + (r.message || 'unknown'));
    state.credential = cred;
    state.credTime = Date.now();
  }
  try {
    return await rawRequest(path, body, { Authorization: 'Bearer ' + state.credential });
  } catch (e) {
    state.credential = null;
    throw e;
  }
}

function checkKey(req, url, body) {
  const key = (req && req.query && req.query.key) || (body && body.key);
  return key && key === state.config.accessKey;
}

async function buildStatus() {
  const unwrap = (r) => (r && typeof r === 'object' && 'data' in r) ? r.data : r;
  const loginRaw = await webuiCall('/api/QQLogin/CheckLoginStatus', {}).catch(e => ({ error: String((e && e.message) || e) }));
  state.ctx.logger.info('CheckLoginStatus raw: ' + JSON.stringify(loginRaw).slice(0, 300));
  const login = unwrap(loginRaw);
  let quickList = [];
  try {
    const r = await webuiCall('/api/QQLogin/GetQuickLoginList', {});
    quickList = unwrap(r) || [];
  } catch {}
  const self = (state.ctx && state.ctx.core && state.ctx.core.selfInfo) || null;
  return {
    login: { isLogin: !!login.isLogin, phase: login.loginPhase || '', error: login.loginError || '', qrcodeurl: login.qrcodeurl || '' },
    self: self ? { uin: String(self.uin), nick: self.nick, avatarUrl: self.avatarUrl, online: self.online } : null,
    quickList,
    autoLoginAccount: readWebuiConfig().autoLoginAccount || ''
  };
}

const pageHtml = () => '<!doctype html><html lang=zh><head><meta charset=utf-8><meta name=viewport content="width=device-width,initial-scale=1"><title>QQ 账号管理</title><style>'
  + 'body{font-family:system-ui,sans-serif;max-width:680px;margin:0 auto;padding:16px;background:#f6f7fb;color:#222}'
  + '.card{background:#fff;border-radius:12px;padding:16px 18px;margin-bottom:14px;box-shadow:0 1px 4px rgba(0,0,0,.06)}'
  + 'h1{font-size:20px}h2{font-size:15px;margin:0 0 10px}'
  + '.row{display:flex;align-items:center;gap:12px;padding:8px 0;border-bottom:1px solid #f0f0f0}'
  + 'img.avatar{width:44px;height:44px;border-radius:50%}'
  + 'button{border:0;border-radius:8px;padding:7px 14px;cursor:pointer;background:#4f6ef7;color:#fff;font-size:13px}'
  + 'button.danger{background:#e5484d}'
  + '.tag{font-size:12px;padding:2px 8px;border-radius:6px;background:#eef;color:#345}'
  + '.ok{background:#e6f7e9;color:#14702e}.warn{background:#fff4e0;color:#8a5a00}'
  + '#qr img{max-width:260px;width:100%;border:8px solid #fff;border-radius:10px}'
  + '.muted{color:#888;font-size:12px}'
  + '</style></head><body><h1>QQ 账号管理</h1><div id=app>loading</div>'
  + '<div class=card id=qr style=display:none><h2>扫码登录新账号</h2><div id=qrbody></div><p class=muted>使用手机 QQ 扫码。成功后可点击「设为自动登录」。</p></div>'
  + '<scr' + 'ipt>' + pageJs() + '</scr' + 'ipt></body></html>';

function pageJs() {
  return [
    'const K = new URLSearchParams(location.search).get("key") || "";',
    'const Q = k => "/plugin/' + PLUGIN_ID + '/api/" + k + (K ? "?key=" + encodeURIComponent(K) : "");',
    'let st = null;',
    'const esc = s => String(s == null ? "" : s).replace(/[<>&\\\"]+/g, c => ({"<":"&lt;",">":"&gt;","&":"&amp;","\\\"":"&quot;"}[c]));',
    'async function refresh(){ try { st = await (await fetch(Q("status"))).json(); render(); } catch(e){ document.getElementById("app").textContent = "加载失败: " + e; } }',
    'async function post(k, body){ const r = await fetch(Q(k), { method:"POST", headers:{"Content-Type":"application/json"}, body: JSON.stringify(Object.assign({key: K}, body||{})) }); return r.json(); }',
    'function render(){',
    '  const a = document.getElementById("app");',
    '  const L = st.login || {};',
    '  let html = "";',
    '  if (L.isLogin && st.self) {',
    '    html += "<div class=card><h2>当前账号</h2><div class=row>" + (st.self.avatarUrl ? "<img class=avatar src=" + JSON.stringify(esc(st.self.avatarUrl)) + ">" : "")',
    '      + "<div><b>" + esc(st.self.nick) + "</b> <span class=tag>" + esc(st.self.uin) + "</span> "',
    '      + "<span class=\"tag " + (st.self.online ? "ok" : "warn") + "\">" + (st.self.online ? "在线" : "离线") + "</span></div></div>`',
    '      + "<p class=muted>自动登录账号: " + (st.autoLoginAccount ? esc(st.autoLoginAccount) : "未设置（重启后需扫码）") + "</p></div>";',
    '  } else {',
    '    html += "<div class=card><h2>当前状态</h2><p>" + (L.error ? esc(L.error) : "未登录，阶段: " + esc(L.phase || "unknown")) + "</p></div>";',
    '  }',
    '  if (Array.isArray(st.quickList) && st.quickList.length) {',
    '    html += "<div class=card><h2>历史登录账号</h2>";',
    '    for (const u of st.quickList) {',
    '      const uin = String(u.uin == null ? u : u.uin);',
    '      const nick = u.nickName || u.nick || "";',
    '      html += "<div class=row><div style=flex:1><b>" + esc(uin) + "</b> " + esc(nick) + "</div>"',
    '        + (st.self && st.self.uin === uin ? "<span class=\"tag ok\">当前</span>" : "<button onclick=doSwitch(\'" + uin + "\')>切换</button>") + "</div>";',
    '    }',
    '    html += "</div>";',
    '  }',
    '  html += "<div class=card><h2>添加账号</h2><p class=muted>重启进入扫码登录状态（会退出当前账号）。</p>'
    + '<button class=danger onclick=doAdd() ' + 'id=addbtn>扫码添加新账号</button>'
    + '<span id=setauto></span></div>";',
    '  a.innerHTML = html;',
    '  if (st.self && st.self.uin) { document.getElementById("setauto").innerHTML = " <button onclick=doSetAuto()>设 " + esc(st.self.uin) + " 为自动登录</button>"; }',
    '  const qr = document.getElementById("qr"), qb = document.getElementById("qrbody");',
    '  if (!L.isLogin && L.qrcodeurl) { qr.style.display = "block"; qb.innerHTML = "<img src=" + JSON.stringify(esc(L.qrcodeurl)) + ">"; }',
    '  else if (!L.isLogin) { qr.style.display = "block"; qb.textContent = "等待二维码… 阶段: " + (L.phase || "无") + (L.error ? " / " + L.error : ""); }',
    '  else qr.style.display = "none";',
    '}',
    'async function doSwitch(uin){ if (!confirm("切换到 " + uin + "？将重启 NapCat 并退出当前账号。")) return; const r = await post("switch", {uin}); alert(r.ok ? "已发起切换，页面将在重启后自动恢复" : "失败: " + r.error); setTimeout(refresh, 5000); }',
    'async function doAdd(){ if (!confirm("确定扫码添加新账号？将重启并退出当前账号！")) return; const r = await post("add"); if (r.ok) { alert("正在重启，稍后此页会出现二维码"); setTimeout(refresh, 8000); } else alert("失败: " + r.error); }',
    'async function doSetAuto(){ if (!st.self || !st.self.uin) return; const r = await post("set-auto", {uin: st.self.uin}); alert(r.ok ? "已设置自动登录: " + st.self.uin : "失败: " + r.error); refresh(); }',
    'refresh(); setInterval(refresh, 4000);'
  ].join('\n');
}

const plugin_init = async (ctx) => {
  state.ctx = ctx;
  try { Object.assign(state.config, JSON.parse(fs.readFileSync(ctx.configPath, 'utf8'))); } catch {}
  ctx.logger.info('账号管理插件就绪');
  ctx.router.getNoAuth('/status', async (req, res) => {
    if (!checkKey(req, req.url)) return res.status(401).json({ error: 'key 无效' });
    try { res.json(await buildStatus()); } catch (e) { res.status(500).json({ error: String((e && e.message) || e) }); }
  });
  ctx.router.postNoAuth('/switch', async (req, res) => {
    const body = req.body || {};
    if (!checkKey(req, req.url, body)) return res.status(401).json({ error: 'key 无效' });
    try {
      const uin = String(body.uin || '');
      if (!/^\d{5,12}$/.test(uin)) return res.json({ ok: false, error: 'uin 无效' });
      writeAutoLoginAccount(uin);
      const r = await webuiCall('/api/QQLogin/RestartNapCat', {});
      ctx.logger.info('switch -> ' + uin + ' restart: ' + JSON.stringify(r).slice(0, 120));
      res.json({ ok: true });
    } catch (e) { res.status(500).json({ ok: false, error: String((e && e.message) || e) }); }
  });
  ctx.router.postNoAuth('/add', async (req, res) => {
    const body = req.body || {};
    if (!checkKey(req, req.url, body)) return res.status(401).json({ error: 'key 无效' });
    try {
      writeAutoLoginAccount('');
      const r = await webuiCall('/api/QQLogin/RestartNapCat', {});
      ctx.logger.info('add mode restart: ' + JSON.stringify(r).slice(0, 120));
      res.json({ ok: true });
    } catch (e) { res.status(500).json({ ok: false, error: String((e && e.message) || e) }); }
  });
  ctx.router.postNoAuth('/set-auto', async (req, res) => {
    const body = req.body || {};
    if (!checkKey(req, req.url, body)) return res.status(401).json({ error: 'key 无效' });
    try {
      const uin = String(body.uin || '');
      if (!/^\d{5,12}$/.test(uin)) return res.json({ ok: false, error: 'uin 无效' });
      writeAutoLoginAccount(uin);
      res.json({ ok: true });
    } catch (e) { res.status(500).json({ ok: false, error: String((e && e.message) || e) }); }
  });
  ctx.router.getNoAuth('/page', (req, res) => {
    res.setHeader('Content-Type', 'text/html; charset=utf-8');
    res.send(pageHtml());
  });
};

const plugin_config_ui = (ctx) => ctx.NapCatConfig.combine(
  ctx.NapCatConfig.html('<div style="padding:12px;background:#eef2ff;border-radius:8px;font-size:13px">账号管理：查看状态 / 切换历史账号 / 扫码添加账号。管理页: /plugin/napcat-plugin-account/page?key=…</div>'),
  ctx.NapCatConfig.number('webuiPort', 'WebUI 端口', 6099, 'NapCat WebUI 端口'),
  ctx.NapCatConfig.text('webuiToken', 'WebUI Token', 'x524980520', '用于调用 WebUI 登录接口'),
  ctx.NapCatConfig.text('accessKey', '页面访问 Key', 'x524980520', '管理页与操作接口的访问密钥')
);

const plugin_get_config = async () => state.config;
const plugin_set_config = async (_ctx, config) => { state.config = { ...DEFAULT_CONFIG, ...config }; };
const plugin_cleanup = async (ctx) => { state.ctx = null; ctx.logger.info('账号管理插件已卸载'); };

export { plugin_cleanup, plugin_config_ui, plugin_get_config, plugin_init, plugin_set_config };

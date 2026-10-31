import fs from 'fs';
import http from 'http';
import crypto from 'crypto';

const PLUGIN_ID = 'napcat-plugin-account';
const MANAGER_PATH = '/root/qq-accounts/manager.js';
const DEFAULT_CONFIG = { webuiPort: 6099, webuiToken: 'x524980520', accessKey: 'x524980520' };

const state = { ctx: null, config: { ...DEFAULT_CONFIG }, credential: null, credTime: 0, manager: null };

function sha256hex(s) { return crypto.createHash('sha256').update(s).digest('hex'); }

// 多实例编排库在容器内 /root/qq-accounts/manager.js，插件动态加载（缺失时多实例接口报错，单实例功能不受影响）
async function getManager() {
  if (!state.manager) state.manager = await import(MANAGER_PATH);
  return state.manager;
}

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

// 主实例 WebUI 调用（switch/add/set-auto 仍走主实例，保持原有单实例切换能力）
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
  + 'body{font-family:system-ui,sans-serif;max-width:720px;margin:0 auto;padding:16px;background:#f6f7fb;color:#222}'
  + '.card{background:#fff;border-radius:12px;padding:16px 18px;margin-bottom:14px;box-shadow:0 1px 4px rgba(0,0,0,.06)}'
  + 'h1{font-size:20px}h2{font-size:15px;margin:0 0 10px}'
  + '.row{display:flex;align-items:center;gap:12px;padding:8px 0;border-bottom:1px solid #f0f0f0;flex-wrap:wrap}'
  + 'img.avatar{width:44px;height:44px;border-radius:50%}'
  + 'button{border:0;border-radius:8px;padding:7px 14px;cursor:pointer;background:#4f6ef7;color:#fff;font-size:13px}'
  + 'button.danger{background:#e5484d}button.gray{background:#8b8f98}'
  + '.tag{font-size:12px;padding:2px 8px;border-radius:6px;background:#eef;color:#345}'
  + '.ok{background:#e6f7e9;color:#14702e}.warn{background:#fff4e0;color:#8a5a00}.off{background:#f2f2f2;color:#888}'
  + '#qr img,.inst-qr img{max-width:260px;width:100%;border:8px solid #fff;border-radius:10px}'
  + '.muted{color:#888;font-size:12px}.meta{font-size:12px;color:#666}'
  + '</style></head><body><h1>QQ 账号管理</h1><div id=app>loading</div>'
  + '<div id=inst></div>'
  + '<div class=card id=qr style=display:none><h2>主实例扫码登录</h2><div id=qrbody></div><p class=muted>使用手机 QQ 扫码。成功后可点击「设为自动登录」。</p></div>'
  + '<scr' + 'ipt>' + pageJs() + '</scr' + 'ipt></body></html>';

function pageJs() {
  return [
    'let K = new URLSearchParams(location.search).get("key") || localStorage.getItem("acctKey") || "";',
    'function saveKey(k){ K = String(k || "").trim(); if (!K) return; localStorage.setItem("acctKey", K); refresh(); }',
    'const Q = k => "/plugin/' + PLUGIN_ID + '/api/" + k + (K ? "?key=" + encodeURIComponent(K) : "");',
    'let st = null, insts = null;',
    'const esc = s => String(s == null ? "" : s).replace(/[<>&\\\"]+/g, c => ({"<":"&lt;",">":"&gt;","&":"&amp;","\\\"":"&quot;"}[c]));',
    'async function refresh(){ let unauthorized = false; try { let r1 = await fetch(Q("status")); if (r1.status === 401) unauthorized = true; else st = await r1.json(); if (!unauthorized) { try { let r2 = await fetch(Q("instances")); if (r2.status === 401) unauthorized = true; else insts = await r2.json(); } catch(e){ insts = {error: String(e)}; } } } catch(e){} if (unauthorized) { showKey(); return; } render(); }',
    'function showKey(){ document.getElementById("app").innerHTML = "<div class=card><h2>访问密钥</h2><p class=muted>输入管理密钥(即插件配置中的『页面访问 Key』),将保存在本浏览器,之后无需重复输入。</p><input id=kin type=password placeholder=访问密钥> <button onclick=saveKey(document.getElementById(\"kin\").value)>进入</button></div>"; }',
    'async function post(k, body){ const r = await fetch(Q(k), { method:"POST", headers:{"Content-Type":"application/json"}, body: JSON.stringify(Object.assign({key: K}, body||{})) }); return r.json(); }',
    'function render(){',
    '  const a = document.getElementById("app");',
    '  if (!st) { a.textContent = "主实例状态加载失败"; } else {',
    '  const L = st.login;',
    '  let html = "<div class=card><h2>主实例 " + esc(st.self ? st.self.uin : "") + "</h2>";',
    '  if (st.self) { html += "<div class=row><img class=avatar src=" + JSON.stringify(st.self.avatarUrl || "") + ">"',
    '    + "<div style=flex:1><b>" + esc(st.self.nick) + "</b> <span class=\"tag " + (st.self.online ? "ok" : "warn") + "\">" + (st.self.online ? "在线" : "离线") + "</span>"',
    '    + "<div class=meta>OneBot ws://127.0.0.1:3001 · 自动登录: " + esc(st.autoLoginAccount || "未设置") + "</div></div></div>"; }',
    '  else html += "<p class=muted>主实例未登录（phase: " + esc(L.phase) + "）</p>";',
    '  if (Array.isArray(st.quickList) && st.quickList.length) {',
    '    html += "<div class=meta style=margin:6px 0>历史登录账号（单实例切换，会重启主实例）</div>";',
    '    for (const u of st.quickList) {',
    '      const uin = String(u.uin == null ? u : u.uin);',
    '      const nick = u.nickName || u.nick || "";',
    '      html += "<div class=row><div style=flex:1><b>" + esc(uin) + "</b> " + esc(nick) + "</div>"',
    '        + (st.self && st.self.uin === uin ? "<span class=\"tag ok\">当前</span>" : "<button onclick=doSwitch(\'" + uin + "\')>切换</button>") + "</div>";',
    '    }',
    '  }',
    '  html += "<div style=margin-top:10px><button class=danger onclick=doAdd() id=addbtn>主实例扫码添加账号</button>"',
    '    + (st.self && st.self.uin ? " <button class=gray onclick=doSetAuto()>设 " + esc(st.self.uin) + " 为自动登录</button>" : "") + "</div></div>";',
    '  a.innerHTML = html;',
    '  const qr = document.getElementById("qr"), qb = document.getElementById("qrbody");',
    '  if (!L.isLogin && L.qrcodeurl) { qr.style.display = "block"; qb.innerHTML = "<img src=" + JSON.stringify(esc(L.qrcodeurl)) + ">"; }',
    '  else if (!L.isLogin) { qr.style.display = "block"; qb.textContent = "等待二维码… 阶段: " + (L.phase || "无") + (L.error ? " / " + L.error : ""); }',
    '  else qr.style.display = "none";',
    '  }',
    '  renderInstances();',
    '}',
    'function renderInstances(){',
    '  const box = document.getElementById("inst");',
    '  if (!insts) return;',
    '  if (insts.error) { box.innerHTML = "<div class=card><h2>多账号实例</h2><p class=muted>加载失败: " + esc(insts.error) + "（检查容器内 /root/qq-accounts/manager.js）</p></div>"; return; }',
    '  const list = Array.isArray(insts) ? insts : (insts.instances || []);',
    '  let html = "<div class=card><h2>多账号实例</h2>";',
    '  for (const it of list) {',
    '    const isMain = it.name === "main";',
    '    const stateTag = it.running ? (it.login.isLogin ? "<span class=\"tag ok\">在线</span>" : "<span class=\"tag warn\">运行中·未登录</span>") : "<span class=\"tag off\">已停止</span>";',
    '    const onebot = it.onebotPort ? " <span class=\"tag " + (it.onebotOk ? "ok" : "off") + "\">OneBot " + it.onebotPort + (it.onebotOk ? " ✓" : " ✗") + "</span>" : "";',
    '    const qr = (!isMain && it.running && !it.login.isLogin && it.login.qrcodeurl)',
    '      ? "<div class=inst-qr style=margin:8px 0><img src=" + JSON.stringify(esc(it.login.qrcodeurl)) + "></div>" : "";',
    '    html += "<div class=row><div style=flex:1;min-width:200px><b>" + esc(it.name) + "</b> " + (it.uin ? esc(it.uin) : "<i class=muted>未绑定</i>")',
    '      + " " + stateTag + onebot',
    '      + "<div class=meta>WebUI :" + it.webuiPort + (it.autologin ? " · 自动登录" : "") + (it.login.phase && !it.login.isLogin ? " · " + esc(it.login.phase) : "") + "</div></div>"',
    '      + (isMain ? "" : (it.running',
    '        ? "<button class=danger onclick=doInstStop(\'" + it.name + "\')>停止</button>"',
    '        : "<button onclick=doInstStart(\'" + it.name + "\')>启动</button>")) + "</div>" + qr;',
    '  }',
    '  html += "<div style=margin-top:10px><button onclick=doInstAdd()>+ 添加新账号（独立实例）</button>',
    '    <span class=muted>新实例需扫码登录，登录后自动绑定并开启 OneBot</span></div>";',
    '  html += "</div>";',
    '  box.innerHTML = html;',
    '}',
    'async function doSwitch(uin){ if (!confirm("切换到 " + uin + "？将重启主实例并退出当前账号。")) return; const r = await post("switch", {uin}); alert(r.ok ? "已发起切换，页面将在重启后自动恢复" : "失败: " + r.error); setTimeout(refresh, 5000); }',
    'async function doAdd(){ if (!confirm("确定扫码添加新账号？将重启主实例并退出当前账号！\n（如需不影响主账号，请用下方「添加新账号」）")) return; const r = await post("add"); if (r.ok) { alert("正在重启，稍后此页会出现二维码"); setTimeout(refresh, 8000); } else alert("失败: " + r.error); }',
    'async function doSetAuto(){ if (!st.self || !st.self.uin) return; const r = await post("set-auto", {uin: st.self.uin}); alert(r.ok ? "已设置自动登录: " + st.self.uin : "失败: " + r.error); refresh(); }',
    'async function doInstAdd(){ if (!confirm("创建新的 QQ 实例并进入扫码登录？主实例不受影响。")) return;',
    '  const r = await post("instance-add");',
    '  if (r.ok) { alert("实例 " + r.name + " 已创建并启动（WebUI :" + r.webuiPort + "），稍后列表中会出现扫码二维码"); refresh(); }',
    '  else alert("失败: " + r.error); }',
    'async function doInstStart(name){ const r = await post("instance-start", {name}); alert(r.ok ? "已启动" : "失败: " + r.error); refresh(); }',
    'async function doInstStop(name){ if (!confirm("停止实例 " + name + "？")) return; const r = await post("instance-stop", {name}); alert(r.ok ? "已停止" : "失败: " + r.error); refresh(); }',
    'refresh(); setInterval(refresh, 4000);'
  ].join('\n');
}

const plugin_init = async (ctx) => {
  state.ctx = ctx;
  try { Object.assign(state.config, JSON.parse(fs.readFileSync(ctx.configPath, 'utf8'))); } catch {}
  ctx.logger.info('账号管理插件就绪');
  plugin_config_ui = ctx.NapCatConfig.combine(
    ctx.NapCatConfig.html('<div style="padding:12px;background:#eef2ff;border-radius:8px;font-size:13px">账号管理功能在 WebUI 侧边栏「账号管理」页面(主实例状态/历史账号切换/多实例添加/扫码登录)。本面板仅配置连接参数;'),
    ctx.NapCatConfig.number('webuiPort', 'WebUI 端口', 6099, '主实例 NapCat WebUI 端口'),
    ctx.NapCatConfig.text('webuiToken', 'WebUI Token', 'x524980520', '用于调用主实例 WebUI 登录接口'),
    ctx.NapCatConfig.text('accessKey', '页面访问 Key', 'x524980520', '管理页与操作接口的访问密钥')
  );
  // WebUI 侧边栏页面:管理界面写盘后注册
  try {
    fs.writeFileSync(ctx.pluginPath + '/dashboard.html', pageHtml());
    ctx.router.page({ title: '账号管理', path: 'dashboard', htmlFile: 'dashboard.html', icon: '👤', description: '主实例状态 / 历史账号切换 / 多实例管理' });
  } catch (e) { ctx.logger.warn('注册侧边栏页面失败:', e && e.message); }
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
  ctx.router.getNoAuth('/instances', async (req, res) => {
    if (!checkKey(req, req.url)) return res.status(401).json({ error: 'key 无效' });
    try {
      const m = await getManager();
      res.json(await m.listInstances());
    } catch (e) { res.status(500).json({ error: String((e && e.message) || e) }); }
  });
  ctx.router.postNoAuth('/instance-add', async (req, res) => {
    const body = req.body || {};
    if (!checkKey(req, req.url, body)) return res.status(401).json({ error: 'key 无效' });
    try {
      const m = await getManager();
      const r = await m.addInstance();
      ctx.logger.info('instance-add: ' + JSON.stringify(r));
      res.json({ ok: true, ...r });
    } catch (e) { res.json({ ok: false, error: String((e && e.message) || e) }); }
  });
  ctx.router.postNoAuth('/instance-start', async (req, res) => {
    const body = req.body || {};
    if (!checkKey(req, req.url, body)) return res.status(401).json({ error: 'key 无效' });
    try {
      const m = await getManager();
      const r = await m.startInstance(String(body.name || ''));
      res.json({ ok: true, ...r });
    } catch (e) { res.json({ ok: false, error: String((e && e.message) || e) }); }
  });
  ctx.router.postNoAuth('/instance-stop', async (req, res) => {
    const body = req.body || {};
    if (!checkKey(req, req.url, body)) return res.status(401).json({ error: 'key 无效' });
    try {
      const m = await getManager();
      const r = await m.stopInstance(String(body.name || ''));
      ctx.logger.info('instance-stop: ' + body.name + ' -> ' + JSON.stringify(r));
      res.json({ ok: true, ...r });
    } catch (e) { res.json({ ok: false, error: String((e && e.message) || e) }); }
  });
  ctx.router.getNoAuth('/page', (req, res) => {
    res.setHeader('Content-Type', 'text/html; charset=utf-8');
    res.send(pageHtml());
  });
};

let plugin_config_ui = [];

const plugin_get_config = async () => state.config;
const plugin_set_config = async (_ctx, config) => { state.config = { ...DEFAULT_CONFIG, ...config }; };
const plugin_cleanup = async (ctx) => { state.ctx = null; ctx.logger.info('账号管理插件已卸载'); };

export { plugin_cleanup, plugin_config_ui, plugin_get_config, plugin_init, plugin_set_config };

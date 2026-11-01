import fs from 'fs';
import http from 'http';
import crypto from 'crypto';

const PLUGIN_ID = 'napcat-plugin-account';
const MANAGER_PATH = '/root/qq-accounts/manager.js';
const DEFAULT_CONFIG = { webuiPort: 6099, webuiToken: 'x524980520', accessKey: 'x524980520' };
const ACCOUNTS_REGISTRY_PATH = '/root/qq-accounts/registry.json';
const MAIN_CONFIG_DIR = '/root/Napcat/opt/QQ/resources/app/app_launcher/napcat/config';
const MAIN_PLUGINS_DIR = '/root/Napcat/opt/QQ/resources/app/app_launcher/napcat/plugins';
const AUTOREPLY_PREFIX = '/plugin/napcat-plugin-autoreply/api';

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

// 通用 WebUI JSON 请求：按端口调用，供主实例与子实例复用
function requestJson(port, path, body, headers) {
  return new Promise((resolve, reject) => {
    const data = body ? JSON.stringify(body) : null;
    const req = http.request({ host: '127.0.0.1', port, path, method: data ? 'POST' : 'GET',
      headers: { 'Content-Type': 'application/json', ...(data ? { 'Content-Length': Buffer.byteLength(data) } : {}), ...(headers || {}) } },
      res => { let d = ''; res.on('data', c => d += c); res.on('end', () => { try { resolve(JSON.parse(d)); } catch { resolve({ raw: d }); } }); });
    req.on('error', reject);
    if (data) req.write(data);
    req.end();
    setTimeout(() => { req.destroy(new Error('timeout')); }, 8000);
  });
}

function rawRequest(path, body, headers) {
  return requestJson(state.config.webuiPort, path, body, headers);
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

// 实例注册表只读：主实例不入表，子实例按 name 查找，避免 name 被用于探测任意端口
function loadAccountsRegistry() {
  try { return JSON.parse(fs.readFileSync(ACCOUNTS_REGISTRY_PATH, 'utf8')); } catch { return { instances: {} }; }
}

function resolveAccountInstance(name) {
  const n = String(name || 'main');
  if (!/^[A-Za-z0-9_-]{1,32}$/.test(n)) throw new Error('实例名无效');
  if (n === 'main') return { name: 'main', webuiPort: state.config.webuiPort, webuiToken: state.config.webuiToken, protected: true };
  const inst = (loadAccountsRegistry().instances || {})[n];
  if (!inst) throw new Error('实例不存在: ' + n);
  return inst;
}

function readInstanceToken(inst) {
  if (inst.protected) return inst.webuiToken || state.config.webuiToken;
  try {
    const j = JSON.parse(fs.readFileSync(inst.workdir + '/config/webui.json', 'utf8'));
    if (j.token) return j.token;
  } catch {}
  return inst.webuiToken || '';
}

// 子实例的 NAPCAT_WORKDIR 独立：缺少 plugins.json 时 NapCat 不加载任何插件，启动前从主实例补齐
function ensureInstancePluginConfig(name) {
  const inst = resolveAccountInstance(name);
  if (inst.protected) return;
  const dir = inst.workdir + '/config';
  fs.mkdirSync(dir + '/plugins', { recursive: true });
  const dst = dir + '/plugins.json';
  if (!fs.existsSync(dst)) {
    const src = MAIN_CONFIG_DIR + '/plugins.json';
    if (fs.existsSync(src)) fs.copyFileSync(src, dst);
    else fs.writeFileSync(dst, JSON.stringify({ 'napcat-plugin-autoreply': true, 'napcat-plugin-account': true }, null, 2));
    state.ctx.logger.info('已补齐实例插件启用配置: ' + name);
  }
  // NapCat 按 NAPCAT_WORKDIR/plugins 查找插件代码: 共享主实例插件目录, 部署一次所有实例生效
  const link = inst.workdir + '/plugins';
  let hasPlugins = false;
  try { hasPlugins = !!fs.lstatSync(link); } catch {}
  if (!hasPlugins) {
    try { fs.symlinkSync(MAIN_PLUGINS_DIR, link, 'dir'); }
    catch (e) {
      fs.cpSync(MAIN_PLUGINS_DIR, link, { recursive: true });
      state.ctx.logger.warn('插件目录软链失败,已复制: ' + String((e && e.message) || e));
    }
    state.ctx.logger.info('已补齐实例插件目录: ' + name);
  }
}

const instanceCreds = new Map(); // webuiPort -> { token, cred, time }
async function loginInstanceWebui(inst, token) {
  const r = await requestJson(inst.webuiPort, '/api/auth/login', { hash: sha256hex(token + '.napcat') });
  const cred = (r.data && r.data.Credential) || r.Credential;
  if (r.code !== 0 || !cred) throw new Error('实例 WebUI 登录失败: ' + (r.message || 'unknown'));
  const cached = { token, cred, time: Date.now() };
  instanceCreds.set(inst.webuiPort, cached);
  return cached;
}

async function instanceWebuiCall(inst, path, body) {
  const token = readInstanceToken(inst);
  if (!token) throw new Error('实例 WebUI token 不可用');
  let cached = instanceCreds.get(inst.webuiPort);
  if (!cached || cached.token !== token || Date.now() - cached.time > 50 * 60 * 1000) {
    cached = await loginInstanceWebui(inst, token);
  }
  let r = await requestJson(inst.webuiPort, path, body, { Authorization: 'Bearer ' + cached.cred });
  // 实例重启后 secret 变化会让旧凭证失效(HTTP 200 + code:-1)，重新登录重试一次
  if (r && r.code === -1) {
    instanceCreds.delete(inst.webuiPort);
    cached = await loginInstanceWebui(inst, token);
    r = await requestJson(inst.webuiPort, path, body, { Authorization: 'Bearer ' + cached.cred });
  }
  return r;
}

// autoreply 可能返回 {code:0,data:config}、{ok:true,config} 或裸 config，统一解包
function unwrapAutoreplyConfig(r) {
  if (!r || typeof r !== 'object') throw new Error('autoreply 返回非 JSON');
  if (r.raw !== undefined) throw new Error('autoreply 返回非 JSON: ' + String(r.raw).slice(0, 120));
  if (r.code !== undefined && r.code !== 0) throw new Error('autoreply: ' + (r.message || ('code ' + r.code)));
  let cfg = r.data !== undefined ? r.data : (r.config !== undefined ? r.config : r);
  if (cfg && typeof cfg === 'object' && cfg.ok === true && cfg.config && typeof cfg.config === 'object') cfg = cfg.config;
  if (!cfg || typeof cfg !== 'object' || Array.isArray(cfg)) throw new Error('autoreply 配置格式异常');
  return cfg;
}

function validateRulesConfig(cfg) {
  if (!cfg || typeof cfg !== 'object' || Array.isArray(cfg)) throw new Error('config 必须是对象');
  if (cfg.keywords !== undefined && !Array.isArray(cfg.keywords)) throw new Error('config.keywords 必须是数组');
  for (const f of ['cooldownSeconds', 'replyDelayMinMs', 'replyDelayMaxMs']) {
    if (cfg[f] !== undefined && (typeof cfg[f] !== 'number' || !isFinite(cfg[f]) || cfg[f] < 0)) throw new Error(f + ' 必须是非负数');
  }
  if (cfg.groupRules !== undefined) {
    const gr = cfg.groupRules;
    if (!gr || typeof gr !== 'object' || Array.isArray(gr)) throw new Error('config.groupRules 必须是对象');
    for (const gid of Object.keys(gr)) {
      if (!gid.trim()) throw new Error('groupRules 存在空群号');
      if (!gr[gid] || typeof gr[gid] !== 'object' || Array.isArray(gr[gid])) throw new Error('groupRules.' + gid + ' 必须是对象');
    }
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
  + '.toast{position:fixed;top:14px;right:14px;background:#222;color:#fff;padding:10px 14px;border-radius:8px;font-size:13px;z-index:99;box-shadow:0 4px 14px rgba(0,0,0,.2)}'
  + '.toast.err{background:#c0392b}'
  + '.inst{display:flex;align-items:center;gap:10px;padding:10px 0;border-bottom:1px solid #f0f0f0}'
  + 'img.mini{width:36px;height:36px;border-radius:50%}'
  + '.grow{flex:1}'
  + '.rules-head{display:flex;align-items:center;justify-content:space-between;gap:8px}'
  + '.rules-panel h3{font-size:14px;margin:18px 0 8px;padding-top:10px;border-top:1px solid #f0f0f0}'
  + '.field{margin:8px 0}.field>label{display:block;font-size:12px;color:#666;margin-bottom:4px}'
  + 'input[type=text],input[type=number],input[type=password],textarea,select{width:100%;box-sizing:border-box;border:1px solid #d9dce3;border-radius:6px;padding:7px 9px;font:inherit;background:#fff;color:#222}'
  + 'textarea{resize:vertical}'
  + '.sw{display:flex;align-items:center;gap:8px;margin:8px 0;font-size:14px}.sw input{width:auto}'
  + '.kw{display:grid;grid-template-columns:minmax(0,1fr) 96px 58px;gap:6px;padding:8px;border:1px solid #eef0f4;border-radius:8px;margin-bottom:8px}'
  + '.kw .kw-reply{grid-column:1/-1}.kw button{padding:6px 8px}'
  + '.grid3{display:grid;grid-template-columns:1fr 1fr 1fr;gap:8px}'
  + '.err{color:#c0392b;font-size:13px}'
  + '.rules-actions{display:flex;gap:8px;margin-top:14px}'
  + '#r-grouprules{font-family:ui-monospace,Menlo,Consolas,monospace;font-size:12px}'
  + '@media(max-width:520px){.grid3{grid-template-columns:1fr}}'
  + '</style></head><body><h1>QQ 账号管理</h1><div id=app>loading</div>'
  + '<div id=inst></div>'
  + '<div id=rules></div>'
  + '<div class=card id=qr style=display:none><h2>主实例扫码登录</h2><div id=qrbody></div><p class=muted>使用手机 QQ 扫码。成功后可点击「设为自动登录」。</p></div>'
  + '<scr' + 'ipt>' + pageJs() + '</scr' + 'ipt></body></html>';

function pageJs() {
  const js = [`
    let K = new URLSearchParams(location.search).get("key") || localStorage.getItem("acctKey") || "";
    const Q = k => "/plugin/napcat-plugin-account/api/" + k + (K ? "?key=" + encodeURIComponent(K) : "");
    function A(k, params){
      const q = new URLSearchParams(params || {});
      if (K) q.set("key", K);
      const s = q.toString();
      return "/plugin/napcat-plugin-account/api/" + k + (s ? "?" + s : "");
    }
    let st = null, insts = null, rules = null;
    const esc = s => String(s == null ? "" : s).replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;");
    async function refresh(){
      let unauthorized = false;
      try {
        const r1 = await fetch(Q("status"));
        if (r1.status === 401) unauthorized = true; else st = await r1.json();
        if (!unauthorized) {
          try { const r2 = await fetch(Q("instances")); if (r2.status === 401) unauthorized = true; else insts = await r2.json(); } catch(e){ insts = {error: String(e)}; }
        }
      } catch(e){}
      if (unauthorized) { showKey(); return; }
      render();
    }
    function showKey(){
      document.getElementById("app").innerHTML = '<div class="card"><h2>访问密钥</h2><p class="muted">输入管理密钥(即插件配置中的「页面访问 Key」),将保存在本浏览器,之后无需重复输入。</p><input id="kin" type="password" placeholder="访问密钥"> <button id="kbtn">进入</button></div>';
      const go = () => saveKey(document.getElementById("kin").value);
      document.getElementById("kbtn").onclick = go;
      document.getElementById("kin").addEventListener("keydown", e => { if (e.key === "Enter") go(); });
    }
    function saveKey(k){ K = String(k || "").trim(); if (!K) return; localStorage.setItem("acctKey", K); refresh(); }
    async function post(k, body){
      const r = await fetch(Q(k), { method: "POST", headers: {"Content-Type": "application/json"}, body: JSON.stringify(Object.assign({key: K}, body || {})) });
      return r.json();
    }
    function render(){
      const a = document.getElementById("app");
      const L = (st && st.login) || {};
      let html = "";
      if (L.isLogin && st.self) {
        html += '<div class="card"><h2>主实例账号</h2><div class="row">';
        if (st.self.avatarUrl) html += '<img class="avatar" src="' + esc(st.self.avatarUrl) + '">';
        html += '<div><b>' + esc(st.self.nick) + '</b> <span class="tag">' + esc(st.self.uin) + '</span> <span class="tag ' + (st.self.online ? "ok" : "warn") + '">' + (st.self.online ? "在线" : "离线") + '</span></div></div>';
        html += '<p class="muted">自动登录账号: ' + (st.autoLoginAccount ? esc(st.autoLoginAccount) : "未设置(重启后需扫码)") + '</p></div>';
      } else {
        html += '<div class="card"><h2>主实例状态</h2><p>' + (L.error ? esc(L.error) : "未登录,阶段: " + esc(L.phase || "unknown")) + '</p></div>';
      }
      if (Array.isArray(st.quickList) && st.quickList.length) {
        html += '<div class="card"><h2>历史登录账号(快速切换)</h2>';
        for (const u of st.quickList) {
          const uin = String(u.uin == null ? u : u.uin);
          const nick = u.nickName || u.nick || "";
          html += '<div class="row"><div style="flex:1"><b>' + esc(uin) + "</b> " + esc(nick) + "</div>";
          if (st.self && st.self.uin === uin) html += '<span class="tag ok">当前</span>';
          else html += '<button data-act="switch" data-name="' + esc(uin) + '">切换</button>';
          html += "</div>";
        }
        html += "</div>";
      }
      html += '<div class="card"><h2>添加账号(独立实例)</h2><p class="muted">创建新的 QQ 实例(独立数据/端口),启动后页面显示二维码,用新 QQ 号扫码登录。主实例不受影响。</p>';
      html += '<button data-act="inst-add">+ 添加新账号实例</button></div>';
      if (Array.isArray(insts) && insts.length) {
        html += '<div class="card"><h2>多实例列表</h2>';
        for (const it of insts) {
          html += '<div class="inst">';
          if (it.avatarUrl) html += '<img class="mini" src="' + esc(it.avatarUrl) + '">';
          html += '<div class="grow"><b>' + esc(it.nick || it.name) + '</b> <span class="tag">' + esc(it.uin || "未登录") + '</span> <span class="tag ' + (it.running ? "ok" : "") + '">' + (it.running ? "运行中" : "已停止") + '</span>';
          html += '<br><span class="meta">' + esc(it.name) + ' · WebUI ' + esc(String(it.webuiPort || "-")) + ' · OneBot ' + esc(String(it.onebotPort || "-")) + '</span>';
          if (it.running && it.login && !it.login.isLogin) html += '<div style="margin-top:8px"><img src="' + Q('qr') + '&name=' + encodeURIComponent(it.name) + '" style="width:150px;border:6px solid #fff;border-radius:8px" onerror="this.style.opacity=0.2"><br><span class="meta">用要添加的 QQ 扫码登录,成功后自动绑定</span></div>';
          else if (it.running && it.login && !it.login.isLogin) html += '<div style="margin-top:6px" class="meta">等待二维码… ' + esc(it.login.phase || "") + '</div>';
          html += '</div>';
          if (it.running && it.login && !it.login.isLogin) html += '<button class="gray" data-act="inst-qr" data-name="' + esc(it.name) + '">刷新二维码</button>';
          if (it.running && it.uin) html += '<button class="gray" data-act="rules" data-name="' + esc(it.name) + '">' + (rules && rules.name === it.name ? "收起配置" : "配置") + '</button>';
          if (it.running) html += '<button class="danger" data-act="inst-stop" data-name="' + esc(it.name) + '">停止</button>';
          else html += '<button data-act="inst-start" data-name="' + esc(it.name) + '">启动</button>';
          if (it.name !== "main") html += '<button class="danger" data-act="inst-delete" data-name="' + esc(it.name) + '">删除</button>';
          html += "</div>";
        }
        html += "</div>";
      }
      a.innerHTML = html;
      if (rules && Array.isArray(insts) && !insts.some(x => x.name === rules.name)) closeRules(false);
      const qr = document.getElementById("qr"), qb = document.getElementById("qrbody");
      if (!L.isLogin && L.qrcodeurl) { qr.style.display = "block"; qb.innerHTML = '<img src="' + esc(L.qrcodeurl) + '">'; }
      else if (!L.isLogin) { qr.style.display = "block"; qb.textContent = "等待二维码… 阶段: " + (L.phase || "无") + (L.error ? " / " + L.error : ""); }
      else qr.style.display = "none";
    }
    function numAttr(v){ return (v === undefined || v === null || v === "") ? "" : esc(String(v)); }
    function rulesHead(){
      const it = (Array.isArray(insts) ? insts : []).find(x => x.name === rules.name);
      const label = it && it.nick ? it.nick + " (" + (it.uin || rules.name) + ")" : rules.name;
      return '<div class="rules-head"><h2>账号配置 · ' + esc(label) + '</h2><button class="gray" data-act="rules-close">关闭</button></div>';
    }
    function kwRow(k){
      k = k || {};
      return '<div class="kw">'
        + '<input class="kw-match" placeholder="匹配词" value="' + esc(k.match || "") + '">'
        + '<select class="kw-type"><option value="contains"' + (k.matchType === "prefix" || k.matchType === "regex" ? "" : " selected") + '>包含</option><option value="prefix"' + (k.matchType === "prefix" ? " selected" : "") + '>前缀</option><option value="regex"' + (k.matchType === "regex" ? " selected" : "") + '>正则</option></select>'
        + '<button class="danger" data-act="kw-del">删除</button>'
        + '<textarea class="kw-reply" rows="2" placeholder="回复内容（可含 |）">' + esc(k.reply || "") + '</textarea>'
        + '</div>';
    }
    function renderRules(){
      const box = document.getElementById("rules");
      if (!box) return;
      if (!rules) { box.innerHTML = ""; return; }
      if (rules.loading) { box.innerHTML = '<div class="card"><h2>账号配置 · ' + esc(rules.name) + '</h2><p class="muted">读取中…</p></div>'; return; }
      if (rules.error) {
        box.innerHTML = '<div class="card">' + rulesHead() + '<p class="err">' + esc(rules.error) + '</p><button data-act="rules-retry" data-name="' + esc(rules.name) + '">重试</button></div>';
        return;
      }
      const c = rules.cfg || {};
      const p = c.private || {};
      const g = c.group || {};
      const gr = {};
      if (c.groupConfigs && typeof c.groupConfigs === "object" && !Array.isArray(c.groupConfigs)) {
        Object.keys(c.groupConfigs).forEach(function(gid){ gr[gid] = { enabled: c.groupConfigs[gid] && c.groupConfigs[gid].enabled !== false }; });
      }
      if (c.groupRules && typeof c.groupRules === "object" && !Array.isArray(c.groupRules)) {
        Object.keys(c.groupRules).forEach(function(gid){ gr[gid] = Object.assign({}, gr[gid] || {}, c.groupRules[gid] || {}); });
      }
      const kw = Array.isArray(c.keywords) ? c.keywords : [];
      let html = '<div class="card rules-panel">' + rulesHead();
      html += '<p class="muted">修改该账号的自动回复规则，保存后立即生效。白名单/黑名单/调试模式等未展示字段保持原值。</p>';
      html += '<div class="sw"><input type="checkbox" id="r-enabled"' + (c.enabled ? " checked" : "") + '><label for="r-enabled">启用自动回复(总开关)</label></div>';
      html += '<h3>私聊</h3>';
      html += '<div class="sw"><input type="checkbox" id="r-private-enabled"' + (p.enabled ? " checked" : "") + '><label for="r-private-enabled">私聊自动回复</label></div>';
      html += '<div class="field"><label for="r-private-default">兜底话术(未命中关键词时回复，留空=不回复)</label><textarea id="r-private-default" rows="2">' + esc(p.defaultReply || "") + '</textarea></div>';
      html += '<h3>关键词规则(私聊/群聊共用)</h3>';
      html += '<div id="r-kw">' + (kw.length ? kw.map(kwRow).join("") : '<p class="muted" style="margin:4px 0">暂无关键词规则</p>') + '</div>';
      html += '<button class="gray" data-act="kw-add">+ 添加规则</button>';
      html += '<h3>群聊</h3>';
      html += '<div class="sw"><input type="checkbox" id="r-group-enabled"' + (g.enabled ? " checked" : "") + '><label for="r-group-enabled">群聊自动回复</label></div>';
      html += '<div class="sw"><input type="checkbox" id="r-atonly"' + (c.atOnly ? " checked" : "") + '><label for="r-atonly">仅 @ 我时回复</label></div>';
      html += '<div class="grid3">';
      html += '<div class="field"><label for="r-cooldown">冷却秒数(0=关闭)</label><input type="number" id="r-cooldown" min="0" step="1" value="' + numAttr(c.cooldownSeconds) + '"></div>';
      html += '<div class="field"><label for="r-dmin">随机延迟下限(ms)</label><input type="number" id="r-dmin" min="0" step="50" value="' + numAttr(c.replyDelayMinMs) + '"></div>';
      html += '<div class="field"><label for="r-dmax">随机延迟上限(ms)</label><input type="number" id="r-dmax" min="0" step="50" value="' + numAttr(c.replyDelayMaxMs) + '"></div>';
      html += '</div>';
      html += '<h3>群级规则</h3>';
      html += '<p class="muted">JSON 对象，键=群号；值可含 enabled / atOnly / keywords。留空或 {} 表示不覆盖。</p>';
      html += '<textarea id="r-grouprules" rows="10" spellcheck="false">' + esc(JSON.stringify(gr, null, 2)) + '</textarea>';
      html += '<div class="rules-actions"><button data-act="rules-save">保存配置</button></div>';
      html += '</div>';
      box.innerHTML = html;
    }
    async function loadRules(name){
      rules = { name: name, cfg: null, dirty: false, loading: true, error: "" };
      const box = document.getElementById("rules");
      if (box && box.scrollIntoView) box.scrollIntoView({ behavior: "smooth", block: "start" });
      renderRules();
      render();
      try {
        const r = await fetch(A("account-rules", { name: name }));
        if (r.status === 401) { rules = null; renderRules(); showKey(); return; }
        const j = await r.json();
        if (!rules || rules.name !== name) return;
        if (!j.ok) throw new Error(j.error || "读取失败");
        rules.cfg = j.config || {};
        rules.loading = false;
        renderRules();
      } catch(e) {
        if (!rules || rules.name !== name) return;
        rules.loading = false;
        rules.error = String((e && e.message) || e);
        renderRules();
        toast("读取配置失败: " + rules.error.slice(0, 160), true);
      }
    }
    function closeRules(redraw){
      rules = null;
      const box = document.getElementById("rules");
      if (box) box.innerHTML = "";
      if (redraw) render();
    }
    function toggleRules(name){
      if (rules && rules.name === name) { closeRules(true); return; }
      loadRules(name);
    }
    function syncKeywordModel(){
      if (!rules || !rules.cfg) return;
      rules.cfg.keywords = Array.from(document.querySelectorAll("#r-kw .kw")).map(function(row){
        return {
          match: row.querySelector(".kw-match").value,
          matchType: row.querySelector(".kw-type").value === "prefix" ? "prefix" : row.querySelector(".kw-type").value === "regex" ? "regex" : "contains",
          reply: row.querySelector(".kw-reply").value
        };
      });
    }
    function drawKeywords(){
      const box = document.getElementById("r-kw");
      if (!box || !rules || !rules.cfg) return;
      const kw = rules.cfg.keywords || [];
      box.innerHTML = kw.length ? kw.map(kwRow).join("") : '<p class="muted" style="margin:4px 0">暂无关键词规则</p>';
    }
    function addKeyword(){
      if (!rules || !rules.cfg) return;
      syncKeywordModel();
      rules.cfg.keywords = (rules.cfg.keywords || []).concat([{ match: "", matchType: "contains", reply: "" }]);
      rules.dirty = true;
      drawKeywords();
      const inputs = document.querySelectorAll("#r-kw .kw-match");
      if (inputs.length) inputs[inputs.length - 1].focus();
    }
    function deleteKeyword(el){
      const row = el.closest(".kw");
      if (!row) return;
      row.remove();
      syncKeywordModel();
      if (rules) rules.dirty = true;
      const box = document.getElementById("r-kw");
      if (box && !box.querySelector(".kw")) box.innerHTML = '<p class="muted" style="margin:4px 0">暂无关键词规则</p>';
    }
    function collectRules(){
      if (!rules || !rules.cfg) throw new Error("配置尚未加载");
      const c = Object.assign({}, rules.cfg);
      const val = function(id){ const el = document.getElementById(id); return el ? el.value : ""; };
      const num = function(id, label){
        const v = String(val(id)).trim();
        const n = Number(v);
        if (v === "" || !isFinite(n) || n < 0) throw new Error(label + " 必须是非负数");
        return n;
      };
      const keywords = [];
      const rows = document.querySelectorAll("#r-kw .kw");
      for (let i = 0; i < rows.length; i++) {
        const match = rows[i].querySelector(".kw-match").value.trim();
        const reply = rows[i].querySelector(".kw-reply").value.trim();
        const type = rows[i].querySelector(".kw-type").value === "prefix" ? "prefix" : rows[i].querySelector(".kw-type").value === "regex" ? "regex" : "contains";
        if (!match && !reply) continue;
        if (!match || !reply) throw new Error("第 " + (i + 1) + " 条关键词规则需同时填写匹配词和回复");
        keywords.push({ match: match, matchType: type, reply: reply });
      }
      let groupRules = {};
      const grText = String(val("r-grouprules")).trim();
      if (grText) {
        try { groupRules = JSON.parse(grText); } catch(e) { throw new Error("群级规则 JSON 格式错误: " + ((e && e.message) || e)); }
        if (!groupRules || typeof groupRules !== "object" || Array.isArray(groupRules)) throw new Error("群级规则必须是 JSON 对象(键=群号)");
        const ids = Object.keys(groupRules);
        for (let i = 0; i < ids.length; i++) {
          const gid = ids[i];
          const rule = groupRules[gid];
          if (!/^\\d+$/.test(gid)) throw new Error("群号必须是数字: " + gid);
          if (!rule || typeof rule !== "object" || Array.isArray(rule)) throw new Error("群 " + gid + " 的规则必须是对象");
          if (rule.enabled !== undefined && typeof rule.enabled !== "boolean") throw new Error("群 " + gid + " 的 enabled 必须是布尔值");
          if (rule.atOnly !== undefined && typeof rule.atOnly !== "boolean") throw new Error("群 " + gid + " 的 atOnly 必须是布尔值");
          if (rule.keywords !== undefined) {
            if (!Array.isArray(rule.keywords)) throw new Error("群 " + gid + " 的 keywords 必须是数组");
            for (let j = 0; j < rule.keywords.length; j++) {
              const k = rule.keywords[j];
              if (!k || typeof k !== "object" || typeof k.match !== "string" || typeof k.reply !== "string" || !k.match.trim() || !k.reply.trim()) throw new Error("群 " + gid + " 第 " + (j + 1) + " 条关键词规则不完整");
              if (k.matchType !== undefined && k.matchType !== "contains" && k.matchType !== "prefix" && k.matchType !== "regex") throw new Error("群 " + gid + " 第 " + (j + 1) + " 条 matchType 只能是 contains/prefix/regex");
            }
          }
        }
      }
      if (!keywords.length) throw new Error("至少保留一条关键词规则（autoreply 会忽略空列表；如需完全停用请关闭总开关）");
      const dmin = num("r-dmin", "随机延迟下限");
      const dmax = num("r-dmax", "随机延迟上限");
      if (dmin > dmax) throw new Error("随机延迟下限不能大于上限");
      const next = Object.assign(c, {
        enabled: document.getElementById("r-enabled").checked,
        keywords: keywords,
        private: Object.assign({}, c.private || {}, {
          enabled: document.getElementById("r-private-enabled").checked,
          defaultReply: val("r-private-default")
        }),
        group: Object.assign({}, c.group || {}, { enabled: document.getElementById("r-group-enabled").checked }),
        atOnly: document.getElementById("r-atonly").checked,
        cooldownSeconds: num("r-cooldown", "冷却秒数"),
        replyDelayMinMs: dmin,
        replyDelayMaxMs: dmax,
        groupRules: groupRules
      });
      // 同步旧版群配置字段: 群管理页仍读 groupConfigs, 保持与 groupRules 的 enabled 一致
      if (Object.prototype.hasOwnProperty.call(c, "groupConfigs")) {
        const gc = {};
        Object.keys(groupRules).forEach(function(gid){
          const rule = groupRules[gid] || {};
          const old = c.groupConfigs && typeof c.groupConfigs === "object" ? c.groupConfigs[gid] : null;
          const item = Object.assign({}, old && typeof old === "object" ? old : {});
          if (typeof rule.enabled === "boolean") item.enabled = rule.enabled;
          gc[gid] = item;
        });
        next.groupConfigs = gc;
      }
      // 派生文本字段: 关键词文本同步; 群规则文本删除, 由服务端按结构化规则重新生成, 避免旧文本反向覆盖
      if (Object.prototype.hasOwnProperty.call(c, "keywordsText")) next.keywordsText = keywords.map(function(k){ return k.match + "|" + (k.matchType || "contains") + "|" + k.reply; }).join("\\n");
      if (Object.prototype.hasOwnProperty.call(c, "groupRulesText")) delete next.groupRulesText;
      return next;
    }
    async function saveRules(el){
      if (!rules || !rules.cfg) return;
      const name = rules.name;
      let cfg;
      try { cfg = collectRules(); } catch(e) { toast(String((e && e.message) || e), true); return; }
      el.disabled = true;
      const old = el.textContent;
      el.textContent = "保存中…";
      try {
        const r = await post("account-rules", { name: name, config: cfg });
        if (!r.ok) throw new Error(r.error || "保存失败");
        if (rules && rules.name === name) { rules.cfg = cfg; rules.dirty = false; }
        toast("已保存 " + name + " 的配置");
      } catch(e) {
        toast("保存失败: " + String((e && e.message) || e).slice(0, 160), true);
      } finally {
        el.disabled = false;
        el.textContent = old;
      }
    }
    function toast(msg, err){
      const d = document.createElement("div"); d.className = "toast" + (err ? " err" : "");
      d.textContent = msg; document.body.appendChild(d); setTimeout(() => d.remove(), 3200);
    }
    const armed = new Map();
    function needConfirm(el, label, fn){
      const id = label + (el.getAttribute("data-name") || "");
      if (armed.get(id)) { armed.delete(id); el.textContent = label; fn(); return; }
      armed.set(id, true); el.textContent = "确认?";
      setTimeout(() => { armed.delete(id); el.textContent = label; }, 3000);
    }
    async function doSwitch(name){ const r = await post("switch", {uin: name}); toast(r.ok ? "已发起切换,重启后自动登录 " + name : ("失败: " + r.error), !r.ok); setTimeout(refresh, 6000); }
    async function doInstAdd(){ const r = await post("instance-add"); if (r.ok) { toast("已创建实例 " + (r.name || "") + ",启动中…"); setTimeout(refresh, 6000); } else toast("失败: " + (r.error || JSON.stringify(r).slice(0,100)), true); }
    async function doInstStart(name){ const r = await post("instance-start", {name}); toast(r.ok ? "已启动 " + name : ("失败: " + r.error), !r.ok); setTimeout(refresh, 5000); }
    async function doInstStop(name){ const r = await post("instance-stop", {name}); toast(r.ok ? "已停止 " + name : ("失败: " + r.error), !r.ok); setTimeout(refresh, 4000); }
    async function doInstQr(name){ const r = await post("instance-qr", {name}); toast(r.ok ? "二维码已刷新" : ("失败: " + r.error), !r.ok); setTimeout(refresh, 2500); }
    async function doInstDelete(name){ const r = await post("instance-delete", {name}); toast(r.ok ? "已删除实例 " + name : ("失败: " + r.error), !r.ok); setTimeout(refresh, 1500); }
    async function doSetAuto(){ const uin = st && st.self && st.self.uin; if (!uin) return; const r = await post("set-auto", {uin}); toast(r.ok ? "已设自动登录 " + uin : ("失败: " + r.error), !r.ok); refresh(); }
    document.addEventListener("click", e => {
      const el = e.target.closest("[data-act]");
      if (!el) return;
      const act = el.getAttribute("data-act");
      const name = el.getAttribute("data-name") || "";
      if (act === "switch") needConfirm(el, "切换", () => doSwitch(name));
      else if (act === "rules") toggleRules(name);
      else if (act === "rules-retry") loadRules(name);
      else if (act === "rules-close") { if (rules && rules.dirty) needConfirm(el, "关闭", function(){ closeRules(true); }); else closeRules(true); }
      else if (act === "rules-save") saveRules(el);
      else if (act === "kw-add") addKeyword();
      else if (act === "kw-del") deleteKeyword(el);
      else if (act === "inst-add") doInstAdd();
      else if (act === "inst-start") doInstStart(name);
      else if (act === "inst-stop") needConfirm(el, "停止", () => doInstStop(name));
      else if (act === "inst-qr") doInstQr(name);
      else if (act === "inst-delete") needConfirm(el, "删除", () => doInstDelete(name));
    });
    document.addEventListener("input", e => { if (rules && e.target && e.target.closest && e.target.closest("#rules")) rules.dirty = true; });
    document.addEventListener("change", e => { if (rules && e.target && e.target.closest && e.target.closest("#rules")) rules.dirty = true; });
    refresh();
    setInterval(refresh, 12000);
  `].join("\n");
  return js;
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
  // 账号配置中心：按实例代理 autoreply 配置读写，不改 manager 现有 API
  ctx.router.getNoAuth('/account-rules', async (req, res) => {
    if (!checkKey(req, req.url)) return res.status(401).json({ error: 'key 无效' });
    try {
      const name = String((req.query && req.query.name) || 'main');
      const inst = resolveAccountInstance(name);
      const r = await instanceWebuiCall(inst, AUTOREPLY_PREFIX + '/config');
      res.json({ ok: true, config: unwrapAutoreplyConfig(r) });
    } catch (e) {
      ctx.logger.warn('account-rules(get) 失败: ' + String((e && e.message) || e));
      res.status(500).json({ ok: false, error: String((e && e.message) || e) });
    }
  });
  ctx.router.postNoAuth('/account-rules', async (req, res) => {
    const body = req.body || {};
    if (!checkKey(req, req.url, body)) return res.status(401).json({ error: 'key 无效' });
    try {
      validateRulesConfig(body.config);
    } catch (e) {
      return res.status(400).json({ ok: false, error: String((e && e.message) || e) });
    }
    try {
      const name = String(body.name || 'main');
      const inst = resolveAccountInstance(name);
      const r = await instanceWebuiCall(inst, AUTOREPLY_PREFIX + '/config', body.config);
      if (r && typeof r === 'object' && r.raw !== undefined) throw new Error('autoreply 返回非 JSON: ' + String(r.raw).slice(0, 120));
      if (r && typeof r === 'object' && r.code !== undefined && r.code !== 0) throw new Error('autoreply 保存失败: ' + (r.message || ('code ' + r.code)));
      ctx.logger.info('account-rules 已保存: ' + name);
      res.json({ ok: true });
    } catch (e) {
      ctx.logger.warn('account-rules(set) 失败: ' + String((e && e.message) || e));
      res.status(500).json({ ok: false, error: String((e && e.message) || e) });
    }
  });
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
  ctx.router.getNoAuth('/qr', async (req, res) => {
    if (!checkKey(req, req.url)) return res.status(401).json({ error: 'key 无效' });
    try {
      const name = String((req.query && req.query.name) || 'main');
      const m = await getManager();
      const p = m.qrcodePath(name);
      if (!p) return res.status(404).json({ error: 'no qrcode' });
      const buf = fs.readFileSync(p);
      res.setHeader('Content-Type', 'image/png');
      res.setHeader('Cache-Control', 'no-store');
      res.send(buf);
    } catch (e) { res.status(500).json({ error: String((e && e.message) || e) }); }
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
      try { ensureInstancePluginConfig(r.name); } catch (e) { ctx.logger.warn('补齐新实例插件配置失败: ' + String((e && e.message) || e)); }
      ctx.logger.info('instance-add: ' + JSON.stringify(r));
      res.json({ ok: true, ...r });
    } catch (e) { res.json({ ok: false, error: String((e && e.message) || e) }); }
  });
  ctx.router.postNoAuth('/instance-start', async (req, res) => {
    const body = req.body || {};
    if (!checkKey(req, req.url, body)) return res.status(401).json({ error: 'key 无效' });
    try {
      const m = await getManager();
      const name = String(body.name || '');
      ensureInstancePluginConfig(name);
      const r = await m.startInstance(name);
      res.json({ ok: true, ...r });
    } catch (e) { res.json({ ok: false, error: String((e && e.message) || e) }); }
  });
  ctx.router.postNoAuth('/instance-delete', async (req, res) => {
    const body = req.body || {};
    if (!checkKey(req, req.url, body)) return res.status(401).json({ error: 'key 无效' });
    try {
      const m = await getManager();
      const r = await m.deleteInstance(String(body.name || ''));
      ctx.logger.info('instance-delete: ' + body.name);
      res.json({ ok: true, ...r });
    } catch (e) { res.json({ ok: false, error: String((e && e.message) || e) }); }
  });
  ctx.router.postNoAuth('/instance-qr', async (req, res) => {
    const body = req.body || {};
    if (!checkKey(req, req.url, body)) return res.status(401).json({ error: 'key 无效' });
    try {
      const m = await getManager();
      const r = await m.refreshQrcode(String(body.name || ''));
      res.json({ ok: true, result: r });
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

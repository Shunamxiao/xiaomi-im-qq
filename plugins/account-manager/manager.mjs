// qq-accounts/manager.js — 多账号实例编排（容器内运行）
// 由账号管理插件动态 import；核心职责：注册表 / 进程探测(按 HOME) / 启动(经宿主 bridge) / 停止 / 状态聚合
import fs from 'fs';
import net from 'net';
import http from 'http';
import crypto from 'crypto';

const BASE = '/root/qq-accounts';
const REGISTRY_PATH = BASE + '/registry.json';
const CMD_DIR = '/data/data/com.termux/files/home/qq-accounts/cmd';
const QQ_BIN = '/root/Napcat/opt/QQ/qq';
const MAX_SECONDARY = 3;
const START_TIMEOUT_MS = 60000;

// 主实例只读注册：不参与启停，仅用于统一状态展示
const MAIN_INSTANCE = {
  name: 'main', uin: '1457278554', home: '/root', workdir: '',
  onebotPort: 3001, webuiPort: 6099, webuiToken: 'x524980520', protected: true
};

function loadRegistry() {
  try { return JSON.parse(fs.readFileSync(REGISTRY_PATH, 'utf8')); } catch {}
  return { instances: {}, next: { onebot: 3002, webui: 6100, display: 1 } };
}
function saveRegistry(r) {
  fs.mkdirSync(BASE, { recursive: true });
  fs.writeFileSync(REGISTRY_PATH, JSON.stringify(r, null, 2));
}

// —— 进程探测：遍历 /proc，按 environ 里的 HOME 区分实例 ——
function procHome(pid) {
  try {
    const env = fs.readFileSync('/proc/' + pid + '/environ', 'utf8').replace(/\0/g, '\n');
    const m = /^HOME=(.*)$/m.exec(env);
    return m ? m[1] : '';
  } catch { return ''; }
}
function findInstancePids(home) {
  const pids = [];
  for (const d of fs.readdirSync('/proc')) {
    if (!/^\d+$/.test(d)) continue;
    if (procHome(d) !== home) continue;
    let comm = '';
    try { comm = fs.readFileSync('/proc/' + d + '/comm', 'utf8').trim(); } catch {}
    if (comm === 'qq' || comm === 'Xvfb') pids.push(Number(d));
  }
  return pids;
}
export function isRunning(home) {
  return findInstancePids(home).some(p => {
    try { return fs.readFileSync('/proc/' + p + '/comm', 'utf8').trim() === 'qq'; } catch { return false; }
  });
}
export function killInstance(home) {
  for (const p of findInstancePids(home)) { try { process.kill(p, 'SIGTERM'); } catch {} }
  return new Promise(resolve => setTimeout(() => {
    for (const p of findInstancePids(home)) { try { process.kill(p, 'SIGKILL'); } catch {} }
    resolve(true);
  }, 3000));
}

// —— 实例启动脚本（HOME + NAPCAT_WORKDIR 双隔离；Xvfb 显示号独立）——
export function instanceScript(inst) {
  const log = inst.home + '/instance.log';
  return [
    '#!/bin/bash',
    '# 由 account-manager 生成：HOME 隔离 QQ 数据，NAPCAT_WORKDIR 隔离 NapCat 配置',
    'export HOME=' + inst.home,
    'export NAPCAT_WORKDIR=' + inst.workdir,
    'export NAPCAT_WEBUI_PREFERRED_PORT=' + inst.webuiPort,
    'Xvfb :' + inst.display + ' -screen 0 1280x720x24 >' + log + ' 2>&1 &',
    'sleep 2',
    'DISPLAY=:' + inst.display + ' LD_PRELOAD=/usr/lib/fakehost.so exec ' + QQ_BIN + ' --no-sandbox --disable-dev-shm-usage --disable-gpu --disable-software-rasterizer --disable-gpu-compositing >>' + log + ' 2>&1'
  ].join('\n') + '\n';
}
function writeInstanceFiles(inst) {
  fs.mkdirSync(inst.home, { recursive: true });
  fs.writeFileSync(BASE + '/' + inst.name + '/instance.sh', instanceScript(inst), { mode: 0o755 });
}

// —— 每实例 WebUI 调用（token 从其实例配置读取，凭据短缓存）——
function readWebuiToken(inst) {
  if (inst.protected) return inst.webuiToken;
  const p = inst.workdir + '/config/webui.json';
  try {
    const j = JSON.parse(fs.readFileSync(p, 'utf8'));
    if (j.token) { inst.webuiToken = j.token; return j.token; }
  } catch {}
  return inst.webuiToken || '';
}
function rawRequest(port, path, body, headers) {
  return new Promise((resolve, reject) => {
    const data = body ? JSON.stringify(body) : null;
    const req = http.request({ host: '127.0.0.1', port, path, method: body ? 'POST' : 'GET',
      headers: { 'Content-Type': 'application/json', ...(data ? { 'Content-Length': Buffer.byteLength(data) } : {}), ...(headers || {}) } },
      res => { let d = ''; res.on('data', c => d += c); res.on('end', () => { try { resolve(JSON.parse(d)); } catch { resolve({ raw: d }); } }); });
    req.on('error', reject);
    req.setTimeout(6000, () => req.destroy(new Error('timeout')));
    if (data) req.write(data);
    req.end();
  });
}
const credCache = new Map(); // webuiPort -> {token, cred, time}
async function loginInstance(inst, token) {
  const r = await rawRequest(inst.webuiPort, '/api/auth/login', { hash: crypto.createHash('sha256').update(token + '.napcat').digest('hex') });
  const cred = (r.data && r.data.Credential) || r.Credential;
  if (r.code !== 0 || !cred) throw new Error('实例 WebUI 登录失败: ' + (r.message || 'unknown'));
  const cached = { token, cred, time: Date.now() };
  credCache.set(inst.webuiPort, cached);
  return cached;
}
async function webuiCall(inst, path, body) {
  const token = readWebuiToken(inst);
  let cached = credCache.get(inst.webuiPort);
  if (!cached || cached.token !== token || Date.now() - cached.time > 50 * 60 * 1000) {
    cached = await loginInstance(inst, token);
  }
  let r = await rawRequest(inst.webuiPort, path, body || {}, { Authorization: 'Bearer ' + cached.cred });
  // 实例重启后 secret 变化会使旧凭证失效(HTTP 200 + code:-1),自动重新登录重试一次
  if (r && r.code === -1) {
    credCache.delete(inst.webuiPort);
    cached = await loginInstance(inst, token);
    r = await rawRequest(inst.webuiPort, path, body || {}, { Authorization: 'Bearer ' + cached.cred });
  }
  return r;
}
function tcpProbe(port, timeout = 1500) {
  return new Promise(resolve => {
    const s = net.connect({ host: '127.0.0.1', port });
    const done = ok => { try { s.destroy(); } catch {} resolve(ok); };
    s.setTimeout(timeout, () => done(false));
    s.on('connect', () => done(true));
    s.on('error', () => done(false));
  });
}

// —— 单实例状态 ——
async function instanceStatus(inst) {
  const running = isRunning(inst.home);
  const st = {
    name: inst.name, uin: inst.uin || '', home: inst.home, webuiPort: inst.webuiPort,
    onebotPort: inst.onebotPort || null, running, onebotOk: false, autologin: !!inst.autologin,
    login: { isLogin: false, phase: '', qrcodeurl: '', uin: '' }
  };
  if (inst.onebotPort) st.onebotOk = running && await tcpProbe(inst.onebotPort);
  if (running && inst.webuiPort) {
    try {
      const r = await webuiCall(inst, '/api/QQLogin/CheckLoginStatus', {});
      const d = (r && typeof r === 'object' && 'data' in r) ? r.data : r;
      if (d) {
        st.login.isLogin = !!d.isLogin;
        st.login.phase = d.loginPhase || '';
        st.login.qrcodeurl = d.qrcodeurl || '';
        st.login.uin = String(d.uin || (d.selfInfo && d.selfInfo.uin) || '');
      }
      // 登录后补充账号资料(昵称/头像),用于页面账号绑定展示
      if (st.login.isLogin) {
        try {
          const infoRaw = await webuiCall(inst, '/api/QQLogin/GetQQLoginInfo', {});
          const info = (infoRaw && typeof infoRaw === 'object' && 'data' in infoRaw) ? infoRaw.data : infoRaw;
          if (info) {
            st.nick = String(info.nick || info.nickname || '');
            st.avatarUrl = info.avatarUrl || info.avatar || '';
            const iu = String(info.uin || info.uid || '');
            if (iu) { st.uin = iu; st.login.uin = iu; }
          }
        } catch {}
      }
    } catch (e) { st.login.phase = 'webui-error: ' + String((e && e.message) || e).slice(0, 80); }
  }
  return st;
}

// 登录后绑定：扫码成功 → 从 workdir/config/napcat_<uin>.json 识别 uin → 写 OneBot 配置 → 重启实例生效
async function pollBinding(reg, inst) {
  if (inst.uin || !isRunning(inst.home)) return;
  let uin = '';
  try {
    for (const f of fs.readdirSync(inst.workdir + '/config')) {
      const m = /^napcat_(\d{5,12})\.json$/.exec(f);
      if (m) { uin = m[1]; break; }
    }
  } catch {}
  if (!uin) return;
  if (!inst.onebotPort) {
    reg.next = reg.next || { onebot: 3002, webui: 6100, display: 1 };
    inst.onebotPort = reg.next.onebot++;
  }
  // OneBot WS 配置：结构对齐主实例 onebot11_*.json
  const cfg = {
    network: {
      httpServers: [], httpSseServers: [], httpClients: [],
      websocketServers: [{ name: 'LocalBot', enable: true, host: '127.0.0.1', port: inst.onebotPort,
        enableForce: false, messagePostFormat: 'array', reportSelfMessage: false, token: '', debug: false, heartInterval: 30000 }],
      websocketClients: [], plugins: []
    },
    musicSignUrl: '', enableLocalFile2Url: false, parseMultMsg: false, imageDownloadProxy: '',
    timeout: { baseTimeout: 10000, uploadSpeedKBps: 256, downloadSpeedKBps: 256, maxTimeout: 1800000 }
  };
  fs.writeFileSync(inst.workdir + '/config/onebot11_' + uin + '.json', JSON.stringify(cfg, null, 2));
  try {
    const p = inst.workdir + '/config/webui.json';
    const j = JSON.parse(fs.readFileSync(p, 'utf8'));
    j.autoLoginAccount = uin;
    fs.writeFileSync(p, JSON.stringify(j, null, 4));
  } catch {}
  inst.uin = uin;
  inst.autologin = true;
  saveRegistry(reg);
  await killInstance(inst.home);
  await startInstance(inst.name);
}

// 实例二维码图片路径(NapCat 保存到 <workdir>/cache/qrcode.png)
export function qrcodePath(name) {
  const reg = loadRegistry();
  const inst = name === 'main' ? MAIN_INSTANCE : reg.instances[name];
  if (!inst) return '';
  const wd = inst.workdir || '/root/.config/QQ';
  const p = wd + '/cache/qrcode.png';
  try { return fs.existsSync(p) ? p : ''; } catch { return ''; }
}

// —— 对外 API ——
export async function listInstances() {
  const reg = loadRegistry();
  const list = [await instanceStatus(MAIN_INSTANCE)];
  for (const name of Object.keys(reg.instances)) {
    const inst = reg.instances[name];
    try { await pollBinding(reg, inst); } catch {}
    list.push(await instanceStatus(inst));
  }
  return list;
}
export async function addInstance() {
  const reg = loadRegistry();
  if (Object.keys(reg.instances).length >= MAX_SECONDARY) throw new Error('从实例已达上限 ' + MAX_SECONDARY + '（设备内存有限）');
  reg.next = reg.next || { onebot: 3002, webui: 6100, display: 1 };
  const webui = reg.next.webui++;
  const inst = {
    name: 'acc-' + webui, uin: '',
    home: BASE + '/acc-' + webui,
    workdir: BASE + '/acc-' + webui + '/workdir',
    display: reg.next.display++,
    onebotPort: null, // 登录绑定 uin 时分配
    webuiPort: webui, webuiToken: '', autologin: false
  };
  reg.instances[inst.name] = inst;
  saveRegistry(reg);
  writeInstanceFiles(inst);
  await startInstance(inst.name);
  return { name: inst.name, webuiPort: inst.webuiPort };
}
export async function startInstance(name) {
  const reg = loadRegistry();
  const inst = reg.instances[name];
  if (!inst) throw new Error('实例不存在: ' + name);
  if (isRunning(inst.home)) return { ok: true, running: true };
  writeInstanceFiles(inst);
  fs.mkdirSync(CMD_DIR, { recursive: true });
  fs.writeFileSync(CMD_DIR + '/' + name + '.start', name + '\n');
  const t0 = Date.now();
  while (Date.now() - t0 < START_TIMEOUT_MS) {
    await new Promise(r => setTimeout(r, 2000));
    if (isRunning(inst.home)) return { ok: true, running: true };
  }
  throw new Error('启动超时（检查宿主 bridge 是否运行: /data/adb/bin/mi8-account-bridge.sh）');
}
export async function stopInstance(name) {
  if (name === MAIN_INSTANCE.name || name === MAIN_INSTANCE.uin) throw new Error('主实例不允许通过本接口停止');
  const reg = loadRegistry();
  const inst = reg.instances[name];
  if (!inst) throw new Error('实例不存在: ' + name);
  try { fs.unlinkSync(CMD_DIR + '/' + name + '.start'); } catch {}
  await killInstance(inst.home);
  return { ok: !isRunning(inst.home) };
}

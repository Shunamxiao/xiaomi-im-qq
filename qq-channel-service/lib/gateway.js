'use strict';
/**
 * 通道 B：pd.qq.com web 网关直发（不依赖真机/ Frida）。
 *
 * 协议结论（2026-11-15 实验验证）：
 * - 网关: POST https://pd.qq.com/qunng/guild/gotrpc/auth/trpc.qchannel.commwriter.ComWriter/PublishFeed
 * - 鉴权: cookie p_skey(pd.qq.com 域) + uin/p_uin(格式 o+tinyid)。Android 端 pskey 与 web 端
 *   p_skey 为不同登录态签发，不可互用（Android pskey 实测 retcode=4003 invalid pskey）。
 * - web p_skey 来源：浏览器扫码登录 pd.qq.com 后的登录态。凭证通过页面上下文导出或
 *   手动写入 credentials-web.json。到期后重新扫码即可。
 *
 * credentials-web.json 格式:
 *   { "pskey": "...", "tinyid": "144115...", "updatedAt": "ISO8601" }
 */
const fs = require('fs');
const path = require('path');
const https = require('https');

const GATEWAY = 'https://pd.qq.com/qunng/guild/gotrpc/auth/trpc.qchannel.commwriter.ComWriter/PublishFeed';
const CRED_FILE = path.join(__dirname, '..', 'credentials-web.json');
// web 端 pskey 实测有效期约 1-7 天；到期前提示重新扫码
const PSKEY_MAX_AGE_MS = 6 * 24 * 3600 * 1000;

/** QQ web 系通用 bkn：g_tk 风格 hash（5381 累加） */
function getBkn(skey) {
  let hash = 5381;
  for (let i = 0; i < skey.length; i++) {
    hash += (hash << 5) + skey.charCodeAt(i);
    hash &= 0x7fffffff;
  }
  return hash;
}

function loadCredentials() {
  try {
    const c = JSON.parse(fs.readFileSync(CRED_FILE, 'utf8'));
    c.ageMs = Date.now() - new Date(c.updatedAt || 0).getTime();
    c.stale = c.ageMs > PSKEY_MAX_AGE_MS;
    return c;
  } catch {
    return null;
  }
}

/**
 * 通过网关发布。guildId/channelId 为字符串；text 为纯文本；
 * images 为 jsonFeed images 数组（可选）；extraFeedFields 可覆盖任意 jsonFeed 顶层字段。
 * 返回 { ok, idd?, retcode?, msg? }
 */
function publishFeed({ guildId, channelId, text, tinyid, images, extraFeedFields }) {
  const cred = loadCredentials();
  if (!cred || !cred.pskey) {
    return Promise.resolve({ ok: false, error: 'no web credentials: 扫码登录 pd.qq.com 后将凭证写入 credentials-web.json' });
  }
  if (cred.stale) {
    return Promise.resolve({ ok: false, error: `web pskey 已过期(已 ${Math.round(cred.ageMs / 3600000)} 小时)，请重新扫码更新 credentials-web.json` });
  }
  const tid = tinyid || cred.tinyid;
  const clientKey = 'GuildWeb_' + Date.now() + '_' + Math.random().toString(16).slice(2, 10);
  const jsonFeed = {
    feed_type: 1,
    title: {},
    poster: { id: tid },
    channelInfo: { sign: { guild_id: guildId, channel_id: channelId }, is_square: true },
    tagInfos: [], recommend_channels: [], images: images || [], videos: [],
    client_task_id: clientKey,
    media_lock_count: 0,
    content_with_style: { paragraphs: [{ elems: [{ text: { text_content: { text } }, type: 1 }] }] },
    advanced_settings: { not_allow_download: false, show_watermark: false },
    files: [],
    ...extraFeedFields
  };
  const uinCookie = 'o' + tid;
  const cookie = `p_skey=${cred.pskey}; uin=${uinCookie}; p_uin=${uinCookie}`;
  const body = JSON.stringify(jsonFeed);
  return new Promise((resolve) => {
    const req = https.request(GATEWAY, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Content-Length': Buffer.byteLength(body),
        'Cookie': cookie,
        'Referer': 'https://pd.qq.com/',
        'Origin': 'https://pd.qq.com',
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36'
      }
    }, (res) => {
      let data = '';
      res.on('data', (c) => { data += c; });
      res.on('end', () => {
        try {
          const j = JSON.parse(data);
          // 成功时 data.data 内含 feed idd（格式待首次成功样本确认）
          const idd = (j.data && (j.data.idd || (j.data.feed && j.data.feed.idd))) || null;
          resolve({ ok: j.retcode === 0, retcode: j.retcode, msg: j.msg, idd });
        } catch (e) {
          resolve({ ok: false, error: 'gateway non-JSON response (可能被 WAF 拦截): ' + data.slice(0, 120) });
        }
      });
    });
    req.on('error', (e) => resolve({ ok: false, error: String(e) }));
    req.setTimeout(20000, () => { req.destroy(); resolve({ ok: false, error: 'gateway timeout' }); });
    req.end(body);
  });
}

module.exports = { publishFeed, loadCredentials, getBkn, GATEWAY, CRED_FILE };

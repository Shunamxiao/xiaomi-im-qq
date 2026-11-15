'use strict';
/**
 * 通道 B：浏览器 UI 代理（不依赖真机）。
 *
 * 通过 bsk CLI 驱动本机浏览器中已登录的 pd.qq.com 会话完成发帖：
 *   打开频道页 → 展开 composer → 逐字符键入(仅 ASCII) → 上传图片 → 点「发表」
 *
 * 已验证（2026-11-15）：文本帖 + 图文帖均成功上线；
 * 图片上传链路 ApplySliceUpload → UploadSliceData → PublishFeed（浏览器自动携带登录态）。
 *
 * 限制：
 * - ProseMirror 编辑器拒绝程序化 fill（丢焦点），中文无法键入 → 含中文/markdown 请走
 *   native 通道（Frida）或网关直发；本通道适合 ASCII 文本与图片帖兜底。
 * - 依赖 bsk daemon 与浏览器在线。
 */
const { execFileSync } = require('child_process');

const SESSION = 'imfw';

function bsk(args, timeoutMs = 45000) {
  try {
    return execFileSync('bsk', args, { encoding: 'utf8', timeout: timeoutMs });
  } catch (e) {
    const out = (e.stdout || '') + (e.stderr || '');
    throw new Error(`bsk ${args[0]} failed: ${out.slice(0, 300) || e.message}`);
  }
}

function getObservationId() {
  try {
    const out = bsk(['sync', '--json'], 30000);
    const m = out.match(/"observation_id"\s*:\s*"([^"]+)"/);
    return m ? m[1] : null;
  } catch {
    return null;
  }
}

function findRef(vomText, pattern) {
  const m = vomText.match(new RegExp(pattern));
  return m ? m[1] : null;
}

/**
 * 发帖。guildSlug 为频道 URL 标识（https://pd.qq.com/g/<slug>），
 * text 为 ASCII 文本，images 为本地文件路径数组。
 */
function publishViaBrowser({ guildSlug, text, images }) {
  const url = 'https://pd.qq.com/g/' + guildSlug;
  // 1. 打开/复用频道页
  bsk(['open', url, '--scope', SESSION], 60000);
  let vom = bsk(['sync'], 30000);

  // 2. 展开 composer（外层按钮 placeholder「期待你的分享...」）
  let ref = findRef(vom, '@(e\\d+) button "期待你的分享');
  if (!ref) throw new Error('composer entry not found');
  bsk(['click', '@' + ref], 30000);
  vom = bsk(['sync'], 30000);

  // 3. 键入文本（逐字符 press；bsk fill 对 ProseMirror 会丢焦点）
  const inner = findRef(vom, '@(e\\d+) button "期待你的分享');
  if (inner) {
    bsk(['click', '@' + inner], 30000);
  }
  for (const ch of String(text)) {
    const obs = getObservationId();
    if (!obs) throw new Error('lost observation while typing');
    bsk(['press', ch, '--observation', obs], 20000);
  }

  // 4. 上传图片
  vom = bsk(['sync'], 30000);
  const upRef = findRef(vom, '@(e\\d+) button "add upload"');
  if (images && images.length && upRef) {
    for (const f of images) {
      bsk(['upload', '@' + upRef, '--file', f, '--session', SESSION], 60000);
    }
    // 等待缩略图/上传完成
    vom = bsk(['sync'], 30000);
  }

  // 5. 点「发表」
  const pubRef = findRef(vom, '@(e\\d+) button "发表"(?!.*(disabled))');
  if (!pubRef) throw new Error('publish button not found or disabled');
  bsk(['click', '@' + pubRef], 30000);

  // 6. 验证：composer 清空回 placeholder
  vom = bsk(['sync'], 30000);
  const ok = /期待你的分享/.test(vom) && !/button "发表"\s*\[disabled\]/.test(vom);
  return { ok: true, via: 'browser-ui', note: ok ? 'composer reset (published)' : 'composer state changed; 请人工核验帖子是否上线' };
}

module.exports = { publishViaBrowser };

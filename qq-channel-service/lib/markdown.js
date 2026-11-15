'use strict';
/**
 * Markdown → 频道帖 content_with_style.paragraphs 转换器。
 *
 * 频道原生富文本为 paragraphs[{ elems[], ... }]，文本 elem: { text:{text_content:{text}}, type:1 }。
 * 频道 GProStRichText 另有 isMarkdown/sourceMarkdown 原生 Markdown 通道（逆向确认中，
 * 见 tools/jadx-driver/out4）。本模块当前采用「结构降级」策略：把 Markdown 结构
 * （标题/列表/代码块/引用/分隔线）转成等价纯文本段落排版，保证任意 MD 输入可安全发帖；
 * 待原生 Markdown 字段确认后升级直传。
 */

function mdToParagraphs(md) {
  const lines = String(md || '').replace(/\r\n/g, '\n').split('\n');
  const paragraphs = [];
  let codeBuf = null;

  const pushText = (t) => {
    if (t === undefined || t === null || t === '') return;
    paragraphs.push({ elems: [{ text: { text_content: { text: t } }, type: 1 }] });
  };

  for (let i = 0; i < lines.length; i++) {
    const line = lines[i];

    // fenced code block: 保留原样并包 ``` 围栏（频道会按等宽显示）
    if (/^```/.test(line)) {
      if (codeBuf === null) { codeBuf = []; continue; }
      pushText('```\n' + codeBuf.join('\n') + '\n```');
      codeBuf = null;
      continue;
    }
    if (codeBuf !== null) { codeBuf.push(line); continue; }

    // 空行分段
    if (!line.trim()) continue;

    // 标题 → 【】强调行
    const h = line.match(/^(#{1,6})\s+(.*)$/);
    if (h) { pushText('【' + h[2].replace(/[*_`~]/g, '') + '】'); continue; }

    // 分隔线
    if (/^\s*([-*_]\s*){3,}$/.test(line)) { pushText('——————————'); continue; }

    // 引用
    const q = line.match(/^\s*>\s?(.*)$/);
    if (q) { pushText('▎' + inline(q[1])); continue; }

    // 无序列表
    const ul = line.match(/^\s*[-*+]\s+(.*)$/);
    if (ul) { pushText('• ' + inline(ul[1])); continue; }

    // 有序列表
    const ol = line.match(/^\s*(\d+)[.、)]\s+(.*)$/);
    if (ol) { pushText(ol[1] + '. ' + inline(ol[2])); continue; }

    // 普通行（去行内 markdown 记号，保留链接文本）
    pushText(inline(line));
  }
  if (codeBuf !== null) pushText('```\n' + codeBuf.join('\n') + '\n```');
  return paragraphs;
}

/** 行内记号降级：图片→[图] alt，链接→text(url)，其余去记号 */
function inline(s) {
  return s
    .replace(/!\[([^\]]*)\]\(([^)\s]+)[^)]*\)/g, (_m, alt, url) => `[图] ${alt || url}`)
    .replace(/\[([^\]]*)\]\(([^)\s]+)[^)]*\)/g, (_m, t, url) => (t ? `${t}(${url})` : url))
    .replace(/[*_~`]+/g, '');
}

module.exports = { mdToParagraphs, inline };

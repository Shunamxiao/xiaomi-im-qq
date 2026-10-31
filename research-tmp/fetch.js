const fs = require("fs");
const path = require("path");
const DIR = path.join(__dirname, "out");
fs.mkdirSync(DIR, { recursive: true });
const targets = [
  ["spec-main", "https://modelcontextprotocol.io/specification/2026-07-28"],
  ["spec-changelog", "https://modelcontextprotocol.io/specification/2026-07-28/changelog"],
  ["spec-transports", "https://modelcontextprotocol.io/specification/2026-07-28/basic/transports"],
  ["spec-tools", "https://modelcontextprotocol.io/specification/2026-07-28/server/tools"],
  ["spec-resources", "https://modelcontextprotocol.io/specification/2026-07-28/server/resources"],
  ["spec-auth", "https://modelcontextprotocol.io/specification/2026-07-28/basic/authorization"],
  ["ts-sdk-readme", "https://raw.githubusercontent.com/modelcontextprotocol/typescript-sdk/main/README.md"],
  ["gh-ts-sdk", "https://api.github.com/repos/modelcontextprotocol/typescript-sdk"],
  ["gh-openclaw", "https://api.github.com/repos/openclaw/openclaw"],
  ["gh-search-onebot-mcp", "https://api.github.com/search/repositories?q=onebot+mcp&sort=stars&per_page=15"],
  ["gh-search-napcat-mcp", "https://api.github.com/search/repositories?q=napcat+mcp&sort=stars&per_page=15"],
  ["gh-search-qq-mcp", "https://api.github.com/search/repositories?q=qq+mcp+server&sort=stars&per_page=15"],
];
(async () => {
  for (const [name, url] of targets) {
    try {
      const r = await fetch(url, { headers: { "User-Agent": "research-agent", "Accept": "application/vnd.github+json" } });
      const t = await r.text();
      fs.writeFileSync(path.join(DIR, name + ".txt"), t);
      console.log(name, r.status, t.length);
    } catch (e) { console.log(name, "ERR", e.message); }
  }
})();

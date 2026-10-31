const fs = require("fs");
const path = require("path");
const DIR = path.join(__dirname, "out");
function grab(file, keywords, ctx = 350) {
  const t = fs.readFileSync(path.join(DIR, file), "utf8");
  const lines = t.split("\n");
  const out = [];
  for (const kw of keywords) {
    let found = 0;
    for (let i = 0; i < lines.length && found < 3; i++) {
      if (lines[i].toLowerCase().includes(kw.toLowerCase())) {
        out.push("--- [" + kw + "] line " + i + " ---\n" + lines.slice(i, i + 6).join("\n").slice(0, 900));
        found++;
        i += 6;
      }
    }
  }
  return out.join("\n\n");
}
console.log("=====CHANGELOG=====");
console.log(grab("spec-changelog.text", ["2026-07-28", "2025-11-25", "2025-06-18"], 200).slice(0, 4000));

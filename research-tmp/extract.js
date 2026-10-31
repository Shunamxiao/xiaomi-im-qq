const fs = require("fs");
const path = require("path");
const DIR = path.join(__dirname, "out");

function htmlToText(h) {
  return h
    .replace(/<script[\s\S]*?<\/script>/gi, " ")
    .replace(/<style[\s\S]*?<\/style>/gi, " ")
    .replace(/<[^>]+>/g, "\n")
    .replace(/&amp;/g, "&").replace(/&lt;/g, "<").replace(/&gt;/g, ">").replace(/&quot;/g, '"').replace(/&#x27;/g, "'").replace(/&[a-z]+;/gi, " ")
    .replace(/[ \t]+/g, " ")
    .replace(/\n\s*\n+/g, "\n")
    .trim();
}

for (const f of fs.readdirSync(DIR)) {
  const p = path.join(DIR, f);
  const raw = fs.readFileSync(p, "utf8");
  if (f.startsWith("gh-search")) {
    const j = JSON.parse(raw);
    const rows = (j.items || []).map(r => [r.full_name, r.stargazers_count + "*", (r.description || "").slice(0, 110), r.pushed_at.slice(0, 10), r.html_url].join(" | "));
    fs.writeFileSync(path.join(DIR, f.replace(".txt", ".summary")), "total:" + j.total_count + "\n" + rows.join("\n"));
    console.log(f, "total", j.total_count);
  } else if (f.startsWith("gh-")) {
    const j = JSON.parse(raw);
    fs.writeFileSync(path.join(DIR, f + ".summary"), [j.full_name, j.stargazers_count + "*", j.open_issues_count + " issues", j.pushed_at, j.description, j.homepage, j.html_url].join("\n"));
  } else {
    const t = htmlToText(raw);
    fs.writeFileSync(path.join(DIR, f.replace(".txt", ".text")), t);
  }
}
console.log("done");

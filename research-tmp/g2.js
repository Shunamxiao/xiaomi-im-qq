const fs = require("fs");
const t = fs.readFileSync("research-tmp/out/spec-changelog.text", "utf8").split("
");
console.log(t.slice(62, 330).join("
").slice(0, 9000));

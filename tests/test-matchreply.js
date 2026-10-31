// matchReply 单元测试：node tests/test-matchreply.js
const assert = require("assert");
const { matchReply } = require("../bot/autoreply.js");

const base = {
    enabled: true,
    keywords: [{ match: "在吗", reply: "在的" }],
    private: { enabled: true, defaultReply: "AR", whitelist: [] },
    group: { enabled: false, whitelist: [] },
};

let passed = 0;
function check(name, actual, expected) {
    assert.strictEqual(actual, expected, name);
    passed++;
}

check("关键词命中", matchReply(base, "private", 111, 999, "在吗"), "在的");
check("关键词未命中走私聊兜底", matchReply(base, "private", 111, 999, "你好"), "AR");
check("全局关闭", matchReply(Object.assign({}, base, { enabled: false }), "private", 111, 999, "在吗"), null);
check("私聊开关关闭", matchReply(Object.assign({}, base, { private: { enabled: false, defaultReply: "AR", whitelist: [] } }), "private", 111, 999, "在吗"), null);
check("群聊关闭", matchReply(base, "group", 222, 999, "在吗"), null);
check("自己消息不回复", matchReply(base, "private", 999, 999, "在吗"), null);
check("空关键词规则跳过", matchReply(Object.assign({}, base, { keywords: [{ match: "", reply: "x" }] }), "private", 111, 999, "任意"), "AR");
check("私聊白名单命中", matchReply(Object.assign({}, base, { private: { enabled: true, defaultReply: "AR", whitelist: [111] } }), "private", 111, 999, "在吗"), "在的");
check("私聊白名单未命中", matchReply(Object.assign({}, base, { private: { enabled: true, defaultReply: "AR", whitelist: [222] } }), "private", 111, 999, "在吗"), null);
check("群聊白名单+开关开启", matchReply(Object.assign({}, base, { group: { enabled: true, whitelist: [222] } }), "group", 222, 999, "在吗"), "在的");
check("群聊白名单未命中", matchReply(Object.assign({}, base, { group: { enabled: true, whitelist: [333] } }), "group", 222, 999, "在吗"), null);
const prefix = Object.assign({}, base, {
    keywords: [
        { match: "有", matchType: "prefix", reply: "去apks.cc" },
        { match: "下载", reply: "去apks.cc" }
    ],
    group: { enabled: true, whitelist: [] }
});
check("前缀命中", matchReply(prefix, "group", 222, 999, "有UU吗"), "去apks.cc");
check("前缀未命中(包含不算)", matchReply(prefix, "group", 222, 999, "我有话说"), null);
check("普通包含仍生效", matchReply(prefix, "group", 222, 999, "哪里能下载啊"), "去apks.cc");

console.log("PASS " + passed + " tests");

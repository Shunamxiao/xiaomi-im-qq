// QQ 自动回复机器人：OneBot 11 正向 WS 客户端
// 规则文件 /root/autoreply-rules.json，enabled=false 时只记录不回复

const fs = require("fs");

const RULES_PATH = process.env.AUTOREPLY_RULES || "/root/autoreply-rules.json";
const WS_URL = process.env.AUTOREPLY_WS || "ws://127.0.0.1:3001";

function loadRules(path) {
    return JSON.parse(fs.readFileSync(path, "utf8"));
}

// 纯逻辑，便于单元测试：返回应答文本或 null
function matchReply(rules, kind, senderId, selfId, text) {
    if (!rules.enabled) return null;
    if (kind === "private" && !rules.private.enabled) return null;
    if (kind === "group" && !rules.group.enabled) return null;
    if (String(senderId) === String(selfId)) return null;
    const list = kind === "private" ? rules.private.whitelist : rules.group.whitelist;
    if (list.length > 0 && !list.map(String).includes(String(senderId))) return null;
    for (const rule of rules.keywords || []) {
        if ((rule.match || "").length > 0 && text.includes(rule.match)) {
            return rule.reply;
        }
    }
    if (kind === "private" && rules.private.defaultReply) {
        return rules.private.defaultReply;
    }
    return null;
}

function log(msg) {
    console.log(new Date().toLocaleString("zh-CN", { hour12: false }) + " " + msg);
}

function main() {
    const WebSocket = require("ws");
    const rules = loadRules(RULES_PATH);
    let selfId = "";
    let ws = null;

    function send(action, params) {
        ws.send(JSON.stringify({ action, params, echo: "ar-" + Date.now() }));
    }

    function connect() {
        ws = new WebSocket(WS_URL);
        ws.on("open", () => log("WS connected: " + WS_URL));
        ws.on("message", (data) => {
            try {
                const event = JSON.parse(data);
                if (event.meta_event_type === "lifecycle") {
                    send("get_login_info", {});
                }
                if (event.post_type === "meta_event" && event.status) {
                    selfId = event.status.self_id || selfId;
                    return;
                }
                if (event.post_type !== "message" && event.post_type !== "message_sent") return;
                const kind = event.message_type;
                const text = (event.message || [])
                    .filter((seg) => seg.type === "text")
                    .map((seg) => seg.data.text)
                    .join("");
                log("recv " + kind + " from " + (event.sender ? event.sender.user_id : "?") + ": " + text.slice(0, 60));
                const reply = matchReply(rules, kind, event.sender ? event.sender.user_id : 0, selfId, text);
                if (reply) {
                    const target = kind === "private"
                        ? { user_id: event.sender.user_id }
                        : { group_id: event.group_id };
                    setTimeout(() => send("send_" + (kind === "private" ? "private" : "group") + "_msg",
                        Object.assign({ message: [{ type: "text", data: { text: reply } }] }, target)), 800);
                    log("reply -> " + reply);
                }
            } catch (error) {
                log("handler error: " + error.message);
            }
        });
        ws.on("close", () => {
            log("WS closed, retry in 5s");
            setTimeout(connect, 5000);
        });
        ws.on("error", (error) => log("WS error: " + error.message));
    }

    fs.watchFile(RULES_PATH, () => {
        try {
            Object.assign(rules, loadRules(RULES_PATH));
            log("rules reloaded, enabled=" + rules.enabled);
        } catch (error) {
            log("rules reload failed: " + error.message);
        }
    });
    connect();
}

module.exports = { matchReply, loadRules };

if (require.main === module) {
    main();
}

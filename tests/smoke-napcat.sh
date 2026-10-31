#!/bin/bash
# NapCat 栈冒烟测试（在 mi8 上以 root 运行）
# 用法: (Get-Content tests/smoke-napcat.sh -Raw) -replace "\r" | ssh mi8 'bash -s'
PASS=0
FAIL=0
AMUX=/data/adb/bin/astermux
RF=/data/data/com.termux/files/usr/var/lib/proot-distro/containers/napcat/rootfs

check() {
    local name="$1" result="$2"
    if [ "$result" = "OK" ]; then
        echo "PASS  $name"; PASS=$((PASS+1))
    else
        echo "FAIL  $name ($result)"; FAIL=$((FAIL+1))
    fi
}

pgrep -f "[o]pt/QQ/qq" >/dev/null 2>&1 && check "QQ(NapCat) 进程存活" OK || check "QQ(NapCat) 进程存活" DEAD
pgrep -f "[a]utoreply.js" >/dev/null 2>&1 && check "自动回复机器人进程存活" OK || check "自动回复机器人进程存活" DEAD
ss -tln | grep -q ":3001" && check "OneBot WS 3001 监听" OK || check "OneBot WS 3001 监听" NOT_LISTENING
ss -tln | grep -q ":6099" && check "WebUI 6099 监听" OK || check "WebUI 6099 监听" NOT_LISTENING

$AMUX proot-distro login napcat -- node -e "JSON.parse(require('fs').readFileSync('/root/autoreply-rules.json','utf8'))" >/dev/null 2>&1 \
    && check "规则文件 JSON 合法" OK || check "规则文件 JSON 合法" INVALID

LOGIN=$($AMUX proot-distro login napcat -- node -e "
const WebSocket=require('/root/node_modules/ws');
const w=new WebSocket('ws://127.0.0.1:3001');
w.on('message',d=>{const e=JSON.parse(d);
  if(e.meta_event_type==='lifecycle'){w.send(JSON.stringify({action:'get_login_info',params:{},echo:'1'}))}
  if(e.echo==='1'){console.log(e.data&&e.data.user_id?('ONLINE '+e.data.user_id):'OFFLINE');process.exit(0)}
});
setTimeout(()=>{console.log('TIMEOUT');process.exit(0)},7000);" 2>/dev/null | tail -1)
case "$LOGIN" in
  ONLINE*) check "OneBot 账号在线" OK;;
  *) check "OneBot 账号在线" "$LOGIN";;
esac

echo "SUMMARY: PASS=$PASS FAIL=$FAIL"
[ "$FAIL" -eq 0 ]

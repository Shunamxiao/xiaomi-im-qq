#!/system/bin/sh
# mi8: NapCat + 自动回复机器人 后台拉起（root 运行；MIUI 不会掐 root 进程）
P=/data/data/com.termux/files/usr/bin/proot-distro
MP=/data/adb/magisk/magiskpolicy
SD=/data/data/com.termux/files/usr/var/lib/proot-distro/containers/napcat/shm
RF=/data/data/com.termux/files/usr/var/lib/proot-distro/containers/napcat/rootfs
$MP --live "allow magisk app_data_file file { create open read write getattr setattr lock map execute execute_no_trans unlink append }" >/dev/null 2>&1
$MP --live "allow magisk app_data_file dir { create add_name remove_name open read write getattr search rmdir reparent }" >/dev/null 2>&1
[ -d "$SD" ] && chmod 1777 "$SD"

if ! pgrep -f "[o]pt/QQ/qq" >/dev/null 2>&1; then
    TERM=xterm setsid nohup "$P" login napcat -- bash -c 'Xvfb :0 -screen 0 1280x720x24 >/root/xvfb.out 2>&1 & sleep 2; DISPLAY=:0 LD_PRELOAD=/usr/lib/fakehost.so /root/Napcat/opt/QQ/qq --no-sandbox --disable-dev-shm-usage -q 1457278554 >/root/napcat.out 2>&1' >/dev/null 2>&1 </dev/null &
    sleep 8
fi

if ! pgrep -f "[a]utoreply.js" >/dev/null 2>&1; then
    setsid nohup "$P" login napcat -- node /root/autoreply.js >"$RF/root/autoreply.log" 2>&1 </dev/null &
fi

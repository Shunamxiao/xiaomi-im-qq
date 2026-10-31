#!/system/bin/sh
# mi8: NapCat 后台拉起（root 运行；MIUI 不会掐 root 进程）
# 自动回复由 NapCat 官方插件 napcat-plugin-autoreply 承载，不再外挂 autoreply.js
P=/data/data/com.termux/files/usr/bin/proot-distro
MP=/data/adb/magisk/magiskpolicy
SD=/data/data/com.termux/files/usr/var/lib/proot-distro/containers/napcat/shm
RF=/data/data/com.termux/files/usr/var/lib/proot-distro/containers/napcat/rootfs
STAGE=/data/data/com.termux/files/home
# 部署同步：暂存区(termux home) → 容器，保证每次启动用的是最新配置
[ -f "$STAGE/onebot11-config.json" ] && cp "$STAGE/onebot11-config.json" "$RF/root/Napcat/opt/QQ/resources/app/app_launcher/napcat/config/onebot11_1457278554.json"
$MP --live "allow magisk app_data_file file { create open read write getattr setattr lock map execute execute_no_trans unlink append }" >/dev/null 2>&1
$MP --live "allow magisk app_data_file dir { create add_name remove_name open read write getattr search rmdir reparent }" >/dev/null 2>&1
[ -d "$SD" ] && chmod 1777 "$SD"

if ! pgrep -f "[o]pt/QQ/qq" >/dev/null 2>&1; then
    TERM=xterm setsid nohup "$P" login napcat -- bash -c 'Xvfb :0 -screen 0 1280x720x24 >/root/xvfb.out 2>&1 & sleep 2; DISPLAY=:0 LD_PRELOAD=/usr/lib/fakehost.so /root/Napcat/opt/QQ/qq --no-sandbox --disable-dev-shm-usage >/root/napcat.out 2>&1' >/dev/null 2>&1 </dev/null &
    sleep 8
fi

# 插件目录与 plugins.json 由插件部署流程维护，启动脚本无需处理
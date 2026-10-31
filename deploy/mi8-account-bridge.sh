#!/system/bin/sh
# mi8-account-bridge: 多账号实例的宿主侧启动通道（root 常驻）
# 容器内 account-manager 写入 $CMD/<name>.start 哨兵文件；
# 本脚本轮询并以独立常驻 proot 会话拉起对应实例脚本（容器内无法自行常驻拉起 proot 会话）
STAGE=/data/data/com.termux/files/home
CMD=$STAGE/qq-accounts/cmd
RF=/data/data/com.termux/files/usr/var/lib/proot-distro/containers/napcat/rootfs
export PATH=/data/data/com.termux/files/usr/bin:$PATH
mkdir -p "$CMD"
while true; do
    for f in "$CMD"/*.start; do
        [ -e "$f" ] || continue
        name=$(basename "$f" .start)
        rm -f "$f"
        script="$RF/root/qq-accounts/$name/instance.sh"
        if [ -f "$script" ]; then
            TERM=xterm setsid nohup proot-distro login napcat -- bash "$script" >/dev/null 2>&1 </dev/null &
        fi
    done
    sleep 2
done

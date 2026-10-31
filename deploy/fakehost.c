// 容器内主机名伪装：让 QQ 上报的设备名与电脑一致
#define _GNU_SOURCE
#include <string.h>
#include <sys/syscall.h>
#include <sys/utsname.h>
#include <unistd.h>

int gethostname(char *name, size_t len) {
    strncpy(name, "ZAIDU-5466", len - 1);
    name[len - 1] = '\0';
    return 0;
}

int uname(struct utsname *u) {
    int r = (int)syscall(SYS_uname, u);
    if (r == 0) {
        strncpy(u->nodename, "ZAIDU-5466", sizeof(u->nodename) - 1);
    }
    return r;
}

// proot 下 ptrace 可用性探针：子进程 TRACEME + 父进程接管验证
#include <stdio.h>
#include <signal.h>
#include <sys/ptrace.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>

int main(void) {
    pid_t child = fork();
    if (child == 0) {
        if (ptrace(PTRACE_TRACEME, 0, 0, 0) != 0) {
            perror("TRACEME_FAILED");
            _exit(126);
        }
        execl("/bin/echo", "echo", "child-exec-ok", (char *)NULL);
        _exit(127);
    }
    int status = 0;
    waitpid(child, &status, 0);
    if (WIFSTOPPED(status)) {
        printf("PTRACE_STOP_OK sig=%d\n", WSTOPSIG(status));
        kill(child, SIGCONT);
        waitpid(child, &status, 0);
        printf(WIFEXITED(status) ? "PTRACE_FULL_OK\n" : "PTRACE_TAIL_FAIL\n");
    } else if (WIFEXITED(status)) {
        printf("PTRACE_TRACEME_FAILED (exited directly)\n");
    } else {
        printf("PTRACE_UNKNOWN_STATE\n");
    }
    return 0;
}

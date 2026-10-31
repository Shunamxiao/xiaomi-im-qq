#!/bin/bash
set -e
gcc -o /tmp/ptrace-probe /data/data/com.termux/files/home/ptrace-probe.c
echo COMPILED
/tmp/ptrace-probe

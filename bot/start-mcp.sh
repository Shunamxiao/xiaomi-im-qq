#!/data/data/com.termux/files/usr/bin/bash
# QQ MCP Server 启动脚本（Termux 环境，不在 proot 容器里）
set -e

BOT_DIR="$HOME/qq-mcp"
cd "$BOT_DIR"

# 安装依赖（幂等）
if [ ! -d ".deps-ok" ]; then
    pip install -r requirements-mcp.txt 2>&1 | tail -1
    touch .deps-ok
fi

# 启动
exec python qq-mcp-server.py

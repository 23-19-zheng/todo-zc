#!/bin/bash

# ========================================
# Todo应用 本地构建脚本
# 用于在没有Maven环境的服务器上使用
# ========================================

set -e

GREEN='\033[0;32m'
NC='\033[0m'

log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

echo "========================================"
echo "   Todo应用 本地构建脚本"
echo "========================================"

# 检查Maven
if ! command -v mvn &> /dev/null; then
    echo "[ERROR] Maven未安装，请先安装Maven"
    exit 1
fi

# 检查Java
if ! command -v java &> /dev/null; then
    echo "[ERROR] Java未安装，请先安装JDK 8"
    exit 1
fi

log_info "开始构建项目..."
mvn clean package -Pprd -DskipTests -B

log_info "构建完成！"
log_info "JAR包位置: todo-app/target/todo-app.jar"
echo ""
echo "将以下文件上传到服务器："
echo "  - Dockerfile"
echo "  - docker-compose.yml"
echo "  - .env.example -> 重命名为 .env"
echo "  - init-sql/"
echo "  - todo-app/target/todo-app.jar"
echo ""
echo "在服务器上运行: docker compose up -d"
#!/bin/bash

# ========================================
# Todo应用 一键部署脚本
# ========================================

set -e

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 日志函数
log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查Docker是否安装
check_docker() {
    if ! command -v docker &> /dev/null; then
        log_error "Docker未安装，请先安装Docker"
        exit 1
    fi

    if ! command -v docker &> /dev/null || ! docker compose version &> /dev/null; then
        log_error "Docker Compose未安装，请先安装"
        exit 1
    fi

    log_info "Docker环境检查通过"
}

# 检查.env文件
check_env() {
    if [ ! -f ".env" ]; then
        log_warn ".env文件不存在，从.env.example复制..."
        cp .env.example .env
        log_warn "请编辑.env文件修改密码配置！"
        read -p "按Enter继续..."
    fi
    log_info "环境配置检查通过"
}

# 停止旧服务
stop_services() {
    log_info "停止现有服务..."
    docker compose down 2>/dev/null || true
}

# 构建镜像
build_image() {
    log_info "开始构建镜像（首次构建可能需要较长时间）..."
    docker compose build --no-cache todo-app
    log_info "镜像构建完成"
}

# 启动服务
start_services() {
    log_info "启动服务..."
    docker compose up -d

    log_info "等待服务启动..."
    sleep 10

    # 等待MySQL健康检查通过
    log_info "等待MySQL初始化完成..."
    for i in {1..30}; do
        if docker compose exec -T mysql mysqladmin ping -h localhost -u root -p"${MYSQL_ROOT_PASSWORD:-TodoRoot@2024}" &>/dev/null; then
            log_info "MySQL已就绪"
            break
        fi
        echo -n "."
        sleep 2
    done
    echo ""

    # 等待应用启动
    log_info "等待应用启动..."
    for i in {1..60}; do
        if curl -sf http://localhost:8080/actuator/health &>/dev/null; then
            log_info "应用已就绪"
            break
        fi
        echo -n "."
        sleep 2
    done
    echo ""
}

# 显示状态
show_status() {
    echo ""
    log_info "===== 服务状态 ====="
    docker compose ps

    echo ""
    log_info "===== 访问信息 ====="
    PUBLIC_IP=$(curl -sf ifconfig.me 2>/dev/null || echo "服务器IP")
    echo "应用地址: http://${PUBLIC_IP}:${APP_PORT:-8080}"
    echo ""
    echo "API文档: http://${PUBLIC_IP}:${APP_PORT:-8080}/doc.html"
    echo ""
    log_info "===== 常用命令 ====="
    echo "查看日志: docker compose logs -f todo-app"
    echo "重启应用: docker compose restart todo-app"
    echo "停止服务: docker compose down"
    echo ""
}

# 主流程
main() {
    echo "========================================"
    echo "   Todo应用 Docker部署脚本"
    echo "========================================"
    echo ""

    check_docker
    check_env
    stop_services
    build_image
    start_services
    show_status

    log_info "部署完成！"
}

main "$@"
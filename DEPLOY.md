# Todo应用 Docker部署指南

> 本文档面向小白用户，提供详细的云服务器Docker部署步骤。

---

## 目录

1. [环境准备](#一环境准备)
2. [服务器配置](#二服务器配置)
3. [项目上传](#三项目上传)
4. [部署应用](#四部署应用)
5. [验证测试](#五验证测试)
6. [日常运维](#六日常运维)
7. [常见问题](#七常见问题)

---

## 一、环境准备

### 1.1 服务器要求

| 配置项 | 最低要求 | 推荐配置 |
|--------|----------|----------|
| CPU | 1核 | 2核+ |
| 内存 | 2GB | 4GB+ |
| 磁盘 | 20GB | 50GB+ |
| 系统 | Ubuntu 20.04/22.04 | CentOS 7+/Debian 10+ |

### 1.2 安装Docker

**Ubuntu/Debian系统：**

```bash
# 1. 更新软件包
sudo apt update && sudo apt upgrade -y

# 2. 安装Docker（官方脚本，最简单）
curl -fsSL https://get.docker.com | bash

# 3. 安装Docker Compose插件
sudo apt install docker-compose-plugin -y

# 4. 启动Docker并设置开机自启
sudo systemctl start docker
sudo systemctl enable docker

# 5. 将当前用户加入docker组（免sudo执行docker）
sudo usermod -aG docker $USER

# 6. 重新登录使权限生效
exit
# 重新SSH登录服务器

# 7. 验证安装
docker --version
docker compose version
```

**CentOS系统：**

```bash
# 1. 安装必要工具
sudo yum install -y yum-utils

# 2. 添加Docker源
sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo

# 3. 安装Docker
sudo yum install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin

# 4. 启动并设置开机自启
sudo systemctl start docker
sudo systemctl enable docker

# 5. 将当前用户加入docker组
sudo usermod -aG docker $USER

# 6. 重新登录后验证
docker --version
```

---

## 二、服务器配置

### 2.1 创建项目目录

```bash
# 创建项目目录
sudo mkdir -p /opt/todo
sudo chown $USER:$USER /opt/todo
```

### 2.2 防火墙配置

**Ubuntu (ufw)：**

```bash
# 开放必要端口
sudo ufw allow 22      # SSH
sudo ufw allow 8080    # 应用端口

# 启用防火墙
sudo ufw enable

# 查看状态
sudo ufw status
```

**CentOS (firewalld)：**

```bash
# 开放端口
sudo firewall-cmd --permanent --add-port=22/tcp
sudo firewall-cmd --permanent --add-port=8080/tcp

# 重载配置
sudo firewall-cmd --reload

# 查看开放的端口
sudo firewall-cmd --list-ports
```

### 2.3 云服务商安全组配置

在云服务器控制台的**安全组**中添加规则：

| 方向 | 端口 | 协议 | 来源 | 说明 |
|------|------|------|------|------|
| 入站 | 22 | TCP | 你的IP | SSH管理 |
| 入站 | 8080 | TCP | 0.0.0.0/0 | 应用访问 |

> ⚠️ **安全提醒**：不要开放3306(MySQL)和6379(Redis)端口，这些仅供容器内部访问。

---

## 三、项目上传

### 方式一：Git克隆（推荐）

```bash
cd /opt
git clone <你的仓库地址> todo
cd todo
```

### 方式二：SCP上传

**1. 本地打包项目**

在本地电脑（Windows PowerShell）执行：

```powershell
# 进入项目目录
cd D:\workSpace\todo

# 打包成tar.gz（需要Git Bash或WSL）
tar -czvf todo-project.tar.gz `
  Dockerfile `
  docker-compose.yml `
  .env.example `
  deploy.sh `
  init-sql `
  pom.xml `
  todo-app `
  todo-common `
  todo-domain `
  todo-facade `
  todo-infrastructure
```

**2. 上传到服务器**

```powershell
scp todo-project.tar.gz root@你的服务器IP:/opt/
```

**3. 服务器解压**

```bash
cd /opt
tar -xzvf todo-project.tar.gz -C todo
cd todo
```

### 方式三：本地构建后上传（适合国内服务器）

**1. 本地构建JAR包**

```bash
# Windows下需要安装Maven和JDK 8
mvn clean package -Pprd -DskipTests
```

**2. 创建简化的Dockerfile（不使用多阶段构建）**

创建 `Dockerfile.simple`：

```dockerfile
FROM openjdk:8-jre-slim
WORKDIR /app
COPY todo-app/target/todo-app.jar app.jar
ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms256m -Xmx512m"
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
```

**3. 上传必要文件**

只需上传：
- `Dockerfile.simple` → 重命名为 `Dockerfile`
- `docker-compose.yml`
- `.env.example` → 重命名为 `.env`
- `init-sql/` 目录
- `todo-app/target/todo-app.jar`

---

## 四、部署应用

### 4.1 配置环境变量

```bash
cd /opt/todo

# 复制环境变量模板
cp .env.example .env

# 编辑配置（重要！修改密码）
nano .env
```

修改以下配置（示例）：

```bash
# MySQL配置 - 请修改密码！
MYSQL_ROOT_PASSWORD=YourSecureRootPassword123!
DB_USERNAME=todo_user
DB_PASSWORD=YourSecureUserPassword123!

# Redis配置 - 请修改密码！
REDIS_PASSWORD=YourSecureRedisPassword123!

# JWT密钥 - 请修改为随机字符串！
JWT_SECRET=your-random-jwt-secret-key-at-least-256-bits-long-change-this

# JVM参数
JAVA_OPTS=-Xms256m -Xmx512m -XX:+UseG1GC
```

> 🔐 **安全提示**：所有密码必须修改为复杂密码，JWT密钥建议使用随机生成器生成。

### 4.2 一键部署

```bash
# 添加执行权限
chmod +x deploy.sh

# 执行部署
./deploy.sh
```

### 4.3 手动部署（分步执行）

如果一键脚本有问题，可以手动执行：

```bash
# 1. 构建镜像
docker compose build

# 2. 启动服务
docker compose up -d

# 3. 查看状态
docker compose ps

# 4. 查看日志
docker compose logs -f todo-app
```

---

## 五、验证测试

### 5.1 检查服务状态

```bash
# 查看容器状态
docker compose ps

# 期望输出：所有容器状态为 "healthy" 或 "running"
```

### 5.2 检查应用日志

```bash
# 查看应用日志
docker compose logs -f todo-app

# 看到 "Started TodoApplication" 表示启动成功
```

### 5.3 接口测试

```bash
# 健康检查
curl http://localhost:8080/actuator/health

# 注册接口测试
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"Test123456"}'

# 登录接口测试
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"Test123456"}'
```

### 5.4 浏览器访问

```
http://你的服务器IP:8080/doc.html
```

看到Swagger文档页面表示部署成功。

---

## 六、日常运维

### 6.1 常用命令

```bash
# 查看服务状态
docker compose ps

# 查看日志
docker compose logs -f todo-app        # 应用日志
docker compose logs -f mysql           # MySQL日志
docker compose logs -f redis           # Redis日志

# 重启服务
docker compose restart todo-app        # 重启应用
docker compose restart                 # 重启所有服务

# 停止服务
docker compose stop                    # 停止所有服务
docker compose down                    # 停止并删除容器（保留数据）

# 启动服务
docker compose start                   # 启动已停止的服务
docker compose up -d                   # 启动所有服务

# 进入容器
docker exec -it todo-app /bin/bash     # 进入应用容器
docker exec -it todo-mysql /bin/bash   # 进入MySQL容器
```

### 6.2 数据库操作

```bash
# 连接MySQL
docker exec -it todo-mysql mysql -u root -p

# 在MySQL中执行
mysql> USE todo_db;
mysql> SHOW TABLES;
mysql> SELECT * FROM t_user;
```

### 6.3 更新应用

```bash
# 1. 拉取最新代码
git pull

# 2. 重新构建并部署
docker compose build --no-cache todo-app
docker compose up -d todo-app
```

### 6.4 备份数据

```bash
# 备份MySQL
docker exec todo-mysql mysqldump -u root -p${MYSQL_ROOT_PASSWORD} todo_db > backup_$(date +%Y%m%d).sql

# 备份Redis
docker exec todo-redis redis-cli -a ${REDIS_PASSWORD} BGSAVE
docker cp todo-redis:/data/dump.rdb ./redis_backup_$(date +%Y%m%d).rdb
```

### 6.5 恢复数据

```bash
# 恢复MySQL
docker exec -i todo-mysql mysql -u root -p${MYSQL_ROOT_PASSWORD} todo_db < backup_20240101.sql
```

---

## 七、常见问题

### 7.1 MySQL连接失败

**现象**：日志显示 `Communications link failure`

**解决**：
```bash
# 检查MySQL是否就绪
docker compose logs mysql | grep "ready for connections"

# 如果MySQL未就绪，等待一段时间后重启应用
docker compose restart todo-app
```

### 7.2 内存不足

**现象**：容器频繁重启，日志显示内存相关错误

**解决**：
```bash
# 编辑.env文件，减小JVM内存
nano .env

# 修改为更小的值
JAVA_OPTS=-Xms128m -Xmx256m -XX:+UseG1GC

# 重启应用
docker compose restart todo-app
```

### 7.3 端口被占用

**现象**：启动失败，提示端口已被使用

**解决**：
```bash
# 查看端口占用
sudo lsof -i:8080

# 杀掉占用进程
sudo kill -9 <PID>

# 或修改.env中的端口
APP_PORT=8081
```

### 7.4 构建超时

**现象**：Maven下载依赖超时

**解决**：
使用国内镜像，创建 `settings.xml`：

```bash
mkdir -p ~/.m2
cat > ~/.m2/settings.xml << 'EOF'
<settings>
  <mirrors>
    <mirror>
      <id>aliyun</id>
      <url>https://maven.aliyun.com/repository/public</url>
      <mirrorOf>central</mirrorOf>
    </mirror>
  </mirrors>
</settings>
EOF
```

### 7.5 权限问题

**现象**：`permission denied`

**解决**：
```bash
# 确保用户在docker组
sudo usermod -aG docker $USER

# 重新登录
exit
# 重新SSH登录
```

---

## 附录：目录结构

```
/opt/todo/
├── Dockerfile              # Docker镜像构建文件
├── docker-compose.yml      # Docker编排配置
├── .env.example            # 环境变量模板
├── .env                    # 环境变量配置（需自行创建）
├── deploy.sh               # 一键部署脚本
├── build-local.sh          # 本地构建脚本
├── pom.xml                 # Maven父项目配置
├── init-sql/               # 数据库初始化脚本
│   └── 01-schema.sql
├── todo-app/               # 应用模块
├── todo-common/            # 公共模块
├── todo-domain/            # 领域模块
├── todo-facade/            # 接口层模块
└── todo-infrastructure/    # 基础设施模块
```

---

## 技术支持

如遇问题，请检查：
1. Docker服务是否正常运行：`systemctl status docker`
2. 容器日志：`docker compose logs`
3. 磁盘空间：`df -h`
4. 内存使用：`free -m`
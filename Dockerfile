# ========================================
# 多阶段构建 - 构建阶段
# ========================================
FROM maven:3.8.6-openjdk-8 AS builder

WORKDIR /build

# 先复制pom文件，利用Docker缓存加速依赖下载
COPY pom.xml .
COPY todo-common/pom.xml todo-common/
COPY todo-facade/pom.xml todo-facade/
COPY todo-infrastructure/pom.xml todo-infrastructure/
COPY todo-domain/pom.xml todo-domain/
COPY todo-app/pom.xml todo-app/

# 下载依赖
RUN mvn dependency:go-offline -B

# 复制源代码并构建
COPY . .
RUN mvn clean package -Pprd -DskipTests -B

# ========================================
# 运行阶段
# ========================================
FROM openjdk:8-jre-slim

LABEL maintainer="todo-team"
LABEL version="1.0.0"
LABEL description="Todo Application"

WORKDIR /app

# 复制构建产物
COPY --from=builder /build/todo-app/target/todo-app.jar app.jar

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# JVM参数（可通过环境变量覆盖）
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError"

# 暴露端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# 启动命令
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
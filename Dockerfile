# 使用官方OpenJDK运行时作为基础镜像
FROM openjdk:21-jdk-slim

# 设置工作目录
WORKDIR /app

# 复制JAR文件到容器中
COPY target/*.jar app.jar

# 暴露应用运行的端口
EXPOSE 8080

# 启动应用的命令
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
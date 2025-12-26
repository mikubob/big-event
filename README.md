# Big Event Showing 项目

![Deploy Spring Boot Application](https://github.com/mikubob/big-event/actions/workflows/deploy.yml/badge.svg)
![Java CI with Maven](https://github.com/mikubob/big-event/actions/workflows/maven.yml/badge.svg)

## 项目说明
这是一个Spring Boot项目，实现了大事件展示平台的后端API功能。

## 部署说明
项目已配置GitHub Actions进行CI/CD，支持自动构建和部署。

### 部署到生产环境
1. 项目会在main分支有更新时自动构建
2. 构建成功后会触发部署流程
3. 部署状态会显示在GitHub的Deployment标签页中

### 环境变量配置
生产环境需要配置以下环境变量：
- DB_URL: 数据库连接URL
- DB_USERNAME: 数据库用户名
- DB_PASSWORD: 数据库密码
- REDIS_HOST: Redis主机地址
- REDIS_PORT: Redis端口
- REDIS_PASSWORD: Redis密码
- OSS_ENDPOINT: 阿里云OSS端点
- OSS_BUCKET_NAME: 阿里云OSS存储桶名称
- OSS_REGION: 阿里云OSS区域

### 本地构建
```bash
mvn clean package -DskipTests
```

### Docker部署
```bash
# 构建镜像
docker build -t big-event-showing .

# 运行容器
docker run -p 8080:8080 big-event-showing
```
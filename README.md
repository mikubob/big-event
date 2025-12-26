# 大事件 - 前后端分离内容管理系统

![Deploy Spring Boot Application](https://github.com/mikubob/big-event/actions/workflows/deploy.yml/badge.svg)
![Java CI with Maven](https://github.com/mikubob/big-event/actions/workflows/maven.yml/badge.svg)

## 项目概述

**大事件**是一个现代化的前后端分离内容管理系统，采用Spring Boot + Vue3 + Element Plus技术栈构建。项目提供了完整的用户管理、文章管理和分类管理功能，适用于个人或团队的内容发布和管理需求。

## 项目特点

- **前后端分离架构**：后端采用Spring Boot 3.2.10 + MyBatis + MySQL，前端采用Vue3 + Vite + Element Plus
- **功能完善**：包含用户注册/登录、个人信息管理、文章发布/管理、分类管理等核心功能
- **技术先进**：集成Redis缓存、阿里云OSS文件存储、JWT身份认证、分页查询等主流技术
- **开发规范**：使用Spring Validation参数校验、统一响应结果封装、全局异常处理等最佳实践
- **文档完善**：集成Knife4j接口文档，便于前后端协作开发

## 功能模块

### 用户模块
- 用户注册：支持用户名密码注册，包含格式校验
- 用户登录：基于JWT的无状态认证，Redis存储token
- 个人信息：查看和修改用户基本信息
- 头像上传：集成阿里云OSS实现头像上传功能
- 密码修改：支持原密码校验和新密码确认

### 文章模块
- 文章发布：支持文章标题、内容、封面、分类、状态（发布/草稿）等信息管理
- 文章管理：分页查询文章列表，支持按分类和状态筛选
- 文章编辑：在线修改文章内容
- 文章删除：支持单个删除和批量删除功能
- 文章详情：查看文章完整内容

### 分类模块
- 分类管理：创建、修改、删除文章分类
- 分类查询：获取所有分类列表供文章选择

## 技术栈

### 后端技术
- **核心框架**：Spring Boot 3.2.10 + JDK 21
- **数据持久化**：MyBatis + MySQL 9.3.0
- **缓存技术**：Spring Cache + Redis
- **文件存储**：阿里云OSS
- **安全认证**：JWT + MD5密码加密
- **接口文档**：Knife4j
- **分页插件**：PageHelper
- **数据校验**：Spring Validation

### 前端技术
- **核心框架**：Vue 3 + Vite
- **UI组件库**：Element Plus
- **状态管理**：Pinia
- **路由管理**：Vue Router
- **HTTP请求**：Axios
- **富文本编辑器**：Vue Quill

## 系统架构

```
前端(Vue3 + Element Plus) <--> API接口(Spring Boot) <--> 数据库(MySQL)
                                    |
                                缓存(Redis)
                                    |
                                文件存储(阿里云OSS)
```

## 快速开始

### 环境要求
- JDK 21
- Maven 3.6+
- Node.js 16+
- MySQL 8.0+
- Redis

### 本地部署

#### 1. 后端服务启动
```bash
# 克隆项目
git clone https://github.com/mikubob/big-event.git

# 配置数据库
# 修改 src/main/resources/application.yml 中的数据库连接信息

# 初始化数据库表结构
# 在MySQL中执行相关建表SQL

# 启动后端服务
cd Big-Event-Showing
mvn clean package -DskipTests
java -jar target/Big-Event-Showing-1.0-SNAPSHOT.jar
```

#### 2. 前端服务启动
```bash
# 进入前端目录
cd big-event

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

#### 3. 访问应用
- 前端访问地址：[http://localhost:5173](http://localhost:5173)
- 后端API文档：[http://localhost:8080/doc.html](http://localhost:8080/doc.html)

### 部署说明

项目已配置GitHub Actions进行CI/CD，支持自动构建和部署。

#### 环境变量配置
生产环境需要配置以下环境变量：
- `DB_URL`: 数据库连接URL
- `DB_USERNAME`: 数据库用户名
- `DB_PASSWORD`: 数据库密码
- `REDIS_HOST`: Redis主机地址
- `REDIS_PORT`: Redis端口
- `REDIS_PASSWORD`: Redis密码
- `OSS_ENDPOINT`: 阿里云OSS端点
- `OSS_BUCKET_NAME`: 阿里云OSS存储桶名称
- `OSS_REGION`: 阿里云OSS区域

### Docker部署
```bash
# 构建镜像
docker build -t big-event-showing .

# 运行容器
docker run -p 8080:8080 big-event-showing
```

## API接口

项目集成了Knife4j接口文档，所有API接口都有详细说明：
- 用户相关接口：/user/*
- 文章相关接口：/article/*
- 分类相关接口：/category/*
- 文件上传接口：/upload/*

## 项目结构

```
Big-Event-Showing/
├── src/main/java/com/itheima/          # 后端源码
│   ├── controller/                     # 控制器层
│   ├── service/                        # 业务逻辑层
│   ├── mapper/                         # 数据访问层
│   ├── pojo/                           # 实体类
│   ├── utils/                          # 工具类
│   └── config/                         # 配置类
├── big-event/                          # 前端源码
│   ├── src/
│   │   ├── api/                        # API接口定义
│   │   ├── views/                      # 页面组件
│   │   ├── stores/                     # Pinia状态管理
│   │   ├── router/                     # 路由配置
│   │   └── utils/                      # 工具函数
├── src/main/resources/                 # 配置文件
└── pom.xml                             # Maven配置文件
```

## 特色功能

- **分页查询**：文章列表支持分页展示，提升大数据量下的用户体验
- **缓存优化**：使用Spring Cache对分类列表等常用数据进行缓存，提升查询性能
- **文件上传**：集成阿里云OSS实现头像等文件的安全存储
- **数据校验**：前后端双重数据校验，保证数据的准确性和安全性
- **响应式设计**：前端界面适配不同屏幕尺寸，提供良好的用户体验

## 应用场景

- 个人博客系统
- 企业内容管理
- 团队知识分享
- 新闻资讯发布
- 产品展示平台

## 贡献者

- mikubob

## 许可证

本项目仅供学习交流使用。

## 联系方式

如需技术支持或有相关问题，请联系：[项目邮箱或联系方式]
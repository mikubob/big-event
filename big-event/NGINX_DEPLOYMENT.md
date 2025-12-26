# Nginx部署Vue项目指南

## 项目构建

首先，确保项目已成功构建：

```bash
npm run build
```

构建成功后会在项目根目录生成 [dist](file:///d:/CodingData/Springboot3+Vue/04_%E4%BB%A3%E7%A0%81/%E5%89%8D%E7%AB%AF%E4%BB%A3%E7%A0%81/big-event/big-event/dist/) 目录，包含所有静态资源文件。

## Nginx配置

将以下配置添加到Nginx配置文件中（通常在 `/etc/nginx/sites-available/default` 或 `/etc/nginx/conf.d/default.conf`）：

```nginx
# Vue.js前端项目Nginx配置示例

server {
    listen       80;
    server_name  localhost;

    # 设置根目录为构建后的文件夹
    root /usr/share/nginx/html/big-event;
    index index.html index.htm;

    # 处理Vue Router的History模式
    location / {
        try_files $uri $uri/ /index.html;
    }

    # 静态资源缓存设置
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }

    # API请求代理到后端服务
    location /api {
        proxy_pass http://localhost:8080;  # 后端服务地址
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_cache_bypass $http_upgrade;
    }

    # 错误页面配置
    error_page   500 502 503 504  /50x.html;
    location = /50x.html {
        root   /usr/share/nginx/html;
    }
}
```

## 部署步骤

1. 将 [dist](file:///d:/CodingData/Springboot3+Vue/04_%E4%BB%A3%E7%A0%81/%E5%89%8D%E7%AB%AF%E4%BB%A3%E7%A0%81/big-event/big-event/dist/) 目录中的所有文件上传到Nginx服务器的 `/usr/share/nginx/html/big-event/` 目录

2. 检查Nginx配置是否正确：
   ```bash
   sudo nginx -t
   ```

3. 重新加载Nginx配置：
   ```bash
   sudo nginx -s reload
   ```

4. 如果Nginx未运行，启动Nginx：
   ```bash
   sudo systemctl start nginx
   ```

## 验证部署

打开浏览器，访问 `http://<your_server_ip>`，应该能看到Vue应用正常显示。

## 注意事项

- 确保防火墙设置允许HTTP（端口80）和HTTPS（端口443）流量
- 如果需要HTTPS，可以配置SSL证书
- 生产环境中，建议配置更安全的错误页面和安全头
- API代理配置确保前端可以访问后端服务
#!/bin/bash

# Vue项目部署脚本
# 用于将构建的前端文件部署到Nginx目录

# 设置Nginx根目录（根据实际情况修改）
NGINX_ROOT="/usr/share/nginx/html/big-event"

echo "开始部署Vue项目到Nginx..."

# 检查dist目录是否存在
if [ ! -d "dist" ]; then
    echo "错误: dist目录不存在，请先运行 'npm run build'"
    exit 1
fi

# 创建Nginx目录（如果不存在）
sudo mkdir -p $NGINX_ROOT

# 复制构建文件到Nginx目录
sudo cp -r dist/* $NGINX_ROOT/

echo "项目已成功部署到 $NGINX_ROOT"
echo "请确保Nginx正在运行:"
echo "  sudo systemctl start nginx"
echo "  或"
echo "  sudo service nginx start"
echo ""
echo "如需重新加载配置:"
echo "  sudo nginx -s reload"
echo ""
echo "部署完成！访问 http://your-server-ip 查看应用"
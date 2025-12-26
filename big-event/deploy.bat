@echo off
REM Vue项目部署脚本（Windows版）
REM 用于将构建的前端文件部署到Nginx目录

REM 设置Nginx根目录（根据实际情况修改）
set NGINX_ROOT=C:\nginx\html\big-event

echo 开始部署Vue项目到Nginx...

REM 检查dist目录是否存在
if not exist "dist" (
    echo 错误: dist目录不存在，请先运行 'npm run build'
    exit /b 1
)

REM 创建Nginx目录（如果不存在）
if not exist "%NGINX_ROOT%" mkdir "%NGINX_ROOT%"

REM 复制构建文件到Nginx目录
xcopy /E /I /Y "dist" "%NGINX_ROOT%"

echo.
echo 项目已成功部署到 %NGINX_ROOT%
echo.
echo 请确保Nginx正在运行:
echo   启动Nginx: start nginx 或双击 nginx.exe
echo   重新加载配置: nginx -s reload
echo.
echo 部署完成！访问 http://localhost 查看应用
pause
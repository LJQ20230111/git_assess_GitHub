# 后端 Docker 部署

将 Spring Boot 后端以容器方式运行在 **9019** 端口。镜像内使用 `backend-1.0.0.jar`（可通过 `build.ps1` / `build.sh` 从 `backend/` 重新打包）。

## 目录说明

| 文件 | 说明 |
|------|------|
| `backend-1.0.0.jar` | 可执行 JAR（需与 `backend/pom.xml` 版本一致） |
| `Dockerfile` | 运行时镜像（JRE 17 + Alpine） |
| `docker-compose.yml` | 一键启动，映射 `9019:9019` |
| `.env.example` | 部署用环境变量模板 |
| `build.ps1` / `build.sh` | 在 `backend` 执行 `mvn package` 并复制 JAR |

## 服务器部署步骤

### 1. 准备 JAR（本地或 CI）

```powershell
cd backend_docker
.\build.ps1
```

Linux：

```bash
cd backend_docker
chmod +x build.sh
./build.sh
```

### 2. 配置环境变量

```bash
cd backend_docker
cp .env.example .env
# 编辑 .env，填写 DB_*、CORS_ALLOWED_ORIGIN_PATTERNS 等
```

`SERVER_PORT` 保持 `9019`。容器内 `EnvLoader` 需要存在 `/app/.env`；`docker-compose` 会把宿主机 `.env` 挂载进去，同时 `env_file` 会把变量注入进程环境（优先级高于 `.env` 文件中的同名项）。

### 3. 构建并启动

```bash
cd backend_docker
docker compose up -d --build
```

### 4. 验证

```bash
curl http://<服务器IP>:9019/api/health
```

期望返回 `status: ok`，且 `database: connected`。

## 常用命令

```bash
docker compose logs -f assess-backend
docker compose restart assess-backend
docker compose down
```

仅构建镜像（不启动）：

```bash
docker build -t assess-backend:1.0.0 .
```

## 仅使用 Docker（不用 Compose）

```bash
docker build -t assess-backend:1.0.0 .
docker run -d --name assess-backend \
  -p 9019:9019 \
  --env-file .env \
  -e SERVER_PORT=9019 \
  -v "$(pwd)/.env:/app/.env:ro" \
  --restart unless-stopped \
  assess-backend:1.0.0
```

## 更新版本

1. 修改代码后执行 `build.ps1` / `build.sh` 更新 JAR  
2. `docker compose up -d --build` 重新构建并滚动替换容器  

## 防火墙

确保服务器安全组 / 防火墙放行 **TCP 9019**。

## 与前端联调

生产前端需将 API 基地址指向 `http(s)://<后端域名或IP>:9019`，并在 `.env` 中配置 `CORS_ALLOWED_ORIGIN_PATTERNS` 包含前端实际来源。

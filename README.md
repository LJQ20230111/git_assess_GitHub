# 系统运行能力考核验证

最小可运行前后端分离框架：Vue3 + Vite + TypeScript ↔ Java 17 + Spring Boot ↔ MySQL（`lab_assess` 表）。

## 项目结构

```
git_assess_GitHub/
├── .env               # 本地配置（从 .env.example 复制，勿提交）
├── .env.example       # 环境变量模板
├── frontend/          # Vue3 + Vite + TypeScript
├── backend/           # Spring Boot + Maven Wrapper
├── sql/               # 数据库脚本
└── README.md
```

## 环境要求

| 组件 | 要求 |
|------|------|
| 前端 | Node.js 18+、npm |
| 后端 | JDK 17+（需配置 `JAVA_HOME`） |
| 数据库 | MySQL 5.7+，已执行 `sql/as_v0.1.1_260522.sql` 建表 |
| Maven | **不需要**（使用 `mvnw` / `mvnw.cmd`） |

## 环境变量（.env）

所有配置集中在**项目根目录** `.env`，代码与 `application.properties` 中不写死主机、库名、端口等。

```powershell
# 在项目根目录执行
copy .env.example .env
# 编辑 .env，至少填写 DB_HOST、DB_NAME、DB_USERNAME、DB_PASSWORD
```

| 变量 | 说明 |
|------|------|
| `SERVER_PORT` | 后端监听端口 |
| `FRONTEND_PORT` | 前端 dev 端口 |
| `BACKEND_HOST` / `BACKEND_PORT` | Vite 代理 `/api` 的目标 |
| `DB_HOST` / `DB_PORT` / `DB_NAME` | MySQL 连接 |
| `DB_USERNAME` / `DB_PASSWORD` | 数据库账号 |
| `DB_TIMEZONE` / `DB_JDBC_PARAMS` | JDBC 参数 |
| `JPA_SHOW_SQL` | 是否打印 SQL |
| `CORS_ALLOWED_ORIGIN_PATTERNS` | 逗号分隔的 CORS 来源 |
| `DOTENV_PATH` | 可选，`.env` 绝对路径 |

1. 在 MySQL 中执行 `sql/as_v0.1.1_260522.sql`（创建 `lab_assess` 表）。
2. 后端启动前由 `EnvLoader` 加载根目录 `.env`；前端 `vite.config.ts` 同样读取根目录 `.env`。

## 启动步骤

### 1. 配置 .env

见上一节。未配置 `.env` 时前后端启动会报错提示。

### 2. 启动后端

```powershell
cd backend

# Windows：确保 JAVA_HOME 指向 JDK 根目录，例如：
# $env:JAVA_HOME = "D:\Java 21"

mvnw.cmd spring-boot:run
```

Linux / macOS：

```bash
cd backend
export JAVA_HOME=/path/to/jdk-17
./mvnw spring-boot:run
```

启动成功后控制台出现：`Started AssessApplication`，端口为 `.env` 中的 `SERVER_PORT`。

### 3. 启动前端

新开一个终端：

```powershell
cd frontend
npm install
npm run dev
```

浏览器访问 `.env` 中 `FRONTEND_PORT` 对应地址（默认 5173）。

## 联调验证

### 方式一：页面验证（推荐）

1. 先启动后端，再启动前端
2. 打开 `http://localhost:5173`
3. 页面自动请求 `/api/health`
4. 显示绿色 **「联调成功」**，且 `database` 为 `connected` 表示数据库已连通
5. 填写「成员考核信息」并点击 **保存到数据库**，下方列表应出现新记录

### 方式二：直接调后端 API

```powershell
Invoke-RestMethod http://localhost:8080/api/health
```

期望返回：

```json
{
  "status": "ok",
  "message": "后端服务运行正常",
  "service": "assess-backend"
}
```

### 方式三：经 Vite 代理验证

```powershell
Invoke-RestMethod http://localhost:5173/api/health
```

前端 `vite.config.ts` 已将 `/api` 代理到 `http://localhost:8080`，开发时无 CORS 问题。后端 `CorsConfig` 也已放行 `localhost` 来源，支持直连后端调试。

## API 说明

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/health` | 健康检查（含数据库连通状态） |
| GET | `/api/assess` | 查询全部考核记录（按 ID 倒序） |
| POST | `/api/assess` | 新增考核记录，JSON 体字段与前端表单一致 |

## Maven Wrapper 说明

`backend` 已包含完整 Maven Wrapper，可在**未安装 Maven** 的机器上构建/运行：

- `mvnw` / `mvnw.cmd` — 启动脚本
- `.mvn/wrapper/maven-wrapper.properties` — Maven 版本与下载地址
- `.mvn/wrapper/maven-wrapper.jar` — Wrapper 引导 JAR（已提交，离线可用）

### 如何重新生成 Wrapper（可选）

在已安装 Maven 的机器上，于 `backend` 目录执行：

```bash
mvn -N wrapper:wrapper -Dtype=bin "-Dmaven=3.9.11"
```

然后下载 JAR 到仓库（若未自动生成）：

```powershell
Invoke-WebRequest `
  -Uri "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.4/maven-wrapper-3.3.4.jar" `
  -OutFile ".mvn/wrapper/maven-wrapper.jar"
```

首次运行 `mvnw` 会自动从 Maven Central 下载 Apache Maven 3.9.11 到用户目录 `~/.m2/wrapper/dists/`。

## 常见问题

**`JAVA_HOME not found`**

设置环境变量指向 JDK 17+ 安装目录（含 `bin/java.exe`），例如：

```powershell
$env:JAVA_HOME = "D:\Java 21"
```

**前端显示「联调失败」**

确认后端已启动且 `http://localhost:8080/api/health` 可访问。

**`database` 为 `disconnected` 或保存失败**

检查项目根目录 `.env` 中的 `DB_*` 配置，确认 MySQL 允许当前 IP 访问，且 `lab_assess` 表已创建。

**端口占用**

- 端口：修改项目根目录 `.env` 中的 `SERVER_PORT` / `FRONTEND_PORT`

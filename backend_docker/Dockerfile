FROM eclipse-temurin:17-jre-alpine

RUN apk add --no-cache curl

WORKDIR /app

ARG JAR_FILE=backend-1.0.0.jar
COPY ${JAR_FILE} app.jar

# EnvLoader 要求存在 .env 文件；实际值优先来自容器环境变量 / env_file，也可挂载覆盖
RUN touch .env

ENV SERVER_PORT=9019

EXPOSE 9019

HEALTHCHECK --interval=30s --timeout=5s --start-period=45s --retries=3 \
  CMD curl -fsS "http://127.0.0.1:9019/api/health" || exit 1

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

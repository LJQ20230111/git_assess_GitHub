#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
BACKEND="$ROOT/backend"
JAR="$BACKEND/target/backend-1.0.0.jar"

cd "$BACKEND"
./mvnw -q package -DskipTests

if [[ ! -f "$JAR" ]]; then
  echo "未找到 $JAR" >&2
  exit 1
fi

cp -f "$JAR" "$(dirname "$0")/backend-1.0.0.jar"
echo "已复制 -> backend_docker/backend-1.0.0.jar"

# 在 backend 目录打包后，将 JAR 复制到 backend_docker
$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
$backend = Join-Path $root "backend"
$jar = Join-Path $backend "target\backend-1.0.0.jar"

Push-Location $backend
try {
    & .\mvnw.cmd -q package -DskipTests
} finally {
    Pop-Location
}

if (-not (Test-Path $jar)) {
    throw "未找到 $jar，请先确认 mvn package 成功"
}

Copy-Item -Force $jar (Join-Path $PSScriptRoot "backend-1.0.0.jar")
Write-Host "已复制 -> backend_docker\backend-1.0.0.jar"

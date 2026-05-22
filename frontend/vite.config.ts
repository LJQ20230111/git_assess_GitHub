import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'
import { fileURLToPath } from 'url'

const __dirname = path.dirname(fileURLToPath(import.meta.url))
const envDir = path.resolve(__dirname, '..')

function requireEnv(env: Record<string, string>, key: string): string {
  const value = env[key]?.trim()
  if (!value) {
    throw new Error(`缺少环境变量 ${key}，请在项目根目录 .env 中配置（参考 .env.example）`)
  }
  return value
}

function resolveBackendTarget(env: Record<string, string>) {
  const target = (env.BACKEND_TARGET?.trim() || 'local').toLowerCase()
  if (target !== 'local' && target !== 'remote') {
    throw new Error(
      `BACKEND_TARGET 须为 local 或 remote，当前为 "${env.BACKEND_TARGET}"，请在项目根目录 .env 中修改`
    )
  }

  const hostKey = target === 'remote' ? 'BACKEND_REMOTE_HOST' : 'BACKEND_LOCAL_HOST'
  const portKey = target === 'remote' ? 'BACKEND_REMOTE_PORT' : 'BACKEND_LOCAL_PORT'

  return {
    target,
    host: requireEnv(env, hostKey),
    port: requireEnv(env, portKey),
  }
}

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, envDir, '')

  const { target, host: backendHost, port: backendPort } = resolveBackendTarget(env)
  const frontendPort = Number(requireEnv(env, 'FRONTEND_PORT'))
  const proxyTarget = `http://${backendHost}:${backendPort}`

  console.log(`[vite] BACKEND_TARGET=${target} -> ${proxyTarget}`)

  return {
    envDir,
    plugins: [vue()],
    server: {
      port: frontendPort,
      proxy: {
        '/api': {
          target: proxyTarget,
          changeOrigin: true,
        },
      },
    },
  }
})

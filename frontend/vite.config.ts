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

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, envDir, '')

  const backendHost = requireEnv(env, 'BACKEND_HOST')
  const backendPort = requireEnv(env, 'BACKEND_PORT')
  const frontendPort = Number(requireEnv(env, 'FRONTEND_PORT'))

  return {
    envDir,
    plugins: [vue()],
    server: {
      port: frontendPort,
      proxy: {
        '/api': {
          target: `http://${backendHost}:${backendPort}`,
          changeOrigin: true,
        },
      },
    },
  }
})

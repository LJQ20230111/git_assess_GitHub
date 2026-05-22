<script setup lang="ts">
import { ref } from 'vue'

interface ApiError {
  status: string
  message: string
}

interface AssessForm {
  name: string
  assessmentDirection: 0 | 1 | 2
  frontendResult: 0 | 1 | 2 | 3
  backendResult: 0 | 1 | 2 | 3
  dataManagementResult: 0 | 1 | 2 | 3
  assessmentTime: string
  assessor: string
}

const DIRECTION_OPTIONS = [
  { value: 0, label: '前端方向' },
  { value: 1, label: '后端方向' },
  { value: 2, label: '管理方向' },
] as const
const RESULT_VALUES = [0, 1, 2, 3] as const

function defaultAssessmentTime(): string {
  const d = new Date()
  const pad = (n: number) => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`
}

const assessForm = ref<AssessForm>({
  name: '',
  assessmentDirection: 0,
  frontendResult: 0,
  backendResult: 0,
  dataManagementResult: 0,
  assessmentTime: defaultAssessmentTime(),
  assessor: '',
})

const submitLoading = ref(false)
const assessError = ref('')
const assessSuccess = ref('')

async function submitAssess() {
  if (!assessForm.value.name.trim()) {
    assessError.value = '请填写姓名'
    assessSuccess.value = ''
    return
  }

  submitLoading.value = true
  assessError.value = ''
  assessSuccess.value = ''

  try {
    const res = await fetch('/api/assess', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        name: assessForm.value.name.trim(),
        assessmentDirection: assessForm.value.assessmentDirection,
        frontendResult: assessForm.value.frontendResult,
        backendResult: assessForm.value.backendResult,
        dataManagementResult: assessForm.value.dataManagementResult,
        assessmentTime: assessForm.value.assessmentTime,
        assessor: assessForm.value.assessor.trim() || null,
      }),
    })
    if (!res.ok) {
      const err = (await res.json().catch(() => null)) as ApiError | null
      throw new Error(err?.message ?? `HTTP ${res.status}`)
    }
    assessSuccess.value = '保存成功'
  } catch (e) {
    assessError.value = e instanceof Error ? e.message : '保存失败'
  } finally {
    submitLoading.value = false
  }
}
</script>

<template>
  <div class="page">
    <h1>成员考核信息</h1>

    <p v-if="assessError" class="msg error">{{ assessError }}</p>
    <p v-if="assessSuccess" class="msg success">{{ assessSuccess }}</p>

    <form class="assess-form" @submit.prevent="submitAssess">
      <label class="field">
        <span class="label">姓名</span>
        <input v-model="assessForm.name" type="text" maxlength="50" placeholder="被考核人姓名" />
      </label>

      <div class="field">
        <span class="label">考核方向</span>
        <div class="radio-group">
          <label v-for="opt in DIRECTION_OPTIONS" :key="opt.value" class="radio-item">
            <input v-model.number="assessForm.assessmentDirection" type="radio" :value="opt.value" />
            <span>{{ opt.label }}</span>
          </label>
        </div>
      </div>

      <div class="field">
        <span class="label">前端考核结果</span>
        <div class="radio-group">
          <label v-for="v in RESULT_VALUES" :key="v" class="radio-item">
            <input v-model.number="assessForm.frontendResult" type="radio" :value="v" />
            <span>{{ v }}</span>
          </label>
        </div>
      </div>

      <div class="field">
        <span class="label">后端考核结果</span>
        <div class="radio-group">
          <label v-for="v in RESULT_VALUES" :key="v" class="radio-item">
            <input v-model.number="assessForm.backendResult" type="radio" :value="v" />
            <span>{{ v }}</span>
          </label>
        </div>
      </div>

      <div class="field">
        <span class="label">数据管理考核结果</span>
        <div class="radio-group">
          <label v-for="v in RESULT_VALUES" :key="v" class="radio-item">
            <input v-model.number="assessForm.dataManagementResult" type="radio" :value="v" />
            <span>{{ v }}</span>
          </label>
        </div>
      </div>

      <label class="field">
        <span class="label">考核时间</span>
        <input v-model="assessForm.assessmentTime" type="datetime-local" />
      </label>

      <label class="field">
        <span class="label">考核人</span>
        <input v-model="assessForm.assessor" type="text" maxlength="50" placeholder="填写考核人姓名" />
      </label>

      <button type="submit" class="submit-btn" :disabled="submitLoading">
        {{ submitLoading ? '保存中...' : '提交' }}
      </button>
    </form>
  </div>
</template>

<style scoped>
.page {
  max-width: 640px;
  margin: 48px auto;
  padding: 0 24px;
  font-family: system-ui, sans-serif;
}

h1 {
  font-size: 1.25rem;
  margin-bottom: 24px;
}

.msg {
  margin: 0 0 16px;
  padding: 10px 12px;
  border-radius: 6px;
  font-size: 0.875rem;
}

.msg.error {
  color: #b91c1c;
  background: #fef2f2;
  border: 1px solid #fca5a5;
}

.msg.success {
  color: #15803d;
  background: #f0fdf4;
  border: 1px solid #86efac;
}

.assess-form {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.field .label {
  font-size: 0.875rem;
  color: #444;
}

.field input[type='text'],
.field input[type='datetime-local'] {
  padding: 8px 10px;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 0.9375rem;
  font-family: inherit;
}

.field input[type='text']:focus,
.field input[type='datetime-local']:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 2px rgb(59 130 246 / 0.15);
}

.radio-group {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.radio-item {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  font-size: 0.9375rem;
}

.radio-item input {
  margin: 0;
  cursor: pointer;
}

.submit-btn {
  align-self: flex-start;
  margin-top: 4px;
  padding: 8px 16px;
  cursor: pointer;
  border: 1px solid #ccc;
  border-radius: 6px;
  background: #fff;
  font-size: 0.9375rem;
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>

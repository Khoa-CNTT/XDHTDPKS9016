<template>
  <div class="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center z-50">
    <div class="bg-white p-6 rounded-lg w-full max-w-md shadow-lg relative">
      <!-- Nút Đóng -->
      <button
        @click="close"
        class="absolute top-3 right-3 text-gray-500 hover:text-gray-700"
        aria-label="Đóng"
      >
        ✕
      </button>

      <h2 class="text-xl font-semibold mb-6 text-blue-600">Tạo mới khách sạn</h2>

      <!-- Step indicator -->
      <div class="flex justify-between mb-6 border-b border-gray-300">
        <div
          class="pb-2 cursor-pointer"
          :class="
            step === 1 ? 'border-b-4 border-blue-600 font-semibold text-blue-600' : 'text-gray-500'
          "
          @click="goStep(1)"
        >
          Bước 1: Nhập thông tin
        </div>
        <div
          class="pb-2 cursor-pointer"
          :class="
            step === 2 ? 'border-b-4 border-blue-600 font-semibold text-blue-600' : 'text-gray-500'
          "
          @click="goStep(2)"
        >
          Bước 2: Thiết lập mật khẩu
        </div>
      </div>

      <!-- Form 2 bước -->
      <form @submit.prevent="onSubmit">
        <!-- Bước 1 -->
        <div v-if="step === 1">
          <div class="mb-3">
            <label class="block font-medium mb-1">Tên khách sạn</label>
            <input
              v-model="form.name"
              type="text"
              class="input"
              required
            />
          </div>

          <div class="mb-3">
            <label class="block font-medium mb-1">Email</label>
            <input
              v-model="form.email"
              type="email"
              class="input"
              required
            />
          </div>

          <div class="mb-3">
            <label class="block font-medium mb-1">Địa chỉ</label>
            <textarea
              v-model="form.address"
              class="input"
              rows="2"
            ></textarea>
          </div>

          <div class="mb-3">
            <label class="block font-medium mb-1">Hotline</label>
            <input
              v-model="form.hotline"
              type="text"
              class="input"
            />
          </div>

          <div class="mb-3">
            <label class="block font-medium mb-1">Mô tả</label>
            <textarea
              v-model="form.description"
              class="input"
              rows="3"
            ></textarea>
          </div>

          <div class="flex justify-end mt-6 gap-2">
            <button
              type="button"
              @click="close"
              class="btn btn-gray"
            >
              Hủy
            </button>
            <button
              type="button"
              @click="nextStep"
              class="btn btn-green"
              :disabled="!canNextStep1"
            >
              Tiếp theo
            </button>
          </div>
        </div>

        <!-- Bước 2 -->
        <div v-if="step === 2">
          <div class="mb-3">
            <label class="block font-medium mb-1">Tên đăng nhập (username)</label>
            <input
              v-model="form.username"
              type="text"
              class="input"
              readonly
            />
          </div>

          <div class="mb-3">
            <label class="block font-medium mb-1">Mật khẩu (password)</label>
            <input
              v-model="form.password"
              type="text"
              class="input"
              readonly
            />
            <button
              type="button"
              class="text-blue-600 underline mt-1"
              @click="generatePassword"
            >
              Tạo mật khẩu mới
            </button>
          </div>

          <div class="mb-3 flex items-center gap-2">
            <input
              v-model="form.sendEmail"
              type="checkbox"
              id="sendEmail"
            />
            <label
              for="sendEmail"
              class="font-medium"
              >Gửi email thông báo</label
            >
          </div>

          <div class="flex justify-between mt-6 gap-2">
            <button
              type="button"
              @click="prevStep"
              class="btn btn-gray"
            >
              Quay lại
            </button>
            <button
              type="submit"
              class="btn btn-green"
              :disabled="!canSubmit"
            >
              Hoàn thành
            </button>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, watch, computed } from 'vue'
import { createHotel } from '@/services/supplier'

const emit = defineEmits<{
  (e: 'close'): void
  (e: 'refresh'): void
}>()

const step = ref(1)

const form = reactive({
  name: '',
  email: '',
  image: null as null | string,
  address: '',
  hotline: '',
  description: '',
  username: '',
  password: '',
  sendEmail: true,
})

watch(
  () => form.email,
  (newEmail) => {
    const usernamePart = newEmail.split('@')[0] || ''
    form.username = usernamePart
    generatePassword()
  },
)

function generatePassword() {
  const length = 12
  const uppercase = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'
  const lowercase = 'abcdefghijklmnopqrstuvwxyz'
  const numbers = '0123456789'
  const special = '!@#$%^&*()_+{}:"<>?|[];\',./`~'

  function getRandomChar(str: string) {
    return str.charAt(Math.floor(Math.random() * str.length))
  }

  let password = ''
  password += getRandomChar(uppercase)
  password += getRandomChar(lowercase)
  password += getRandomChar(numbers)
  password += getRandomChar(special)

  const allChars = uppercase + lowercase + numbers + special
  for (let i = 4; i < length; i++) {
    password += getRandomChar(allChars)
  }

  password = password
    .split('')
    .sort(() => Math.random() - 0.5)
    .join('')

  form.password = password
}

const canNextStep1 = computed(() => {
  return form.name.trim() !== '' && validateEmail(form.email)
})

const canSubmit = computed(() => {
  return form.username.trim() !== '' && form.password.trim() !== ''
})

function validateEmail(email: string) {
  const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return re.test(email)
}

function nextStep() {
  if (step.value === 1 && canNextStep1.value) {
    step.value = 2
  }
}

function prevStep() {
  if (step.value === 2) {
    step.value = 1
  }
}

function goStep(s: number) {
  if (s === 1) {
    step.value = 1
  } else if (s === 2 && canNextStep1.value) {
    step.value = 2
  }
}

const close = () => {
  emit('close')
}

const onSubmit = async () => {
  const body = {
    name: form.name,
    email: form.email,
    image: form.image,
    address: form.address,
    hotline: form.hotline,
    description: form.description,
    username: form.username,
    password: form.password,
    sendEmail: form.sendEmail,
  }

  try {
    const response = await createHotel(body)
    emit('refresh')
    emit('close')
  } catch (error) {
    void error
  }
}
</script>

<style scoped>
.input {
  @apply w-full border border-gray-300 rounded px-3 py-2;
}
.btn {
  @apply px-4 py-2 rounded font-medium;
}
.btn-gray {
  @apply bg-gray-300 hover:bg-gray-400;
}
.btn-green {
  @apply bg-green-600 text-white hover:bg-green-700 disabled:opacity-60 disabled:cursor-not-allowed;
}

/* Container chính */
div.bg-white {
  width: 100%;
  max-width: 500px; /* nửa màn hình máy tính */
  padding: 1.5rem 2rem;
  box-sizing: border-box;
  border-radius: 0.5rem;
}

/* Responsive: màn hình nhỏ */
@media (max-width: 768px) {
  div.bg-white {
    max-width: 90vw;
    padding: 1rem 1.2rem;
  }
}

@media (max-width: 400px) {
  div.bg-white {
    max-width: 100vw;
    margin: 0 0.5rem;
    padding: 0.8rem 1rem;
    border-radius: 0;
  }
}
</style>

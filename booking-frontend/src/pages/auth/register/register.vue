<template>
  <div class="fixed inset-0 bg-gray-600 bg-opacity-50 flex justify-center items-center p-4 overflow-hidden">
    <div class="relative w-full max-w-3xl bg-white shadow-lg rounded-lg overflow-hidden">
      <button @click="$emit('close')" class="absolute top-4 right-4 text-gray-500 hover:text-gray-700 text-2xl">
        &times;
      </button>
      <div class="flex w-full h-auto">
        <div class="hidden md:block flex-1 bg-cover bg-center" style="background-image: url('/assets/images/bg.jpg');">
          <div class="h-full flex justify-center items-center bg-white/70 p-6">
            <h1 class="text-4xl font-bold text-center">
              <span class="text-blue-500">Elite</span>
              <span class="text-gray-600">Booking</span>
            </h1>
          </div>
        </div>

        <!-- Form đăng ký -->
        <div class="flex-1 flex flex-col justify-center items-center bg-white p-10">
          <h2 class="text-3xl font-semibold mb-6 uppercase">Đăng ký</h2>

          <form class="w-full max-w-md space-y-5" @submit.prevent="handleRegister" novalidate>
            <div>
              <label class="block text-lg font-bold mb-1" for="username">Tên người dùng</label>
              <input
                id="username"
                v-model="formData.username"
                @blur="validateField('username')"
                type="text"
                placeholder="Enter your username"
                class="w-full p-4 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-400" />
              <p v-if="errors.username" class="text-red-600 text-sm mt-1">{{ errors.username }}</p>
            </div>

            <div>
              <label class="block text-lg font-bold mb-1" for="email">Email</label>
              <input
                id="email"
                v-model="formData.email"
                @blur="validateField('email')"
                type="email"
                placeholder="Enter your email"
                class="w-full p-4 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-400" />
              <p v-if="errors.email" class="text-red-600 text-sm mt-1">{{ errors.email }}</p>
            </div>

            <div>
              <label class="block text-lg font-bold mb-1" for="password">Mật khẩu</label>
              <input
                id="password"
                v-model="formData.password"
                @blur="validateField('password')"
                type="password"
                placeholder="6+ characters"
                class="w-full p-4 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-400" />
              <p v-if="errors.password" class="text-red-600 text-sm mt-1">{{ errors.password }}</p>
            </div>

            <button
              type="submit"
              :disabled="hasErrors"
              class="w-full p-5 bg-blue-500 text-white rounded text-lg hover:bg-blue-600 transition disabled:opacity-50 disabled:cursor-not-allowed">
              Đăng ký
            </button>

            <p class="text-center mt-5">
              Bạn đã có tài khoản chưa?
              <RouterLink to="/login" class="text-blue-500 hover:underline">Đăng nhập</RouterLink>
              ngay.
            </p>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, computed } from 'vue'
import { registerApi } from '@/services/auth'
import { useRouter } from 'vue-router'
import { toast } from 'vue3-toastify'
import 'vue3-toastify/dist/index.css'
import { validEmail, validUsername, validPass } from '@/utils/validate'

const emit = defineEmits(['close', 'switchToLogin'])
const router = useRouter()

const formData = reactive({
  email: '',
  username: '',
  password: ''
})

const errors = reactive({
  email: '',
  username: '',
  password: ''
})

const hasErrors = computed(() => Object.values(errors).some(msg => msg !== ''))

function validateField(field: keyof typeof formData) {
  let result
  if (field === 'email') {
    result = validEmail(formData.email)
    errors.email = result.check ? '' : result.mess
  } else if (field === 'username') {
    result = validUsername(formData.username)
    errors.username = result.check ? '' : result.mess
  } else if (field === 'password') {
    result = validPass(formData.password)
    errors.password = result.check ? '' : result.mess
  }
}

async function handleRegister() {
  // Validate all
  validateField('username')
  validateField('email')
  validateField('password')

  if (hasErrors.value) return

  try {
    await registerApi(formData)
    toast.success('Đăng ký thành công!', { autoClose: 10000, position: 'top-right' })
    setTimeout(() => {
      emit('close')
      router.push({ name: 'login' })
    }, 3000)
  } catch (error) {
    console.error('Lỗi đăng ký:', error)
    toast.error('Đăng ký thất bại! Vui lòng kiểm tra lại thông tin.', { autoClose: 10000, position: 'top-right' })
  }
}
</script>
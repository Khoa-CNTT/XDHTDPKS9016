<template>
  <div class="fixed inset-0 bg-gray-600 bg-opacity-50 flex justify-center items-center p-4 overflow-hidden">
    <div class="relative w-full max-w-3xl bg-white shadow-lg rounded-lg overflow-hidden">
      <button @click="$emit('close')" class="absolute top-4 right-4 text-gray-500 hover:text-gray-700 text-2xl">&times;</button>
      <div class="flex w-full h-auto">
        <!-- Left Image -->
        <div class="hidden md:block flex-1 bg-cover bg-center" style="background-image: url('/assets/images/bg.jpg');">
          <div class="h-full flex justify-center items-center bg-white/70 p-6">
            <h1 class="text-4xl font-bold text-center">
              <span class="text-blue-500">Elite</span>
              <span class="text-gray-600">Booking</span>
            </h1>
          </div>
        </div>
        <!-- Login Form -->
        <div class="flex-1 flex flex-col justify-center items-center bg-white p-14">
          <h2 class="text-3xl font-semibold mb-6 uppercase">Đăng nhập</h2>
          <form class="w-full max-w-md space-y-5" @submit.prevent="handleLogin" novalidate>
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
            <div class="w-full text-right">
              <RouterLink to="/password/forgot" class="text-blue-500 hover:underline text-sm">Quên mật khẩu?</RouterLink>
            </div>
            <button
              type="submit"
              :disabled="hasErrors"
              class="w-full p-5 bg-blue-500 text-white rounded text-lg hover:bg-blue-600 transition disabled:opacity-50 disabled:cursor-not-allowed">
              Đăng nhập
            </button>
            <p class="text-center mt-5">
              Bạn chưa có tài khoản?
              <RouterLink to="/register" class="text-blue-500 hover:underline">Đăng ký</RouterLink>
              ngay.
            </p>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, computed,onMounted } from 'vue'
import { loginApi } from '@/services/auth'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'
import { toast } from 'vue3-toastify'
import 'vue3-toastify/dist/index.css'
import { validUsername, validPass } from '@/utils/validate'

const emit = defineEmits(['close', 'switchToRegister'])
const router = useRouter()
const authStore = useAuthStore()

const formData = reactive({
  username: '',
  password: ''
})
const errors = reactive({
  username: '',
  password: ''
})

const hasErrors = computed(() => Object.values(errors).some(msg => msg !== ''))

function validateField(field: keyof typeof formData) {
  if (field === 'username') {
    const res = validUsername(formData.username)
    errors.username = res.check ? '' : res.mess
  } else if (field === 'password') {
    const res = validPass(formData.password)
    errors.password = res.check ? '' : res.mess
  }
}

async function handleLogin() {
  // Validate inputs
  validateField('username')
  validateField('password')
  if (hasErrors.value) return

  try {
    const response = await loginApi(formData.username, formData.password)
    if (!response.token) throw new Error('Không nhận được token')
    localStorage.setItem('access_token', response.token)
    await authStore.setupAuth()
    toast.success('Đăng nhập thành công!', { autoClose: 5000, position: 'top-right' })

    // Redirect based on role
    const role = response.user?.role
    if (role === 'SUPPLIER') {
      router.push('/supplier')
    } else if (role === 'ADMIN') {
      router.push('/admin')
    } else {
      router.push('/')
    }
    emit('close')
  } catch (err) {
    console.error('Lỗi đăng nhập:', err)
    toast.error('Đăng nhập thất bại! Vui lòng kiểm tra lại thông tin.', { autoClose: 5000, position: 'top-right' })
  }
}

// Clear form when mounted
onMounted(() => {
  formData.username = ''
  formData.password = ''
})
</script>

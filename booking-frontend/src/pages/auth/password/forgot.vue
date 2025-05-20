<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100 p-4">
    <div class="bg-white shadow-md rounded-lg w-full max-w-md p-8">
      <h2 class="text-2xl font-bold text-center text-blue-600 mb-6 uppercase">Quên mật khẩu</h2>

      <form @submit.prevent="submitEmail" class="space-y-5">
        <div>
          <label class="block text-gray-700 font-medium mb-2">Email</label>
          <input v-model="email"
            :class="['w-full p-3 border rounded focus:outline-none focus:ring-2', emailError ? 'border-red-500 focus:ring-red-400' : 'border-gray-300 focus:ring-blue-400']"
            type="email" placeholder="Nhập địa chỉ email của bạn" required />
          <p v-if="emailError" class="text-red-500 text-sm mt-2">{{ emailError }}</p>
        </div>

        <button type="submit"
          class="w-full bg-blue-500 hover:bg-blue-600 text-white py-3 rounded font-semibold transition">
          Gửi yêu cầu đặt lại mật khẩu
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { forgotPasswordApi } from '@/services/auth'
import { useRouter } from 'vue-router'
import { validEmail } from '@/utils/validate'

const router = useRouter()
const email = ref('')
const emailError = ref('')

async function submitEmail() {
  const emailValidation = validEmail(email.value)

  if (!emailValidation.check) {
    emailError.value = emailValidation.mess
    return
  } else {
    emailError.value = ''
  }

  try {
    const res = await forgotPasswordApi(encodeURIComponent(email.value))
    console.log('Phản hồi API:', res)

    router.push({
      name: 'verify-otp',
      query: { email: email.value }
    })
  } catch (err) {
    console.error('Lỗi gửi yêu cầu:', err)
    emailError.value = 'Không thể gửi yêu cầu. Vui lòng thử lại sau.'
  }
}
</script>

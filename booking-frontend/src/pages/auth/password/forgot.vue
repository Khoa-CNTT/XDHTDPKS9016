<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100 p-4">
    <div class="bg-white shadow-md rounded-lg w-full max-w-md p-8">
      <h2 class="text-2xl font-bold text-center text-blue-600 mb-6 uppercase">Quên mật khẩu</h2>

      <form @submit.prevent="submitEmail" class="space-y-5">
        <div>
          <label class="block text-gray-700 font-medium mb-2">Email</label>
          <input
            @input="logEmailInput"
            v-model="email"
            :class="[
              'w-full p-3 border rounded focus:outline-none focus:ring-2',
              emailError ? 'border-red-500 focus:ring-red-400' : 'border-gray-300 focus:ring-blue-400'
            ]"
            type="email"
            placeholder="Nhập địa chỉ email của bạn"
            required
          />
          <p v-if="emailError" class="text-red-500 text-sm mt-2">{{ emailError }}</p>
        </div>

        <button
          type="submit"
          :disabled="loading"
          class="w-full bg-blue-500 hover:bg-blue-600 text-white py-3 rounded font-semibold transition disabled:opacity-50"
        >
          {{ loading ? 'Đang gửi...' : 'Gửi yêu cầu đặt lại mật khẩu' }}
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
import { toast } from 'vue3-toastify'

const router = useRouter()
const email = ref('')
const emailError = ref('')
const loading = ref(false)

async function submitEmail() {
  await new Promise(resolve => setTimeout(resolve, 1000)) 

  const emailValidation = validEmail(email.value)

  if (!emailValidation.check) {
    emailError.value = emailValidation.mess
    return
  } else {
    emailError.value = ''
  }

  loading.value = true

  try {
    const res = await forgotPasswordApi(encodeURIComponent(email.value))
    toast.success('Gửi email thành công! Vui lòng kiểm tra hộp thư.', {
      autoClose: 3000
    })

    setTimeout(() => {
      router.push({
        name: 'verify-otp',
        query: { email: email.value }
      })
    }, 1000)
  } catch (err) {
    emailError.value = 'Không thể gửi yêu cầu. Vui lòng thử lại sau.'
    toast.error('Lỗi gửi email. Vui lòng thử lại.', { autoClose: 5000, position: 'top-right' })
  } finally {
    loading.value = false
  }
}


function logEmailInput() {
  console.log('Người dùng đang nhập:', email.value)
}
</script>

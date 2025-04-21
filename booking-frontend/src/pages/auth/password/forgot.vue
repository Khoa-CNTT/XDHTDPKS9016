<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100 p-4">
    <div class="bg-white shadow-md rounded-lg w-full max-w-md p-8">
      <h2 class="text-2xl font-bold text-center text-blue-600 mb-6 uppercase">Quên mật khẩu</h2>

      <form @submit.prevent="submitEmail" class="space-y-5">
        <div>
          <label class="block text-gray-700 font-medium mb-2">Email</label>
          <input
            v-model="email"
            type="email"
            placeholder="Nhập địa chỉ email của bạn"
            required
            class="w-full p-3 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
          />
        </div>

        <button
          type="submit"
          class="w-full bg-blue-500 hover:bg-blue-600 text-white py-3 rounded font-semibold transition"
        >
          Gửi yêu cầu đặt lại mật khẩu
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { forgotPasswordApi } from '@/services/auth'
import { toast } from 'vue3-toastify'
import 'vue3-toastify/dist/index.css'
import { useRouter } from 'vue-router'

const router = useRouter()
const email = ref('')

async function submitEmail() {
  if (!email.value) {
    toast.error('Vui lòng nhập email.')
    return
  }

  try {
    const res = await forgotPasswordApi(encodeURIComponent(email.value))
    console.log('Phản hồi API:', res)

    toast.success('Yêu cầu đặt lại mật khẩu đã được gửi tới email của bạn.')
    router.push({
      name: 'verify-otp',
      query: { email: email.value }
    })
  } catch (err) {
    console.error('Lỗi gửi yêu cầu:', err)
    toast.error('Không thể gửi yêu cầu. Vui lòng kiểm tra lại email hoặc thử lại sau.')
  }
}
</script>

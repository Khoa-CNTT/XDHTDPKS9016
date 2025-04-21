<template>
  <div class="fixed inset-0 bg-gray-600 bg-opacity-50 flex justify-center items-center p-4 overflow-hidden">
    <div class="relative w-full max-w-3xl bg-white shadow-lg rounded-lg overflow-hidden"> <!-- Tăng max-width -->
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


        <div class="flex-1 flex flex-col justify-center items-center bg-white p-14">
          <h2 class="text-3xl font-semibold mb-6 uppercase">Đăng nhập</h2>
          <form class="w-full max-w-md space-y-5" @submit.prevent="handleLogin">
            <div>
              <label class="block text-lg font-bold mb-1">Tên người dùng</label>
              <input v-model="formData.username" type="text" placeholder="Enter your username" required
                class="w-full p-4 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-400" />
            </div>

            <div>
              <label class="block text-lg font-bold mb-1">Mật khẩu</label>
              <input v-model="formData.password" type="password" placeholder="6+ characters" required
                class="w-full p-4 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-400" />
            </div>
            <RouterLink to="/password/forgot"><p class="text-right text-blue-500 cursor-pointer hover:underline">Quên mật khẩu?</p></RouterLink>
           

            <div class="flex items-center my-5">
              <div class="flex-1 border-b border-gray-300"></div>
              <span class="px-4 text-gray-600 font-bold">Hoặc</span>
              <div class="flex-1 border-b border-gray-300"></div>
            </div>

            <div class="flex justify-around gap-5">
              <button class="flex-1 p-4 bg-blue-600 text-white rounded hover:bg-blue-700 transition">Facebook</button>
              <button class="flex-1 p-4 bg-red-500 text-white rounded hover:bg-red-600 transition">Google</button>
            </div>

            <button type="submit"
              class="w-full p-5 bg-blue-500 text-white rounded text-lg hover:bg-blue-600 transition">
              Đăng nhập
            </button>

            <p class="text-center mt-5">
              Bạn chưa có tài khoản? <RouterLink href="#" to="/register" class="text-blue-500 hover:underline">Đăng ký
              </RouterLink>
            </p>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { loginApi } from '@/services/auth';
import { getInfoApi } from '@/services/user';
import { useAuthStore } from '@/stores/auth';
import { ref ,onMounted} from 'vue'
import { toast } from 'vue3-toastify'
import { useRouter } from 'vue-router'

const formData = ref({
  username: '',
  password: '',
})

const errorMessage = ref('')
const router = useRouter()
const handleLogin = async () => {
  try {
    const response = await loginApi(formData.value.username, formData.value.password)
    console.log('Đăng nhập thành công:', response)
    console.log('Đăng nhập thành công:', response.token)
    if (response && response.token) {
      const authStore = useAuthStore()
      localStorage.setItem('access_token', response.token)
      await authStore.setupAuth()
      console.log('Đã lưu token vào localStorage:', localStorage.getItem('access_token'))
      toast.success('Đăng nhập thành công!', {
        autoClose: 10000,
        position: 'top-right',
      })
      setTimeout(() => {
        // window.location.reload();
        router.push('/')
      }, 3000)
    } else {
      throw new Error('Không nhận được token từ API')
    }
  } catch (error) {
    console.error('Lỗi đăng nhập:', error)
    errorMessage.value = 'Đã có lỗi xảy ra, vui lòng thử lại!'
    toast.error('Đăng nhập thất bại! Vui lòng kiểm tra lại thông tin.', {
      autoClose: 10000,
      position: 'top-right',
    })
  }
}
onMounted(() => {
  const savedToken = localStorage.getItem('access_token')
  console.log('Token lưu trong localStorage là:', savedToken)
})

</script>
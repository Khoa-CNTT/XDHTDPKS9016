<!-- <template>
    <div class="min-h-screen flex items-center justify-center bg-gray-100 p-4">
      <div class="bg-white shadow-md rounded-lg w-full max-w-md p-8">
        <h2 class="text-2xl font-bold text-center text-blue-600 mb-4">Xác minh mã OTP</h2>
        <p class="text-center text-gray-600 mb-6">
          Vui lòng nhập mã OTP đã được gửi đến email của bạn.
        </p>
  
        <form @submit.prevent="verifyOtp" class="space-y-5">
          <div>
            <label class="block text-gray-700 font-medium mb-2">Mã OTP</label>
            <input
              v-model="otp"
              type="text"
              maxlength="6"
              placeholder="Nhập mã OTP gồm 6 chữ số"
              required
              class="w-full p-3 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-400 text-center tracking-widest text-xl"
            />
          </div>
  
          <button
            type="submit"
            class="w-full bg-blue-500 hover:bg-blue-600 text-white py-3 rounded font-semibold transition"
          >
            Xác minh
          </button>
        </form>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import { confirmOtpApi } from '@/services/auth'
  const otp = ref('');
  
  </script>
   -->
   <template>
    <div class="min-h-screen flex items-center justify-center bg-gray-100 p-4">
      <div class="bg-white shadow-md rounded-lg w-full max-w-md p-8">
        <h2 class="text-2xl font-bold text-center text-blue-600 mb-4">Xác minh mã OTP</h2>
        <p class="text-center text-gray-600 mb-6">
          Vui lòng nhập mã OTP đã được gửi đến email của bạn.
        </p>
  
        <form @submit.prevent="verifyOtp" class="space-y-5">
          <div>
            <label class="block text-gray-700 font-medium mb-2">Mã OTP</label>
            <input
              v-model="otp"
              type="text"
              maxlength="6"
              placeholder="Nhập mã OTP gồm 6 chữ số"
              required
              class="w-full p-3 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-400 text-center tracking-widest text-xl"
            />
          </div>
  
          <button
            type="submit"
            class="w-full bg-blue-500 hover:bg-blue-600 text-white py-3 rounded font-semibold transition"
          >
            Xác minh
          </button>
        </form>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  import { useRoute, useRouter } from 'vue-router'
  import { confirmOtpApi } from '@/services/auth'
  import { toast } from 'vue3-toastify'
  import 'vue3-toastify/dist/index.css'
  
  const otp = ref('')
  const route = useRoute()
  const router = useRouter()
  
  const email = route.query.email
  
  async function verifyOtp() {
    if (!otp.value || otp.value.length !== 6) {
      toast.error('Mã OTP phải gồm 6 chữ số.', { autoClose: 5000, position: 'top-right' })
      return
    }
  
    if (!email) {
      toast.error('Không tìm thấy email để xác minh.', { autoClose: 5000, position: 'top-right' })
      return
    }
  
    try {
      const res = await confirmOtpApi(otp.value, email)
      console.log('Phản hồi xác minh:', res)
  
      toast.success('Xác minh thành công!', { autoClose: 5000, position: 'top-right' })
  
      router.push({
        name: 'password-reset',
        query: { email }
      })
    } catch (err) {
      console.error('Lỗi xác minh OTP:', err)
      toast.error('Mã OTP không chính xác hoặc đã hết hạn.', { autoClose: 5000, position: 'top-right' })
    }
  }
  </script>
  
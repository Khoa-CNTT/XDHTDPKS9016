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
          <form class="w-full max-w-md space-y-5 mx-auto" @submit.prevent="handleLogin" autocomplete="off">
            <div>
              <label class="block text-lg font-bold mb-1">Tên người dùng</label>
              <input v-model="formData.username" type="text" placeholder="Enter your username" required
                class="w-full p-4 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-400" />
              <span v-if="errorMessages.username" class="text-red-500 text-sm">{{ errorMessages.username }}</span>
            </div>

            <div>
              <label class="block text-lg font-bold mb-1">Mật khẩu</label>
              <input v-model="formData.password" type="password" placeholder="6+ characters" required
                class="w-full p-4 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-400" />
              <span v-if="errorMessages.password" class="text-red-500 text-sm">{{ errorMessages.password }}</span>
            </div>
            <RouterLink to="/password/forgot">
              <p class="text-right text-blue-500 cursor-pointer hover:underline">Quên mật khẩu?</p>
            </RouterLink>

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
              class="w-full max-w-sm mx-auto block p-5 bg-blue-500 text-white rounded text-lg hover:bg-blue-600 transition">
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
import { ref, onMounted } from 'vue'
import { toast } from 'vue3-toastify'
import { useRouter } from 'vue-router'
import { validUsername, validPass } from '@/utils/validate';
const formData = ref({
  username: '',
  password: '',
})
const errorMessages = ref({
  username: '',
  password: '',
});
const authStore = useAuthStore()
const errorMessage = ref('')
const router = useRouter()
const handleLogin = async () => {
  // Reset lỗi trước khi kiểm tra
  errorMessages.value.username = '';
  errorMessages.value.password = '';

  // Kiểm tra username và password
  const usernameValidation = validUsername(formData.value.username);
  const passwordValidation = validPass(formData.value.password);

  if (!usernameValidation.check) {
    errorMessages.value.username = usernameValidation.mess;
  }

  if (!passwordValidation.check) {
    errorMessages.value.password = passwordValidation.mess;
  }

  // Nếu có lỗi, không tiếp tục
  if (!usernameValidation.check || !passwordValidation.check) {
    return;
  }

  try {
    const response = await loginApi(formData.value.username, formData.value.password);
    console.log('=================ta day ', response);
    
    if (response && response.token) {
      localStorage.setItem('access_token', response.token);
      await authStore.setupAuth();
      toast.success('Đăng nhập thành công!', {
        autoClose: 10000,
        position: 'top-right',
      });

      const role = response.user?.role;
      console.log("--------------->",role);
      // router.push('/');
      if (role === 'USER') {
        router.push('/');
      } else if (role === 'SUPPLIER') {
        router.push('/supplier');
      } else if (role === 'ADMIN') {
        router.push('/admin');
      } else {
        router.push('/');
      }
    } else {
      throw new Error('Không nhận được token từ API');
    }
  } catch (error) {
    console.error('Lỗi đăng nhập:', error);
    toast.error('Đăng nhập thất bại! Vui lòng kiểm tra lại thông tin.', {
      autoClose: 10000,
      position: 'top-right',
    });
  }
};

onMounted(async () => {
  formData.value.username = '';
  formData.value.password = '';
  const savedToken = localStorage.getItem('access_token');

  if (savedToken) {
    try {
      await authStore.setupAuth();
    } catch (error) {
      console.error('Lỗi tự động đăng nhập:', error);
    }
  }
});

</script>
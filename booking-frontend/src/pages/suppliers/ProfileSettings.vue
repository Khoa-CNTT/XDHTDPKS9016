<template>
  <div class="max-w-md mx-auto mt-10 p-8 bg-gradient-to-br from-white to-gray-100 rounded-2xl shadow-lg">
    <h2 class="text-3xl font-extrabold mb-6 text-center text-gray-800">🔒 Đổi mật khẩu</h2>
    <form @submit.prevent="onSubmit" class="space-y-6">
      <!-- Mật khẩu hiện tại -->
      <div>
        <label for="current" class="block text-sm font-semibold text-gray-700 mb-2">Mật khẩu hiện tại</label>
        <input
          id="current"
          v-model="form.password"
          type="password"
          required
          class="w-full px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-indigo-400 transition"
        />
      </div>

      <!-- Mật khẩu mới -->
      <div>
        <label for="new" class="block text-sm font-semibold text-gray-700 mb-2">Mật khẩu mới</label>
        <input
          id="new"
          v-model="form.newPassword"
          type="password"
          required
          class="w-full px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-indigo-400 transition"
        />
      </div>

      <!-- Xác nhận mật khẩu mới -->
      <div>
        <label for="confirm" class="block text-sm font-semibold text-gray-700 mb-2">Xác nhận mật khẩu mới</label>
        <input
          id="confirm"
          v-model="form.confirmPassword"
          type="password"
          required
          class="w-full px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-indigo-400 transition"
        />
      </div>

      <!-- Nút submit -->
      <button
        type="submit"
        class="w-full flex justify-center items-center bg-indigo-600 hover:bg-indigo-700 text-white font-semibold px-6 py-3 rounded-xl shadow-md transition-all active:scale-95"
      >
        Đổi mật khẩu
      </button>
    </form>
  </div>
</template>
<script setup lang="ts">
import { reactive } from 'vue';
import { updateUserApi } from '@/services/user'; 
import { ChangePasswordPayload } from '@/types/user'; 
import { toast } from 'vue3-toastify';

const form = reactive<ChangePasswordPayload>({
  password: '',
  newPassword: '',
  confirmPassword: '',
});

const onSubmit = async () => {
  console.log('Form Data:', form); // 👉 Log dữ liệu ra

  if (form.newPassword !== form.confirmPassword) {
    toast.error('Mật khẩu xác nhận không khớp!', { autoClose: 5000, position: 'top-right' });
    return;
  }

  try {
    await updateUserApi(form);
    toast.success('Đổi mật khẩu thành công!', { autoClose: 5000, position: 'top-right' });
  } catch (err) {
    toast.error('Đổi mật khẩu thất bại!', { autoClose: 5000, position: 'top-right' });
    console.error('API Error:', err);
  }
};
</script>



<style scoped>
/* Card bóng mềm */
.shadow-lg {
  box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1);
}

/* Shadow input/button */
.shadow-md {
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.08);
}
</style>

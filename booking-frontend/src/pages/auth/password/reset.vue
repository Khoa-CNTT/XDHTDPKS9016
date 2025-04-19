<template>
    <div class="min-h-screen flex items-center justify-center bg-gray-100 p-4">
      <div class="bg-white shadow-lg rounded-lg w-full max-w-md p-8">
        <h2 class="text-2xl font-bold text-center text-blue-600 mb-4">Đặt lại mật khẩu</h2>
        <p class="text-center text-gray-600 mb-6">
          Vui lòng nhập mật khẩu mới của bạn bên dưới.
        </p>
  
        <form @submit.prevent="resetPassword" class="space-y-5">
          <div>
            <label class="block text-gray-700 font-medium mb-2">Mật khẩu mới</label>
            <input
              type="password"
              v-model="password"
              placeholder="Nhập mật khẩu mới"
              required
              class="w-full p-3 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
            />
          </div>
  
          <div>
            <label class="block text-gray-700 font-medium mb-2">Nhập lại mật khẩu</label>
            <input
              type="password"
              v-model="confirmPassword"
              placeholder="Nhập lại mật khẩu"
              required
              class="w-full p-3 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
            />
          </div>
  
          <p v-if="errorMessage" class="text-red-500 text-sm text-center">{{ errorMessage }}</p>
  
          <button
            type="submit"
            class="w-full bg-blue-500 hover:bg-blue-600 text-white py-3 rounded font-semibold transition"
          >
            Xác nhận
          </button>
        </form>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  
  const password = ref('');
  const confirmPassword = ref('');
  const errorMessage = ref('');
  
  function resetPassword() {
    if (password.value !== confirmPassword.value) {
      errorMessage.value = 'Mật khẩu không khớp. Vui lòng kiểm tra lại.';
      return;
    }
  
    // Gọi API để cập nhật mật khẩu mới ở đây
    console.log('Mật khẩu mới:', password.value);
    alert('Mật khẩu đã được cập nhật thành công!');
  }
  </script>
  
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
  
        <!-- <div class="text-center mt-6">
          <p class="text-sm text-gray-600">Không nhận được mã?</p>
          <button
            @click="resendOtp"
            class="text-blue-500 hover:underline font-medium mt-1"
          >
            Gửi lại mã OTP
          </button>
        </div> -->
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  
  const otp = ref('');
  
  function verifyOtp() {
    console.log('Mã OTP người dùng nhập:', otp.value);
  
    // Ở đây bạn sẽ gọi API để xác minh OTP
    alert('Đang xác minh mã OTP...');
  }
  
  function resendOtp() {
    // Gửi lại mã OTP qua email
    alert('Mã OTP mới đã được gửi đến email của bạn.');
  }
  </script>
  
<script setup lang="ts">
import { RouterLink } from 'vue-router'
</script>

<template>
  <footer class="bg-text text-white py-12 text-center">
    <h2 class="text-4xl font-bold mb-4">Elite<span class="text-gray-400">Booking</span></h2>

    <nav class="mt-6 flex justify-center space-x-6 text-sm md:text-base">
      <RouterLink
        to="/"
        class="hover:underline transition-all duration-300 hover:text-blue-400"
        >Trang chủ</RouterLink
      >
      <RouterLink
        to="/hotels"
        class="hover:underline transition-all duration-300 hover:text-blue-400"
        >Khách sạn</RouterLink
      >
      <RouterLink
        to="/about"
        class="hover:underline transition-all duration-300 hover:text-blue-400"
        >Giới thiệu</RouterLink
      >
      <RouterLink
        to="/contact"
        class="hover:underline transition-all duration-300 hover:text-blue-400"
        >Liên hệ</RouterLink
      >
    </nav>

    <div class="mt-6 flex justify-center space-x-4 text-3xl">
      <a
        href="#"
        class="hover:text-blue-400 transition-all duration-300"
      >
        <i class="fab fa-facebook"></i>
      </a>
      <a
        href="#"
        class="hover:text-blue-400 transition-all duration-300"
      >
        <i class="fab fa-twitter"></i>
      </a>
      <a
        href="#"
        class="hover:text-blue-400 transition-all duration-300"
      >
        <i class="fab fa-instagram"></i>
      </a>
    </div>

    <p class="mt-6 text-sm text-gray-400">© 2025 EliteBooking. All rights reserved.</p>
  </footer>
</template>

<style scoped>


/* Tiêu đề footer */
h2 {
  font-size: 2.5rem;
  letter-spacing: 1px;
  color: white;
}

/* Nav Links */
nav a {
  color: #e2e8f0;
  font-weight: 500;
  transition:
    color 0.3s ease,
    transform 0.3s ease;
}

nav a:hover {
  color: #4fd1c5; /* Màu hover xanh mát */
  transform: translateY(-2px); /* Tạo hiệu ứng nổi */
}

/* Social Media Icons */
footer .fab {
  transition:
    color 0.3s ease,
    transform 0.3s ease;
}

footer .fab:hover {
  color: #4fd1c5; /* Màu xanh mát cho biểu tượng mạng xã hội */
  transform: translateY(-2px); /* Tạo hiệu ứng nổi cho biểu tượng */
}

/* Copyright Text */
p {
  color: #cbd5e0; /* Màu chữ xám nhạt */
  font-size: 0.875rem;
}
</style>

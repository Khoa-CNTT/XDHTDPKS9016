<script setup lang="ts">
import { RouterLink } from 'vue-router';

</script>

<template>
    <footer class="bg-text text-white py-8 text-center">
  <h2 class="text-xl text-white font-bold">Elite<span class="text-gray-300">Booking</span></h2>
  
  <nav class="mt-4 flex justify-center space-x-6 text-sm md:text-base">
    <RouterLink to="/" href="#" class="hover:underline">Trang chủ</RouterLink>
    <RouterLink to="/hotels" href="#" class="hover:underline">Khách sạn</RouterLink>
    <RouterLink to="/about" href="#" class="hover:underline">Giới thiệu</RouterLink>
    <RouterLink to="/contact" href="#" class="hover:underline">Liên hệ</RouterLink>
  </nav>

  <div class="mt-6 flex justify-center space-x-4 text-2xl">
    <a href="#" class="hover:text-gray-300"><i class="fab fa-facebook"></i></a>
    <a href="#" class="hover:text-gray-300"><i class="fab fa-twitter"></i></a>
    <a href="#" class="hover:text-gray-300"><i class="fab fa-instagram"></i></a>
  </div>

  <p class="mt-4 text-sm text-gray-300">© 2025 EliteBooking. All rights reserved.</p>
</footer>

  </template>
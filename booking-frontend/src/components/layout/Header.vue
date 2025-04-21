<template>
    <div>
        <header class="bg-white shadow-md py-5 px-6 mx-auto flex justify-between ">
            <div class="container mx-auto flex justify-between items-center">
                <div class="text-2xl font-bold text-text">Elite<span class="text-gray-400">Booking</span></div>
                <nav class="flex-1 flex justify-center space-x-6 mb-100">
                    <RouterLink to="/" class="text-gray-600 hover:text-blue-600"
                        active-class="text-blue-700 font-semibold" exact-active-class="text-blue-700 font-semibold">
                        Trang chủ
                    </RouterLink>
                    <RouterLink to="/hotels" class="text-gray-600 hover:text-blue-600"
                        active-class="text-blue-700 font-semibold" exact-active-class="text-blue-700 font-semibold">
                        Khách sạn
                    </RouterLink>
                    <RouterLink to="/about" class="text-gray-600 hover:text-blue-600"
                        active-class="text-blue-700 font-semibold" exact-active-class="text-blue-700 font-semibold">
                        Giới thiệu
                    </RouterLink>
                    <RouterLink to="/contact" class="text-gray-600 hover:text-blue-600"
                        active-class="text-blue-700 font-semibold" exact-active-class="text-blue-700 font-semibold">
                        Liên hệ
                    </RouterLink>
                </nav>
                <!-- Hiển thị avatar và tên người dùng nếu đã đăng nhập -->
                <div v-if="authStore.user" class="flex items-center space-x-4">
                    <img :src="authStore.user.avatar" alt="Avatar" class="w-8 h-8 rounded-full" />
                    <span class="text-gray-700">{{ authStore.user.name }}</span>
                </div>

                <!-- Hiển thị nút login nếu chưa đăng nhập -->
                <div v-else class="flex-1 flex justify-end">
                    <RouterLink to="/login">
                        <button class="bg-text text-white px-4 py-2 rounded-lg hover:bg-blue-700">Login</button>
                    </RouterLink>
                </div>
            </div>

        </header>
    </div>
</template>
<script setup>
import { ref } from 'vue';
import { RouterLink } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import login from '@/pages/auth/Login.vue';
import Avatar from '../base/Avatar.vue';
import { defineStore } from 'pinia'
const authStore = useAuthStore();
const isLoginOpen = ref(false);
</script>
<template>
<div>
    <header class="bg-white shadow-md py-5 px-6 mx-auto flex justify-between ">
        <div class="container mx-auto flex justify-between items-center">
            <div class="text-2xl font-bold text-text">Elite<span class="text-gray-400">Booking</span></div>
            <nav class="flex-1 flex justify-center space-x-6 mb-100">
                <RouterLink to="/" class="text-gray-600 hover:text-blue-600">Home</RouterLink>
                <RouterLink to="/hotels" class="text-gray-600 hover:text-blue-600">Hotels</RouterLink>
                <RouterLink to="/rooms" class="text-gray-600 hover:text-blue-600">Rooms</RouterLink>
                <RouterLink to="/about" class="text-gray-600 hover:text-blue-600">About</RouterLink>
                <RouterLink to="/contact" class="text-gray-600 hover:text-blue-600">Contact</RouterLink>
            </nav>
            <div class="flex-1 flex justify-end">
                <button @click="isLoginOpen = true" class="bg-text text-white px-4 py-2 rounded-lg hover:bg-blue-700">Login</button>
            </div>

        </div>
 
    </header>
    <login v-if="isLoginOpen" @close="isLoginOpen = false" />
</div>
</template>
<script setup>
import { ref } from 'vue';
import { RouterLink } from 'vue-router';
import login from '@/pages/auth/login.vue';

const isLoginOpen = ref(false);
</script>
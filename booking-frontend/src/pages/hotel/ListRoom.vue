<template>
    <div class="bg-gradient-to-r from-gray-100 to-blue-100 py-1">
        <div class="container mx-auto px-4 md:px-8">
            <div class="flex flex-col md:flex-row gap-8 border border-gray-300 p-6 rounded-xl shadow-lg">
                <!-- Cột bên trái -->
                <div class="md:w-1/3 bg-white rounded-xl shadow-md p-4">
                    <img src="https://via.placeholder.com/400x250" alt="Main room image" class="rounded-lg w-full" />
                    <div class="flex gap-2 mt-3">
                        <img src="https://via.placeholder.com/100x70" alt="Sub image 1" class="rounded-md w-1/2" />
                        <img src="https://via.placeholder.com/100x70" alt="Sub image 2" class="rounded-md w-1/2" />
                    </div>
                    <a href="#" class="text-blue-600 text-sm mt-2 inline-block">📷 Xem ảnh và chi tiết</a>

                    <h4 class="text-lg font-semibold mt-4 mb-2">🛏️ Loại phòng</h4>
                    <div class="mb-4 text-sm">
                        <strong class="block mb-1">Ưu tiên giường (nếu có):</strong>
                        <label class="block"><input type="radio" name="bed" class="mr-1" /> 1 giường lớn</label>
                        <label class="block"><input type="radio" name="bed" class="mr-1" /> 2 giường đơn</label>
                    </div>

                    <ul class="text-sm text-gray-700 space-y-1">
                        <li>📏 Diện tích phòng: 34 m²</li>
                        <li>🌇 Hướng Thành phố</li>
                        <li>🚭 Không hút thuốc</li>
                        <li>🛁 Phòng tắm vòi sen & bồn tắm</li>
                        <li><a href="#" class="text-blue-600 hover:underline">Các tiện ích khác</a></li>
                    </ul>
                </div>

                <!-- Cột bên phải -->
                <div class="md:w-2/3 space-y-6">
                    <BookingCard v-for="n in 2" :key="n" />
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import BookingCard from './BookingCard.vue'
</script>

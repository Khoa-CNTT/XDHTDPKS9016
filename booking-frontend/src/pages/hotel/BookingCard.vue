<template>
  <div class="flex flex-col sm:flex-row border border-gray-300 rounded-lg overflow-hidden bg-white shadow-sm">
    <!-- Thông tin chính -->
    <div class="sm:w-1/3 p-4 border-b sm:border-b-0 sm:border-r border-gray-300">
      <strong class="block mb-2">Giá thấp nhất!</strong>
      <ul class="list-none space-y-1 text-sm">
        <li>✔ Có bữa sáng tuyệt hảo (519.481 ₫ /người)</li>
        <li>✘ Không hoàn tiền (Giá thấp!)</li>
        <li>✔ Đặt và trả tiền ngay</li>
      </ul>
    </div>

    <!-- Biểu tượng -->
    <div class="sm:w-1/12 flex items-center justify-center text-xl border-b sm:border-b-0 sm:border-r border-gray-300">
      👥 ℹ️
    </div>

    <!-- Giá -->
    <div class="sm:w-1/4 p-4 text-red-700 border-b sm:border-b-0 sm:border-r border-gray-300">
      <div class="bg-red-100 text-red-600 text-sm font-bold p-1 mb-1">
        📍 Tuyệt quá! Giá rẻ nhất từ trước đến giờ đây
      </div>
      <div class="line-through text-gray-600 text-sm">
        4.299.134 ₫ <span class="text-red-500 ml-1">-63%</span>
      </div>
      <div class="text-xl font-bold">1.596.555 ₫</div>
      <div class="text-xs text-gray-500">Giá mỗi đêm chưa gồm thuế và phí</div>
    </div>

    <!-- Số lượng -->
    <div class="sm:w-1/12 flex items-center justify-center border-b sm:border-b-0 sm:border-r border-gray-300">
      <input type="number" v-model="quantity" min="1" class="w-12 p-1 border rounded" />
    </div>

    <!-- Nút đặt -->
    <div class="sm:w-1/4 flex flex-col items-center justify-center px-4 py-2">
      <button class="bg-blue-600 text-white px-4 py-2 rounded mb-1 w-full">Đặt ngay</button>
      <div class="text-green-600 text-sm font-semibold">Còn ít phòng</div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const quantity = ref(1)
</script>

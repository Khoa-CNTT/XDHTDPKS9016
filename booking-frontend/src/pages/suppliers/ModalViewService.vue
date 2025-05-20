<template>
  <div
    class="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center z-50"
    @click="$emit('close')"
  >
    <div
      class="bg-white rounded-2xl p-6 w-full max-w-md shadow-xl relative"
      @click.stop
    >
      <!-- Nút đóng -->
      <button
        @click="$emit('close')"
        class="absolute top-3 right-3 text-gray-500 hover:text-gray-800 text-xl"
        aria-label="Đóng"
      >
        &times;
      </button>

      <!-- Tiêu đề -->
      <h3 class="text-2xl font-bold text-center text-gray-800 mb-6">Chi tiết dịch vụ</h3>

      <!-- Nội dung -->
      <div class="space-y-4 text-gray-700">
        <div>
          <span class="font-semibold">Tên dịch vụ:</span>
          <p class="ml-2">{{ service.name }}</p>
        </div>
        <div>
          <span class="font-semibold">Giá:</span>
          <p class="ml-2">{{ service.price.toLocaleString() }} đ</p>
        </div>
        <div>
          <span class="font-semibold">Mô tả:</span>
          <p class="ml-2 whitespace-pre-line">{{ service.description }}</p>
        </div>
      </div>

      <!-- Nút đóng -->
      <div class="flex justify-center mt-6">
        <button
          @click="$emit('close')"
          class="px-5 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition"
        >
          Đóng
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { GetService } from '@/types/supplier'

defineProps<{
  service: GetService
}>()
</script>

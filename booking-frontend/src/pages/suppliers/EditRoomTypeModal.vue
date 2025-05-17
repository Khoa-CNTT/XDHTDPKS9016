<template>
  <div class="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center z-50">
    <div class="bg-white p-6 rounded-lg w-full max-w-lg shadow-lg">
      <h2 class="text-xl font-semibold mb-4 text-blue-700">Chỉnh sửa loại phòng</h2>
      
      <form >
        <!-- Tên loại phòng -->
        <div class="mb-4">
          <label class="block font-medium mb-1">Tên loại phòng</label>
          <input  type="text" class="w-full border rounded px-3 py-2" required />
        </div>

        <!-- Số lượng -->
        <div class="mb-4">
          <label class="block font-medium mb-1">Số lượng</label>
          <input  type="number" class="w-full border rounded px-3 py-2" required />
        </div>
      <!-- Ảnh -->
        <div class="mb-4">
          <label class="block font-medium mb-1">Hình ảnh</label>
          <input type="file" accept="image/*"  class="w-full" />
          <div  class="mt-2">
            <img alt="Preview" class="h-32 rounded object-cover" />
          </div>
        </div>
        <!-- Mô tả -->
        <div class="mb-4">
          <label class="block font-medium mb-1">Mô tả</label>
          <textarea  class="w-full border rounded px-3 py-2"></textarea>
        </div>

  

        <!-- Nút hành động -->
        <div class="flex justify-end gap-3 mt-4">
          <button type="button" @click="$emit('close')" class="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400">Hủy</button>
          <button type="submit" class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">Lưu thay đổi</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, reactive } from 'vue'
import type { RoomTypeSummary } from '@/types/supplier'
import { updateRoomTypeApi } from '@/services/supplier'
import { toast } from 'vue3-toastify'

const props = defineProps<{
  roomType: RoomTypeSummary | null
}>()

const emit = defineEmits(['close', 'updated'])




</script>

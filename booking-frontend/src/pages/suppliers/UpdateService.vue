<template>
  <div
    class="fixed inset-0 bg-gray-500 bg-opacity-50 flex justify-center items-center z-50"
    @click="$emit('close')"
  >
    <div
      class="bg-white p-6 rounded-lg w-96 shadow-lg relative"
      @click.stop
    >
      <!-- Nút đóng -->
      <button
        class="absolute top-2 right-2 text-xl text-gray-600 hover:text-gray-800"
        @click="$emit('close')"
        aria-label="Đóng modal"
      >
        ×
      </button>

      <h2 class="text-xl font-bold text-center mb-6 text-gray-800">Cập nhật dịch vụ</h2>

      <form
        @submit.prevent="submitForm"
        class="space-y-4"
      >
        <!-- Tên dịch vụ -->
        <div>
          <label
            for="service_name"
            class="block text-sm font-semibold text-gray-700 mb-1"
            >Tên dịch vụ</label
          >
          <input
            v-model="form.service_name"
            type="text"
            id="service_name"
            class="w-full p-3 border rounded-md"
            required
          />
        </div>

        <!-- Giá dịch vụ -->
        <div>
          <label
            for="service_price"
            class="block text-sm font-semibold text-gray-700 mb-1"
            >Giá</label
          >
          <input
            v-model.number="form.service_price"
            type="number"
            id="service_price"
            class="w-full p-3 border rounded-md"
            required
          />
        </div>

        <!-- Mô tả -->
        <div>
          <label
            for="description"
            class="block text-sm font-semibold text-gray-700 mb-1"
            >Mô tả chi tiết</label
          >
          <textarea
            v-model="form.description"
            id="description"
            class="w-full p-3 border rounded-md"
            rows="4"
            required
          ></textarea>
        </div>

        <!-- Nút hành động -->
        <div class="flex justify-end gap-4 pt-2">
          <button
            type="button"
            @click="$emit('close')"
            class="w-1/3 py-2 bg-gray-300 text-gray-700 rounded hover:bg-gray-400 transition"
          >
            Hủy
          </button>
          <button
            type="submit"
            class="w-1/3 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition"
          >
            Lưu
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { updateServiceApi } from '@/services/supplier'
import type { GetService } from '@/types/supplier'

const props = defineProps<{
  service: GetService
}>()

const emit = defineEmits<{
  (e: 'close'): void
  (e: 'serviceUpdated'): void
}>()

const form = ref({
  service_name: '',
  service_price: 0,
  description: '',
})

// Load dữ liệu ban đầu từ props.service
watch(
  () => props.service,
  (newVal) => {
    if (newVal) {
      form.value = {
        service_name: newVal.name || '',
        service_price: newVal.price || 0,
        description: newVal.description || '',
      }
    }
  },
  { immediate: true },
)

// Gửi form cập nhật
const submitForm = async () => {
  try {
    await updateServiceApi(props.service.id, form.value)
    emit('serviceUpdated')
    emit('close')
  } catch (error) {
    console.error('Lỗi khi cập nhật dịch vụ:', error)
  }
}
</script>

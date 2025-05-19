<template>
  <div
    class="fixed inset-0 bg-gray-500 bg-opacity-50 flex justify-center items-center"
    @click="$emit('close')"
  >
    <div
      class="bg-white p-6 rounded-lg w-96 shadow-lg relative"
      @click.stop
    >
      <button
        class="absolute top-2 right-2 text-xl"
        @click="$emit('close')"
        aria-label="Đóng modal"
      >
        ×
      </button>
      <h2 class="text-xl font-bold text-center mb-4">Thêm Dịch Vụ Mới</h2>
      <form @submit.prevent="submitForm">
        <div class="mb-4">
          <label
            for="service_name"
            class="block text-sm font-semibold text-gray-700"
          >
            Tên dịch vụ
          </label>
          <input
            v-model="form.service_name"
            type="text"
            id="service_name"
            class="w-full p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-400"
            required
            placeholder="Nhập tên dịch vụ"
          />
        </div>

        <div class="mb-4">
          <label
            for="service_price"
            class="block text-sm font-semibold text-gray-700"
          >
            Giá
          </label>
          <input
            v-model.number="form.service_price"
            type="number"
            id="service_price"
            class="w-full p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-400"
            required
            placeholder="Nhập giá dịch vụ"
          />
        </div>

        <div class="mb-4">
          <label
            for="description"
            class="block text-sm font-semibold text-gray-700"
          >
            Mô tả chi tiết
          </label>
          <textarea
            v-model="form.description"
            id="description"
            class="w-full p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-400"
            required
            placeholder="Nhập mô tả dịch vụ"
          ></textarea>
        </div>

        <div class="flex justify-end gap-4">
          <button
            @click="$emit('close')"
            type="button"
            class="w-1/3 py-2 px-4 bg-gray-300 text-gray-700 rounded-md hover:bg-gray-400 focus:outline-none focus:ring-2 focus:ring-gray-500"
          >
            Hủy
          </button>
          <button
            type="submit"
            class="w-1/3 py-2 px-4 bg-green-600 text-white rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500"
          >
            Thêm
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { createServiceApi } from '@/services/supplier'
import { NewService } from '@/types/supplier'
import { toast } from 'vue3-toastify'

const emit = defineEmits<{
  (e: 'close'): void
  (e: 'serviceAdded'): void
}>()

const form = ref<NewService>({
  service_name: '',
  service_price: 0,
  description: '',
})

// Gửi form tạo dịch vụ
const submitForm = async () => {
  try {
    console.log('Dữ liệu form gửi đi:', form.value)
    await createServiceApi(form.value)
    toast.success('Thêm dịch vụ thành công!')
    emit('serviceAdded')
    emit('close')
  } catch (error) {
    console.error('Lỗi khi tạo dịch vụ mới:', error)
    toast.error('Tạo dịch vụ thất bại!')
  }
}
</script>

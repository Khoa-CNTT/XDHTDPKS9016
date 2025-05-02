<!-- components/AddSupplierPopup.vue -->
<template>
    <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white p-6 rounded-lg w-[400px]">
        <h2 class="text-xl font-semibold mb-4">Thêm nhà cung cấp</h2>
        <form @submit.prevent="handleSubmit">
          <input v-model="form.name" placeholder="Tên nhà cung cấp" class="w-full mb-2 p-2 border rounded" />
          <input v-model="form.address" placeholder="Địa chỉ" class="w-full mb-2 p-2 border rounded" />
          <input v-model="form.hotline" placeholder="Số điện thoại" class="w-full mb-2 p-2 border rounded" />
          <textarea v-model="form.description" placeholder="Mô tả" class="w-full mb-2 p-2 border rounded"></textarea>
          <div class="flex justify-end gap-2 mt-4">
            <button type="button" class="px-4 py-2 bg-gray-300 rounded" @click="$emit('close')">Huỷ</button>
            <button type="submit" class="px-4 py-2 bg-green-500 text-white rounded">Tạo</button>
          </div>
        </form>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref } from 'vue'
  import { createSupplierApi } from '@/services/admin'
  
  const emit = defineEmits(['close', 'created'])
  
  const form = ref({
    name: '',
    image: null,
    address: '',
    hotline: '',
    description: null
  })
  
  const handleSubmit = async () => {
    try {
      await createSupplierApi(form.value)
      emit('created')
      emit('close')
    } catch (error) {
      console.error('Lỗi khi tạo nhà cung cấp:', error)
    }
  }
  </script>
  
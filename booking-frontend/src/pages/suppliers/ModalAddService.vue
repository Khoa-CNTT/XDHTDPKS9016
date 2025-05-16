<template>
    <div class="fixed inset-0 bg-gray-500 bg-opacity-50 flex justify-center items-center" @click="$emit('close')">
      <div class="bg-white p-6 rounded-lg w-96 shadow-lg" @click.stop>
        <button class="absolute top-2 right-2 text-xl" @click="$emit('close')">×</button>
        <h2 class="text-xl font-bold text-center mb-4">Thêm Dịch Vụ Mới</h2>
        <form @submit.prevent="submitForm">
          <!-- Tên Dịch Vụ -->
          <div class="mb-4">
            <label for="service_name" class="block text-sm font-semibold text-gray-700">Tên dịch vụ</label>
            <input v-model="form.service_name" type="text" id="service_name"
              class="w-full p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-400"
              required placeholder="Nhập tên dịch vụ" />
          </div>
  
          <!-- Giá Dịch Vụ -->
          <div class="mb-4">
            <label for="service_price" class="block text-sm font-semibold text-gray-700">Giá</label>
            <input v-model="form.service_price" type="number" id="service_price"
              class="w-full p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-400"
              required placeholder="Nhập giá dịch vụ" />
          </div>
  
          <!-- Tải Ảnh -->
          <div class="mb-4">
            <label for="service_image" class="block text-sm font-semibold text-gray-700">Ảnh dịch vụ</label>
            <input @change="handleFileUpload" type="file" accept="image/*"
              class="w-full p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-400" />
            <!-- Hiển thị ảnh đã chọn -->
            <div v-if="form.service_image" class="mt-2">
              <img :src="form.service_image" alt="Ảnh đã chọn"  class="w-32 h-32 border rounded object-contain"/>
            </div>
          </div>
  
          <!-- Mô Tả Dịch Vụ -->
          <div class="mb-4">
            <label for="description" class="block text-sm font-semibold text-gray-700">Mô tả chi tiết</label>
            <textarea v-model="form.description" id="description"
              class="w-full p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-400"
              required placeholder="Nhập mô tả dịch vụ"></textarea>
          </div>
  
          <!-- Nút Hủy và Nút Thêm Dịch Vụ -->
          <div class="flex justify-end gap-4">
            <button @click="$emit('close')" type="button"
              class="w-1/3 py-2 px-4 bg-gray-300 text-gray-700 rounded-md hover:bg-gray-400 focus:outline-none focus:ring-2 focus:ring-gray-500">
              Hủy
            </button>
            <button type="submit"
              class="w-1/3 py-2 px-4 bg-green-600 text-white rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500">
              Thêm
            </button>
          </div>
        </form>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref } from 'vue';
  import { createServiceApi, uploadImageApi } from '@/services/supplier';
  import { AddService, Service } from '@/types/supplier';
  const emit = defineEmits<{
  (e: 'close'): void;
  (e: 'serviceAdded', service: Service): void;
}>();

  const form = ref<AddService>({
    service_name: '',
    service_price: 0,
    service_image: '',
    description: '',
  });

const handleFileUpload = async (event: Event) => {
  const target = event.target as HTMLInputElement;
  const file = target.files?.[0];
  if (file) {
    try {
      const uploadedUrl = await uploadImageApi(file);
      const fullImageUrl = `http://157.66.101.165:8080${uploadedUrl}`;
      form.value.service_image = fullImageUrl;
      console.log('Uploaded full image URL:', fullImageUrl);
    } catch (error) {
      console.error('Lỗi khi tải ảnh:', error);
    }
  }
};
  
  const submitForm = async () => {
    try {
      const service: Service = await createServiceApi(form.value);
      emit('serviceAdded', service);
      emit('close');
    } catch (error) {
      console.error('Lỗi khi tạo dịch vụ mới:', error);
    }
  };
  </script>
  
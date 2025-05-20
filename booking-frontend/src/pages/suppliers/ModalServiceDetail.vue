<template>
    <div v-if="service" class="fixed inset-0 bg-gray-400 bg-opacity-50 flex justify-center items-center z-50">
      <div class="bg-white p-6 rounded-lg w-11/12 max-w-lg shadow-lg animate-fadeIn relative">
        <!-- Nút tắt -->
        <button @click="close" class="absolute top-4 right-4 text-black-500 font-bold p-2 rounded-full focus:outline-none">
          X
        </button>
        <h3 class="text-2xl font-bold mb-4">Chi tiết dịch vụ</h3>
        <div class="space-y-4">
          <p><strong>Tên dịch vụ:</strong> {{ service.service_name }}</p>
          <p><strong>Giá:</strong> {{ service.service_price }}</p>
          <p><strong>Mô tả:</strong> {{ service.description }}</p>
          <div class="flex justify-center items-center">
          <p v-if="!service.service_image" class="text-gray-500 italic">Không có hình ảnh</p>
          <img
            v-else
            :src="fullImageUrl"
            alt="Hình ảnh dịch vụ"
            class="max-w-xs rounded-lg shadow-md"
          />
        </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import {computed} from 'vue';
  import { defineProps, defineEmits } from 'vue';
  import { Service } from '@/types/supplier';
  
  const props = defineProps({
    service: {
      type: Object as () => Service | null,
      required: true
    }
  });
  
  const emit = defineEmits(['close']);
  
  const close = () => {
    emit('close');
  };
  const fullImageUrl = computed(() => {
  if (!props.service?.service_image) return '';
  const imageUrl = props.service.service_image;
  return imageUrl.startsWith('http') ? imageUrl : `http://157.66.101.165:8080${imageUrl}`;
});
  </script>
  
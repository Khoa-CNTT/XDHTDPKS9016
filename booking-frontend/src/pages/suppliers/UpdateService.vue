<template>
  <div class="fixed inset-0 bg-gray-500 bg-opacity-50 flex justify-center items-center z-50" @click="$emit('close')">
    <div class="bg-white rounded-lg shadow-xl w-96 p-6 relative" @click.stop>
      <!-- Nút đóng modal -->
      <button @click="$emit('close')" class="absolute top-2 right-2 text-xl text-gray-600 hover:text-gray-800 focus:outline-none">
        &times;
      </button>

      <h2 class="text-2xl font-semibold text-center text-gray-800 mb-6">Cập nhật dịch vụ</h2>

      <!-- Tên dịch vụ -->
      <div class="mb-6">
        <label class="block text-sm font-medium text-gray-700 mb-2">Tên dịch vụ</label>
        <input v-model="form.service_name" type="text" class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Nhập tên dịch vụ" />
      </div>

      <!-- Giá dịch vụ -->
      <div class="mb-6">
        <label class="block text-sm font-medium text-gray-700 mb-2">Giá dịch vụ</label>
        <input v-model.number="form.service_price" type="number" class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Nhập giá" />
      </div>

      <!-- Mô tả dịch vụ -->
      <div class="mb-6">
        <label class="block text-sm font-medium text-gray-700 mb-2">Mô tả</label>
        <textarea v-model="form.description" class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" rows="4" placeholder="Nhập mô tả"></textarea>
      </div>

      <!-- Chọn ảnh -->
      <!-- <div class="mb-6">
        <label class="block text-sm font-medium text-gray-700 mb-2">Chọn ảnh</label>
        <input type="file" @change="handleFileChange" class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
        <div v-if="selectedImage" class="mt-2 text-sm text-gray-600">Đã chọn ảnh: {{ selectedImage.name }}</div>
      </div> -->

      <!-- Nút cập nhật -->
      <div class="flex justify-center">
        <button @click="handleUpdate" class="w-1/2 py-2 px-4 bg-blue-600 text-white rounded-lg hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500">
          Cập nhật dịch vụ
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { updateServiceApi } from '@/services/supplier';
import type { AddService, Service } from '@/types/supplier';

const props = defineProps<{
  service: Service | null;
}>();

const emits = defineEmits<{
  (e: 'close'): void;
  (e: 'updated'): void;
}>();

// Kiểm tra service trước khi sử dụng
const form = ref<AddService>({
  service_name: props.service?.service_name ?? '',
  service_price: props.service?.service_price ?? 0,
  service_image: props.service?.service_image ?? null,
  description: props.service?.description ?? '',
});

const successMessage = ref('');
const errorMessage = ref('');

const handleUpdate = async () => {
  if (!props.service) {
    errorMessage.value = 'Không có thông tin dịch vụ để cập nhật.';
    return; // Thoát sớm nếu service là null
  }

  try {
    await updateServiceApi(props.service.service_id, form.value);
    successMessage.value = 'Cập nhật thành công!';
    emits('updated');
    emits('close');
  } catch (err) {
    errorMessage.value = 'Cập nhật thất bại!';
  }
};
</script>


<style scoped>
.input {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
</style>

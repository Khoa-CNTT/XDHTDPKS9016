<template>
  <div class="fixed inset-0 bg-gray-500 bg-opacity-50 flex justify-center items-center z-50" @click="$emit('close')">
    <div class="bg-white rounded-lg shadow-xl w-96 p-6 relative" @click.stop>
      <button @click="$emit('close')" class="absolute top-2 right-2 text-xl text-gray-600 hover:text-gray-800">
        &times;
      </button>

      <h2 class="text-2xl font-semibold text-center text-gray-800 mb-6">Cập nhật dịch vụ</h2>

      <div class="mb-6">
        <label class="block text-sm font-medium text-gray-700 mb-2">Tên dịch vụ</label>
        <input v-model="name" type="text"
          class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          placeholder="Nhập tên dịch vụ" />
      </div>

      <div class="mb-6">
        <label class="block text-sm font-medium text-gray-700 mb-2">Giá dịch vụ</label>
        <input v-model="price" type="number"
          class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          placeholder="Nhập giá" />
      </div>

      <div class="mb-6">
        <label class="block text-sm font-medium text-gray-700 mb-2">Mô tả</label>
        <textarea v-model="description"
          class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          rows="4" placeholder="Nhập mô tả"></textarea>
      </div>

      <div class="mb-6">
        <label class="block text-sm font-medium text-gray-700 mb-2">Chọn ảnh</label>
        <input type="file" @change="handleImageUpload"
          class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
        <div class="mt-2 text-sm text-gray-600">Đã chọn ảnh: {{ imageName }}</div>
        <div v-if="imageUrl" class="mt-2">
          <img :src="imageUrl" alt="Ảnh dịch vụ" class="w-full rounded-md" />
        </div>
      </div>

      <div class="flex justify-center">
        <button
          @click="submitUpdate"
          class="w-1/2 py-2 px-4 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
          Cập nhật dịch vụ
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { uploadImageApi, updateServiceApi } from '@/services/supplier';
import { toast } from 'vue3-toastify';

const props = defineProps<{
  service: {
    service_id: number;
    service_name: string;
    service_price: number;
    service_image: string;
    description: string;
  };
}>();
const emit = defineEmits(['close', 'serviceUpdated']);

const name = ref(props.service.service_name);
const price = ref(props.service.service_price);
const description = ref(props.service.description);
const imageName = ref('');
const imageUrl = ref(props.service.service_image || '');

watch(() => props.service, (newVal) => {
  name.value = newVal.service_name;
  price.value = newVal.service_price;
  description.value = newVal.description;
  imageUrl.value = newVal.service_image || '';
});

const handleImageUpload = async (e: Event) => {
  const target = e.target as HTMLInputElement;
  const file = target.files?.[0];
  if (!file) return;

  imageName.value = file.name;

  try {
    // Gọi uploadImageApi với file trực tiếp
    const res = await uploadImageApi(file);

    // res có dạng { url: string }, nối url đầy đủ
    imageUrl.value = `http://157.66.101.165:8080${res}`;

    toast.success('Tải ảnh lên thành công!');
  } catch (error) {
    toast.error('Lỗi khi tải ảnh!');
    console.error(error);
  }
};


const submitUpdate = async () => {
  try {
    await updateServiceApi(props.service.service_id, {
      service_name: name.value,
      service_price: price.value,
      service_image: imageUrl.value,
      description: description.value,
    });

    toast.success('Cập nhật dịch vụ thành công!');
    emit('serviceUpdated');
  } catch (error) {
    toast.error('Lỗi khi cập nhật dịch vụ!');
    console.error(error);
  }
};
</script>

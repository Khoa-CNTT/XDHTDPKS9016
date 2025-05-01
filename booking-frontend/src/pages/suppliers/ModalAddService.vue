<template>
    <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white rounded-lg w-[400px] p-6">
            <h2 class="text-lg font-bold mb-4">Thêm dịch vụ mới</h2>
            <form >
                <div class="mb-4">
                    <label class="block text-sm font-medium">Tên dịch vụ</label>
                    <input v-model="form.service_name" type="text" class="w-full border rounded px-3 py-2 mt-1"
                        required />
                </div>
                <div class="mb-4">
                    <label class="block text-sm font-medium">Giá</label>
                    <input v-model.number="form.service_price" type="number"
                        class="w-full border rounded px-3 py-2 mt-1" required />
                </div>
                <div class="mb-4">
                    <label class="block text-sm font-medium">Hình ảnh</label>
                    <input type="file" @change="handleImageChange" class="w-full border rounded px-3 py-2 mt-1" />
      
                    <div v-if="imagePreview" class="mt-2">
                        <img :src="imagePreview" alt="Preview" class="w-full h-auto"/>
                    </div>
                </div>
                <div class="mb-4">
                    <label class="block text-sm font-medium">Mô tả</label>
                    <textarea v-model="form.description" class="w-full border rounded px-3 py-2 mt-1" rows="3"
                        required></textarea>
                </div>
                <div class="flex justify-end gap-2">
                    <button type="button" @click="$emit('close')" class="px-3 py-2 bg-gray-300 rounded">Hủy</button>
                    <button type="submit" class="px-3 py-2 bg-green-600 text-white rounded">Tạo</button>
                </div>
            </form>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { createServiceApi } from '@/services/supplier'

const emit = defineEmits(['close', 'created'])

const form = ref({
  service_name: '',
  service_price: 0,
  service_image: null as File | null,
  description: ''
})

const imagePreview = ref<string | null>(null)

const handleImageChange = (event: Event) => {
  const target = event.target as HTMLInputElement;
  const file = target.files?.[0];
  if (file) {
    form.value.service_image = file;
    const reader = new FileReader();
    reader.onload = () => {
      imagePreview.value = reader.result as string;
    }
    reader.readAsDataURL(file); 
  }
}


// const handleSubmit = async () => {
//   try {
//     const formData = new FormData();
//     formData.append('service_name', form.value.service_name);
//     formData.append('service_price', String(form.value.service_price));
//     formData.append('description', form.value.description);
//     if (form.value.service_image) {
//       formData.append('service_image', form.value.service_image);
//     }

//     await createServiceApi(formData);
//     emit('created');
//     emit('close');
//   } catch (error) {
//     console.error('Lỗi khi tạo dịch vụ:', error);
//   }
// };
</script>

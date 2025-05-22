<template>
  <div class="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-50">
    <div class="bg-white p-6 rounded-lg w-full max-w-lg shadow-lg relative">
      <h2 class="text-xl font-bold mb-4 text-blue-700">Cập nhật thông tin khách sạn</h2>

      <form @submit.prevent="handleSubmit" class="space-y-4">
        <div>
          <label class="font-medium">Tên khách sạn:</label>
          <input v-model="form.name" class="input" />
        </div>

        <!-- Chọn ảnh từ máy -->
        <div>
          <label class="font-medium">Hình ảnh:</label>
          <input type="file" accept="image/*" @change="handleFileUpload" class="input" />
          <div v-if="previewImage" class="mt-2">
            <img :src="previewImage" alt="Preview" class="w-32 h-32 object-cover rounded border" />
          </div>
        </div>

        <div>
          <label class="font-medium">Địa chỉ:</label>
          <input v-model="form.address" class="input" />
        </div>
        <div>
          <label class="font-medium">Hotline:</label>
          <input v-model="form.hotline" class="input" />
        </div>
        <div>
          <label class="font-medium">Mô tả:</label>
          <textarea v-model="form.description" class="input" rows="3" />
        </div>

        <div class="flex justify-end gap-2">
          <button type="button" @click="emit('close')" class="px-4 py-2 bg-gray-400 rounded text-white">Đóng</button>
          <button type="submit" class="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded">Lưu</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { updateHotelInfoApi, uploadImageApi } from '@/services/supplier'
import { toast } from 'vue3-toastify'
import 'vue3-toastify/dist/index.css'
  import {BASE_URL} from '@/utils/imageHelper'
const emit = defineEmits(['close', 'updated'])
const props = defineProps<{ hotel: any }>()

// Base URL để nối trước đường dẫn tương đối
const baseUrl = 'http://localhost:8080'

const form = ref({ ...props.hotel })
const previewImage = ref(form.value.image
  ? `${baseUrl}${props.hotel.image.trim()}`
  : '')
const handleFileUpload = async (event: Event) => {
  const target = event.target as HTMLInputElement
  const file = target.files?.[0]
  if (!file) return

  try {
    const uploadedPath = await uploadImageApi(file)
    console.log('Server returned path:=========', uploadedPath)
    form.value.image = uploadedPath.trim()
    previewImage.value = form.value.image

  } catch (err) {
    console.error('Lỗi upload ảnh:', err)
    toast.error('Upload ảnh thất bại')
  }
}


const handleSubmit = async () => {
  try {
    const payload = {
      idHotel: form.value.idHotel,
      name: form.value.name,
      image: form.value.image.startsWith('http')
        ? form.value.image
        : `${baseUrl}${form.value.image}`,
      address: form.value.address,
      hotline: form.value.hotline,
      description: form.value.description
    }
    await updateHotelInfoApi(payload)

    toast.success('Cập nhật thành công!')
    emit('updated')
    emit('close')
  } catch (err) {
    console.error('Lỗi khi cập nhật:', err)
    toast.error('Cập nhật thất bại!')
  }
}
</script>


<style scoped>
.input {
  @apply w-full border border-gray-300 rounded px-3 py-2;
}
</style>

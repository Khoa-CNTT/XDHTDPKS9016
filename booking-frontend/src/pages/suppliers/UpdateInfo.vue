<template>
  <div class="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-50">
    <div class="bg-white p-6 rounded-lg w-full max-w-lg shadow-lg relative">
      <h2 class="text-xl font-bold mb-4 text-blue-700">Cập nhật thông tin khách sạn</h2>

      <form @submit.prevent="handleSubmit" class="space-y-4">
        <!-- Tên khách sạn -->
        <div>
          <label class="font-medium">Tên khách sạn:</label>
          <input v-model="form.name" class="input" />
        </div>

        <!-- Chọn ảnh -->
        <div>
          <label class="font-medium">Hình ảnh:</label>
          <input type="file" accept="image/*" @change="handleFileUpload" class="input" />
          <div v-if="previewImage" class="mt-2">
            <img :src="previewImage" alt="Preview" class="w-32 h-32 object-cover rounded border" />
          </div>
        </div>

        <!-- Địa chỉ -->
        <div>
          <label class="font-medium">Địa chỉ:</label>
          <input v-model="form.address" class="input" />
        </div>

        <!-- Hotline -->
        <div>
          <label class="font-medium">Hotline:</label>
          <input v-model="form.hotline" class="input" />
        </div>

        <!-- Mô tả -->
        <div>
          <label class="font-medium">Mô tả:</label>
          <textarea v-model="form.description" class="input" rows="3" />
        </div>

        <!-- Nút hành động -->
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

const emit = defineEmits(['close', 'updated'])
const props = defineProps<{ hotel: any }>()

// Biến baseUrl nếu cần
const baseUrl = 'http://localhost:8080'

// Khởi tạo form từ props
const form = ref({ ...props.hotel })

// Preview ảnh nếu có ảnh sẵn
const previewImage = ref(
  form.value.image?.startsWith('http')
    ? form.value.image
    : form.value.image
    ? `${baseUrl}${form.value.image.trim()}`
    : ''
)

// Xử lý upload ảnh khi chọn ảnh mới
const handleFileUpload = async (event: Event) => {
  const target = event.target as HTMLInputElement
  const file = target.files?.[0]
  if (!file) return

  try {
    const uploadedPath = await uploadImageApi(file)
    
    form.value.image = uploadedPath.trim()
    previewImage.value = form.value.image.startsWith('http')
      ? form.value.image
      : `${baseUrl}${form.value.image}`
    toast.success('Tải ảnh lên thành công!', { autoClose: 5000, position: 'top-right' })
  } catch (err) {
    console.error('Lỗi upload ảnh:', err)
    toast.error('Upload ảnh thất bại', { autoClose: 5000, position: 'top-right' })
  }
}

// Gửi dữ liệu cập nhật
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

    toast.success('Cập nhật thành công!', { autoClose: 5000, position: 'top-right' })
    emit('updated')
    emit('close')
  } catch (err) {
    console.error('Lỗi khi cập nhật:', err)
    toast.error('Cập nhật thất bại!', { autoClose: 5000, position: 'top-right' })
  }
}
</script>

<style scoped>
.input {
  @apply w-full border border-gray-300 rounded px-3 py-2;
}
</style>

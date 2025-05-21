<template>
  <div v-if="isOpen" class="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
    <div class="bg-white p-6 rounded-md w-full max-w-xl">
      <h2 class="text-lg font-semibold mb-4">Thêm loại phòng mới</h2>
      <div class="grid grid-cols-2 gap-4">
        <div>
          <label class="block text-sm font-medium mb-1">Tên loại phòng</label>
          <input v-model="form.type_name" placeholder="Tên loại phòng" class="input" />
        </div>
        <div>
          <label class="block text-sm font-medium mb-1">Số giường</label>
          <input v-model.number="form.number_bed" type="number" placeholder="Số giường" class="input" />
        </div>
        <div>
          <label class="block text-sm font-medium mb-1">Số người tối đa</label>
          <input v-model.number="form.maximum_people" type="number" placeholder="Số người tối đa" class="input" />
        </div>
        <div>
          <label class="block text-sm font-medium mb-1">Giá</label>
          <input v-model.number="form.price" type="number" placeholder="Giá" class="input" />
        </div>
        <div class="col-span-2">
          <label class="block text-sm font-medium mb-1">Mô tả</label>
          <input v-model="form.description" placeholder="Mô tả" class="input" />
        </div>
        <div class="col-span-2">
          <label class="block text-sm font-medium mb-1">Ảnh</label>
          <input @change="handleFileUpload" type="file" accept="image/*" class="input" />
          <div v-if="previewImage" class="mt-2">
            <img :src="previewImage" alt="Ảnh đã chọn" class="w-32 h-auto border rounded" />
          </div>
        </div>
        <div class="hidden">
          <label class="block text-sm font-medium mb-1">Phòng trống</label>
          <input v-model.number="form.available_room" type="number" placeholder="Phòng trống" class="input" />
        </div>

      </div>

      <div class="flex justify-end mt-4 gap-2">
        <button @click="close" class="px-3 py-1 bg-gray-400 rounded text-white">Hủy</button>
        <button @click="submit" class="px-3 py-1 bg-green-600 rounded text-white">Tạo</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, defineEmits, defineProps } from 'vue'
import { createRoomTypeApi, uploadImageApi } from '@/services/supplier'
import { RoomTypeDetail } from '@/types/supplier'
  import {BASE_URL} from '@/utils/imageHelper'
const emit = defineEmits(['close', 'created', 'added'])
const props = defineProps<{ isOpen: boolean, fetchRoomTypes: Function }>()
const form = ref<RoomTypeDetail>({
  type_name: '',
  number_bed: 1,
  maximum_people: 1,
  price: 0,
  description: '',
  room_image: '',
  available_room: 0
})

// ✅ Hàm upload ảnh và lưu URL
const previewImage = ref<string | null>(null)

const handleFileUpload = async (event: Event) => {
  const target = event.target as HTMLInputElement;
  const file = target.files?.[0];
  if (file) {
    try {
      const uploadedFilename = await uploadImageApi(file)
      const baseUrl = 'http://localhost:8080'
      previewImage.value = baseUrl + uploadedFilename
      form.value.room_image = uploadedFilename
      console.log('URL hiển thị ảnh:', previewImage.value)
    } catch (error) {
      console.error('Lỗi upload ảnh:', error)
    }
  }
}

const close = () => {
  emit('close')
}

import { toast } from 'vue3-toastify'

const submit = async () => {
  try {
    console.log('Dữ liệu gửi lên:', form.value)
    const res = await createRoomTypeApi(form.value)
    console.log('hihi', res)

    toast.success('Tạo loại phòng thành công!', {
      autoClose: 5000,
      position: 'top-right'
    })

    emit('created', res)
    props.fetchRoomTypes()
    close()
  } catch (e) {
    console.error('Tạo thất bại:', e)

    toast.error('Tạo loại phòng thất bại!', {
      autoClose: 5000,
      position: 'top-right'
    })
  }
}

</script>

<style scoped>
.input {
  border: 1px solid #ccc;
  padding: 6px 10px;
  border-radius: 4px;
  width: 100%;
}
</style>
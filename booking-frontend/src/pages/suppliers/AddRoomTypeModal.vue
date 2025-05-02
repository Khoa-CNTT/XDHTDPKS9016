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
            <div v-if="form.room_image" class="mt-2">
              <img :src="form.room_image" alt="Ảnh đã chọn" class="w-32 h-auto border rounded" />
            </div>
          </div>
          <div>
            <label class="block text-sm font-medium mb-1">Phòng trống</label>
            <input v-model.number="form.available_room" type="number" placeholder="Phòng trống" class="input" />
          </div>
          <div>
            <label class="block text-sm font-medium mb-1">Trạng thái</label>
            <select v-model="form.status" class="input">
              <option value="ACTIVE">ACTIVE</option>
              <option value="INACTIVE">INACTIVE</option>
            </select>
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
  
  const emit = defineEmits(['close', 'created','added'])
  const props = defineProps<{ isOpen: boolean }>()
  
  const form = ref({
    type_name: '',
    number_bed: 1,
    maximum_people: 1,
    price: 0,
    description: '',
    room_image: '',
    available_room: 1,
    status: ''
  })
  
  // ✅ Hàm upload ảnh và lưu URL
  const handleFileUpload = async (event: Event) => {
    const target = event.target as HTMLInputElement;
    const file = target.files?.[0];
    if (file) {
      try {
        const uploadedUrl = await uploadImageApi(file)
        form.value.room_image = uploadedUrl
        console.log('Uploaded image URL:', uploadedUrl)
      } catch (error) {
        console.error('Lỗi upload ảnh:', error)
      }
    }
  }
  
  const close = () => {
    emit('close')
  }
  
  const submit = async () => {
    try {
      const res = await createRoomTypeApi(form.value)
      emit('added', res)
      close()
    } catch (e) {
      console.error('Tạo thất bại:', e)
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
  
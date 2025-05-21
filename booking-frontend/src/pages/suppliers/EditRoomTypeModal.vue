<template>
  <div class="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center z-50">
    <div class="bg-white p-6 rounded-lg w-full max-w-lg shadow-lg">
      <h2 class="text-xl font-semibold mb-4 text-blue-700">Chỉnh sửa loại phòng</h2>
      
      <form @submit.prevent="submitForm">
        <!-- Tên loại phòng -->
        <div class="mb-4">
          <label class="block font-medium mb-1">Tên loại phòng</label>
          <input v-model="type_name" type="text" class="w-full border rounded px-3 py-2" required />
        </div>

        <!-- Số lượng -->
        <div class="mb-4">
          <label class="block font-medium mb-1">Số lượng</label>
          <input v-model="number_room" type="number" class="w-full border rounded px-3 py-2" required />
        </div>

        <!-- Ảnh -->
       <div class="mb-4">
  <label class="block font-medium mb-1">Hình ảnh</label>
  <input 
    key="image-input"
    type="file"
    accept="image/*"
    class="w-full"
    @change="onFileChange"
  />
  <div class="mt-2" v-if="previewImageUrl">
    <img :src="previewImageUrl" alt="Preview" class="h-32 rounded object-cover" />
  </div>
</div>

        <!-- Mô tả -->
        <div class="mb-4">
          <label class="block font-medium mb-1">Mô tả</label>
          <textarea v-model="description" class="w-full border rounded px-3 py-2"></textarea>
        </div>

        <!-- Nút hành động -->
        <div class="flex justify-end gap-3 mt-4">
          <button type="button" @click="$emit('close')" class="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400">Hủy</button>
          <button type="submit" class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">Lưu thay đổi</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, computed } from 'vue'
import type { RoomTypeSummary } from '@/types/supplier'
import { updateRoomTypeApi, uploadImageApi } from '@/services/supplier'
import { toast } from 'vue3-toastify'
  import {BASE_URL} from '@/utils/imageHelper'
const props = defineProps<{
  roomType: RoomTypeSummary | null
}>()

const emit = defineEmits(['close', 'updated'])

const type_name = ref('')
const number_room = ref(0)
const description = ref('')
const room_image = ref('') // Chỉ lưu đường dẫn tương đối (ví dụ: /images/image1.jpg)

// URL gốc của server chứa ảnh
const baseUrl = 'http://localhost:8080'

// Tạo đường dẫn đầy đủ để hiển thị ảnh
const fullImageUrl = computed(() => {
  return room_image.value ? `${baseUrl}${room_image.value}` : ''
})

watch(
  () => props.roomType,
  (newVal) => {
    if (newVal) {
      type_name.value = newVal.type_name
      number_room.value = newVal.number_room
      description.value = newVal.description
      room_image.value = newVal.room_image // chỉ lưu đường dẫn tương đối
    }
  },
  { immediate: true }
)
const previewImageUrl = ref<string>('')

const onFileChange = async (e: Event) => {
  const target = e.target as HTMLInputElement
  const file = target.files?.[0]
  if (!file) return

  try {
    // ✅ Preview ngay ảnh vừa chọn bằng URL.createObjectURL
    previewImageUrl.value = URL.createObjectURL(file)

    // ✅ Upload file lên server
    const res = await uploadImageApi(file) // API trả về đường dẫn tương đối

    room_image.value = res // lưu đường dẫn tương đối vào server
    toast.success('Tải ảnh lên thành công!')
  } catch (err) {
    console.error(err)
    toast.error('Lỗi khi tải ảnh!')
  }
}

// Khi load dữ liệu ban đầu thì hiển thị ảnh gốc từ server
watch(
  () => props.roomType,
  (newVal) => {
    if (newVal) {
      type_name.value = newVal.type_name
      number_room.value = newVal.number_room
      description.value = newVal.description
      room_image.value = newVal.room_image
      previewImageUrl.value = newVal.room_image ? `${baseUrl}${newVal.room_image}` : ''
    }
  },
  { immediate: true }
)
const submitForm = async () => {
  if (!props.roomType) return
  try {
    const updatedData = {
      type_name: type_name.value,
      number_room: number_room.value,
      description: description.value,
      room_image: room_image.value // gửi đường dẫn tương đối về server
    }

    const res = await updateRoomTypeApi(props.roomType.room_type_id, updatedData)
    console.log('==>ảnh cập nhật', res)
    
    toast.success('Cập nhật loại phòng thành công!')
    emit('updated')
    emit('close')
  } catch (err) {
    console.error(err)
    toast.error('Cập nhật thất bại!')
  }
}
</script>

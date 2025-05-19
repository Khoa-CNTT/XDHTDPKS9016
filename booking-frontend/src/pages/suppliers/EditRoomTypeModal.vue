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
          <input type="file" accept="image/*" class="w-full" @change="onFileChange" />
          <div class="mt-2" v-if="previewImage">
            <img :src="previewImage" alt="Preview" class="h-32 rounded object-cover" />
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
import { updateRoomTypeApi } from '@/services/supplier'
import { toast } from 'vue3-toastify'

const props = defineProps<{
  roomType: RoomTypeSummary | null
}>()

const emit = defineEmits(['close', 'updated'])

const type_name = ref('')
const number_room = ref(0)
const description = ref('')
const room_image = ref('')
const fileUpload = ref<File | null>(null)

watch(
  () => props.roomType,
  (newVal) => {
    if (newVal) {
      type_name.value = newVal.type_name
      number_room.value = newVal.number_room
      description.value = newVal.description
      room_image.value = newVal.room_image
    }
  },
  { immediate: true }
)

const previewImage = computed(() => {
  if (fileUpload.value) {
    return URL.createObjectURL(fileUpload.value)
  }
  if (!room_image.value) return ''
  return room_image.value.startsWith('/')
    ? `http://157.66.101.165:8080${room_image.value}`
    : room_image.value
})

const onFileChange = (e: Event) => {
  const file = (e.target as HTMLInputElement).files?.[0]
  if (file) {
    fileUpload.value = file
  }
}

const submitForm = async () => {
  if (!props.roomType) return
  try {
    const formData = new FormData()
    formData.append('type_name', type_name.value)
    formData.append('number_room', number_room.value.toString())
    formData.append('description', description.value)
    if (fileUpload.value) {
      formData.append('room_image', fileUpload.value)
    }

    await updateRoomTypeApi(props.roomType.room_type_id, formData)
    toast.success('Cập nhật loại phòng thành công!')
    emit('updated')
    emit('close')
  } catch (err) {
    console.error(err)
    toast.error('Cập nhật thất bại!')
  }
}
</script>

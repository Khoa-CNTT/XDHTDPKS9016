<template>
  <div class="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50 p-4"
    @click.self="$emit('close')">
    <div class="bg-white rounded-lg shadow-lg w-full max-w-2xl p-8 flex flex-col gap-6" style="max-height: 90vh;">
      <h3 class="text-2xl font-semibold text-center text-blue-800">
        Chỉnh sửa thông tin khách sạn
      </h3>

      <form @submit.prevent="submitEdit" class="flex flex-col gap-6 flex-grow overflow-visible">
        <!-- 3 ô input ngang -->
        <div class="flex flex-col sm:flex-row gap-6">
          <div class="flex-1">
            <label class="block mb-2 font-semibold text-gray-700">Tên nhà cung cấp</label>
            <input v-model="form.name" type="text" placeholder="Nhập tên nhà cung cấp"
              class="w-full border border-gray-300 rounded-lg px-4 py-3 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500"
              required />
          </div>

          <div class="flex-1">
            <label class="block mb-2 font-semibold text-gray-700">Địa chỉ</label>
            <input v-model="form.address" type="text" placeholder="Nhập địa chỉ"
              class="w-full border border-gray-300 rounded-lg px-4 py-3 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500"
              required />
          </div>

          <div class="flex-1">
            <label class="block mb-2 font-semibold text-gray-700">Hotline</label>
            <input v-model="form.hotline" type="tel" pattern="[0-9]+" placeholder="Chỉ nhập số"
              class="w-full border border-gray-300 rounded-lg px-4 py-3 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500"
              required />
          </div>
        </div>

        <!-- Upload ảnh -->
        <div>
          <label class="block mb-2 font-semibold text-gray-700">Chọn ảnh</label>
          <input type="file" accept="image/*" @change="handleFileUpload" class="block w-full text-gray-700" />
          <div v-if="form.image" class="mt-4 flex justify-center">
            <img
              :src="`http://157.66.101.165:8080${form.image}`"
              alt="Ảnh preview"
              class="max-w-full max-h-40 object-contain rounded-lg shadow-md mx-auto"
            />
          </div>
        </div>

        <!-- Mô tả -->
        <div>
          <label class="block mb-2 font-semibold text-gray-700">Mô tả</label>
          <textarea v-model="form.description" placeholder="Nhập mô tả"
            class="w-full border border-gray-300 rounded-lg px-4 py-3 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none"
            rows="3"></textarea>
        </div>

        <!-- Nút hành động -->
        <div class="flex justify-end gap-4">
          <button type="button" @click="$emit('close')"
            class="px-6 py-3 bg-gray-300 rounded-lg hover:bg-gray-400 transition">Hủy</button>
          <button
            type="submit"
            class="px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition"
          >
            Lưu
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, watch, defineProps, defineEmits } from 'vue'
import { uploadImageApi } from '@/services/supplier'
import { updateSupplierApi } from '@/services/admin'

const props = defineProps({
  supplier: Object
})
const emit = defineEmits(['close', 'save'])

const form = reactive({
  name: '',
  image: '',
  address: '',
  hotline: '',
  description: ''
})

// Đổ dữ liệu khi mở popup
watch(
  () => props.supplier,
  (newVal) => {
    if (newVal) {
      form.name = newVal.name || ''
      form.address = newVal.address || ''
      form.hotline = newVal.hotline || ''
      form.description = newVal.description || ''
      form.image = newVal.image || ''
    }
  },
  { immediate: true }
)

// Upload ảnh khi người dùng chọn
const handleFileUpload = async (event: Event) => {
  const target = event.target as HTMLInputElement
  const file = target.files?.[0]
  if (file) {
    try {
      const uploadedUrl = await uploadImageApi(file)
      form.image = uploadedUrl
      console.log('Uploaded image URL:', uploadedUrl)
    } catch (error) {
      console.error('Lỗi upload ảnh:', error)
    }
  }
}

// Submit form
const submitEdit = async () => {
  try {
    const supplierId = props.supplier?.hotel_id || props.supplier?.id // tùy API trả về id
    if (!supplierId) {
      console.error('Không tìm thấy ID khách sạn')
      return
    }

    const payload = {
      name: form.name,
      address: form.address,
      hotline: form.hotline,
      description: form.description,
      image: form.image
    }

    const response = await updateSupplierApi(supplierId, payload)
    console.log('API Response:', response)

    emit('save', response)
    emit('close')
  } catch (error) {
    console.error('Lỗi cập nhật nhà cung cấp:', error)
  }
}
</script>

<style scoped>
/* Nếu cần thêm style riêng */
</style>

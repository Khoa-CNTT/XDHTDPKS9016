<template>
  <div
    class="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50 p-4"
    @click.self="$emit('close')"
  >
    <div
      class="bg-white rounded-lg shadow-lg w-full max-w-2xl p-8 flex flex-col gap-6"
      style="max-height: 90vh;"
    >
      <h3 class="text-2xl font-semibold text-center text-blue-800">
        Chỉnh sửa thông tin khách sạn
      </h3>
      <form
        @submit.prevent="submitEdit"
        class="flex flex-col gap-6 flex-grow overflow-visible"
      >
        <!-- Hàng 3 input ngang -->
        <div class="flex flex-col sm:flex-row gap-6">
          <!-- Tên nhà cung cấp -->
          <div class="flex-1">
            <label class="block mb-2 font-semibold text-gray-700">
              Tên nhà cung cấp
            </label>
            <input
              v-model="form.name"
              type="text"
              placeholder="Nhập tên nhà cung cấp"
              class="w-full border border-gray-300 rounded-lg px-4 py-3 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>

          <!-- Địa chỉ -->
          <div class="flex-1">
            <label class="block mb-2 font-semibold text-gray-700">Địa chỉ</label>
            <input
              v-model="form.address"
              type="text"
              placeholder="Nhập địa chỉ"
              class="w-full border border-gray-300 rounded-lg px-4 py-3 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>

          <!-- Hotline -->
          <div class="flex-1">
            <label class="block mb-2 font-semibold text-gray-700">Hotline</label>
            <input
              v-model="form.hotline"
              type="tel"
              pattern="[0-9]+"
              placeholder="Chỉ nhập số"
              class="w-full border border-gray-300 rounded-lg px-4 py-3 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>
        </div>

        <!-- Chọn ảnh -->
        <div>
          <label class="block mb-2 font-semibold text-gray-700">Chọn ảnh</label>
          <input
            type="file"
            accept="image/*"
            @change="onFileChange"
            class="block w-full text-gray-700"
            required
          />
          <div v-if="previewImage" class="mt-4 flex justify-center">
            <img
              :src="previewImage"
              alt="Ảnh preview"
              class="max-w-full max-h-40 object-contain rounded-lg shadow-md mx-auto"
            />
          </div>
        </div>

        <!-- Mô tả -->
        <div>
          <label class="block mb-2 font-semibold text-gray-700">Mô tả</label>
          <textarea
            v-model="form.description"
            placeholder="Nhập mô tả"
            class="w-full border border-gray-300 rounded-lg px-4 py-3 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none"
            rows="3"
          ></textarea>
        </div>

        <!-- Nút hành động -->
        <div class="flex justify-end gap-4">
          <button
            type="button"
            @click="$emit('close')"
            class="px-6 py-3 bg-gray-300 rounded-lg hover:bg-gray-400 transition"
          >
            Hủy
          </button>
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
import { reactive, watch, ref, defineProps, defineEmits } from 'vue'

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

const previewImage = ref<string>('')

// Đổ dữ liệu ban đầu
watch(
  () => props.supplier,
  (newVal) => {
    if (newVal) {
      form.name = newVal.name || ''
      form.address = newVal.address || ''
      form.hotline = newVal.hotline || ''
      form.description = newVal.description || ''
      previewImage.value = newVal.image || ''
      form.image = ''
    }
  },
  { immediate: true }
)

const onFileChange = (e: Event) => {
  const files = (e.target as HTMLInputElement).files
  if (files && files.length > 0) {
    const file = files[0]
    const reader = new FileReader()
    reader.onload = () => {
      previewImage.value = reader.result as string
      form.image = ''
    }
    reader.readAsDataURL(file)
  }
}

const submitEdit = () => {
  emit('save', {
    ...form,
    image: previewImage.value
  })
  emit('close')
}
</script>

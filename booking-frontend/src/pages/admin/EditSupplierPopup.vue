<template>
  <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white rounded p-6 w-96 max-w-full">
      <h3 class="text-lg font-semibold mb-4">Sửa khách sạn</h3>
      <form @submit.prevent="submitForm">
        <div class="mb-3">
          <label class="block mb-1 font-medium">Tên khách sạn</label>
          <input
            v-model="name"
            type="text"
            class="w-full border rounded px-2 py-1"
          />
        </div>
        <div class="mb-3">
          <label class="block mb-1 font-medium">Địa chỉ</label>
          <input
            v-model="address"
            type="text"
            class="w-full border rounded px-2 py-1"
          />
        </div>
        <div class="mb-3">
          <label class="block mb-1 font-medium">Hotline</label>
          <input
            v-model="hotline"
            type="text"
            class="w-full border rounded px-2 py-1"
          />
        </div>
        <div class="mb-3">
          <label class="block mb-1 font-medium">Ảnh khách sạn</label>

          <div class="mt-2">
            <input
              type="file"
              accept="image/*"
              @change="handleFileChange"
            />
          </div>
        </div>

        <!-- Mô tả khách sạn -->
        <div class="mb-3">
          <label class="block mb-1 font-medium">Mô tả khách sạn</label>
          <textarea
            v-model="description"
            rows="4"
            class="w-full border rounded px-2 py-1"
          ></textarea>
        </div>

        <div class="flex justify-end gap-2">
          <button
            type="button"
            @click="$emit('close')"
            class="px-4 py-2 border rounded"
          >
            Hủy
          </button>
          <button
            type="submit"
            class="px-4 py-2 bg-green-600 text-white rounded"
          >
            Lưu
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { uploadImageApi } from '@/services/supplier'
import { updateSupplierApi } from '@/services/admin'
import { toast } from 'vue3-toastify'
const props = defineProps({
  supplier: {
    type: Object,
    required: true,
  },
  hotelId: {
    type: [String, Number],
    required: false,
  },
})
watch(
  () => props.hotelId,
  (newVal) => {
    // console.log('hotelId thay đổi:', newVal)
  },
  { immediate: true },
)
const name = ref('')
const address = ref('')
const previewImage = ref('')
const hotline = ref('')
const description = ref('')

const imagePath = ref('')

watch(
  () => props.supplier,
  (newVal) => {
    if (newVal) {
      name.value = newVal.name || ''
      address.value = newVal.address || ''
      hotline.value = newVal.hotline || ''
      description.value = newVal.description || ''
      previewImage.value = newVal.image ? `http://localhost:8080${newVal.image}` : ''
    }
  },
  { immediate: true },
)
const handleFileChange = async (e: Event) => {
  const target = e.target as HTMLInputElement
  const file = target.files?.[0]
  if (!file) return

  try {
    previewImage.value = URL.createObjectURL(file)
    const res = await uploadImageApi(file)
    imagePath.value = res

    toast.success('Tải ảnh lên thành công!', { autoClose: 5000, position: 'top-right' })
  } catch (err) {
    toast.error('Lỗi khi tải ảnh!', { autoClose: 5000, position: 'top-right' })
  }
}

const emit = defineEmits(['close', 'save'])

const submitForm = async () => {
  try {
    if (props.hotelId === undefined) {
      toast.error('Hotel ID không hợp lệ', { autoClose: 5000, position: 'top-right' })
      return
    }

    const hotelIdNumber = Number(props.hotelId)
    if (isNaN(hotelIdNumber)) {
      toast.error('Hotel ID không hợp lệ', { autoClose: 5000, position: 'top-right' })
      return
    }

    const payload = {
      name: name.value,
      image: imagePath.value || props.supplier.image,
      address: address.value,
      hotline: hotline.value,
      description: description.value,
    }

    const res = await updateSupplierApi(hotelIdNumber, payload)

    toast.success('Cập nhật khách sạn thành công!', { autoClose: 5000, position: 'top-right' })

    emit('save', res)
    emit('close')
  } catch (error) {
    toast.error('Cập nhật thất bại!', { autoClose: 5000, position: 'top-right' })
  }
}
</script>

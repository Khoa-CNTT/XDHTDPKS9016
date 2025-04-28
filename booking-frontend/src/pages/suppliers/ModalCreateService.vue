<template>
    <div class="fixed inset-0 bg-gray-400 bg-opacity-40 flex items-center justify-center z-50">
        <div class="bg-white p-6 rounded-lg w-[600px]">
            <h2 class="text-xl font-semibold mb-4">Thêm dịch vụ mới</h2>

            <div class="flex flex-col gap-3">
                <!-- Tên dịch vụ -->
                <input v-model="localService.service_name" type="text" placeholder="Tên dịch vụ"
                    class="border rounded p-2" />

                <!-- Giá dịch vụ -->
                <div>
                    <input v-model="localService.service_price" type="number" placeholder="Giá"
                        class="border rounded p-2 w-full" min="0" step="any" />
                    <p class="text-sm text-gray-500 mt-1">Vui lòng nhập giá dịch vụ</p>
                </div>

                <!-- Hình ảnh dịch vụ -->
                <div>
                    <input @change="handleImageChange" type="file" accept="image/*"
                        class="border rounded p-2 w-full" />
                    <p class="text-sm text-gray-500 mt-1">Chọn hình ảnh từ máy tính</p>
                    <div v-if="localService.service_image" class="mt-3">
                        <!-- Hiển thị ảnh với kích thước giới hạn -->
                        <img :src="localService.service_image" alt="Preview"
                            class="max-w-[200px] max-h-[200px] object-contain rounded-md" />
                    </div>
                </div>

                <!-- Mô tả dịch vụ -->
                <textarea v-model="localService.description" placeholder="Mô tả chi tiết"
                    class="border rounded p-2"></textarea>
            </div>

            <div class="flex justify-end gap-2 mt-4">
                <button @click="$emit('close')"
                    class="px-4 py-2 bg-gray-400 text-white rounded-md hover:bg-gray-500 transition">
                    Hủy
                </button>
                <button @click="handleSave"
                    class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition">
                    Lưu
                </button>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import type { Service } from '@/types/supplier'

const emit = defineEmits<{
    (e: 'close'): void,
    (e: 'save', service: Service): void
}>()

const localService = ref<Service>({
    service_name: '',
    service_price: 0,
    service_image: '',
    description: '',
    service_id: 0,
    bookings: []
})

const handleImageChange = (event: Event) => {
    const fileInput = event.target as HTMLInputElement
    if (fileInput.files && fileInput.files[0]) {
        const file = fileInput.files[0]
        localService.value.service_image = URL.createObjectURL(file) 
    }
}

const handleSave = () => {
    emit('save', localService.value)
}
</script>

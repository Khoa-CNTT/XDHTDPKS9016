<template>
    <div class="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center z-50">
        <div class="bg-white p-6 rounded-lg w-full max-w-2xl max-h-[90vh] overflow-y-auto shadow-lg">
            <h2 class="text-2xl font-semibold mb-4 text-blue-600">Chi tiết nhà cung cấp</h2>
            <p class="mb-2 text-gray-700"><strong>Tên:</strong> {{ supplier.name }}</p>
            <p class="mb-2 text-gray-700"><strong>Địa chỉ:</strong> {{ supplier.address }}</p>
            <p class="mb-2 text-gray-700"><strong>Hotline:</strong> {{ supplier.hotline }}</p>
            <p class="mb-4 text-gray-700"><strong>Mô tả:</strong> {{ supplier.description || 'Không có' }}</p>

            <div class="mt-4">
                <h3 class="font-bold text-lg text-blue-500">Dịch vụ:</h3>
                <ul class="list-disc ml-5 mt-2 text-gray-700">
                    <li v-for="service in supplier.services" :key="service.service_id">
                        <span class="font-semibold">{{ service.service_name }}</span> - 
                        <span class="text-green-600">{{ service.service_price.toLocaleString() }} VNĐ</span>
                    </li>
                </ul>
            </div>

            <div class="mt-6 text-right">
                <button class="px-4 py-2 bg-gray-400 text-white rounded hover:bg-gray-500 transition duration-200" @click="$emit('close')">
                    Đóng
                </button>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
defineProps<{
    supplier: any
}>()

defineEmits(['close'])
</script>


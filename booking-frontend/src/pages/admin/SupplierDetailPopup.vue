<template>
  <div class="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center z-50">
   <div class="relative bg-white p-6 rounded-lg w-full max-w-5xl max-h-[90vh] overflow-y-auto shadow-lg">
      <button
        class="absolute top-2 right-2 text-gray-500 hover:text-red-600 text-2xl font-bold focus:outline-none"
        @click="$emit('close')"
        aria-label="Đóng"
      >
        &times;
      </button>
      <!-- Tiêu đề -->
      <h2 class="text-2xl font-semibold mb-4 text-blue-600">Chi tiết khách sạn</h2>

      <!-- Ảnh và thông tin cơ bản -->
      <div class="flex flex-col md:flex-row gap-6 mb-6">
        <img
          :src="`http://localhost:8080${supplier.image}`"
          alt="Ảnh khách sạn"
          class="w-full md:w-64 h-48 object-cover rounded-lg shadow"
        />
        <div class="flex-1 space-y-2 text-gray-700">
          <p><strong>Tên:</strong> {{ supplier.name }}</p>
          <p><strong>Địa chỉ:</strong> {{ supplier.address }}</p>
          <p><strong>Hotline:</strong> {{ supplier.hotline }}</p>
          <p><strong>Mô tả:</strong> {{ supplier.description || 'Không có' }}</p>
        </div>
      </div>

      <!-- Dịch vụ -->
      <div class="mt-4">
        <h3 class="font-bold text-lg text-blue-500 mb-2">Dịch vụ:</h3>
        <div v-if="supplier.services.length" class="grid md:grid-cols-2 gap-4">
          <div
            v-for="service in supplier.services"
            :key="service.serviceId"
            class="border rounded-lg p-4 flex gap-4 items-start"
          >
            <img
              :src="`http://localhost:8080${service.serviceImage}`"
              alt="Dịch vụ"
              class="w-24 h-24 object-cover rounded"
            />
            <div>
              <h4 class="font-semibold text-blue-600">{{ service.serviceName }}</h4>
              <p class="text-green-600 font-medium">{{ service.servicePrice.toLocaleString() }} VNĐ</p>
              <p class="text-gray-600 text-sm mt-1">{{ service.description }}</p>
            </div>
          </div>
        </div>
        <div v-else class="italic text-gray-500">Không có dịch vụ</div>
      </div>

      <!-- Loại phòng -->
      <div class="mt-6">
        <h3 class="font-bold text-lg text-blue-500 mb-2">Loại phòng:</h3>
        <div v-if="supplier.roomTypes.length" class="space-y-6">
          <div
            v-for="roomType in supplier.roomTypes"
            :key="roomType.roomTypeId"
            class="border rounded-lg p-4"
          >
            <div class="flex flex-col md:flex-row gap-4">
              <img
                :src="`http://localhost:8080${roomType.roomImage.trim()}`"
                alt="Loại phòng"
                class="w-full md:w-64 h-40 object-cover rounded"
              />
              <div class="flex-1">
                <h4 class="text-blue-600 font-semibold">{{ roomType.typeName }}</h4>
                <p class="text-gray-700">{{ roomType.description }}</p>
                <p class="text-sm text-gray-500 mt-1">
                  <strong>Số phòng:</strong> {{ roomType.numberRoom }} |
                  <strong>Giá trung bình:</strong> {{ roomType.averagePrice.toLocaleString() }} VNĐ
                </p>
               <div class="mt-4 text-sm text-gray-800">
  <strong class="block mb-2">Phòng:</strong>
  <div class="space-y-3">
    <div
      v-for="room in roomType.rooms"
      :key="room.roomId"
      class="border border-gray-200 rounded-lg p-3 shadow-sm hover:shadow-md transition"
    >
      <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between">
        <div>
          <p class="font-medium text-blue-600">Phòng {{ room.roomId }}</p>
          <p class="text-gray-600">
            {{ room.numberBeds }} giường - {{ room.price.toLocaleString() }} VNĐ
          </p>
        </div>
        <div class="mt-2 sm:mt-0">
          <span
            :class="[
              'text-sm font-semibold px-2 py-1 rounded-full',
              room.status === 'AVAILABLE'
                ? 'bg-green-100 text-green-700'
                : 'bg-red-100 text-red-700'
            ]"
          >
            {{ room.status === 'AVAILABLE' ? 'Còn phòng' : 'Đã đặt' }}
          </span>
        </div>
      </div>
    </div>
  </div>
</div>

              </div>
            </div>
          </div>
        </div>
        <div v-else class="italic text-gray-500">Không có loại phòng</div>
      </div>

      <!-- Nút đóng -->
      <div class="mt-8 text-right">
        <button
          class="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 transition duration-200"
          @click="$emit('close')"
        >
          Đóng
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import {BASE_URL} from '@/utils/imageHelper'
defineProps<{
  supplier: any
}>()

defineEmits(['close'])
</script>

<template>
  <div class="space-y-6 px-4 py-6">
    <!-- Bảng danh sách -->
    <div class="border rounded-lg p-6 bg-white shadow">
      <h3 class="text-lg font-semibold mb-4 text-blue-700 flex">
        <Icon icon="mdi:clipboard-list-outline" width="24" height="24" class="mr-2" /> Thông tin khách sạn
      </h3>

      <div v-if="hotel.name">
        <table class="min-w-full divide-y divide-gray-200 border">
          <thead class="bg-gray-100">
            <tr>
              <th class="px-4 py-2 text-left">Tên</th>
              <th class="px-4 py-2 text-left">Địa chỉ</th>
              <th class="px-4 py-2 text-left">Hình ảnh</th>
              <th class="px-4 py-2 text-left">Mô tả</th>
              <th class="px-4 py-2 text-left">Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td class="px-4 py-2">{{ hotel.name }}</td>
              <td class="px-4 py-2 max-w-[160px] truncate">{{ hotel.address }}</td>
              <td class="px-4 py-2">
                <img :src="hotel.image" alt="Ảnh khách sạn"
                  class="w-20 h-14 object-cover rounded" />
              </td>
              <td class="px-4 py-2 max-w-[160px] truncate">{{ hotel.description }}</td>
              <td class="px-4 py-2 text-center">
                <div class="flex justify-center gap-2 flex-wrap">
                  <!-- Nút Xem (với Icon) -->
                  <button @click="showModal = true"
                    class="p-2 bg-blue-500 hover:bg-blue-600 text-white rounded transition">
                    <Icon icon="mdi:eye" width="20" height="20" class="mr-1" />
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div v-else class="text-center py-6 text-gray-500">
        Đang tải dữ liệu khách sạn...
      </div>
    </div>
    <UpdateInfo v-if="showModal" :hotel="hotel" @close="showModal = false" @updated="fetchHotelInfo" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getInfoHotelApi } from '@/services/supplier'
import UpdateInfo from './UpdateInfo.vue'
import { toast } from 'vue3-toastify'
import 'vue3-toastify/dist/index.css'
const showModal = ref(false)
const baseUrl = 'http://157.66.101.165:8080'
const hotel = ref({
  name: "",
  image: "",
  address: "",
  hotline: "",
  description: "",
  services: [],
  roomTypes: []
})

const fetchHotelInfo = async () => {
  try {
    const data = await getInfoHotelApi()
    hotel.value = {
      name: data.name,
      image: data.image,
      address: data.address,
      hotline: data.hotline,
      description: data.description,
      services: data.services,
      roomTypes: data.roomTypes
    }
  } catch (error) {
    console.error('Lỗi khi lấy thông tin khách sạn:', error)
    toast.error('Lỗi khi tải thông tin khách sạn')
  }
}

onMounted(() => {
  fetchHotelInfo()
})
</script>

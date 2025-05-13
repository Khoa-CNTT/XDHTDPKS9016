<template>
  <div class="space-y-6 px-4 py-6">
    <!-- Bộ lọc -->
    <div class="border rounded-lg p-6 bg-white shadow flex justify-between items-center">
      <h3 class="text-lg font-semibold text-blue-700 flex">
        <Icon icon="mdi:filter-variant" width="24" height="24" class="mr-2" /> Bộ lọc
      </h3>
      <div class="flex flex-wrap gap-6 items-center">
        <div class="flex items-center">
          <label for="arrival-date" class="mr-2 text-sm font-medium">Ngày đến</label>
          <input type="date" id="arrival-date" v-model="filters.arrivalDate"
            class="px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500" />
        </div>
        <div class="flex items-center">
          <label for="departure-date" class="mr-2 text-sm font-medium">Ngày đi</label>
          <input type="date" id="departure-date" v-model="filters.departureDate"
            class="px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500" />
        </div>
        <div class="flex items-center">
          <label for="status" class="mr-2 text-sm font-medium">Trạng thái</label>
          <select id="status" v-model="filters.status"
            class="px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500">
            <option value="">Tất cả</option>
            <option value="Chờ xác nhận">Chờ xác nhận</option>
            <option value="Đã xác nhận">Đã xác nhận</option>
            <option value="Hoàn tất">Hoàn tất</option>
            <option value="Đã hủy">Đã hủy</option>
          </select>
        </div>
      </div>
    </div>

    <!-- Bảng danh sách -->
    <div class="border rounded-lg p-6 bg-white shadow">
      <h3 class="text-lg font-semibold mb-4 text-blue-700 flex">
        <Icon icon="mdi:clipboard-list-outline" width="24" height="24" class="mr-2" /> Danh sách đặt phòng
      </h3>
      <table class="min-w-full divide-y divide-gray-200 border">
        <thead class="bg-gray-100">
          <tr>
            <th class="px-4 py-2 text-left">STT</th>
            <th class="px-4 py-2 text-left">Tên người dùng</th>
            <th class="px-4 py-2 text-left">Email</th>
            <th class="px-4 py-2 text-left">Ngày đến</th>
            <th class="px-4 py-2 text-left">Ngày đi</th>
            <th class="px-4 py-2 text-left">Loại phòng</th>
            <th class="px-4 py-2 text-left">Trạng thái</th>
            <th class="px-4 py-2 text-center">Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(row, index) in paginatedRows" :key="index" class="border-t">
            <td class="px-4 py-2">{{ row.stt }}</td>
            <td class="px-4 py-2 max-w-[160px] truncate">{{ row.name }}</td>
            <td class="px-4 py-2 max-w-[160px] truncate">{{ row.email }}</td>
            <td class="px-4 py-2">{{ row.arrivalDate }}</td>
            <td class="px-4 py-2">{{ row.departureDate }}</td>
            <td class="px-4 py-2">{{ row.roomType }}</td>
            <td class="px-4 py-2">
              <span class="px-2 py-1 rounded font-semibold border" :class="{
                'bg-yellow-100 text-yellow-700 border-yellow-300': row.status === 'Chờ xác nhận',
                'bg-green-100 text-green-700 border-green-300': row.status === 'Đã xác nhận',
                'bg-blue-100 text-blue-700 border-blue-300': row.status === 'Hoàn tất',
                'bg-red-100 text-red-700 border-red-300': row.status === 'Đã hủy'
              }">
                {{ row.status }}
              </span>
            </td>
         <td class="px-4 py-2 text-center">
  <div class="flex justify-center gap-2 flex-wrap">
    <!-- Nút Xác nhận (với Icon) -->
    <button v-if="row.status === 'Chờ xác nhận'" @click="confirmBooking(index)"
    class="p-2 bg-green-500 hover:bg-green-600 text-white rounded transition">
      <Icon icon="mdi:check-circle" width="20" height="20" class="mr-1" />
    
    </button>

    <!-- Nút Hủy (với Icon) -->
    <button v-if="row.status === 'Chờ xác nhận' || row.status === 'Đã xác nhận'" @click="cancel(index)"
     class="p-2 bg-red-500 hover:bg-green-600 text-white rounded transition">
      <Icon icon="mdi:cancel" width="20" height="20" class="mr-1" />
   
    </button>

    <!-- Nút Xem (với Icon) -->
    <button @click="view(row)"
       class="p-2 bg-blue-500 hover:bg-blue-600 text-white rounded transition">
      <Icon icon="mdi:eye" width="20" height="20" class="mr-1" />
   
    </button>
  </div>
</td>





          </tr>
        </tbody>
      </table>

      <!-- Phân trang -->
      <div class="pt-6 flex justify-center">
        <Pagination :total="filteredRows.length" :items-per-page="itemsPerPage" :default-page="1"
          @page-change="handlePageChange" />
      </div>
    </div>

    <!-- Popup chi tiết -->
    <div v-if="showDetailPopup" class="fixed inset-0 bg-gray-800 bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white p-8 rounded-2xl shadow-lg w-full max-w-2xl relative border border-blue-200 animate-fade-in">
        <h2 class="text-2xl font-bold text-center mb-6 text-purple-800">🎉 Chi tiết đặt phòng</h2>
        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 text-gray-700">
          <p><span class="font-semibold text-blue-600">👤 Họ tên:</span> {{ selectedBooking.name }}</p>
          <p><span class="font-semibold text-blue-600">📧 Email:</span> {{ selectedBooking.email }}</p>
          <p><span class="font-semibold text-blue-600">📅 Ngày đến:</span> {{ selectedBooking.arrivalDate }}</p>
          <p><span class="font-semibold text-blue-600">📅 Ngày đi:</span> {{ selectedBooking.departureDate }}</p>
          <p><span class="font-semibold text-blue-600">🏨 Loại phòng:</span> {{ selectedBooking.roomType }}</p>
          <p><span class="font-semibold text-blue-600">📌 Trạng thái:</span> {{ selectedBooking.status }}</p>
          <p><span class="font-semibold text-blue-600">📞 SĐT:</span> {{ selectedBooking.phone || 'Chưa cập nhật' }}</p>
          <p><span class="font-semibold text-blue-600">👥 Số khách:</span> {{ selectedBooking.guestCount || '1' }}</p>
          <p class="sm:col-span-2"><span class="font-semibold text-blue-600">📝 Ghi chú:</span> {{ selectedBooking.note
            || 'Không có' }}</p>
        </div>
        <button
          class="absolute top-3 right-3 text-gray-600 hover:text-red-500 text-2xl transition-transform hover:scale-125"
          @click="showDetailPopup = false">
          ✕
        </button>
      </div>
    </div>
  </div>
</template>




<script setup lang="ts">

import { ref, computed } from 'vue'
import Pagination from '@/components/base/Pagination.vue'
const tableHeaders = ['STT', 'Tên người dùng', 'Email', 'Ngày đến', 'Ngày đi', 'Loại phòng', 'Trạng thái', 'Hành động']
const selectedBooking = ref<any>(null)
const showDetailPopup = ref(false);

const view = (row: any) => {
  selectedBooking.value = row
  showDetailPopup.value = true
}
const tableRows = ref([
  {
    name: 'Nguyễn Văn A',
    email: 'nguyenvana@example.com',
    arrivalDate: '2025-05-01',
    departureDate: '2025-05-05',
    roomType: 'Phòng đơn',
    status: 'Chờ xác nhận',
  },
  {
    name: 'Trần Thị B',
    email: 'tranthib@example.com',
    arrivalDate: '2025-05-03',
    departureDate: '2025-05-05',
    roomType: 'Phòng đôi',
    status: 'Đã xác nhận',
  },
  {
    name: 'Lê Văn C',
    email: 'levanc@example.com',
    arrivalDate: '2025-05-10',
    departureDate: '2025-05-12',
    roomType: 'Phòng VIP',
    status: 'Hoàn tất',
  },
  {
    name: 'Lê Văn C',
    email: 'levanc@example.com',
    arrivalDate: '2025-05-10',
    departureDate: '2025-05-12',
    roomType: 'Phòng VIP',
    status: 'Hoàn tất',
  },
  {
    name: 'Lê Văn C',
    email: 'levanc@example.com',
    arrivalDate: '2025-05-10',
    departureDate: '2025-05-12',
    roomType: 'Phòng VIP',
    status: 'Hoàn tất',
  }
])


const filters = ref({
  arrivalDate: '',
  departureDate: '',
  status: '',
})


const filteredRows = computed(() => {
  return tableRows.value.filter(row => {
    const matchesArrivalDate = filters.value.arrivalDate
      ? row.arrivalDate === filters.value.arrivalDate
      : true
    const matchesDepartureDate = filters.value.departureDate
      ? row.departureDate === filters.value.departureDate
      : true
    const matchesStatus = filters.value.status
      ? row.status === filters.value.status
      : true

    return matchesArrivalDate && matchesDepartureDate && matchesStatus
  })
})

const cancel = (index: number) => {
  const confirmed = window.confirm('Bạn có chắc chắn muốn hủy đặt phòng này không?')
  if (confirmed) {
    tableRows.value.splice(index, 1)
  }
}


const confirmBooking = (index: number) => {
  tableRows.value[index].status = 'Đã xác nhận'
}






const currentPage = ref(1)
const itemsPerPage = 10
const paginatedRows = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage
  return filteredRows.value.slice(start, start + itemsPerPage).map((row, index) => ({
    stt: start + index + 1,
    ...row,
  }))
})
const handlePageChange = (newPage: number) => {
  currentPage.value = newPage
}
</script>

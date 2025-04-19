<template>
  <div class="card">
    <div class="card-header">
      <div class="flex gap-6 items-center">
        <div class="flex items-center">
          <label for="arrival-date" class="mr-2">Ngày đến</label>
          <input type="date" id="arrival-date" v-model="filters.arrivalDate"
            class="px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500" />
        </div>
        <div class="flex items-center">
          <label for="departure-date" class="mr-2">Ngày đi</label>
          <input type="date" id="departure-date" v-model="filters.departureDate"
            class="px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500" />
        </div>
        <div class="flex items-center">
          <label for="status" class="mr-2">Trạng thái</label>
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

    <div class="card-body">
      <CustomTable :headers="tableHeaders" :rows="paginatedRows">
        <template #actions="{ row, index }">
          <div class="flex gap-2 justify-end">
            <button v-if="row.status === 'Chờ xác nhận'" class="px-2 py-1 bg-green-500 text-white rounded"
              @click="confirmBooking(index)">
              Xác nhận
            </button>
            <button v-if="row.status === 'Chờ xác nhận' || row.status === 'Đã xác nhận'"
              class="px-2 py-1 bg-red-500 text-white rounded" @click="cancel(index)">
              Hủy
            </button>
            <button class="px-2 py-1 bg-blue-500 text-white rounded" @click="view(row)">
              Xem chi tiết
            </button>
          </div>
        </template>
      </CustomTable>
    </div>
    <!-- Popup xem chi tiết đặt phòng -->
    <!-- Popup chi tiết đặt phòng -->
    <div v-if="showDetailPopup" class="fixed inset-0 bg-gray-200 bg-opacity-80 flex items-center justify-center z-50">
      <div class="bg-gradient-to-br from-white via-blue-50 to-purple-100 p-8 rounded-3xl shadow-2xl w-full max-w-2xl relative border border-blue-200 animate-fade-in">
    <h2 class="text-3xl font-extrabold mb-6 text-purple-800 text-center">🎉 Chi tiết đặt phòng</h2>
    
    <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 text-gray-800">
      <p><span class="font-semibold text-blue-600">👤 Họ tên:</span> {{ selectedBooking.name }}</p>
      <p><span class="font-semibold text-blue-600">📧 Email:</span> {{ selectedBooking.email }}</p>
      <p><span class="font-semibold text-blue-600">📅 Ngày đến:</span> {{ selectedBooking.arrivalDate }}</p>
      <p><span class="font-semibold text-blue-600">📅 Ngày đi:</span> {{ selectedBooking.departureDate }}</p>
      <p><span class="font-semibold text-blue-600">🏨 Loại phòng:</span> {{ selectedBooking.roomType }}</p>
      <p><span class="font-semibold text-blue-600">📌 Trạng thái:</span> 
        <span :class="{
          'text-green-600 font-bold': selectedBooking.status === 'Đã xác nhận',
          'text-yellow-600 font-bold': selectedBooking.status === 'Đang chờ',
          'text-red-600 font-bold': selectedBooking.status === 'Hủy'
        }">
          {{ selectedBooking.status }}
        </span>
      </p>
      <p><span class="font-semibold text-blue-600">📞 Số điện thoại:</span> {{ selectedBooking.phone || 'Chưa cập nhật' }}</p>
      <p><span class="font-semibold text-blue-600">👥 Số lượng khách:</span> {{ selectedBooking.guestCount || '1' }}</p>
      <p class="sm:col-span-2"><span class="font-semibold text-blue-600">📝 Ghi chú:</span> {{ selectedBooking.note || 'Không có' }}</p>
    </div>
    
    <button class="absolute top-3 right-3 text-gray-600 hover:text-red-500 text-2xl transition-transform hover:scale-125"
      @click="showDetailPopup = false">
      ✕
    </button>
  </div>
    </div>

    <div class="pb-32 flex justify-center">
      <!-- Component phân trang -->
      <Pagination :total="filteredRows.length" :items-per-page="itemsPerPage" :default-page="1"
        @page-change="handlePageChange" />
    </div>
  </div>
</template>



<script setup lang="ts">
import CustomTable from '@/components/base/CustomTable.vue'
import { ref, computed } from 'vue'
import Pagination from '@/components/base/Pagination.vue'
const tableHeaders = ['STT', 'Tên người dùng', 'Email', 'Ngày đến', 'Ngày đi', 'Loại phòng', 'Trạng thái', 'Hành động']
const selectedBooking = ref<any>(null)
const showDetailPopup = ref(false);
// const selectedBooking = ref<any>(null)
//   const viewForm = (row: any) => {
//   selectedBooking.value = row
//   showDetailPopup.value = true
// }
const view = (row: any) => {
  selectedBooking.value = row
  showDetailPopup.value = true
}
const tableRows = ref([
  {
    name: 'Nguyễn Văn A',
    email: 'nguyenvana@example.com',
    arrivalDate: '2025-05-01', // Đổi sang định dạng YYYY-MM-DD
    departureDate: '2025-05-05', // Đổi sang định dạng YYYY-MM-DD
    roomType: 'Phòng đơn',
    status: 'Chờ xác nhận',
  },
  {
    name: 'Trần Thị B',
    email: 'tranthib@example.com',
    arrivalDate: '2025-05-03', // Đổi sang định dạng YYYY-MM-DD
    departureDate: '2025-05-05', // Đổi sang định dạng YYYY-MM-DD
    roomType: 'Phòng đôi',
    status: 'Đã xác nhận',
  },
  {
    name: 'Lê Văn C',
    email: 'levanc@example.com',
    arrivalDate: '2025-05-10', // Đổi sang định dạng YYYY-MM-DD
    departureDate: '2025-05-12', // Đổi sang định dạng YYYY-MM-DD
    roomType: 'Phòng VIP',
    status: 'Hoàn tất',
  },
  {
    name: 'Lê Văn C',
    email: 'levanc@example.com',
    arrivalDate: '2025-05-10', // Đổi sang định dạng YYYY-MM-DD
    departureDate: '2025-05-12', // Đổi sang định dạng YYYY-MM-DD
    roomType: 'Phòng VIP',
    status: 'Hoàn tất',
  },
  {
    name: 'Lê Văn C',
    email: 'levanc@example.com',
    arrivalDate: '2025-05-10', // Đổi sang định dạng YYYY-MM-DD
    departureDate: '2025-05-12', // Đổi sang định dạng YYYY-MM-DD
    roomType: 'Phòng VIP',
    status: 'Hoàn tất',
  },
  {
    name: 'Lê Văn C',
    email: 'levanc@example.com',
    arrivalDate: '2025-05-10', // Đổi sang định dạng YYYY-MM-DD
    departureDate: '2025-05-12', // Đổi sang định dạng YYYY-MM-DD
    roomType: 'Phòng VIP',
    status: 'Hoàn tất',
  },
  {
    name: 'Lê Văn C',
    email: 'levanc@example.com',
    arrivalDate: '2025-05-10', // Đổi sang định dạng YYYY-MM-DD
    departureDate: '2025-05-12', // Đổi sang định dạng YYYY-MM-DD
    roomType: 'Phòng VIP',
    status: 'Hoàn tất',
  },
  {
    name: 'Lê Văn C',
    email: 'levanc@example.com',
    arrivalDate: '2025-05-10', // Đổi sang định dạng YYYY-MM-DD
    departureDate: '2025-05-12', // Đổi sang định dạng YYYY-MM-DD
    roomType: 'Phòng VIP',
    status: 'Hoàn tất',
  },
  {
    name: 'Lê Văn C',
    email: 'levanc@example.com',
    arrivalDate: '2025-05-10', // Đổi sang định dạng YYYY-MM-DD
    departureDate: '2025-05-12', // Đổi sang định dạng YYYY-MM-DD
    roomType: 'Phòng VIP',
    status: 'Hoàn tất',
  },
  {
    name: 'Lê Văn C',
    email: 'levanc@example.com',
    arrivalDate: '2025-05-10', // Đổi sang định dạng YYYY-MM-DD
    departureDate: '2025-05-12', // Đổi sang định dạng YYYY-MM-DD
    roomType: 'Phòng VIP',
    status: 'Hoàn tất',
  },
  {
    name: 'Lê Văn C',
    email: 'levanc@example.com',
    arrivalDate: '2025-05-10', // Đổi sang định dạng YYYY-MM-DD
    departureDate: '2025-05-12', // Đổi sang định dạng YYYY-MM-DD
    roomType: 'Phòng VIP',
    status: 'Hoàn tất',
  },
  {
    name: 'Lê Văn C',
    email: 'levanc@example.com',
    arrivalDate: '2025-05-10', // Đổi sang định dạng YYYY-MM-DD
    departureDate: '2025-05-12', // Đổi sang định dạng YYYY-MM-DD
    roomType: 'Phòng VIP',
    status: 'Hoàn tất',
  },
  {
    name: 'Lê Văn C',
    email: 'levanc@example.com',
    arrivalDate: '2025-05-10', // Đổi sang định dạng YYYY-MM-DD
    departureDate: '2025-05-12', // Đổi sang định dạng YYYY-MM-DD
    roomType: 'Phòng VIP',
    status: 'Hoàn tất',
  },
  {
    name: 'Lê Văn C',
    email: 'levanc@example.com',
    arrivalDate: '2025-05-10', // Đổi sang định dạng YYYY-MM-DD
    departureDate: '2025-05-12', // Đổi sang định dạng YYYY-MM-DD
    roomType: 'Phòng VIP',
    status: 'Hoàn tất',
  },
])

// Bộ lọc
const filters = ref({
  arrivalDate: '',
  departureDate: '',
  status: '',
})

// Lọc dữ liệu
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

// Hàm hủy
const cancel = (index: number) => {
  const confirmed = window.confirm('Bạn có chắc chắn muốn hủy đặt phòng này không?')
  if (confirmed) {
    tableRows.value.splice(index, 1)
  }
}

// Hàm xác nhận
const confirmBooking = (index: number) => {
  tableRows.value[index].status = 'Đã xác nhận'
}

// Hàm xem chi tiết




const currentPage = ref(1)
const itemsPerPage = 10
const paginatedRows = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage
  return filteredRows.value.slice(start, start + itemsPerPage).map((row, index) => ({
    stt: start + index + 1, // tính STT dựa trên trang hiện tại
    ...row,
  }))
})
const handlePageChange = (newPage: number) => {
  currentPage.value = newPage
}
</script>

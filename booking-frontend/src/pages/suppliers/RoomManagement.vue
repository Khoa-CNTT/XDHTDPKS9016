<template>
  <div class="space-y-6 px-4 py-6 relative">
    <!-- Bộ lọc -->
    <div
      class="border rounded-lg p-4 bg-white shadow flex flex-wrap justify-between items-center gap-2"
    >
      <h3 class="text-lg font-semibold text-blue-700 flex items-center">Quản lý đặt phòng</h3>

      <div class="relative">
      </div>
    </div>

    <!-- Danh sách -->
    <div class="border rounded-lg p-4 bg-white shadow overflow-x-auto">
      <h3 class="text-lg font-semibold mb-4 text-blue-700 flex flex-wrap items-center gap-2">
        <span class="mr-2">📋</span> Danh sách đặt phòng
      </h3>

      <table class="min-w-full divide-y divide-gray-200 border text-sm">
        <thead class="bg-gray-100">
          <tr>
            <th class="px-3 py-2 text-left whitespace-nowrap">STT</th>
            <th class="px-3 py-2 text-left whitespace-nowrap">Ngày đến</th>
            <th class="px-3 py-2 text-left whitespace-nowrap">Ngày đi</th>
            <th class="px-3 py-2 text-left whitespace-nowrap">Giờ nhận</th>
            <th class="px-3 py-2 text-left whitespace-nowrap">Giờ trả</th>
            <th class="px-3 py-2 text-left max-w-[120px] truncate">Tên</th>
            <th class="px-3 py-2 text-left max-w-[160px] truncate">Email</th>
            <th class="px-3 py-2 text-left max-w-[120px] truncate">SĐT</th>
            <th class="px-3 py-2 text-left max-w-[140px] truncate">Loại phòng</th>
            <th class="px-3 py-2 text-left whitespace-nowrap">Số phòng</th>
            <th class="px-3 py-2 text-left whitespace-nowrap">Trạng thái</th>
            <!-- <th class="px-3 py-2 text-left whitespace-nowrap">Hành động</th> -->
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="(booking, index) in filteredBookings"
            :key="booking.bookingId"
            class="border-t"
          >
            <td class="px-3 py-2">{{ index + 1 }}</td>
            <td class="px-3 py-2 whitespace-nowrap">{{ booking.checkInDate }}</td>
            <td class="px-3 py-2 whitespace-nowrap">{{ booking.checkOutDate }}</td>
            <td class="px-3 py-2">{{ booking.checkInTime }}</td>
            <td class="px-3 py-2">{{ booking.checkOutTime }}</td>
            <td class="px-3 py-2 truncate max-w-[120px]">{{ booking.contactName }}</td>
            <td class="px-3 py-2 truncate max-w-[160px]">{{ booking.contactEmail }}</td>
            <td class="px-3 py-2 truncate max-w-[120px]">{{ booking.contactPhone }}</td>
            <td class="px-3 py-2 truncate max-w-[140px]">
              <span v-if="booking.rooms.length > 0">{{ booking.rooms[0].roomTypeName }}</span>
              <span v-else>—</span>
            </td>
            <td class="px-3 py-2">
              <span v-if="booking.rooms.length > 0">{{ booking.rooms[0].numberOfRooms }}</span>
              <span v-else>—</span>
            </td>
            <td class="px-3 py-2">
              <span
                class="inline-block px-2 py-1 rounded font-medium text-xs border whitespace-nowrap"
                :class="{
                  'bg-yellow-100 text-yellow-700 border-yellow-300': booking.status === 'PAID',
                  'bg-blue-100 text-blue-700 border-blue-300': booking.status === 'CONFIRMED',
                  'bg-green-100 text-green-700 border-green-300': booking.status === 'COMPLETED',
                }"
              >
                {{ getStatusLabel(booking.status) }}
              </span>
            </td>
          </tr>

          <tr v-if="filteredBookings.length === 0">
            <td
              colspan="12"
              class="text-center py-4 text-gray-500"
            >
              Không có dữ liệu phù hợp
            </td>
          </tr>
        </tbody>
      </table>
      <!-- Pagination -->
      <!-- <div class="mt-5 flex justify-center">
        <Pagination
          :total="totalElements"
          :items-per-page="size"
          :default-page="currentPage + 1"
          :sibling-count="1"
          @page-change="handlePageChange"
        />
      </div> -->
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { Icon } from '@iconify/vue'
import { getBookingsManager } from '@/services/supplier'
import Pagination from '@/components/base/Pagination.vue'
const bookings = ref<any[]>([])
const isDropdownOpen = ref(false)
const currentStatus = ref('ALL')

const states = [{ value: 'PAID', label: 'Chờ xác nhận' }]

onMounted(async () => {
  try {
    const res = await getBookingsManager()
    console.log('data dat phong', res)

    bookings.value = res.map((booking: any) => ({
      ...booking,
      statusDisplay: getStatusLabel(booking.status),
    }))
  } catch (error) {
    console.error('Lỗi khi lấy danh sách đặt phòng:', error)
  }
})

function getStatusLabel(status: string) {
  const match = states.find((s) => s.value === status)
  return match ? match.label : status
}

function toggleDropdown() {
  isDropdownOpen.value = !isDropdownOpen.value
}

function selectStatus(status: string) {
  currentStatus.value = status
  isDropdownOpen.value = false
}

const filteredBookings = computed(() => {
  if (currentStatus.value === 'ALL') return bookings.value
  return bookings.value.filter((b) => b.status === currentStatus.value)
})

const displayStatus = computed(() => {
  const found = states.find((s) => s.value === currentStatus.value)
  return found ? found.label : 'Tất cả'
})

function confirmBooking(bookingId: number) {
  const booking = bookings.value.find((b) => b.bookingId === bookingId)
  if (booking && booking.status === 'CONFIRMED') {
    booking.status = 'PAID'
    booking.statusDisplay = getStatusLabel('PAID')
  }
}
</script>

<style>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>

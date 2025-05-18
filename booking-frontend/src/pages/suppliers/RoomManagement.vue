<template>
  <div class="space-y-6 px-4 py-6 relative">
    <!-- Bộ lọc -->
    <div class="border rounded-lg p-6 bg-white shadow flex justify-between items-center relative">
      <h3 class="text-lg font-semibold text-blue-700 flex items-center"></h3>

      <!-- Icon lọc ở góc phải -->
      <div class="relative">
        <button
          @click="toggleDropdown"
          aria-label="Toggle Filter Dropdown"
          class="text-blue-700 text-2xl hover:text-blue-900 focus:outline-none"
        >
          <Icon icon="mdi:filter-variant" width="24" height="24" class="mr-2" />
        </button>

        <!-- Dropdown xổ xuống -->
        <transition name="fade">
          <ul
            v-if="isDropdownOpen"
            class="absolute right-0 top-full mt-2 w-44 bg-white border border-gray-300 rounded shadow-lg z-10"
          >
            <li
              v-for="state in states"
              :key="state.value"
              @click="selectStatus(state.value)"
              :class="[
                'px-4 py-2 cursor-pointer hover:bg-blue-100',
                currentStatus === state.value ? 'bg-blue-500 text-white' : 'text-gray-700'
              ]"
            >
              {{ state.label }}
            </li>
          </ul>
        </transition>
      </div>
    </div>

    <!-- Bảng danh sách -->
    <div class="border rounded-lg p-6 bg-white shadow">
      <h3 class="text-lg font-semibold mb-4 text-blue-700 flex">
        <span class="mr-2">📋</span> Danh sách đặt phòng
        <span class="ml-auto text-sm text-gray-500">
          (Lọc trạng thái: <strong>{{ displayStatus }}</strong>)
        </span>
      </h3>
      <table class="min-w-full divide-y divide-gray-200 border">
        <thead class="bg-gray-100">
          <tr>
            <th class="px-4 py-2 text-left">STT</th>
            <th class="px-4 py-2 text-left">Ngày đến</th>
            <th class="px-4 py-2 text-left">Ngày đi</th>
            <th class="px-4 py-2 text-left">Giờ nhận phòng</th>
            <th class="px-4 py-2 text-left">Giờ trả phòng</th>
            <th class="px-4 py-2 text-left">Số lượng người</th>
            <th class="px-4 py-2 text-left">Trạng thái</th>
            <th class="px-4 py-2 text-left">Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(booking, index) in filteredBookings" :key="booking.bookingId" class="border-t">
            <td class="px-4 py-2">{{ index + 1 }}</td>
            <td class="px-4 py-2 max-w-[160px] truncate">{{ booking.checkInDate }}</td>
            <td class="px-4 py-2 max-w-[160px] truncate">{{ booking.checkOutDate }}</td>
            <td class="px-4 py-2">{{ booking.checkInTime }}</td>
            <td class="px-4 py-2">{{ booking.checkOutTime }}</td>
            <td class="px-4 py-2">{{ booking.numberPeople }}</td>
            <td class="px-4 py-2">
              <span
                class="px-2 py-1 rounded font-semibold border"
                :class="{
                  'bg-yellow-100 text-yellow-700 border-yellow-300': booking.status === 'CONFIRMED',
                  'bg-green-100 text-green-700 border-green-300': booking.status === 'PAID',
                  'bg-red-100 text-red-700 border-red-300': booking.status === 'CANCELLED'
                }"
              >
                {{ booking.statusDisplay }}
              </span>
            </td>
            <td class="px-4 py-2">
              <button
                v-if="booking.status === 'CONFIRMED'"
                @click="confirmBooking(booking.bookingId)"
                class="p-2 bg-green-500 hover:bg-green-600 text-white rounded transition"
                title="Xác nhận đặt phòng"
              >
                <Icon icon="mdi:check" width="20" height="20" />
              </button>
              <span v-else class="text-gray-400">—</span>
            </td>
          </tr>
          <tr v-if="filteredBookings.length === 0">
            <td colspan="8" class="text-center py-4 text-gray-500">Không có dữ liệu phù hợp</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { Icon } from '@iconify/vue'

// Dữ liệu giả
const bookings = ref([
  {
    bookingId: 8,
    checkInDate: "2025-05-18",
    checkOutDate: "2025-05-20",
    checkInTime: "12:00:00",
    checkOutTime: "12:00:00",
    numberPeople: 2,
    status: "PAID",
    statusDisplay: "Đã thanh toán",
  },
  {
    bookingId: 9,
    checkInDate: "2025-06-10",
    checkOutDate: "2025-06-15",
    checkInTime: "14:00:00",
    checkOutTime: "12:00:00",
    numberPeople: 3,
    status: "CONFIRMED",
    statusDisplay: "Đang xác nhận",
  },
])

const isDropdownOpen = ref(false)
const currentStatus = ref('ALL')

const states = [
  { value: 'ALL', label: 'Tất cả' },
  { value: 'PAID', label: 'Đã thanh toán' },
  { value: 'CONFIRMED', label: 'Đang xác nhận' }
]

function toggleDropdown() {
  isDropdownOpen.value = !isDropdownOpen.value
}

function selectStatus(status: string) {
  currentStatus.value = status
  isDropdownOpen.value = false
}

const filteredBookings = computed(() => {
  if (currentStatus.value === 'ALL') {
    return bookings.value
  }
  return bookings.value.filter(b => b.status === currentStatus.value)
})

const displayStatus = computed(() => {
  const found = states.find(s => s.value === currentStatus.value)
  return found ? found.label : 'Tất cả'
})

// Hàm xác nhận đặt phòng (giả lập cập nhật trạng thái)
function confirmBooking(bookingId: number) {
  const booking = bookings.value.find(b => b.bookingId === bookingId)
  if (booking && booking.status === 'CONFIRMED') {
    booking.status = 'PAID'
    booking.statusDisplay = 'Đã thanh toán'
  }
}
</script>

<style>
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.2s ease;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}
</style>

<template>
  <div class="space-y-6 px-4 py-6">
    <div class="border rounded-lg p-6 bg-white shadow">
      <h3 class="text-lg font-semibold mb-4 text-blue-700 flex">
        <Icon
          icon="mdi:clipboard-list-outline"
          class="mr-2"
          width="24"
          height="24"
        />
        LỊCH SỬ GIAO DỊCH
      </h3>

      <div v-if="bookings.length">
        <table class="min-w-full divide-y divide-gray-200 border">
          <thead class="bg-gray-100">
            <tr>
              <th class="px-4 py-2 text-left">STT</th>
              <th class="px-4 py-2 text-left">Tên người đặt</th>
              <th class="px-4 py-2 text-left">Ngày nhận phòng</th>
              <th class="px-4 py-2 text-left">Ngày trả phòng</th>
              <th class="px-4 py-2 text-left">Tổng tiền</th>
              <th class="px-4 py-2 text-left">Trạng thái</th>
              <th class="px-4 py-2 text-center">Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(booking, index) in bookings"
              :key="booking.bookingId"
            >
              <td class="px-4 py-2">{{ index + 1 }}</td>
              <td class="px-4 py-2">{{ booking.user?.full_name || 'N/A' }}</td>
              <td class="px-4 py-2">{{ formatDate(booking.checkInDate) }}</td>
              <td class="px-4 py-2">{{ formatDate(booking.checkOutDate) }}</td>
              <td class="px-4 py-2">{{ formatCurrency(booking.bill?.total || 0) }}</td>
              <td class="px-4 py-2">
                <span
                  :class="statusClass(booking.statusDisplay)"
                  class="px-2 py-1 rounded-full text-sm font-medium inline-block"
                >
                  {{ booking.statusDisplay }}
                </span>
              </td>
              <td class="px-4 py-2 text-center space-x-2">
                <button
                  @click="viewBooking(booking)"
                  class="p-2 bg-blue-500 hover:bg-blue-600 text-white rounded"
                >
                  <Icon
                    icon="mdi:eye"
                    width="20"
                    height="20"
                  />
                </button>
                <button
                  v-if="canReview(booking.checkOutDate)"
                  @click="openReviewModal(booking)"
                  class="p-2 bg-green-500 hover:bg-green-600 text-white rounded"
                >
                  <Icon
                    icon="mdi:star"
                    width="20"
                    height="20"
                  />
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div
        v-else
        class="text-center py-6 text-gray-500"
      >
        Không có dữ liệu đặt phòng...
      </div>
    </div>

    <ReviewModal
      v-if="showReviewModal"
      :booking="bookingToReview"
      @close="showReviewModal = false"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Icon } from '@iconify/vue'
import { getBookingListApi } from '@/services/booking'
import type { BookingListItem } from '@/types/booking'
import ReviewModal from './ReviewModal.vue'

const bookings = ref<BookingListItem[]>([])
const showReviewModal = ref(false)
const bookingToReview = ref<BookingListItem | null>(null)

onMounted(async () => {
  try {
    const data = await getBookingListApi()
    bookings.value = data
  } catch (error) {
    console.error('Lỗi khi lấy danh sách đặt phòng:', error)
  }
})

function openReviewModal(booking: BookingListItem) {
  bookingToReview.value = booking
  showReviewModal.value = true
}

function formatDate(dateStr: string) {
  return new Date(dateStr).toLocaleDateString('vi-VN')
}

function formatCurrency(amount: number) {
  return amount.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })
}

function statusClass(status: string) {
  switch (status.toLowerCase()) {
    case 'đã xác nhận':
      return 'border border-green-500 bg-green-100 text-green-700'
    case 'chờ xác nhận':
      return 'border border-yellow-500 bg-yellow-100 text-yellow-700'
    case 'đã hủy':
      return 'border border-red-500 bg-red-100 text-red-700'
    default:
      return 'border border-gray-400 bg-gray-100 text-gray-700'
  }
}

function canReview(checkOutDate: string): boolean {
  return new Date() > new Date(checkOutDate)
}
</script>

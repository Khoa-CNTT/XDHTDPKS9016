<template>
  <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 px-4">
    <div
      class="bg-white rounded-lg shadow-xl p-8 w-full max-w-2xl relative max-h-[80vh] overflow-y-auto"
      role="dialog"
      aria-modal="true"
      aria-labelledby="modal-title"
    >
      <button
        class="absolute top-4 right-4 text-gray-400 hover:text-gray-700 transition-colors"
        @click="$emit('close')"
        aria-label="Đóng"
      >
        <Icon
          icon="mdi:close"
          width="24"
          height="24"
        />
      </button>

      <h2
        id="modal-title"
        class="text-2xl font-semibold mb-6 text-blue-600 flex items-center gap-2 select-none"
      >
        <Icon
          icon="mdi:information-outline"
          width="24"
          height="24"
        />
        Chi tiết đặt phòng
      </h2>

      <div class="space-y-4 text-gray-700 text-base">
        <div class="flex justify-between border-b border-gray-200 pb-2">
          <span class="font-semibold">Người đặt:</span>
          <span>{{ booking.contactName ?? 'N/A' }}</span>
        </div>
        <div class="flex justify-between border-b border-gray-200 pb-2">
          <span class="font-semibold">Khách sạn:</span>
          <span>{{ booking.hotel?.name ?? 'N/A' }}</span>
        </div>
        <div class="flex justify-between border-b border-gray-200 pb-2">
          <span class="font-semibold">Ngày nhận phòng:</span>
          <span>{{ formatDate(booking.checkInDate) }}</span>
        </div>
        <div class="flex justify-between border-b border-gray-200 pb-2">
          <span class="font-semibold">Giờ nhận phòng:</span>
          <span>{{ booking.checkInTime ?? 'N/A' }}</span>
        </div>
        <div class="flex justify-between border-b border-gray-200 pb-2">
          <span class="font-semibold">Ngày trả phòng:</span>
          <span>{{ formatDate(booking.checkOutDate) }}</span>
        </div>
        <div class="flex justify-between border-b border-gray-200 pb-2">
          <span class="font-semibold">Giờ trả phòng:</span>
          <span>{{ booking.checkOutTime ?? 'N/A' }}</span>
        </div>
        <div class="flex justify-between border-b border-gray-200 pb-2">
          <span class="font-semibold">Số người:</span>
          <span>{{ booking.numberPeople ?? 'N/A' }}</span>
        </div>

        <div class="mt-4 pt-4 border-t border-gray-300 space-y-2 text-gray-800 font-medium">
          <div class="flex justify-between">
            <span>Tổng tiền phòng:</span>
            <span>{{ formatCurrency(booking.bill?.roomTotal ?? 0) }}</span>
          </div>
          <div class="flex justify-between">
            <span>Tổng tiền dịch vụ:</span>
            <span>{{ formatCurrency(booking.bill?.serviceTotal ?? 0) }}</span>
          </div>
          <!-- <div class="flex justify-between">
            <span>Tiền đặt cọc:</span>
            <span>{{ formatCurrency(booking.bill?.deposit ?? 0) }}</span>
          </div> -->
          <div class="flex justify-between text-blue-700 text-lg font-semibold">
            <span>Tổng tiền:</span>
            <span>{{ formatCurrency(booking.bill?.total ?? 0) }}</span>
          </div>
          <!-- <div class="flex justify-between">
            <span>Trạng thái:</span>
            <span>{{ booking.statusDisplay ?? 'N/A' }}</span>
          </div> -->
        </div>

        <div class="mt-6 text-gray-600 italic">
          <strong>Ghi chú:</strong> {{ booking.specialRequests ?? 'Không có' }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Icon } from '@iconify/vue'
import type { BookingListItem } from '@/types/booking'

defineProps<{
  booking: BookingListItem
}>()

function formatDate(dateStr?: string): string {
  if (!dateStr) return 'N/A'
  const d = new Date(dateStr)
  return d.toLocaleDateString('vi-VN')
}

function formatCurrency(amount: number): string {
  return amount.toLocaleString('vi-VN', {
    style: 'currency',
    currency: 'VND',
  })
}
</script>

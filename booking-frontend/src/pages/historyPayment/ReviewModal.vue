<template>
  <div
    class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50"
    @click.self="close"
  >
    <div class="bg-white rounded-xl shadow-lg w-full max-w-lg p-6 relative">
      <h3 class="text-lg font-semibold text-blue-700 flex items-center mb-4">
        <Icon
          icon="mdi:star-circle-outline"
          class="mr-2"
          width="24"
          height="24"
        />
        Chi tiết đánh giá
      </h3>

      <div v-if="booking?.review">
        <div class="mb-3">
          <span class="font-semibold">Người đánh giá:</span>
          {{ booking.user?.full_name || 'N/A' }}
        </div>

        <div class="mb-3">
          <span class="font-semibold">Số sao:</span>
          <span class="text-yellow-500">
            <Icon
              v-for="i in booking.review.rating"
              :key="i"
              icon="mdi:star"
              width="20"
              height="20"
            />
          </span>
        </div>

        <div>
          <span class="font-semibold">Nội dung đánh giá:</span>
          <p class="mt-1 text-gray-700 whitespace-pre-line">
            {{ booking.review.comment || 'Không có nội dung' }}
          </p>
        </div>
      </div>
      <div
        v-else
        class="text-gray-500 italic"
      >
        Chưa có đánh giá nào.
      </div>

      <button
        @click="close"
        class="absolute top-3 right-3 text-gray-500 hover:text-gray-800 transition"
      >
        <Icon
          icon="mdi:close"
          width="24"
          height="24"
        />
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Icon } from '@iconify/vue'
import type { BookingListItem } from '@/types/booking'

defineProps<{
  booking: BookingListItem | null
}>()

const emit = defineEmits<{
  (e: 'close'): void
}>()

function close() {
  emit('close')
}
</script>

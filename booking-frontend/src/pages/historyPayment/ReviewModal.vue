<template>
  <div class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
    <div class="bg-white p-6 rounded-lg w-full max-w-lg">
      <h3 class="text-lg font-semibold mb-4 text-blue-700">Đánh giá phòng</h3>

      <div class="space-y-4">
        <textarea
          v-model="comment"
          placeholder="Nhập nhận xét..."
          class="w-full border rounded p-3"
        ></textarea>

        <div class="flex items-center space-x-2">
          <span class="text-sm text-gray-700">Đánh giá:</span>
          <select
            v-model.number="score"
            class="border rounded p-2"
          >
            <option
              v-for="i in 5"
              :key="i"
              :value="i"
            >
              {{ i }} sao
            </option>
          </select>
        </div>

        <div class="flex justify-end space-x-2 pt-4">
          <button
            @click="$emit('close')"
            class="px-4 py-2 bg-gray-300 hover:bg-gray-400 rounded"
          >
            Hủy
          </button>
          <button
            @click="submitReview"
            class="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded"
          >
            Gửi đánh giá
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, toRef } from 'vue'
import type { BookingListItem } from '@/types/booking'
import { toast } from 'vue3-toastify'
import { postCommentApi, postRatingApi } from '@/services/booking'

const props = defineProps<{
  booking: BookingListItem
}>()

const emit = defineEmits(['close'])

const comment = ref('')
const score = ref(5)

// Khi prop booking thay đổi, bạn có thể reset form nếu muốn
watch(() => props.booking, () => {
  comment.value = ''
  score.value = 5
})

const submitReview = async () => {
  try {
    const room = props.booking.rooms?.[0]
    if (!room) throw new Error('Không tìm thấy thông tin phòng')

    await postCommentApi(room.roomId, comment.value)
    await postRatingApi(room.roomId, score.value)

    toast.success('Đánh giá thành công!')
    comment.value = ''
    score.value = 5
    emit('close')
  } catch (error: any) {
    console.error('Lỗi khi gửi đánh giá:', error)
    toast.error(error.message || 'Gửi đánh giá thất bại')
  }
}
</script>


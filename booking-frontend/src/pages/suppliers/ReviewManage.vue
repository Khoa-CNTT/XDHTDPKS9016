<template>
  <div class="card">
    <div class="card-body">
      <CustomTable :headers="tableHeaders" :rows="commentData">
        <template #actions="{ row, index }">
          <div class="flex gap-2 justify-end">

            <button @click="toggleReplyForm(index)"
              class="px-4 py-2 bg-blue-600 text-white rounded-lg shadow-md hover:bg-blue-700 transition duration-300">
              Trả lời
            </button>

            <button @click="hideComment(index)"
              class="px-4 py-2 bg-gray-600 text-white rounded-lg shadow-md hover:bg-gray-700 transition duration-300">
              Ẩn
            </button>
          </div>


          <div v-if="showReplyForm === index" class="mt-4 p-4 bg-gray-50 border border-gray-200 rounded-lg shadow-lg">
            <textarea v-model="replyContent"
              class="w-full p-3 border border-gray-300 rounded-md text-gray-800 focus:ring-2 focus:ring-blue-500 focus:outline-none"
              rows="4" placeholder="Nhập câu trả lời..."></textarea>
            <button @click="submitReply(index)"
              class="mt-3 px-5 py-2 bg-green-600 text-white rounded-lg shadow-md hover:bg-green-700 transition duration-300">
              Gửi câu trả lời
            </button>
          </div>


          <div v-if="row.reply"
            class="mt-4 pl-4 text-gray-700 bg-blue-50 p-4 border-l-4 border-blue-500 rounded-lg shadow-md">
            <strong class="text-blue-600">Câu trả lời:</strong>
            <p class="mt-2">{{ row.reply }}</p>
          </div>
        </template>
      </CustomTable>



    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import CustomTable from '@/components/base/CustomTable.vue'

const tableHeaders = ['Tên khách', 'Nội dung', 'Đánh giá', 'Ngày', 'Hành động']
const commentData = ref([
  { customerName: 'Minh Khoa', content: 'Phòng sạch, view đẹp', rating: '⭐⭐⭐⭐', date: '02/04', reply: '' },
  { customerName: 'Lan Chi', content: 'Dịch vụ tốt, nhân viên thân thiện', rating: '⭐⭐⭐⭐⭐', date: '01/04', reply: '' },
])

const showReplyForm = ref<number | null>(null)
const replyContent = ref('')

const toggleReplyForm = (index: number) => {
  showReplyForm.value = showReplyForm.value === index ? null : index
}

const submitReply = (index: number) => {
  if (replyContent.value.trim() !== '') {
    commentData.value[index].reply = replyContent.value.trim()
    showReplyForm.value = null
    replyContent.value = ''

    console.log('Câu trả lời đã được gửi:', commentData.value[index].reply)
  } else {
    console.log('Câu trả lời không được để trống')
  }
}

const hideComment = (index: number) => {
  commentData.value.splice(index, 1)
  console.log('Đã ẩn bình luận tại index:', index)
}
</script>

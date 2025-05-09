<template>
    <div class="w-full max-w-4xl mx-auto p-4 bg-gray-50 min-h-screen">
      <!-- Hotel Info -->
      <div class="mb-6">
        <h1 class="text-3xl font-bold text-gray-800 mb-2">Khách sạn Paradise</h1>
        <p class="text-gray-600">Địa chỉ: 123 Đường Biển, Đà Nẵng</p>
      </div>
  
      <!-- Review List -->
      <div class="space-y-4 mb-8">
        <h2 class="text-xl font-semibold text-gray-700">📝 Đánh giá của khách</h2>
        <div
          v-for="(review, index) in reviews"
          :key="index"
          class="bg-white p-4 rounded-lg shadow-md hover:shadow-lg transition"
        >
          <div class="flex items-center justify-between">
            <h3 class="font-medium text-indigo-700">{{ review.name }}</h3>
            <span class="text-yellow-500">⭐ {{ review.rating }}/5</span>
          </div>
          <p class="text-gray-600 mt-2">{{ review.comment }}</p>
  
          <!-- Nếu là comment của chính mình -->
          <div v-if="review.name === currentUser" class="flex justify-end gap-3 mt-3 text-sm">
            <button
              @click="startEditReview(index)"
              class="text-blue-600 hover:underline font-medium"
            >Sửa</button>
            <button
              @click="deleteReview(index)"
              class="text-red-500 hover:underline font-medium"
            >Xoá</button>
          </div>
        </div>
      </div>
  
      <!-- Submit Review Form -->
      <div class="bg-white p-6 rounded-lg shadow-lg">
        <h2 class="text-xl font-semibold text-gray-700 mb-4">
          {{ isEditing ? '✏️ Cập nhật đánh giá' : '🖊️ Gửi đánh giá của bạn' }}
        </h2>
        <form @submit.prevent="submitReview">
          <input
            v-model="form.name"
            type="text"
            class="w-full mb-3 px-4 py-2 border rounded-lg"
            readonly
          />
          <select
            v-model="form.rating"
            class="w-full mb-3 px-4 py-2 border rounded-lg"
            required
          >
            <option value="" disabled>Chọn sao đánh giá</option>
            <option v-for="n in 5" :key="n" :value="n">{{ n }} sao</option>
          </select>
          <textarea
            v-model="form.comment"
            rows="4"
            class="w-full mb-3 px-4 py-2 border rounded-lg"
            placeholder="Viết đánh giá của bạn..."
            required
          ></textarea>
          <div class="flex justify-between gap-3">
            <button
              type="submit"
              class="flex-1 bg-indigo-600 text-white py-2 rounded-lg hover:bg-indigo-700 transition"
            >
              {{ isEditing ? 'Cập nhật' : 'Gửi đánh giá' }}
            </button>
            <button
              v-if="isEditing"
              type="button"
              @click="cancelEdit"
              class="flex-1 border border-gray-300 text-gray-700 py-2 rounded-lg hover:bg-gray-100 transition"
            >
              Hủy
            </button>
          </div>
        </form>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  
  // Tên người dùng đang đăng nhập
  const currentUser = 'Minh Thư'
  
  // Danh sách đánh giá ban đầu
  const reviews = ref([
    { name: 'Minh Thư', rating: 5, comment: 'Khách sạn tuyệt vời, phục vụ rất chu đáo!' },
    { name: 'Quang', rating: 4, comment: 'Phòng sạch, view đẹp nhưng hơi ồn.' }
  ])
  
  // Biến lưu dữ liệu form
  const form = ref({
    name: currentUser,
    rating: '',
    comment: ''
  })
  
  // Index đánh giá đang được chỉnh sửa, nếu null nghĩa là đang tạo mới
  const editIndex = ref(null)
  
  // Kiểm tra trạng thái chỉnh sửa
  const isEditing = computed(() => editIndex.value !== null)
  
  // Gửi hoặc cập nhật đánh giá
  const submitReview = () => {
    if (!form.value.rating || !form.value.comment) return
  
    const review = { ...form.value }
  
    if (editIndex.value !== null) {
      // Cập nhật đánh giá
      reviews.value[editIndex.value] = review
      editIndex.value = null
    } else {
      // Gửi đánh giá mới
      reviews.value.push(review)
    }
  
    resetForm()
  }
  
  // Bắt đầu chỉnh sửa đánh giá
  const startEditReview = (index) => {
    const review = reviews.value[index]
    if (review.name === currentUser) {
      form.value = { ...review }
      editIndex.value = index
    }
  }
  
  // Hủy chỉnh sửa
  const cancelEdit = () => {
    resetForm()
    editIndex.value = null
  }
  
  // Xoá đánh giá
  const deleteReview = (index) => {
    if (reviews.value[index].name === currentUser && confirm('Bạn có chắc muốn xoá đánh giá này?')) {
      reviews.value.splice(index, 1)
      if (editIndex.value === index) {
        cancelEdit()
      }
    }
  }
  
  // Reset form
  const resetForm = () => {
    form.value = {
      name: currentUser,
      rating: '',
      comment: ''
    }
  }
  </script>
  
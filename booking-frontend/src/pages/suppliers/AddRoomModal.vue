<template>
  <transition name="fade">
    <div
      v-if="show"
      class="fixed inset-0 z-60 flex items-center justify-center bg-black bg-opacity-50"
      @click.self="closeModal"
    >
      <div
        class="bg-white rounded-3xl shadow-2xl p-8 max-w-md w-full mx-4 relative"
        @click.stop
      >
        <button
          class="absolute top-5 right-5 text-gray-400 hover:text-gray-700 transition"
          @click="closeModal"
          aria-label="Đóng"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-6 w-6"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
            stroke-width="2"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              d="M6 18L18 6M6 6l12 12"
            />
          </svg>
        </button>

        <h3 class="text-2xl font-semibold text-gray-800 mb-6">Thêm phòng mới</h3>

        <form
          @submit.prevent="createRoom"
          class="space-y-5 text-gray-700 text-base"
        >
          <div>
            <label class="block font-medium mb-1">Loại phòng</label>
            <select
              v-model.number="form.room_type_id"
              class="w-full border rounded px-3 py-2"
              required
            >
              <option
                value=""
                disabled
              >
                Chọn loại phòng
              </option>
              <option
                v-for="type in roomTypes"
                :key="type.room_type_id"
                :value="type.room_type_id"
              >
                {{ type.type_name }}
              </option>
            </select>
          </div>

          <div>
            <label class="block font-medium mb-1">Giá (đồng)</label>
            <input
              v-model.number="form.price"
              type="number"
              min="0"
              class="w-full border rounded px-3 py-2"
              required
            />
          </div>

          <div>
            <label class="block font-medium mb-1">Số giường</label>
            <input
              v-model.number="form.number_bed"
              type="number"
              min="1"
              class="w-full border rounded px-3 py-2"
              required
            />
          </div>

          <div class="flex justify-end gap-4 mt-6">
            <button
              type="submit"
              class="px-6 py-2 bg-blue-600 text-white rounded-lg shadow hover:bg-blue-700 transition"
              :disabled="loading"
            >
              {{ loading ? 'Đang lưu...' : 'Thêm' }}
            </button>
            <button
              type="button"
              class="px-6 py-2 bg-gray-400 text-white rounded-lg shadow hover:bg-gray-500 transition"
              @click="closeModal"
              :disabled="loading"
            >
              Hủy
            </button>
          </div>
        </form>
      </div>
    </div>
  </transition>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getRoomTypesApi, createRoomApi } from '@/services/supplier'
import type { RoomTypeSummary, RoomTypeResponse, Room } from '@/types/supplier'
import { toast } from 'vue3-toastify'
import 'vue3-toastify/dist/index.css'

const emit = defineEmits(['close', 'created'])

const show = ref(true)
const loading = ref(false)
const roomTypes = ref<RoomTypeSummary[]>([])

const form = reactive({
  room_type_id: 0,
  price: 0,
  number_bed: 1,
})

onMounted(async () => {
  try {
    const res: RoomTypeResponse = await getRoomTypesApi()
    roomTypes.value = res.content
  } catch (error) {
    toast.error('Lấy danh sách loại phòng thất bại')
  }
})

const closeModal = () => {
  show.value = false
  setTimeout(() => {
    emit('close')
  }, 300) // tương ứng thời gian fade animation
}

const createRoom = async () => {
  if (!form.room_type_id) {
    toast.error('Vui lòng chọn loại phòng')
    return
  }

  loading.value = true
  try {
    const newRoom: Partial<Room> = {
      price: form.price,
      number_bed: form.number_bed,
      status: 'AVAILABLE', // mặc định khi tạo, không có chọn trạng thái
    }

    // Giao diện hiện tại không có room_type_id trong Room interface,
    // nếu API cần thì bạn xử lý truyền đúng tham số trong createRoomApi
    // ví dụ: createRoomApi({...newRoom, room_type_id: form.room_type_id})

    const created = await createRoomApi({ ...newRoom, room_type_id: form.room_type_id } as any)

    toast.success('Tạo phòng thành công!')
    emit('created', created)
    closeModal()
  } catch (error: any) {
    toast.error('Lỗi khi tạo phòng')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>

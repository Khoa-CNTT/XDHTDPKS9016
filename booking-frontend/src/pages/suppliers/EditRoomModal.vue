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

        <h3 class="text-2xl font-semibold text-gray-800 mb-6">
          Sửa phòng <span class="text-blue-600">P{{ room.id_room }}</span>
        </h3>

        <form
          @submit.prevent="saveRoom"
          class="space-y-5 text-gray-700 text-base"
        >
          <div>
            <label class="block font-medium mb-1">Trạng thái</label>
            <select
              v-model="form.status"
              class="w-full border rounded px-3 py-2"
              required
            >
              <option value="AVAILABLE">Còn trống</option>
              <option value="UNAVAILABLE">Đang có khách</option>
            </select>
          </div>

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
              class="px-6 py-2 bg-green-600 text-white rounded-lg shadow hover:bg-green-700 transition"
              :disabled="loading"
            >
              {{ loading ? 'Đang lưu...' : 'Lưu' }}
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
import { getRoomTypesApi, updateRoomApi } from '@/services/supplier'
import type { Room, RoomTypeResponse, RoomTypeSummary } from '@/types/supplier'
import { ref, reactive, watch, onMounted } from 'vue'
import { toast } from 'vue3-toastify'
import 'vue3-toastify/dist/index.css'

const props = defineProps({
  room: { type: Object as () => Room, required: true },
})
const emit = defineEmits(['close', 'saved'])

const show = ref(true)
const loading = ref(false)
const roomTypes = ref<RoomTypeSummary[]>([])

const form = reactive({
  status: props.room.status,
  room_type_id: 0, // sẽ gán sau khi load loại phòng
  price: props.room.price,
  number_bed: props.room.number_bed,
})

onMounted(async () => {
  try {
    const res: RoomTypeResponse = await getRoomTypesApi()
    roomTypes.value = res.content

    const found = roomTypes.value.find((t) => t.type_name === props.room.type_name)
    form.room_type_id = found ? found.room_type_id : 0
  } catch (error) {
    toast.error('Lấy danh sách loại phòng thất bại')
  }
})

watch(
  () => props.room,
  (newRoom) => {
    form.status = newRoom.status
    form.price = newRoom.price
    form.number_bed = newRoom.number_bed
    const found = roomTypes.value.find((t) => t.type_name === newRoom.type_name)
    form.room_type_id = found ? found.room_type_id : 0
  },
  { immediate: true },
)

const closeModal = () => {
  show.value = false
  setTimeout(() => {
    emit('close')
  }, 300) // thời gian animation fade
}

const saveRoom = async () => {
  if (!form.room_type_id) {
    toast.error('Vui lòng chọn loại phòng')
    return
  }
  loading.value = true
  try {
    const payload = {
      status: form.status,
      room_type_id: form.room_type_id,
      price: form.price,
      number_bed: form.number_bed,
    }

    const updatedRoom = await updateRoomApi(props.room.id_room, payload)
    toast.success('Sửa phòng thành công!')
    emit('saved', updatedRoom)
    closeModal()
  } catch (error: any) {
    toast.error('Lỗi khi sửa phòng')
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

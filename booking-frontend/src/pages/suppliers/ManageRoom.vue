<template>
  <div class="p-6 max-w-7xl mx-auto">
    <!-- Tiêu đề và nút thêm mới -->
    <div class="flex justify-between items-center mb-8">
      <h2 class="text-3xl font-bold text-gray-800">Sơ đồ phòng khách sạn</h2>
      <button
        class="px-4 py-2 bg-green-600 text-white rounded-lg shadow hover:bg-green-700 transition"
        @click="showAddModal = true"
      >
        + Thêm mới
      </button>
    </div>

    <!-- Grid các phòng -->
    <div class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-6 gap-6 justify-center">
      <div
        v-for="room in rooms"
        :key="room.id_room"
        class="w-24 h-24 flex flex-col items-center justify-center rounded-xl font-semibold cursor-pointer select-none shadow-md transition duration-300 ease-in-out text-white"
        :class="
          room.status === 'AVAILABLE'
            ? 'bg-green-500 hover:bg-green-600'
            : 'bg-red-500 hover:bg-red-600'
        "
        @click="selectRoom(room)"
        :title="`Phòng ${room.id_room} - ${statusText(room.status)}`"
      >
        <div class="text-2xl font-bold">P{{ room.id_room }}</div>
        <div class="text-sm opacity-90 mt-1">🛏 {{ room.number_bed }} giường</div>
      </div>
    </div>

    <!-- Modal chi tiết phòng -->
    <transition name="fade">
      <div
        v-if="selectedRoom && !showEditModal"
        class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50"
        @click.self="closeDetailModal"
      >
        <div
          class="bg-white rounded-3xl shadow-2xl p-8 max-w-md w-full mx-4 relative"
          @click.stop
        >
          <!-- Close -->
          <button
            class="absolute top-5 right-5 text-gray-400 hover:text-gray-700 transition"
            @click="closeDetailModal"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="h-6 w-6"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="M6 18L18 6M6 6l12 12"
              />
            </svg>
          </button>

          <!-- Nội dung -->
          <h3 class="text-2xl font-semibold text-gray-800 mb-6">
            Chi tiết phòng <span class="text-blue-600">P{{ selectedRoom.id_room }}</span>
          </h3>
          <div class="space-y-4 text-gray-700 text-base">
            <p>
              <span class="font-medium">Trạng thái:</span>
              <span
                :class="[
                  selectedRoom.status === 'AVAILABLE' ? 'text-green-600' : 'text-red-600',
                  'font-semibold flex items-center gap-1',
                ]"
              >
                <template v-if="selectedRoom.status === 'AVAILABLE'"> ✅ Còn trống </template>
                <template v-else> ❌ Đang có khách </template>
              </span>
            </p>
            <p><span class="font-medium">Loại phòng:</span> {{ selectedRoom.type_name }}</p>
            <p><span class="font-medium">Giá:</span> {{ selectedRoom.price.toLocaleString() }} đ</p>
            <p><span class="font-medium">Số giường:</span> {{ selectedRoom.number_bed }}</p>
          </div>

          <!-- Nút hành động -->
          <div class="mt-8 flex justify-end gap-4">
            <button
              class="px-6 py-2 bg-yellow-500 text-white rounded-lg shadow hover:bg-yellow-600 transition"
              @click="showEditModal = true"
            >
              Sửa
            </button>
            <button
              class="px-6 py-2 bg-red-600 text-white rounded-lg shadow hover:bg-red-700 transition"
              @click="confirmDeleteRoom(selectedRoom)"
            >
              Xóa
            </button>
            <button
              class="px-6 py-2 bg-blue-600 text-white rounded-lg shadow hover:bg-blue-700 transition"
              @click="closeDetailModal"
            >
              Đóng
            </button>
          </div>
        </div>
      </div>
    </transition>

    <!-- Modal xác nhận xóa -->
    <div
      v-if="showDeleteModal"
      class="fixed inset-0 bg-gray-600 bg-opacity-50 flex justify-center items-center z-50"
      @click.self="cancelDelete"
    >
      <div class="bg-white p-6 rounded-lg shadow-lg w-full max-w-md">
        <div class="text-center mb-6">
          <h3 class="text-lg font-medium text-gray-900">Xác nhận xóa phòng</h3>
          <p class="mt-2 mb-6">
            Bạn có chắc chắn muốn xóa phòng
            <span class="font-semibold">P{{ selectedRoomForDelete?.id_room }}</span> không?
          </p>
        </div>
        <div class="flex justify-center gap-3">
          <button
            @click="cancelDelete"
            class="px-4 py-2 bg-gray-300 text-gray-700 rounded hover:bg-gray-400 transition"
          >
            Hủy
          </button>
          <button
            @click="deleteRoom"
            class="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700 transition"
          >
            Xóa
          </button>
        </div>
      </div>
    </div>

    <!-- Modal sửa -->
    <EditRoomModal
      v-if="showEditModal"
      :room="selectedRoom"
      @close="showEditModal = false"
      @saved="onRoomUpdated"
    />

    <!-- Modal thêm -->
    <AddRoomModal
      v-if="showAddModal"
      @close="showAddModal = false"
      @created="fetchRooms"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getRoomsApi, deleteRoomApi } from '@/services/supplier'
import EditRoomModal from './EditRoomModal.vue'
import AddRoomModal from './AddRoomModal.vue'
import { toast } from 'vue3-toastify'
import 'vue3-toastify/dist/index.css'

const rooms = ref([])
const selectedRoom = ref(null)
const selectedRoomForDelete = ref(null)
const showEditModal = ref(false)
const showAddModal = ref(false)
const showDeleteModal = ref(false)

const fetchRooms = async () => {
  try {
    const data = await getRoomsApi()
    rooms.value = data
  } catch (error) {
    console.error('Lỗi tải phòng:', error)
    toast.error('Lỗi tải phòng')
  }
}
onMounted(fetchRooms)

const statusText = (status) =>
  status === 'AVAILABLE' ? 'Còn trống' : status === 'UNAVAILABLE' ? 'Đang có khách' : 'Không rõ'

const selectRoom = (room) => {
  selectedRoom.value = room
}
const closeDetailModal = () => {
  selectedRoom.value = null
}

const onRoomUpdated = (updatedRoom) => {
  const index = rooms.value.findIndex((r) => r.id_room === updatedRoom.id_room)
  if (index !== -1) rooms.value[index] = { ...updatedRoom }
  selectedRoom.value = { ...updatedRoom }
  showEditModal.value = false
}

const confirmDeleteRoom = (room) => {
  selectedRoomForDelete.value = room
  showDeleteModal.value = true
}
const cancelDelete = () => {
  selectedRoomForDelete.value = null
  showDeleteModal.value = false
}
const deleteRoom = async () => {
  try {
    await deleteRoomApi(selectedRoomForDelete.value.id_room)
    rooms.value = rooms.value.filter((r) => r.id_room !== selectedRoomForDelete.value.id_room)
    toast.success('Xóa phòng thành công!')
    if (selectedRoom.value?.id_room === selectedRoomForDelete.value.id_room) {
      selectedRoom.value = null
    }
  } catch (err) {
    toast.error('Lỗi khi xóa phòng')
  } finally {
    cancelDelete()
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

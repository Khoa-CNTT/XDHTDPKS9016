<template>
  <div class="p-6">
    <h2 class="text-2xl font-bold mb-6">Sơ đồ phòng khách sạn</h2>

    <!-- Grid các phòng -->
    <div class="grid grid-cols-5 gap-[2px] justify-center">
      <div
        v-for="(room, index) in rooms"
        :key="room.number"
        class="w-14 h-14 text-sm flex items-center justify-center border 
               rounded-sm cursor-pointer font-bold text-white select-none"
        :class="[
          room.status === 'occupied' ? 'bg-red-600' :
          room.status === 'reserved' ? 'bg-yellow-400 text-black' :
          'bg-green-600',
          index % 2 === 0 ? 'bg-opacity-90' : 'bg-opacity-100',
          'col-background'
        ]"
        @click="selectRoom(room)"
        :title="`Phòng ${room.number} - ${statusText(room.status)}`"
      >
        {{ room.number }}
      </div>
    </div>

    <!-- Chi tiết phòng -->
    <div v-if="selectedRoom" class="mt-6 p-4 border rounded shadow bg-white max-w-sm">
      <h3 class="text-lg font-semibold mb-2">Chi tiết phòng {{ selectedRoom.number }}</h3>
      <p>Trạng thái: 
        <span :class="statusClass(selectedRoom.status)">
          {{ statusText(selectedRoom.status) }}
        </span>
      </p>
      <button class="mt-4 px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700" @click="selectedRoom = null">
        Đóng
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// Danh sách 25 phòng
const rooms = ref(
  Array.from({ length: 25 }, (_, i) => ({
    number: 101 + i,
    status: ['occupied', 'reserved', 'available'][Math.floor(Math.random() * 3)],
  }))
)

const selectedRoom = ref(null)

const selectRoom = (room) => {
  selectedRoom.value = room
}

const statusText = (status) => {
  switch (status) {
    case 'occupied': return 'Có người ở'
    case 'reserved': return 'Đã đặt, chưa ở'
    case 'available': return 'Còn trống'
    default: return ''
  }
}

const statusClass = (status) => {
  switch (status) {
    case 'occupied': return 'text-red-600 font-semibold'
    case 'reserved': return 'text-yellow-600 font-semibold'
    case 'available': return 'text-green-600 font-semibold'
    default: return ''
  }
}
</script>

<style scoped>
/* Ghi nhạt cho các ô ở vị trí cột chẵn */
.col-background:nth-child(5n+1),
.col-background:nth-child(5n+3),
.col-background:nth-child(5n+5) {
  background-color: #e5e7eb !important; /* Tailwind's gray-200 */
  color: black;
}
</style>

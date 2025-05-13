<template>
  <div class="space-y-6 px-4 py-6">
    <!-- Tiêu đề -->
    <div class="border rounded-lg p-6 bg-white shadow flex justify-between items-center">
      <h3 class="text-lg font-semibold text-blue-700 flex">
        <Icon icon="mdi:hotel" width="24" height="24" class="mr-2" />
        Quản lý phòng
      </h3>
      <button @click="showAddModal = true"
        class="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 transition flex">
        <Icon icon="mdi:plus-box" class="text-xl pr-2" width="24" height="24" /> Thêm phòng mới
      </button>
    </div>

    <!-- Bảng danh sách phòng -->
    <div class="border rounded-lg p-6 bg-white shadow">
      <h3 class="text-lg font-semibold mb-4 text-blue-700 flex">
        <Icon icon="mdi:bed-outline" class="mr-2 text-xl mr-2" width="24" height="24" /> Danh sách phòng
      </h3>
      <table class="min-w-full divide-y divide-gray-200 border">
        <thead class="bg-gray-100">
          <tr>
            <th class="px-4 py-2 text-left">STT</th>
            <th class="px-4 py-2 text-left">Tên phòng</th>
            <th class="px-4 py-2 text-left">Loại phòng</th>
            <th class="px-4 py-2 text-left">Giá</th>
            <th class="px-4 py-2 text-left">Trạng thái</th>
            <th class="px-4 py-2 text-center">Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(room, index) in rooms" :key="index" class="border-t">
            <td class="px-4 py-2">{{ index + 1 }}</td>
            <td class="px-4 py-2">{{ room.name }}</td>
            <td class="px-4 py-2">{{ room.type }}</td>
            <td class="px-4 py-2">{{ room.price.toLocaleString() }} VND</td>
            <td class="px-4 py-2">
              <span class="px-2 py-1 rounded font-semibold border" :class="{
                'bg-green-100 text-green-700 border-green-300': room.status === 'Available',
                'bg-red-100 text-red-700 border-red-300': room.status === 'Occupied'
              }">
                {{ room.status }}
              </span>
            </td>
            <td class="px-4 py-2 text-center">
              <div class="flex justify-center gap-3">
                <!-- Chỉnh sửa -->
                <button @click="editRoom(room)"
                 class="p-2 bg-green-500 hover:bg-green-600 text-white rounded transition">
                  <Icon icon="mdi:pencil" width="20" height="20" class="mr-1" />

                </button>

                <!-- Xóa -->
                <button @click="deleteRoom(room)"
                 class="p-2 bg-red-500 hover:bg-red-600 text-white rounded transition">
                  <Icon icon="mdi:delete" width="20" height="20" class="mr-1" />
                </button>
              </div>
            </td>

          </tr>
        </tbody>
      </table>
    </div>

    <!-- Popup thêm phòng -->
    <div v-if="showAddModal" class="fixed inset-0 bg-gray-800 bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white p-8 rounded-2xl shadow-lg w-full max-w-2xl relative border border-blue-200 animate-fade-in">
        <h2 class="text-2xl font-bold text-center mb-6 text-purple-800">➕ Thêm phòng mới</h2>
        <form @submit.prevent="addRoom" class="space-y-4">
          <div>
            <label for="roomName" class="block text-sm font-semibold text-gray-700">Tên phòng</label>
            <input v-model="newRoom.name" type="text" id="roomName" class="w-full px-3 py-2 border rounded"
              placeholder="Nhập tên phòng" required />
          </div>
          <div>
            <label for="roomType" class="block text-sm font-semibold text-gray-700">Loại phòng</label>
            <select v-model="newRoom.type" id="roomType" class="w-full px-3 py-2 border rounded" required>
              <option value="Single">Phòng đơn</option>
              <option value="Double">Phòng đôi</option>
              <option value="Suite">Phòng Suite</option>
            </select>
          </div>
          <div>
            <label for="roomPrice" class="block text-sm font-semibold text-gray-700">Giá</label>
            <input v-model="newRoom.price" type="number" id="roomPrice" class="w-full px-3 py-2 border rounded"
              placeholder="Nhập giá phòng" required />
          </div>
          <div>
            <label for="roomStatus" class="block text-sm font-semibold text-gray-700">Trạng thái</label>
            <select v-model="newRoom.status" id="roomStatus" class="w-full px-3 py-2 border rounded" required>
              <option value="Available">Còn trống</option>
              <option value="Occupied">Đã thuê</option>
            </select>
          </div>
          <div class="flex justify-end gap-4 mt-4">
            <button type="button" @click="showAddModal = false"
              class="px-4 py-2 bg-gray-600 text-white rounded hover:bg-gray-700">
              Hủy
            </button>
            <button type="submit" class="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700">
              Thêm phòng
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      showAddModal: false,
      rooms: [
        { name: 'Phòng 101', type: 'Single', price: 500000, status: 'Available' },
        { name: 'Phòng 102', type: 'Double', price: 800000, status: 'Occupied' },
        { name: 'Phòng 103', type: 'Suite', price: 1200000, status: 'Available' }
      ],
      newRoom: {
        name: '',
        type: 'Single',
        price: 0,
        status: 'Available'
      }
    };
  },
  methods: {
    addRoom() {
      this.rooms.push({ ...this.newRoom });
      this.newRoom = { name: '', type: 'Single', price: 0, status: 'Available' };
      this.showAddModal = false;
    },
    editRoom(room) {
      console.log('Chỉnh sửa phòng:', room);
    },
    deleteRoom(room) {
      this.rooms = this.rooms.filter(r => r !== room);
    }
  }
};
</script>

<style scoped>
/* Các kiểu CSS cho popup */
.animate-fade-in {
  animation: fadeIn 0.3s ease-in-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }

  to {
    opacity: 1;
  }
}
</style>

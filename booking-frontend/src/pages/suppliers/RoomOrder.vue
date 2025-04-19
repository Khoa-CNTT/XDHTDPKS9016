<!-- <template>
    <div class="p-6">
      
      <div class="flex justify-end mb-4">
        <button
          class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
          @click="openAddRoom"
        >
          + Thêm phòng
        </button>
      </div>
  
      
      <table class="w-full border rounded overflow-hidden text-sm">
        <thead class="bg-gray-100">
          <tr>
            <th class="p-2 text-left">Loại phòng</th>
            <th class="p-2 text-left">Còn trống</th>
            <th class="p-2 text-left">Ngày trống</th>
            <th class="p-2 text-left">Hình ảnh</th>
            <th class="p-2 text-center">Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="room in rooms" :key="room.id" class="border-t">
            <td class="p-2">{{ room.type }}</td>
            <td class="p-2">{{ room.available }}</td>
            <td class="p-2">{{ room.availableDates.join(', ') }}</td>
            <td class="p-2">
              <img :src="room.image" alt="Hình phòng" class="w-16 h-16 object-cover rounded" />
            </td>
            <td class="p-2 text-center">
              <button class="text-green-600 mr-2" @click="editRoom(room)">Sửa</button>
              <button class="text-red-600" @click="deleteRoom(room.id)">Xóa</button>
            </td>
          </tr>
        </tbody>
      </table>
  
    
      <div v-if="showForm" class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
        <div class="bg-white p-6 rounded w-full max-w-lg relative">
          <h2 class="text-xl font-semibold mb-4">{{ isEdit ? 'Sửa phòng' : 'Thêm phòng' }}</h2>
  
          <div class="mb-3">
            <label class="block mb-1 font-medium">Loại phòng</label>
            <input v-model="form.type" type="text" class="w-full border rounded px-3 py-2" />
          </div>
  
          <div class="mb-3">
            <label class="block mb-1 font-medium">Số lượng còn trống</label>
            <input v-model="form.available" type="number" class="w-full border rounded px-3 py-2" />
          </div>
  
          <div class="mb-3">
            <label class="block mb-1 font-medium">Ngày trống (phân cách bởi dấu phẩy)</label>
            <input v-model="form.availableDates" type="text" class="w-full border rounded px-3 py-2" />
          </div>
  
          <div class="mb-4">
            <label class="block mb-1 font-medium">Hình ảnh (URL)</label>
            <input v-model="form.image" type="text" class="w-full border rounded px-3 py-2" />
          </div>
  
          <div class="flex justify-end">
            <button @click="closeForm" class="px-4 py-2 mr-2 border rounded">Hủy</button>
            <button @click="submitForm" class="bg-blue-600 text-white px-4 py-2 rounded">
              {{ isEdit ? 'Cập nhật' : 'Thêm' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  
  const rooms = ref([
    {
      id: 1,
      type: 'Phòng đơn',
      available: 5,
      availableDates: ['2025-04-15', '2025-04-16'],
      image: '/assets/images/img-hotel-3.png',
    },
    {
      id: 2,
      type: 'Phòng đôi',
      available: 2,
      availableDates: ['2025-04-17'],
      image: '/assets/images/img-hotel-3.png',
    },
  ])
  
  const showForm = ref(false)
  const isEdit = ref(false)
  const form = ref({
    id: null,
    type: '',
    available: 0,
    availableDates: '',
    image: '',
  })
  
  function openAddRoom() {
    isEdit.value = false
    form.value = { id: null, type: '', available: 0, availableDates: '', image: '' }
    showForm.value = true
  }
  
  function editRoom(room) {
    isEdit.value = true
    form.value = {
      ...room,
      availableDates: room.availableDates.join(', '),
    }
    showForm.value = true
  }
  
  function deleteRoom(id) {
    rooms.value = rooms.value.filter(r => r.id !== id)
  }
  
  function closeForm() {
    showForm.value = false
  }
  
  function submitForm() {
    const newRoom = {
      ...form.value,
      availableDates: form.value.availableDates.split(',').map(d => d.trim()),
    }
  
    if (isEdit.value) {
      const index = rooms.value.findIndex(r => r.id === form.value.id)
      rooms.value[index] = newRoom
    } else {
      newRoom.id = Date.now()
      rooms.value.push(newRoom)
    }
  
    closeForm()
  }
  </script>
  
  <style scoped>
  
  </style>
   -->

   <template>
    <div class="p-8 max-w-5xl mx-auto space-y-6">
      <div class="flex justify-between items-center">
        <h1 class="text-2xl font-bold text-text">Quản lý thông tin khách sạn</h1>
        <button
          @click="editMode ? saveHotel() : editMode = true"
          class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
        >
          {{ editMode ? 'Lưu' : 'Chỉnh sửa' }}
        </button>
      </div>
  
      <!-- Tên & địa chỉ -->
      <div class="bg-white p-4 rounded shadow space-y-2">
        <div>
          <label class="font-medium block">Tên khách sạn:</label>
          <template v-if="editMode">
            <input v-model="hotel.name" class="w-full border p-2 rounded" />
          </template>
          <template v-else>
            <p>{{ hotel.name }}</p>
          </template>
        </div>
        <div>
          <label class="font-medium block">Địa chỉ:</label>
          <template v-if="editMode">
            <input v-model="hotel.address" class="w-full border p-2 rounded" />
          </template>
          <template v-else>
            <p>{{ hotel.address }}</p>
          </template>
        </div>
      </div>
  
      <!-- Mô tả -->
      <div class="bg-white p-4 rounded shadow">
        <label class="font-medium block mb-2">Giới thiệu:</label>
        <template v-if="editMode">
          <textarea v-model="hotel.description" rows="10" class="w-full border p-2 rounded"></textarea>
        </template>
        <template v-else>
          <p class="whitespace-pre-line">{{ hotel.description }}</p>
        </template>
      </div>
  
      <!-- Dịch vụ -->
      <div class="bg-white p-4 rounded shadow">
        <label class="font-medium block mb-2">Dịch vụ & Tiện ích:</label>
        <ul class="list-disc pl-5">
          <li v-for="(service, i) in hotel.services" :key="i">
            <template v-if="editMode">
              <input v-model="hotel.services[i]" class="w-full border p-1 mb-1 rounded" />
            </template>
            <template v-else>{{ service }}</template>
          </li>
        </ul>
      </div>
  
      <!-- Bảng giá -->
      <div class="bg-white p-4 rounded shadow">
        <label class="font-medium block mb-2">Bảng giá phòng:</label>
        <table class="w-full border border-gray-400 text-sm">
          <thead>
            <tr class="bg-gray-100 text-left">
              <th class="p-2 border border-gray-300">Loại phòng</th>
              <th class="p-2 border border-gray-300">Giá (VND)</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(room, i) in hotel.prices" :key="i">
              <td class="p-2 border border-gray-300">
                <template v-if="editMode">
                  <input v-model="room.type" class="w-full border p-1 rounded" />
                </template>
                <template v-else>{{ room.type }}</template>
              </td>
              <td class="p-2 border border-gray-300">
                <template v-if="editMode">
                  <input type="number" v-model="room.price" class="w-full border p-1 rounded" />
                </template>
                <template v-else>{{ room.price.toLocaleString() }} VND</template>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </template>
  
<script setup>
import { ref } from 'vue'

const editMode = ref(false)

const hotel = ref({
  name: "La Vela Saigon Hotel",
  address: "Số 123, Quận 1, TP.HCM",
  description: `La Vela Saigon Hotel là khách sạn 5 sao sang trọng, tọa lạc ngay trung tâm TP. Hồ Chí Minh...`,
  services: [
    "Hồ bơi vô cực",
    "Trung tâm spa & gym",
    "Nhà hàng sang trọng",
    "Phòng hội nghị"
  ],
  prices: [
    { type: "Phòng Deluxe", price: 1500000 },
    { type: "Phòng Suite", price: 2500000 },
    { type: "Phòng VIP", price: 5000000 }
  ]
})

const saveHotel = () => {
  console.log("Thông tin sau khi lưu:", hotel.value)
  editMode.value = false
}
</script>
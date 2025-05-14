<template>
  <div class="space-y-6 px-4 py-6">
    <!-- Header Thêm -->
    <div class="border rounded-lg p-6 bg-white shadow flex justify-between items-center">
      <h3 class="text-lg font-semibold text-blue-700 flex">
        <Icon icon="mdi:bed-outline" class="mr-2 text-xl mr-2" width="24" height="24" /> Danh sách loại phòng
      </h3>
      <button @click="showModal = true"
        class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition flex items-center">
        <Icon icon="mdi:plus-box" class="text-xl pr-2" width="24" height="24" /> Thêm loại phòng mới
      </button>
    </div>

    <!-- Bảng danh sách -->
    <div class="border rounded-lg p-6 bg-white shadow">
      <table class="min-w-full divide-y divide-gray-200 border">
        <thead class="bg-gray-100">
          <tr>
            <th class="px-4 py-2 text-left">STT</th>
            <th class="px-4 py-2 text-left">Tên loại phòng</th>
            <th class="px-4 py-2 text-left">Số giường</th>
            <th class="px-4 py-2 text-left">Số người</th>
            <th class="px-4 py-2 text-left">Giá</th>
            <th class="px-4 py-2 text-left">Mô tả</th>
            <th class="px-4 py-2 text-left">Hình ảnh</th>
            <th class="px-4 py-2 text-left">Số lượng</th>
            <th class="px-4 py-2 text-left">Trạng thái</th>
            <th class="px-4 py-2 text-center">Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(row, index) in roomData" :key="index" class="border-t">
            <td class="px-4 py-2">{{ row['STT'] }}</td>
            <td class="px-4 py-2 max-w-[160px] truncate">{{ row['Tên loại phòng'] }}</td>
            <td class="px-4 py-2">{{ row['Số lượng giường'] }}</td>
            <td class="px-4 py-2">{{ row['Số lượng người'] }}</td>
            <td class="px-4 py-2">{{ row['Giá'] }}</td>
            <td class="px-4 py-2 max-w-[160px] truncate">{{ row['Mô tả'] }}</td>
            <td class="px-4 py-2">
              <img :src="row['Hình ảnh']" alt="room" class="w-16 h-16 object-cover rounded" />
            </td>
            <td class="px-4 py-2">{{ row['Số lượng'] }}</td>
            <td class="px-4 py-2">
              <span class="px-2 py-1 rounded font-semibold border" :class="{
                'bg-green-100 text-green-700 border-green-300': row['Trạng thái'] === 'ACTIVE',
                'bg-gray-100 text-gray-700 border-gray-300': row['Trạng thái'] !== 'ACTIVE'
              }">
                {{ row['Trạng thái'] }}
              </span>
            </td>
            <td class="px-4 py-2 text-center">
              <div class="flex justify-center gap-3">
                <!-- Icon Sửa -->
                <button @click="handleEdit(row['room_type_id'])"
                  class="p-2 bg-green-500 hover:bg-green-600 text-white rounded transition">
                  <Icon icon="mdi:pencil" width="20" height="20" class="mr-1" />
                </button>

                <!-- Icon Xóa -->
                <button @click="askDelete(row['room_type_id'])"
                  class="p-2 bg-red-500 hover:bg-red-600 text-white rounded transition">
                  <Icon icon="mdi:trash-can" width="20" height="20" class="mr-1" />
                </button>
              </div>
            </td>

          </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal thêm / sửa / xác nhận xóa -->
    <EditRoomTypeModal :isOpen="showEditModal" :roomType="selectedRoomType" @onClose="showEditModal = false"
      @onUpdated="fetchRoomTypes" />
    <AddRoomTypeModal :isOpen="showModal" @close="showModal = false" @added="handleRoomTypeAdded"
      :fetchRoomTypes="fetchRoomTypes" />
    <ConfirmDeleteModal v-if="showDeleteConfirm" @cancel="cancelDelete" @confirm="confirmDelete" />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, computed } from 'vue'
import { getRoomTypesApi, deleteRoomTypeApi } from '@/services/supplier'
import AddRoomTypeModal from '@/pages/suppliers/AddRoomTypeModal.vue'
import ConfirmDeleteModal from './ConfirmDeleteModal.vue'
import EditRoomTypeModal from './EditRoomTypeModal.vue'
import type { RoomType, AddRoomType } from '@/types/supplier'
import { toast } from 'vue3-toastify'
const showEditModal = ref(false)
const selectedRoomType = ref<RoomType | null>(null)
const roomTypes = ref<RoomType[]>([])
const isLoading = ref(false)
const error = ref<string | null>(null)
const showModal = ref(false)
const showDeleteConfirm = ref(false)
const selectedRoomTypeId = ref<number | null>(null)
const fetchRoomTypes = async () => {
  isLoading.value = true
  try {
    const response = await getRoomTypesApi()
    roomTypes.value = response.content
  } catch (err) {
    error.value = 'Không thể tải dữ liệu loại phòng'
    console.error(err)
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  fetchRoomTypes()
})

const handleRoomTypeAdded = () => {
  showModal.value = false
  fetchRoomTypes()
}

const roomData = computed(() => {
  return roomTypes.value.map((room, index) => ({
    'STT': index + 1,
    'Tên loại phòng': room.type_name,
    'Số lượng giường': room.number_bed,
    'Số lượng người': room.maximum_people,
    'Giá': `${room.price.toLocaleString()} VNĐ`,
    'Mô tả': room.description,
    'Hình ảnh': room.room_image,
    'Số lượng': room.available_room,
    'Trạng thái': room.status,
    'Hành động': '',
    'room_type_id': room.room_type_id,
  }))
})

const askDelete = (id: number) => {
  selectedRoomTypeId.value = id
  showDeleteConfirm.value = true
}

const confirmDelete = async () => {
  if (selectedRoomTypeId.value !== null) {
    try {
      await deleteRoomTypeApi(selectedRoomTypeId.value)
      toast.success('Xóa loại phòng thành công!', {
        autoClose: 10000,
        position: 'top-right',
      });
      await fetchRoomTypes()
    } catch (err) {
      console.error('Xoá thất bại:', err)
      toast.error('Xóa loại phòng thất bại!', {
        autoClose: 10000,
        position: 'top-right',
      });
    } finally {
      showDeleteConfirm.value = false
      selectedRoomTypeId.value = null
    }
  }
}

const cancelDelete = () => {
  showDeleteConfirm.value = false
  selectedRoomTypeId.value = null
}
const handleEdit = (id: number) => {
  const room = roomTypes.value.find(r => r.room_type_id === id)
  if (room) {
    selectedRoomType.value = room
    showEditModal.value = true
  }
}
</script>
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
            <th class="px-4 py-2 text-left">Số lượng</th>
            <th class="px-4 py-2 text-left">Hình ảnh</th>
            <th class="px-4 py-2 text-left">Mô tả</th>
            <th class="px-4 py-2 text-center">Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(row, index) in roomTypes" :key="index" class="border-t">
            <td class="px-4 py-2">{{ index + 1 }}</td>
            <td class="px-4 py-2">{{ row.type_name }}</td>
            <td class="px-4 py-2 max-w-[160px] truncate">{{ row.number_room }}</td>
            <td class="px-4 py-2">{{ row.description }}</td>
            <td class="px-4 py-2">
              <img
             :src="row.room_image ? `http://157.66.101.165:8080${row.room_image.startsWith('/') ? row.room_image : '/' + row.room_image}` : ''"
                alt="Ảnh khách sạn" class="w-20 h-14 object-cover rounded" />
            </td>
            <td class="px-4 py-2 text-center">
              <div class="flex justify-center gap-3">
                <!-- Icon Sửa -->
                <button   @click="editRoomType(row)"
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
    <AddRoomTypeModal :isOpen="showModal" @close="showModal = false" @added="handleRoomTypeAdded"
      :fetchRoomTypes="fetchRoomTypes" />
    <ConfirmDeleteModal v-if="showDeleteConfirm" @cancel="cancelDelete" @confirm="confirmDelete" />
    <EditRoomTypeModal
  v-if="showEditModal"
  :roomType="selectedRoomType"
  @close="showEditModal = false"
  @updated="handleRoomTypeUpdated"
/>

  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, computed } from 'vue'
import { getRoomTypesApi,deleteRoomTypeApi } from '@/services/supplier'
import AddRoomTypeModal from '@/pages/suppliers/AddRoomTypeModal.vue'
import ConfirmDeleteModal from './ConfirmDeleteModal.vue'
import EditRoomTypeModal from './EditRoomTypeModal.vue'
import type { RoomTypeSummary, AddRoomType } from '@/types/supplier'
import { toast } from 'vue3-toastify'

const roomTypes = ref<RoomTypeSummary[]>([])
const isLoading = ref(false)
const error = ref<string | null>(null)
const showModal = ref(false)
const showDeleteConfirm = ref(false)
const selectedRoomTypeId = ref<number | null>(null)
const fetchRoomTypes = async () => {
  isLoading.value = true
  try {
    const response = await getRoomTypesApi()
    console.log('====>ne', response);

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
const showEditModal = ref(false)
const selectedRoomType = ref<RoomTypeSummary | null>(null)
const editRoomType = (room: RoomTypeSummary) => {
  selectedRoomType.value = room
  showEditModal.value = true
}
const handleRoomTypeUpdated = () => {
  showEditModal.value = false
  fetchRoomTypes()
}
</script>
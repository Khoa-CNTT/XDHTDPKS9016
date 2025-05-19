<!-- HotelManagement.vue (component cha) -->
<template>
  <div class="space-y-6 px-4 py-6">
    <div class="border rounded-lg p-6 bg-white shadow flex justify-between items-center">
      <h3 class="text-lg font-semibold text-blue-700 flex">
        <Icon icon="mdi:tools" class="mr-2 text-xl" width="24" height="24" /> Quản lý khách sạn
      </h3>
      <button @click="showCreateHotel = true"
        class="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 transition flex items-center">
        <Icon icon="mdi:plus-box" class="text-xl pr-2" width="24" height="24" /> Thêm mới khách sạn
      </button>
    </div>

    <div class="border rounded-lg p-6 bg-white shadow">
      <h3 class="text-lg font-semibold mb-4 text-blue-700 flex">
        <Icon icon="mdi:clipboard-list" width="24" height="24" class="mr-2" /> Danh sách khách sạn
      </h3>
      <table class="min-w-full divide-y divide-gray-200 border">
        <thead class="bg-gray-100">
          <tr>
            <th class="px-4 py-2 text-left">STT</th>
            <th class="px-4 py-2 text-left">Tên</th>
            <th class="px-4 py-2 text-left">Ảnh</th>
            <th class="px-4 py-2 text-left">Địa chỉ</th>
            <th class="px-4 py-2 text-left">Số điện thoại</th>
            <th class="px-4 py-2 text-left">Dịch vụ</th>
            <th class="px-4 py-2 text-left">Loại phòng</th>
            <th class="px-4 py-2 text-left">Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(hotel, index) in hotels" :key="hotel.idHotel" class="border-t align-top">
            <td class="px-4 py-2">{{ index + 1 }}</td>
            <td class="px-4 py-2">{{ hotel.name }}</td>
            <td class="px-4 py-2">
              <img :src="`http://157.66.101.165:8080${hotel.image}`" alt="Ảnh khách sạn"
                class="w-20 h-14 object-cover rounded" />
            </td>
            <td class="px-4 py-2 whitespace-pre-line">{{ hotel.address }}</td>
            <td class="px-4 py-2">{{ hotel.hotline }}</td>
            <td class="px-4 py-2">
              <div v-if="hotel.services.length">
                <div v-for="svc in hotel.services" :key="svc.serviceId" class="truncate">
                  {{ svc.serviceName }}
                </div>
              </div>
              <div v-else class="italic text-gray-500">Không có</div>
            </td>
            <td class="px-4 py-2">
              <div v-if="hotel.roomTypes.length">
                <div v-for="rt in hotel.roomTypes" :key="rt.roomTypeId" class="truncate">
                  {{ rt.typeName }}
                </div>
              </div>
              <div v-else class="italic text-gray-500">Không có</div>
            </td>
            <td class="px-4 py-2 text-center">
              <div class="flex justify-center gap-2">
                <button @click="openEditModal(hotel)"
                  class="p-2 bg-green-500 hover:bg-green-600 text-white rounded transition">
                  <Icon icon="mdi:pencil" width="20" height="20" />
                </button>
                <button @click="confirmDelete(hotel.idHotel)"
                  class="p-2 bg-red-500 hover:bg-red-600 text-white rounded transition">
                  <Icon icon="mdi:cancel" width="20" height="20" />
                </button>
                <button @click="handleView(hotel.idHotel)"
                  class="p-2 bg-blue-500 hover:bg-blue-600 text-white rounded transition">
                  <Icon icon="mdi:eye" width="20" height="20" />
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>

      <div class="pt-6 flex justify-center">
        <Pagination
          :total="totalElements"
          :items-per-page="pageSize"
          :default-page="currentPage"
          @page-change="fetchInfoHotel"
        />
      </div>
    </div>

    <CreateHotel
      v-if="showCreateHotel"
      @close="showCreateHotel = false"
      @refresh="fetchInfoHotel"
    />

    <DeleteConfirmModal
      v-if="showDeleteModal"
      @cancel="showDeleteModal = false"
      @confirm="handleDelete"
    />

    <SupplierDetailPopup
      v-if="showDetailPopup"
      :supplier="selectedSupplier"
      @close="showDetailPopup = false"
    />

    <EditSupplierPopup
      v-if="showEditModal"
      :supplier="selectedHotelToEdit"
      @close="showEditModal = false"
      @save="handleHotelSave"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import Pagination from '@/components/base/Pagination.vue'
import SupplierDetailPopup from './SupplierDetailPopup.vue'
import CreateHotel from './CreateHotel.vue'
import DeleteConfirmModal from './DeleteConfirmModal.vue'
import EditSupplierPopup from './EditSupplierPopup.vue'
import {
  getManagementSupplier,
  getSupplierByIdApi,
  deleteSupplierApi
} from '@/services/admin'

interface Hotel {
  idHotel: number
  name: string
  image: string
  address: string
  hotline: string
  description: string
  services: { serviceId: number; serviceName: string }[]
  roomTypes: { roomTypeId: number; typeName: string }[]
}

const hotels = ref<Hotel[]>([])
const currentPage = ref(1)
const pageSize = ref(10)
const totalElements = ref(0)

const showCreateHotel = ref(false)
const showEditModal = ref(false)
const showDeleteModal = ref(false)
const showDetailPopup = ref(false)

const selectedHotelToEdit = ref<Hotel | null>(null)
const selectedSupplier = ref<any>(null)
const deleteId = ref<number | null>(null)

const fetchInfoHotel = async (page = 1) => {
  try {
    const res = await getManagementSupplier(page - 1)
    hotels.value = res.content
    totalElements.value = res.page.totalElements
    pageSize.value = res.page.size
    currentPage.value = res.page.number + 1
  } catch (error) {
    console.error('Lỗi khi gọi API nhà cung cấp:', error)
  }
}

const openEditModal = (hotel: Hotel) => {
  selectedHotelToEdit.value = hotel
  showEditModal.value = true
}

const handleHotelSave = (updated: Hotel) => {
  // tìm index và thay thế bằng object mới
  const idx = hotels.value.findIndex(h => h.idHotel === updated.idHotel)
  if (idx !== -1) {
    hotels.value.splice(idx, 1, updated)
  }
  showEditModal.value = false
}

const confirmDelete = (id: number) => {
  deleteId.value = id
  showDeleteModal.value = true
}

const handleDelete = async () => {
  if (deleteId.value == null) return
  try {
    await deleteSupplierApi(deleteId.value)
    await fetchInfoHotel(currentPage.value)
    showDeleteModal.value = false
    deleteId.value = null
  } catch (error) {
    console.error('Lỗi khi xóa nhà cung cấp:', error)
  }
}

const handleView = async (id: number) => {
  try {
    const res = await getSupplierByIdApi(id)
    selectedSupplier.value = res
    showDetailPopup.value = true
  } catch (error) {
    console.error('Lỗi khi lấy chi tiết nhà cung cấp:', error)
  }
}

onMounted(() => {
  fetchInfoHotel()
})
</script>

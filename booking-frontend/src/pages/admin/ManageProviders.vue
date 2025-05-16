<template>
  <div class="card relative">
    <div class="card-header flex justify-between items-center flex-wrap gap-4">
      <div class="flex items-center">
        <label for="search" class="mr-2">Tìm tên nhà cung cấp</label>
        <input id="search" v-model="searchTerm" placeholder="Nhập tên nhà cung cấp..."
          class="px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500" />
      </div>
      <button class="bg-green-500 text-white px-4 py-2 rounded" @click="showAddPopup = true">
        Thêm nhà cung cấp +
      </button>
    </div>

    <div class="card-body">
      <CustomTable :headers="tableHeaders" :rows="formattedTableData">
        <template #actions="{ row, index }">
          <div class="flex gap-2 justify-end">
            <button class="px-2 py-1 bg-green-500 text-white rounded"
              @click="() => openEditPopup(Number(row.hotel_id))">
              Sửa
            </button>
            <button class="px-2 py-1 bg-red-500 text-white rounded" @click="() => confirmDelete(Number(row.hotel_id))">
              Xóa
            </button>
            <button class="px-2 py-1 bg-blue-500 text-white rounded" @click="() => openDetail(Number(row.hotel_id))">
              Xem chi tiết
            </button>
          </div>
        </template>
      </CustomTable>
    </div>
    <AddSupplierPopup v-if="showAddPopup" @close="showAddPopup = false" @created="fetchInfoHotel" />
    <SupplierDetailPopup v-if="showDetailPopup" :supplier="selectedSupplier" @close="showDetailPopup = false" />
    <EditSupplierPopup v-if="showEditPopup" :supplier="selectedSupplier" @close="showEditPopup = false"
      @updated="fetchInfoHotel" />
    <DeleteConfirmModal v-if="showDeleteModal" @cancel="showDeleteModal = false" @confirm="handleDelete" />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import CustomTable from '@/components/base/CustomTable.vue'
import { getManagementSupplier, getSupplierByIdApi, deleteSupplierApi } from '@/services/admin'
import SupplierDetailPopup from '@/pages/admin/SupplierDetailPopup.vue'
import AddSupplierPopup from '@/pages/admin/AddSupplierPopup.vue'
import EditSupplierPopup from '@/pages/admin/EditSupplierPopup.vue'
import DeleteConfirmModal from '@/pages/admin/DeleteConfirmModal.vue'
import { log } from 'console'
const showAddPopup = ref(false)
const tableHeaders = ['STT', 'Tên nhà cung cấp', 'Hình ảnh', 'Địa chỉ', 'Số điện thoại', 'Mô tả', 'Hành động']

const suppliers = ref<any[]>([])
const searchTerm = ref('')
const selectedSupplier = ref<any | null>(null)
const showDetailPopup = ref(false)
const showEditPopup = ref(false)
const fetchInfoHotel = async () => {
  try {
    const res = await getManagementSupplier()
   console.log('====>',res);
   
    suppliers.value = res.content
  } catch (error) {
    console.error('Lỗi khi gọi API nhà cung cấp:', error)
  }
}

onMounted(() => {
  fetchInfoHotel()
})
const openDetail = async (supplierId: number) => {
  try {
    const supplier = await getSupplierByIdApi(supplierId)
    console.log(supplier);

    selectedSupplier.value = supplier
    showDetailPopup.value = true
  } catch (error) {
    console.error('Lỗi khi lấy thông tin nhà cung cấp:', error)
  }
}
const openEditPopup = async (supplierId: number) => {
  try {
    const supplier = await getSupplierByIdApi(supplierId)
    selectedSupplier.value = supplier
    showEditPopup.value = true
  } catch (error) {
    console.error('Lỗi khi lấy thông tin nhà cung cấp:', error)
  }
}
const formattedTableData = computed(() => {
  return suppliers.value.map((item, index) => ({
    stt: index + 1,
    ten: item.name,
    hinhAnh: item.image || 'null',
    diaChi: item.address,
    sdt: item.hotline,
    moTa: item.description || 'Chưa có mô tả',
    hotel_id: item.hotel_id,
    raw: item
  }))
})

const showDeleteModal = ref(false)
const deleteId = ref<number | null>(null)

const confirmDelete = (id: number) => {
  deleteId.value = id
  showDeleteModal.value = true
}

const handleDelete = async () => {
  if (!deleteId.value) return
  try {
    await deleteSupplierApi(deleteId.value)
    await fetchInfoHotel()
  } catch (error) {
    console.error('Lỗi khi xóa nhà cung cấp:', error)
  } finally {
    showDeleteModal.value = false
    deleteId.value = null
  }
}

const filteredData = computed(() => {
  if (!searchTerm.value) return suppliers.value
  return suppliers.value.filter(item =>
    item.ten.toLowerCase().includes(searchTerm.value.toLowerCase())
  )
})
</script>

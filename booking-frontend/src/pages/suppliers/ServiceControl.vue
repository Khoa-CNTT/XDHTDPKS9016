<template>
  <div class="space-y-6 px-4 py-6">
    <!-- Header -->
    <div class="border rounded-lg p-6 bg-white shadow flex justify-between items-center">
      <h3 class="text-lg font-semibold text-blue-700 flex">
        <Icon
          icon="mdi:tools"
          class="mr-2 text-xl"
          width="24"
          height="24"
        />
        Quản lý dịch vụ
      </h3>
      <button
        @click="showAddModal = true"
        class="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 transition flex items-center"
      >
        <Icon
          icon="mdi:plus-box"
          class="text-xl pr-2"
          width="24"
          height="24"
        />
        Thêm dịch vụ mới
      </button>
    </div>

    <!-- Table -->
    <div class="border rounded-lg p-6 bg-white shadow overflow-x-auto">
      <table class="min-w-full divide-y divide-gray-200 border">
        <thead class="bg-gray-100">
          <tr>
            <th class="px-4 py-2 text-left">STT</th>
            <th class="px-4 py-2 text-left">Tên dịch vụ</th>
            <th class="px-4 py-2 text-left">Giá</th>
            <th class="px-4 py-2 text-left max-w-xs">Mô tả chi tiết</th>
            <th class="px-4 py-2 text-center">Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="(row, index) in services"
            :key="row.id"
            class="border-t hover:bg-gray-50"
          >
            <td class="px-4 py-2">{{ currentPage * size + index + 1 }}</td>
            <td class="px-4 py-2">{{ row.name }}</td>
            <td class="px-4 py-2">{{ row.price }} đ</td>
            <td class="px-4 py-2 max-w-xs truncate">{{ row.description }}</td>
            <td class="px-4 py-2 text-center">
              <div class="flex gap-2 justify-center">
                <button
                  @click="handleView(row)"
                  class="p-2 bg-blue-500 hover:bg-blue-600 text-white rounded transition"
                  title="Xem chi tiết"
                >
                  <Icon
                    icon="mdi:eye"
                    width="20"
                    height="20"
                  />
                </button>
                <button
                  @click="handleEdit(row)"
                  class="p-2 bg-green-500 hover:bg-green-600 text-white rounded transition"
                  title="Chỉnh sửa"
                >
                  <Icon
                    icon="mdi:pencil"
                    width="20"
                    height="20"
                  />
                </button>
                <button
                  @click="handleDelete(row)"
                  class="p-2 bg-red-500 hover:bg-red-600 text-white rounded transition"
                  title="Xóa"
                >
                  <Icon
                    icon="mdi:trash-can"
                    width="20"
                    height="20"
                  />
                </button>
              </div>
            </td>
          </tr>
          <tr v-if="services.length === 0">
            <td
              colspan="5"
              class="text-center py-4 text-gray-500"
            >
              Không có dữ liệu
            </td>
          </tr>
        </tbody>
      </table>
      <!-- Pagination -->
      <div class="mt-5 flex justify-center">
        <Pagination
          :total="totalElements"
          :items-per-page="size"
          :default-page="currentPage + 1"
          :sibling-count="1"
          @page-change="handlePageChange"
        />
      </div>
    </div>

    <!-- Modal thêm dịch vụ -->
    <ModalAddService
      v-if="showAddModal"
      @close="showAddModal = false"
      @serviceAdded="handleServiceAdded"
    />

    <!-- Modal cập nhật dịch vụ -->
    <UpdateService
      v-if="showEditModal && editingService"
      :service="editingService"
      @close="showEditModal = false"
      @serviceUpdated="handleServiceUpdated"
    />

    <!-- Modal xem chi tiết dịch vụ -->
    <ModalViewService
      v-if="showViewModal && viewingService"
      :service="viewingService"
      @close="showViewModal = false"
    />

    <!-- Modal xóa dịch vụ -->
    <div
      v-if="showDeleteModal"
      class="fixed inset-0 bg-gray-600 bg-opacity-50 flex justify-center items-center z-50"
      @click.self="showDeleteModal = false"
    >
      <div class="bg-white p-6 rounded-lg shadow-lg w-full max-w-md">
        <div class="text-center mb-6">
          <h3 class="text-lg font-medium text-gray-900">Xác nhận xóa dịch vụ</h3>
          <div class="mt-2">
            <p class="mb-6">Bạn có chắc chắn muốn xóa dịch vụ này không?</p>
          </div>
        </div>
        <div class="flex justify-center gap-3">
          <button
            @click="showDeleteModal = false"
            class="px-4 py-2 bg-gray-300 text-gray-700 rounded hover:bg-gray-400 transition"
          >
            Hủy
          </button>
          <button
            @click="confirmDelete"
            class="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700 transition"
          >
            Xóa
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import ModalAddService from './ModalAddService.vue'
import UpdateService from './UpdateService.vue'
import ModalViewService from './ModalViewService.vue'
import { getServiceListApi, deleteServiceApi } from '@/services/supplier'
import { GetService } from '@/types/supplier'
import { toast } from 'vue3-toastify'
import 'vue3-toastify/dist/index.css'
import Pagination from '@/components/base/Pagination.vue'
// State
const services = ref<GetService[]>([])
const showAddModal = ref(false)
const showEditModal = ref(false)
const showViewModal = ref(false)
const showDeleteModal = ref(false)

const editingService = ref<GetService | null>(null)
const viewingService = ref<GetService | null>(null)
const deletingService = ref<GetService | null>(null)
const currentPage = ref(0) // lưu ý: server thường tính page từ 0
const size = ref(5) // số item mỗi trang
const totalElements = ref(0) // tổng số dịch vụ (để tính tổng số trang)
// Fetch danh sách dịch vụ
const fetchServices = async () => {
  try {
    const response = await getServiceListApi(currentPage.value, size.value)
    services.value = response.content
    totalElements.value = response.page.totalElements
    console.log('Danh sách dịch vụ:', services.value)
  } catch (error) {
    console.error('Lỗi khi tải danh sách dịch vụ:', error)
    toast.error('Lỗi tải danh sách dịch vụ')
  }
}
onMounted(fetchServices)
const handlePageChange = async (page: number) => {
  currentPage.value = page - 1
  await fetchServices()
}
// Thêm dịch vụ
const handleServiceAdded = async () => {
  await fetchServices()
  toast.success('Thêm dịch vụ thành công!', { autoClose: 5000, position: 'top-right' })
  showAddModal.value = false
}

// Xem chi tiết
const handleView = (service: GetService) => {
  viewingService.value = { ...service }
  showViewModal.value = true
}

// Chỉnh sửa
const handleEdit = (service: GetService) => {
  editingService.value = { ...service }
  showEditModal.value = true
}

// Sau khi cập nhật dịch vụ
const handleServiceUpdated = async () => {
  await fetchServices()
  toast.success('Cập nhật dịch vụ thành công!', { autoClose: 5000, position: 'top-right' })

  showEditModal.value = false
}

// Xóa dịch vụ
const handleDelete = (service: GetService) => {
  deletingService.value = service
  showDeleteModal.value = true
}

const confirmDelete = async () => {
  if (!deletingService.value) return
  try {
    await deleteServiceApi(deletingService.value.id)
    toast.success('Xóa dịch vụ thành công!', { autoClose: 5000, position: 'top-right' })
    await fetchServices()
  } catch (error) {
    toast.error('Lỗi khi xóa dịch vụ', { autoClose: 5000, position: 'top-right' })
  } finally {
    showDeleteModal.value = false
    deletingService.value = null
  }
}
</script>

<template>
  <div class="card relative">
    <div class="card-header flex justify-start items-center gap-4">
      <label
        for="search"
        class="mr-2"
        >Tìm tên</label
      >
      <input
        id="search"
        v-model="searchKeyword"
        placeholder="Nhập tên người dùng..."
        class="px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
      />
    </div>

    <div class="card-body">
      <CustomTable
        :headers="tableHeaders"
        :rows="filteredData"
      >
        <template #actions="{ row }">
          <div class="flex gap-2 justify-end">
            <button
              @click="handleDelete(row)"
              class="px-2 py-1 bg-red-500 text-white rounded"
            >
              Xóa
            </button>
            <button
              @click="handleView(row)"
              class="px-2 py-1 bg-blue-500 text-white rounded"
            >
              Xem thông tin
            </button>
          </div>
        </template>
      </CustomTable>

      <div class="mt-4 flex justify-center">
        <Pagination
          :total="pageInfo.totalElements"
          :items-per-page="pageInfo.size"
          :default-page="pageInfo.number + 1"
          @page-change="handlePageChange"
        />
      </div>
    </div>

    <!-- Modal xem thông tin -->
    <div
      v-if="isViewModalVisible"
      class="fixed inset-0 bg-gray-400 bg-opacity-50 flex justify-center items-center z-50"
      @click.self="closeModal"
    >
      <div class="bg-white p-6 rounded-lg w-1/2 max-w-3xl">
        <h3 class="text-xl font-semibold mb-4">Thông tin người dùng</h3>
        <div v-if="userToView">
          <p><strong>Tên người dùng:</strong> {{ userToView.full_name || '' }}</p>
          <p><strong>Email:</strong> {{ userToView.email || 'Chưa có thông tin' }}</p>
          <p><strong>Số điện thoại:</strong> {{ userToView.phone || 'Chưa có thông tin' }}</p>
          <p>
            <strong>Ngày sinh:</strong>
            {{ userToView.birth_date ? formatDate(userToView.birth_date) : 'Chưa có thông tin' }}
          </p>
          <p><strong>Giới tính:</strong> {{ userToView.gender || 'Chưa có thông tin' }}</p>
          <p><strong>Địa chỉ:</strong> {{ userToView.address || 'Chưa có thông tin' }}</p>
        </div>
        <div v-else>
          <p>Không có thông tin người dùng.</p>
        </div>
        <div class="mt-4 flex justify-end gap-2">
          <button
            @click="closeModal"
            class="px-4 py-2 bg-gray-300 text-black rounded hover:bg-gray-400"
          >
            Đóng
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import CustomTable from '@/components/base/CustomTable.vue'
import { getUsersApi, deleteUserApi, getUserDetailsApi } from '@/services/admin'
import type { User } from '@/services/admin'
import Pagination from '@/components/base/Pagination.vue'
import { computed } from 'vue'

// Định dạng ngày sinh
const formatDate = (dateString: string) => {
  const parts = dateString.split('-')
  if (parts.length !== 3) return dateString
  const [y, m, d] = parts
  return `${d}/${m}/${y}`
}

const filteredData = computed(() => {
  if (!searchKeyword.value) return tableData.value
  return tableData.value.filter((row) =>
    (row.raw.full_name || row.raw.email || '')
      .toLowerCase()
      .includes(searchKeyword.value.toLowerCase()),
  )
})

const tableHeaders = ['STT', 'Tên người dùng', 'Email', 'Số điện thoại', 'Ngày sinh', 'Hành động']
const tableData = ref<any[]>([])

const searchKeyword = ref('')
const pageInfo = ref({ number: 0, size: 10, totalElements: 0 })

const isViewModalVisible = ref(false)
const userToView = ref<User | null>(null)

// Map dữ liệu
const mapUser = (user: User, idx: number) => ({
  stt: idx + 1 + pageInfo.value.number * pageInfo.value.size,
  tenNguoiDung: user.full_name?.trim() || '',  // Sửa tại đây để trả về trống nếu không có tên
  email: user.email,
  soDienThoai: user.phone || '—',
  ngaySinh: user.birth_date ? formatDate(user.birth_date) : 'Chưa xác định',
  raw: user,
})

// Fetch danh sách
const fetchUsers = async (page = 0) => {
  const res = await getUsersApi(page, pageInfo.value.size)
  if (res?.content) {
    tableData.value = res.content.map(mapUser)
    pageInfo.value = {
      number: res.page.number,
      size: res.page.size,
      totalElements: res.page.totalElements,
    }
  }
}

// Lần đầu load
onMounted(() => fetchUsers())

const handlePageChange = (newPage: number) => {
  fetchUsers(newPage - 1) // Lưu ý newPage là base-1
}

const handleDelete = async (row: any) => {
  if (!confirm(`Bạn có chắc chắn muốn xóa "${row.tenNguoiDung}"?`)) return
  await deleteUserApi(row.raw.user_id)
  fetchUsers(pageInfo.value.number) // Reload lại trang hiện tại
}

const handleView = async (row: any) => {
  userToView.value = await getUserDetailsApi(row.raw.user_id)
  isViewModalVisible.value = true
}

const closeModal = () => {
  isViewModalVisible.value = false
}
</script>

<style scoped>
/* Nếu cần CSS thêm thì thêm ở đây */
</style>

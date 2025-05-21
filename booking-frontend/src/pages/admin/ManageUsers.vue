<template>
  <div class="space-y-6 px-4 py-6">
    <!-- Tiêu đề và phần đầu -->
    <div class="border rounded-lg p-6 bg-white shadow flex justify-between items-center">
      <h2 class="text-lg font-semibold text-blue-700 flex items-center">
        <Icon
          icon="mdi:account-group"
          class="mr-2"
          width="24"
          height="24"
        />
        Quản lý người dùng
      </h2>
    </div>

    <!-- Bảng danh sách người dùng -->
    <div class="border rounded-lg p-6 bg-white shadow">
      <h3 class="text-lg font-semibold mb-4 text-blue-700 flex items-center">
        <Icon
          icon="mdi:format-list-bulleted"
          class="mr-2"
          width="24"
          height="24"
        />
        Danh sách người dùng
      </h3>

      <!-- Loading indicator -->
      <div
        v-if="loading"
        class="py-8 text-center"
      >
        <div
          class="inline-block animate-spin rounded-full h-8 w-8 border-4 border-blue-500 border-t-transparent"
        ></div>
        <p class="mt-2 text-gray-600">Đang tải dữ liệu...</p>
      </div>

      <!-- Empty state -->
      <div
        v-else-if="filteredData.length === 0"
        class="py-12 text-center"
      >
        <Icon
          icon="mdi:account-off"
          class="mx-auto mb-4 text-gray-400"
          width="64"
          height="64"
        />
        <p class="text-gray-500 text-lg">Không có người dùng nào được tìm thấy</p>
      </div>

      <!-- Data table -->
      <div
        v-else
        class="overflow-x-auto"
      >
        <table class="min-w-full divide-y divide-gray-200 border">
          <thead class="bg-gray-100">
            <tr>
              <th class="px-4 py-3 text-left text-xs font-bold uppercase tracking-wider">STT</th>
              <th class="px-4 py-3 text-left text-xs font-bold uppercase tracking-wider">
                Tên người dùng
              </th>
              <th class="px-4 py-3 text-left text-xs font-bold uppercase tracking-wider">Email</th>
              <th class="px-4 py-3 text-left text-xs font-bold uppercase tracking-wider">
                Số điện thoại
              </th>
              <th class="px-4 py-3 text-left text-xs font-bold uppercase tracking-wider">
                Ngày sinh
              </th>
              <th class="px-4 py-3 text-left text-xs font-bold uppercase tracking-wider">
                Giới tính
              </th>
              <th class="px-4 py-3 text-left text-xs font-bold uppercase tracking-wider">
                Trạng thái
              </th>
              <th class="px-4 py-3 text-left text-xs font-bold uppercase tracking-wider">
                Hành động
              </th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr
              v-for="(user, index) in filteredData"
              :key="user.raw.user_id"
              class="hover:bg-gray-50"
            >
              <!-- STT -->
              <!-- <td class="px-4 py-3 whitespace-nowrap">{{ user.stt }}</td> -->
              <th class="px-4 py-3 whitespace-nowrap">
                {{ pageInfo.number * pageInfo.size + index + 1 }}
              </th>
              <!-- Tên người dùng -->
              <td class="px-4 py-3">
                <div class="font-medium text-gray-900">{{ getUserDisplayName(user.raw) }}</div>
                <div
                  v-if="user.raw.role"
                  class="text-xs text-gray-500"
                >
                  {{ getUserRole(user.raw.role) }}
                </div>
              </td>

              <!-- Email -->
              <td class="px-4 py-3">
                <div class="text-sm">{{ user.email || '—' }}</div>
              </td>

              <!-- Số điện thoại -->
              <td class="px-4 py-3 whitespace-nowrap">
                <div class="text-sm">{{ formatPhone(user.soDienThoai) }}</div>
              </td>

              <!-- Ngày sinh -->
              <td class="px-4 py-3 whitespace-nowrap">
                <div class="text-sm">{{ user.ngaySinh }}</div>
              </td>

              <!-- Giới tính -->
              <td class="px-4 py-3 whitespace-nowrap">
                <div class="text-sm">{{ user.raw.gender || '—' }}</div>
              </td>

              <!-- Trạng thái -->
              <td class="px-4 py-3 whitespace-nowrap">
                <span
                  :class="[
                    'px-2 py-1 text-xs font-medium rounded-full',
                    user.raw.status === 'active'
                      ? 'bg-green-100 text-green-800'
                      : 'bg-gray-100 text-gray-800',
                  ]"
                >
                  {{ getUserStatus(user.raw.status) }}
                </span>
              </td>

              <!-- Hành động -->
              <td class="px-4 py-3 whitespace-nowrap">
                <div class="flex items-center space-x-2">
                  <button
                    @click="handleView(user)"
                    class="p-2 bg-blue-500 hover:bg-blue-600 text-white rounded transition"
                    title="Xem chi tiết"
                  >
                    <Icon
                      icon="mdi:eye"
                      width="18"
                      height="18"
                    />
                  </button>

                  <button
                    @click="handleDelete(user)"
                    class="p-2 bg-red-500 hover:bg-red-600 text-white rounded transition"
                    title="Xóa"
                  >
                    <Icon icon="mdi:trash-can" width="20" height="20"/>
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <div class="pt-6 flex justify-center">
        <Pagination
          :total="pageInfo.totalElements"
          :items-per-page="pageInfo.size"
          :current-page="pageInfo.number + 1"
          @page-change="onPageChange"
        />
      </div>
    </div>

    <!-- Modal xem chi tiết người dùng -->
    <div
      v-if="isViewModalVisible"
      class="fixed inset-0 bg-gray-600 bg-opacity-50 flex justify-center items-center z-50"
      @click.self="closeModal"
    >
      <div class="bg-white p-6 rounded-lg shadow-lg w-full max-w-2xl">
        <div class="flex justify-between items-center mb-6">
          <h3 class="text-xl font-semibold text-blue-700 flex items-center">
            <Icon
              icon="mdi:account-details"
              class="mr-2"
              width="24"
              height="24"
            />
            Thông tin chi tiết người dùng
          </h3>
          <button
            @click="closeModal"
            class="text-gray-500 hover:text-gray-700"
          >
            <Icon
              icon="mdi:close"
              width="24"
              height="24"
            />
          </button>
        </div>

        <div
          v-if="userToView"
          class="grid grid-cols-1 md:grid-cols-2 gap-6"
        >
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-500">Tên người dùng</label>
              <p class="text-lg font-medium">{{ userToView.full_name || 'Chưa có thông tin' }}</p>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-500">Email</label>
              <p class="text-lg">{{ userToView.email || 'Chưa có thông tin' }}</p>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-500">Số điện thoại</label>
              <p class="text-lg">
                {{ userToView.phone ? formatPhone(userToView.phone) : 'Chưa có thông tin' }}
              </p>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-500">Ngày sinh</label>
              <p class="text-lg">
                {{
                  userToView.birth_date ? formatDate(userToView.birth_date) : 'Chưa có thông tin'
                }}
              </p>
            </div>
          </div>

          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-500">Giới tính</label>
              <p class="text-lg">{{ userToView.gender || 'Chưa có thông tin' }}</p>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-500">Địa chỉ</label>
              <p class="text-lg">{{ userToView.address || 'Chưa có thông tin' }}</p>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-500">Trạng thái</label>
              <p class="text-lg">
                <span
                  :class="[
                    'px-2 py-1 text-xs font-medium rounded-full',
                    userToView.status === 'active'
                      ? 'bg-green-100 text-green-800'
                      : 'bg-gray-100 text-gray-800',
                  ]"
                >
                  {{ getUserStatus(userToView.status) }}
                </span>
              </p>
            </div>
          </div>
        </div>

        <div
          v-else
          class="py-8 text-center text-gray-500"
        >
          <Icon
            icon="mdi:account-question"
            class="mx-auto mb-4"
            width="48"
            height="48"
          />
          <p>Không có thông tin người dùng</p>
        </div>

        <div class="mt-8 flex justify-end gap-3">
          <button
            @click="closeModal"
            class="px-4 py-2 bg-gray-300 text-gray-700 rounded hover:bg-gray-400 transition flex items-center"
          >
            <Icon
              icon="mdi:close"
              class="mr-2"
              width="18"
              height="18"
            />
            Đóng
          </button>
        </div>
      </div>
    </div>

    <!-- Modal xác nhận xóa -->
    <div
      v-if="showDeleteConfirm"
      class="fixed inset-0 bg-gray-600 bg-opacity-50 flex justify-center items-center z-50"
      @click.self="showDeleteConfirm = false"
    >
      <div class="bg-white p-6 rounded-lg shadow-lg w-full max-w-md">
        <div class="text-center mb-6">
          <h3 class="text-lg font-medium text-gray-900">Xác nhận xóa người dùng</h3>
          <div class="mt-2">
            <p class="mb-6">Bạn có chắc chắn muốn xóa người dùng này không?</p>
          </div>
        </div>
        <div class="flex justify-center gap-3">
          <button
            @click="showDeleteConfirm = false"
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
import { ref, computed, onMounted } from 'vue'
import { Icon } from '@iconify/vue'
import { getUsersApi, deleteUserApi, getUserDetailsApi } from '@/services/admin'
import { User } from '@/types/admin'
import Pagination from '@/components/base/Pagination.vue'

// State
const tableData = ref<any[]>([])
const searchKeyword = ref('')
const selectedGender = ref('')
const pageInfo = ref({ number: 0, size: 5, totalElements: 0 })
const loading = ref(false)

// Modals
const isViewModalVisible = ref(false)
const showDeleteConfirm = ref(false)
const userToView = ref<User | null>(null)
const userToDelete = ref<any>(null)

// Format ngày tháng
const formatDate = (dateString: string) => {
  if (!dateString) return '—'

  const parts = dateString.split('-')
  if (parts.length !== 3) return dateString
  const [y, m, d] = parts
  return `${d}/${m}/${y}`
}

// Format ngày giờ đầy đủ
// const formatDateTime = (dateTimeString: string) => {
//   if (!dateTimeString) return '—'

//   const date = new Date(dateTimeString)
//   if (isNaN(date.getTime())) return dateTimeString

//   const day = date.getDate().toString().padStart(2, '0')
//   const month = (date.getMonth() + 1).toString().padStart(2, '0')
//   const year = date.getFullYear()
//   const hours = date.getHours().toString().padStart(2, '0')
//   const minutes = date.getMinutes().toString().padStart(2, '0')

//   return `${day}/${month}/${year} ${hours}:${minutes}`
// }

// Format số điện thoại
const formatPhone = (phone: string): string => {
  if (!phone || phone === '—') return '—'

  // Loại bỏ các ký tự không phải số
  phone = phone.replace(/\D/g, '')

  if (phone.length === 10) {
    return phone.replace(/(\d{3})(\d{3})(\d{4})/, '$1 $2 $3')
  }
  return phone
}

// Hiển thị tên người dùng
const getUserDisplayName = (user: User): string => {
  if (user.full_name?.trim()) return user.full_name.trim()
  if (user.email) return user.email
  return 'Người dùng'
}

// Hiển thị vai trò người dùng
const getUserRole = (role: string): string => {
  const roles: Record<string, string> = {
    admin: 'Quản trị viên',
    user: 'Người dùng thường',
    manager: 'Quản lý',
  }
  return roles[role] || role
}

// Hiển thị trạng thái người dùng
const getUserStatus = (status?: string | null): string => {
  const statuses: Record<string, string> = {
    active: 'Đang hoạt động',
    inactive: 'Không hoạt động',
    pending: 'Chờ xác thực',
    suspended: 'Bị khóa',
  }
  return statuses[status ?? ''] ?? 'Không xác định'
}
// Chuyển đổi dữ liệu người dùng để hiển thị
const mapUser = (user: User, idx: number) => ({
  stt: idx + 1 + pageInfo.value.number * pageInfo.value.size,
  tenNguoiDung: user.full_name?.trim() || user.email || '—',
  email: user.email,
  soDienThoai: user.phone || '—',
  ngaySinh: user.birth_date ? formatDate(user.birth_date) : 'Chưa xác định',
  raw: user,
})

// Lấy dữ liệu người dùng từ API
// const fetchUsers = async (page = 0) => {
//   loading.value = true
//   try {
//     const res = await getUsersApi(
//       page,
//       pageInfo.value.size,
//       searchKeyword.value,
//       selectedGender.value,
//     )
//     if (res?.content) {
//       tableData.value = res.content.map(mapUser)
//       pageInfo.value = {
//         number: res.page.number,
//         size: res.page.size,
//         totalElements: res.page.totalElements,
//       }
//     }
//   } catch (error) {
//     console.error('Lỗi khi tải dữ liệu người dùng:', error)
//   } finally {
//     loading.value = false
//   }
// }
const fetchUsers = async (page = 0) => {
  loading.value = true
  try {
    const res = await getUsersApi(
      page,
      pageInfo.value.size,
      searchKeyword.value,
      selectedGender.value,
    )
    if (res?.content) {
      tableData.value = res.content.map(mapUser)
      pageInfo.value = {
        number: res.page.number,
        size: res.page.size,
        totalElements: res.page.totalElements,
      }
    }
  } catch (error) {
    console.error('Lỗi khi tải dữ liệu người dùng:', error)
  } finally {
    loading.value = false
  }
}

// // Lấy thông tin chi tiết người dùng
// const getResultsLabel = (): string => {
//   if (pageInfo.value.totalElements === 0) return 'Không có kết quả'

//   const start = pageInfo.value.number * pageInfo.value.size + 1
//   const end = Math.min(
//     (pageInfo.value.number + 1) * pageInfo.value.size,
//     pageInfo.value.totalElements,
//   )

//   return `Hiển thị ${start}-${end} trên ${pageInfo.value.totalElements} người dùng`
// }

// Xử lý khi thay đổi trang
const onPageChange = (page: number) => {
  fetchUsers(page - 1)
}

// Lọc dữ liệu theo từ khóa tìm kiếm
const filteredData = computed(() =>
  tableData.value.filter((row) =>
    row.tenNguoiDung.toLowerCase().includes(searchKeyword.value.toLowerCase()),
  ),
)

// Xử lý tìm kiếm với debounce
// const debounceTimeout = ref<number | null>(null)
// const onSearchInput = () => {
//   pageInfo.value.number = 0
//   if (debounceTimeout.value) {
//     clearTimeout(debounceTimeout.value)
//   }

//   debounceTimeout.value = window.setTimeout(() => {
//     fetchUsers(0)
//   }, 300)
// }

// Thêm mới người dùng
// const openCreateUser = () => {
//   // Implement later
//   alert('Chức năng đang được phát triển')
// }

// Xem chi tiết người dùng
const handleView = async (user: any) => {
  try {
    loading.value = true
    userToView.value = await getUserDetailsApi(user.raw.user_id)
    isViewModalVisible.value = true
  } catch (error) {
    console.error('Lỗi khi lấy thông tin chi tiết người dùng:', error)
    alert('Không thể tải thông tin chi tiết người dùng')
  } finally {
    loading.value = false
  }
}

// Xóa người dùng
const handleDelete = (user: any) => {
  userToDelete.value = user
  showDeleteConfirm.value = true
}

// Xác nhận xóa người dùng
const confirmDelete = async () => {
  if (!userToDelete.value) return

  try {
    loading.value = true
    await deleteUserApi(userToDelete.value.raw.user_id)
    await fetchUsers(pageInfo.value.number)
    showDeleteConfirm.value = false
    userToDelete.value = null
  } catch (error) {
    console.error('Lỗi khi xóa người dùng:', error)
    alert('Không thể xóa người dùng')
  } finally {
    loading.value = false
  }
}

// Đóng modal
const closeModal = () => {
  isViewModalVisible.value = false
  userToView.value = null
}

// Khởi tạo
onMounted(() => {
  fetchUsers()
})
</script>

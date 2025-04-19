<template>
    <div class="card relative">
      <div class="card-header flex justify-between items-center flex-wrap gap-4">
        <div class="flex items-center">
          <label for="search" class="mr-2">Tìm tên nhà cung cấp</label>
          <input
            id="search"
            v-model="searchKeyword"
            placeholder="Nhập tên nhà cung cấp..."
            class="px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <button
          class="bg-green-500 text-white px-4 py-2 rounded"
          @click="handleAddSupplier"
        >
          Thêm nhà cung cấp +
        </button>
      </div>
  
      <div class="card-body">
        <CustomTable :headers="tableHeaders" :rows="filteredData">
          <template #actions="{ row, index }">
            <div class="flex gap-2 justify-end">
              <button class="px-2 py-1 bg-green-500 text-white rounded" @click="handleEdit(row)">Sửa</button>
              <button class="px-2 py-1 bg-red-500 text-white rounded" @click="handleDelete(row)">Xóa</button>
              <button class="px-2 py-1 bg-yellow-500 text-white rounded" @click="handleToggleStatus(row)">
                {{ row.trangThai === 'Hoạt động' ? 'Tạm dừng' : 'Hoạt động' }}
              </button>
              <button class="px-2 py-1 bg-blue-500 text-white rounded" @click="handleView(row)">Xem chi tiết</button>
            </div>
          </template>
        </CustomTable>
      </div>
  
      <!-- Nút đóng -->
      <button
        class="absolute top-3 right-3 text-gray-600 hover:text-red-500 text-2xl transition-transform hover:scale-125"
      >
        ✕
      </button>
  
      <!-- Form chỉnh sửa hoặc xem chi tiết -->
      <div v-if="showForm" class="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
        <div class="bg-white p-6 rounded-md w-96">
          <h3 class="text-xl mb-4">{{ editMode ? 'Chỉnh sửa thông tin nhà cung cấp' : 'Chi tiết nhà cung cấp' }}</h3>
          <form @submit.prevent="handleSubmit">
            <div class="mb-4">
              <label for="name" class="block">Tên nhà cung cấp:</label>
              <input
                v-model="selectedSupplier.tenNhaCungCap"
                id="name"
                type="text"
                :readonly="!editMode"
                class="w-full px-3 py-2 border rounded"
                :disabled="!editMode"
              />
            </div>
            <div class="mb-4">
              <label for="address" class="block">Địa chỉ:</label>
              <input
                v-model="selectedSupplier.diaChi"
                id="address"
                type="text"
                :readonly="!editMode"
                class="w-full px-3 py-2 border rounded"
                :disabled="!editMode"
              />
            </div>
            <div class="mb-4">
              <label for="phone" class="block">Số điện thoại:</label>
              <input
                v-model="selectedSupplier.soDienThoai"
                id="phone"
                type="text"
                :readonly="!editMode"
                class="w-full px-3 py-2 border rounded"
                :disabled="!editMode"
              />
            </div>
            <div class="mb-4">
              <label for="email" class="block">Email:</label>
              <input
                v-model="selectedSupplier.email"
                id="email"
                type="email"
                :readonly="!editMode"
                class="w-full px-3 py-2 border rounded"
                :disabled="!editMode"
              />
            </div>
            <div class="flex gap-2 justify-end">
              <button
                v-if="editMode"
                type="submit"
                class="px-4 py-2 bg-green-500 text-white rounded"
              >
                Lưu
              </button>
              <button
                type="button"
                @click="closeForm"
                class="px-4 py-2 bg-gray-500 text-white rounded"
              >
                Đóng
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import CustomTable from '@/components/base/CustomTable.vue'
  import { ref, computed } from 'vue'
  
  const tableHeaders = ['STT', 'Tên nhà cung cấp', 'Địa chỉ', 'Số điện thoại', 'Email', 'Trạng thái', 'Hành động']
  
  const tableData = ref([
    {
      stt: 1,
      tenNhaCungCap: 'Công ty A',
      diaChi: 'Địa chỉ A',
      soDienThoai: '0123456789',
      email: 'cota@example.com',
      trangThai: 'Hoạt động'
    },
    {
      stt: 2,
      tenNhaCungCap: 'Công ty B',
      diaChi: 'Địa chỉ B',
      soDienThoai: '0987654321',
      email: 'cotb@example.com',
      trangThai: 'Tạm dừng'
    }
  ])
  
  const searchKeyword = ref('')
  const showForm = ref(false)
  const editMode = ref(false)
  const selectedSupplier = ref({
    stt: 0,
    tenNhaCungCap: '',
    diaChi: '',
    soDienThoai: '',
    email: '',
    trangThai: 'Hoạt động'
  })
  
  const filteredData = computed(() => {
    return tableData.value.filter(item => {
      return item.tenNhaCungCap.toLowerCase().includes(searchKeyword.value.toLowerCase())
    })
  })
  
  const handleAddSupplier = () => {
    // Mở form để thêm nhà cung cấp
    selectedSupplier.value = {
      stt: tableData.value.length + 1,  // Tạo STT mới cho nhà cung cấp
      tenNhaCungCap: '',
      diaChi: '',
      soDienThoai: '',
      email: '',
      trangThai: 'Hoạt động'
    }
    editMode.value = true
    showForm.value = true
  }
  
  const handleEdit = (row) => {
    selectedSupplier.value = { ...row }
    editMode.value = true
    showForm.value = true
  }
  
  const handleDelete = (row) => {
    const index = tableData.value.indexOf(row)
    if (index !== -1) {
      tableData.value.splice(index, 1)
    }
  }
  
  const handleToggleStatus = (row) => {
    row.trangThai = row.trangThai === 'Hoạt động' ? 'Tạm dừng' : 'Hoạt động'
  }
  
  const handleView = (row) => {
    selectedSupplier.value = { ...row }
    editMode.value = false
    showForm.value = true
  }
  
  const handleSubmit = () => {
    if (editMode.value) {
      // Cập nhật nhà cung cấp
      const index = tableData.value.findIndex(item => item.stt === selectedSupplier.value.stt)
      if (index !== -1) {
        tableData.value[index] = { ...selectedSupplier.value }
      }
    } else {
      // Thêm mới nhà cung cấp
      selectedSupplier.value.stt = tableData.value.length + 1  // Tạo STT mới cho nhà cung cấp
      tableData.value.push({ ...selectedSupplier.value })
    }
    closeForm()
  }
  
  const closeForm = () => {
    showForm.value = false
    selectedSupplier.value = {
      stt: 0,
      tenNhaCungCap: '',
      diaChi: '',
      soDienThoai: '',
      email: '',
      trangThai: 'Hoạt động'
    }
  }
  </script>

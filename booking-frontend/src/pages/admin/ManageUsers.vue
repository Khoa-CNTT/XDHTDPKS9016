
<template>
    <div class="card relative">
      <div class="card-header flex justify-between items-center flex-wrap gap-4">
        <div class="flex items-center">
          <label for="status" class="mr-2">Trạng thái</label>
          <select id="status"
            v-model="selectedStatus"
            class="px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500">
            <option value="">Tất cả</option>
            <option value="Chờ xác nhận">Chờ xác nhận</option>
            <option value="Đã xác nhận">Đã xác nhận</option>
            <option value="Hoàn tất">Hoàn tất</option>
            <option value="Đã hủy">Đã hủy</option>
          </select>
        </div>
        <div class="flex items-center">
          <label for="search" class="mr-2">Tìm tên</label>
          <input
            id="search"
            v-model="searchKeyword"
            placeholder="Nhập tên người dùng..."
            class="px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
      </div>
  
      <div class="card-body">
        <CustomTable :headers="tableHeaders" :rows="filteredData">
          <template #actions="{ row, index }">
            <div class="flex gap-2 justify-end">
              <button class="px-2 py-1 bg-green-500 text-white rounded" @click="handleConfirm(row)">Xác nhận</button>
              <button class="px-2 py-1 bg-red-500 text-white rounded" @click="handleDelete(row)">Xóa</button>
              <button class="px-2 py-1 bg-yellow-500 text-white rounded" @click="handleLock(row)">Khóa tài khoản</button>
              <button class="px-2 py-1 bg-blue-500 text-white rounded" @click="handleRestore(row)">Khôi phục tài khoản</button>
              <button class="px-2 py-1 bg-blue-500 text-white rounded" @click="handleView(row)">Xem thông tin</button>
            </div>
          </template>
        </CustomTable>
      </div>
      <button
        class="absolute top-3 right-3 text-gray-600 hover:text-red-500 text-2xl transition-transform hover:scale-125"
      >
        ✕
      </button>
    </div>
  </template>
  <script setup lang="ts">
  import CustomTable from '@/components/base/CustomTable.vue'
  import { ref, computed } from 'vue'
  
  const tableHeaders = ['STT', 'Tên người dùng', 'Email', 'Ngày tạo ', 'Trạng thái', 'Hành động']
  
  const tableData = ref([
    {
      stt: 1,
      tenNguoiDung: 'Nguyễn Văn A',
      email: 'nguyenvana@example.com',
      ngayTao: '2024-08-15',
      trangThai: 'Chờ xác nhận',
    },
    {
      stt: 2,
      tenNguoiDung: 'Trần Thị B',
      email: 'tranthib@example.com',
      ngayTao: '2024-07-30',
      trangThai: 'Đã xác nhận',
    },
    {
      stt: 3,
      tenNguoiDung: 'Lê Văn C',
      email: 'levanc@example.com',
      ngayTao: '2024-06-20',
      trangThai: 'Hoàn tất',
    }
  ])
  
  // lọc
  const selectedStatus = ref('')
  const searchKeyword = ref('')
  
  const filteredData = computed(() => {
    return tableData.value.filter(row => {
      const matchStatus = selectedStatus.value === '' || row.trangThai === selectedStatus.value
      const matchName = row.tenNguoiDung.toLowerCase().includes(searchKeyword.value.toLowerCase())
      return matchStatus && matchName
    })
  })
  
  const handleConfirm = (row: any) => {
    console.log('✅ Xác nhận:', row)
  }
  
  const handleDelete = (row: any) => {
    console.log('🗑️ Xóa:', row)
  }
  
  const handleLock = (row: any) => {
    console.log('🔒 Khóa:', row)
  }
  
  const handleRestore = (row: any) => {
    console.log('🔓 Khôi phục:', row)
  }
  
  const handleView = (row: any) => {
    console.log('👁️ Xem thông tin:', row)
  }
  </script>
  



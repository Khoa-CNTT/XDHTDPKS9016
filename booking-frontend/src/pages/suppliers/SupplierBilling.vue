<template>
    <div class="card">
      <div class="card-header flex justify-between items-center">
        <h2 class="text-lg font-semibold">Quản lý giao dịch thanh toán</h2>
      </div>
  
      <div class="card-body mt-4">
        <CustomTable :headers="tableHeaders" :rows="txData">
          <template #actions="{ row, index }">
            <div class="flex gap-2 justify-end">
              <button class="px-3 py-1 bg-gray-500 hover:bg-gray-600 text-white text-sm rounded-md" @click="viewDetail(row)">
                Xem
              </button>
              <button
                v-if="row.status === 'Pending'"
                class="px-3 py-1 bg-red-500 hover:bg-red-600 text-white text-sm rounded-md"
                @click="cancelTransaction(index)"
              >
                Huỷ
              </button>
              <button
                v-if="row.status === 'Pending'"
                class="px-3 py-1 bg-green-600 hover:bg-green-700 text-white text-sm rounded-md"
                @click="confirmTransaction(index)"
              >
                Xác nhận
              </button>
            </div>
          </template>
        </CustomTable>
      </div>
  
      <!-- Popup chi tiết -->
      <div v-if="selectedRow" class="fixed top-0 left-0 w-full h-full bg-gray-400 bg-opacity-30 flex justify-center items-center z-50">
        <div class="bg-white rounded-xl shadow-lg w-full max-w-md p-6">
          <h2 class="text-xl font-bold mb-4 text-center">Chi tiết giao dịch</h2>
          <ul class="space-y-2">
            <li><strong>Khách hàng:</strong> {{ selectedRow.customer }}</li>
            <li><strong>Số tài khoản:</strong> {{ selectedRow.accountNumber }}</li>
            <li><strong>Số tiền:</strong> {{ selectedRow.amount.toLocaleString() }} VND</li>
            <li><strong>Ngày:</strong> {{ selectedRow.date }}</li>
            <li><strong>Trạng thái:</strong> {{ selectedRow.status }}</li>
          </ul>
          <div class="mt-4 text-right">
            <button class="px-4 py-2 bg-blue-500 text-white rounded-md" @click="selectedRow = null">Đóng</button>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  import CustomTable from '@/components/base/CustomTable.vue'
  
  const txData = ref([
    { id: 1, customer: 'Nguyễn Văn A', accountNumber: '0123456789', amount: 100000, date: '2025-04-01', status: 'Completed' },
    { id: 2, customer: 'Trần Thị B', accountNumber: '9876543210', amount: 200000, date: '2025-04-02', status: 'Pending' }
  ])
  
  const tableHeaders = ['STT', 'Khách hàng', 'Số tài khoản', 'Số tiền', 'Ngày', 'Trạng thái', 'Hành động']
  
  const selectedRow = ref(null)
  
  const viewDetail = (row) => {
    selectedRow.value = row
  }
  
  const cancelTransaction = (index) => {
    if (confirm('Bạn có chắc chắn muốn huỷ giao dịch này?')) {
      txData.value.splice(index, 1)
    }
  }
  
  const confirmTransaction = (index) => {
    if (confirm('Bạn có chắc chắn muốn xác nhận giao dịch này?')) {
      txData.value[index].status = 'Completed'
    }
  }
  </script>
  
  <style scoped>
  /* Add any specific styling you need here */
  </style>
  
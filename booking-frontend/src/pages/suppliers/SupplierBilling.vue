<template>
  <div class="space-y-6 px-4 py-6">
    <!-- Tiêu đề -->
    <div class="border rounded-lg p-6 bg-white shadow flex flex-col sm:flex-row sm:justify-between sm:items-center gap-4">
      <h3 class="text-lg font-semibold text-blue-700 flex">
       Quản lý lịch sử giao dịch
      </h3>
      
    </div>

    <!-- Bảng danh sách -->
    <div class="border rounded-lg p-6 bg-white shadow">
      <h3 class="text-lg font-semibold mb-4 text-blue-700 flex">
        <Icon icon="mdi:clipboard-list-outline" width="24" height="24" class="mr-2" /> Danh sách giao dịch
      </h3>
      <table class="min-w-full divide-y divide-gray-200 border">
        <thead class="bg-gray-100">
          <tr>
            <th class="px-4 py-2 text-left">STT</th>
            <th class="px-4 py-2 text-left">Khách hàng</th>
            <th class="px-4 py-2 text-left">Số tài khoản</th>
            <th class="px-4 py-2 text-left">Số tiền</th>
            <th class="px-4 py-2 text-left">Ngày</th>
            <th class="px-4 py-2 text-left">Trạng thái</th>
            <th class="px-4 py-2 text-center">Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(row, index) in txData" :key="index" class="border-t">
            <td class="px-4 py-2">{{ index + 1 }}</td>
            <td class="px-4 py-2">{{ row.customer }}</td>
            <td class="px-4 py-2">{{ row.accountNumber }}</td>
            <td class="px-4 py-2">{{ row.amount.toLocaleString() }} VND</td>
            <td class="px-4 py-2">{{ row.date }}</td>
            <td class="px-4 py-2">
              <span class="px-2 py-1 rounded font-semibold border" :class="{
                'bg-yellow-100 text-yellow-700 border-yellow-300': row.status === 'Pending',
                'bg-green-100 text-green-700 border-green-300': row.status === 'Completed' || row.status === 'PAID',
                'bg-red-100 text-red-700 border-red-300': row.status === 'Cancelled'
              }">
                {{ row.status }}
              </span>
            </td>
            <td class="px-4 py-2 text-center">
              <div class="flex justify-center gap-2 flex-wrap">
                <!-- Nút Xác nhận -->
                <button
                  v-if="row.status === 'Pending'"
                  @click="confirmTransaction(index)"
                  class="p-2 bg-green-500 hover:bg-green-600 text-white rounded transition"
                  title="Xác nhận giao dịch"
                >
                  <Icon icon="mdi:check-circle" width="20" height="20" />
                </button>

               
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'


const txData = ref([

  { id: 2, customer: 'Trần Thị B', accountNumber: '9876543210', amount: 200000, date: '2025-04-02', status: 'Pending' },
  { id: 3, customer: 'Lê Văn D', accountNumber: '9876543222', amount: 150000, date: '2025-04-03', status: 'PAID' },
  
])

const selectedRow = ref(null)

const viewDetail = (row) => {
  selectedRow.value = row
}

const cancelTransaction = (index) => {
  if (confirm('Bạn có chắc chắn muốn huỷ giao dịch này?')) {
    txData.value[index].status = 'Cancelled'
  }
}

const confirmTransaction = (index) => {
  if (confirm('Bạn có chắc chắn muốn xác nhận giao dịch này?')) {
    txData.value[index].status = 'Completed'
  }
}
</script>

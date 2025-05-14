<template>
  <div class="space-y-6 px-4 py-6">
    <!-- Tiêu đề -->
    <div
      class="border rounded-lg p-6 bg-white shadow flex flex-col sm:flex-row sm:justify-between sm:items-center gap-4">
      <h3 class="text-lg font-semibold text-blue-700 flex">
        <Icon icon="mdi:filter-variant" width="24" height="24" class="mr-2" /> Bộ lọc
      </h3>
      <div class="flex items-center gap-2">
        <label for="search-service" class="text-sm font-medium text-gray-700 flex">
          <Icon icon="mdi:magnify" width="20" height="20" class="mr-2 text-gray-700" /> Tìm kiếm tên dịch vụ:
        </label>
        <input type="text" id="search-service" placeholder="Nhập tên dịch vụ..."
          class="px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500 w-64" />
      </div>
    </div>

    <!-- Bảng danh sách -->
    <div class="border rounded-lg p-6 bg-white shadow">
      <h3 class="text-lg font-semibold mb-4 text-blue-700 flex ">
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
                'bg-green-100 text-green-700 border-green-300': row.status === 'Completed',
                'bg-red-100 text-red-700 border-red-300': row.status === 'Cancelled'
              }">
                {{ row.status }}
              </span>
            </td>
            <td class="px-4 py-2 text-center">
              <div class="flex justify-center gap-2 flex-wrap">
                <!-- Nút Xác nhận -->
                <button v-if="row.status === 'Pending'" @click="confirmTransaction(index)"
                 class="p-2 bg-green-500 hover:bg-green-600 text-white rounded transition">
                  <Icon icon="mdi:check-circle" width="20" height="20" />
                </button>

                <!-- Nút Hủy -->
                <button v-if="row.status === 'Pending'" @click="cancelTransaction(index)"
                   class="p-2 bg-red-500 hover:bg-red-600 text-white rounded transition">
                  <Icon icon="mdi:cancel" width="20" height="20" />
                </button>

                <!-- Nút Xem -->
                <button @click="viewDetail(row)"
                 class="p-2 bg-blue-500 hover:bg-blue-600 text-white rounded transition">
                  <Icon icon="mdi:eye" width="20" height="20" />
                </button>
              </div>
            </td>


          </tr>
        </tbody>
      </table>
    </div>

    <!-- Popup chi tiết -->
    <div v-if="selectedRow" class="fixed inset-0 bg-gray-800 bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white p-8 rounded-2xl shadow-lg w-full max-w-2xl relative border border-blue-200 animate-fade-in">
        <h2 class="text-2xl font-bold text-center mb-6 text-purple-800">📄 Chi tiết giao dịch</h2>
        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 text-gray-700">
          <p><span class="font-semibold text-blue-600">👤 Khách hàng:</span> {{ selectedRow.customer }}</p>
          <p><span class="font-semibold text-blue-600">💳 Số tài khoản:</span> {{ selectedRow.accountNumber }}</p>
          <p><span class="font-semibold text-blue-600">💰 Số tiền:</span> {{ selectedRow.amount.toLocaleString() }} VND
          </p>
          <p><span class="font-semibold text-blue-600">📅 Ngày:</span> {{ selectedRow.date }}</p>
          <p class="sm:col-span-2"><span class="font-semibold text-blue-600">📌 Trạng thái:</span> {{ selectedRow.status
          }}</p>
        </div>
        <button
          class="absolute top-3 right-3 text-gray-600 hover:text-red-500 text-2xl transition-transform hover:scale-125"
          @click="selectedRow = null">
          ✕
        </button>
      </div>
    </div>
  </div>
</template>


<script setup>
import { ref } from 'vue'
import CustomTable from '@/components/base/CustomTable.vue'

const txData = ref([
  { id: 1, customer: 'Nguyễn Văn A', accountNumber: '0123456789', amount: 100000, date: '2025-04-01', status: 'Completed' },
  { id: 2, customer: 'Trần Thị B', accountNumber: '9876543210', amount: 200000, date: '2025-04-02', status: 'Pending' },
  { id: 3, customer: 'Trần Thị C', accountNumber: '9876543210', amount: 200000, date: '2025-04-02', status: 'Cancelled' }
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
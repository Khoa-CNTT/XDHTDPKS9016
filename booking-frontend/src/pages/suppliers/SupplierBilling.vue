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
        <Icon icon="mdi:clipboard-list-outline" width="24" height="24" class="mr-2" />
        Danh sách giao dịch
      </h3>

      <table class="min-w-full divide-y divide-gray-200 border table-auto">
        <thead class="bg-gray-100">
          <tr>
            <th class="px-4 py-2 text-left">STT</th>
            <th class="px-4 py-2 text-left">Tên khách hàng</th>
            <th class="px-4 py-2 text-left">Giá</th>
            <th class="px-4 py-2 text-left">Số tài khoản</th>
            <th class="px-4 py-2 text-left">Mã giao dịch</th>
            <th class="px-4 py-2 text-left">Ngày thanh toán</th>
            <th class="px-4 py-2 text-left">Thời gian thanh toán</th>
            <th class="px-4 py-2 text-left">Phương thức thanh toán</th>
            <th class="px-4 py-2 text-left">Trạng thái</th>
            <th class="px-4 py-2 text-center">Hành động</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="(row, index) in paymentList" :key="row.paymentId" class="border-t">
            <td class="px-4 py-2">{{ index + 1 }}</td>

            <td class="px-4 py-2 truncate max-w-[180px]" :title="row.customerName">
              {{ row.customerName }}
            </td>

            <td class="px-4 py-2">{{ row.amount.toLocaleString() }} VND</td>

            <td class="px-4 py-2 truncate max-w-[160px]" :title="row.accountNumber">
              {{ row.accountNumber }}
            </td>

            <td class="px-4 py-2 truncate max-w-[160px]" :title="row.transactionId">
              {{ row.transactionId }}
            </td>

            <td class="px-4 py-2">{{ row.paymentDate }}</td>

            <td class="px-4 py-2">{{ row.paymentTime }}</td>

            <td class="px-4 py-2 truncate max-w-[140px]" :title="row.paymentMethod">
              {{ row.paymentMethod }}
            </td>

            <td class="px-4 py-2">
              <span class="px-2 py-1 rounded font-semibold border"
                :class="{
                  'bg-yellow-100 text-yellow-700 border-yellow-300': row.status === 'Pending',
                  'bg-green-100 text-green-700 border-green-300': row.status === 'Completed' || row.status === 'PAID' || row.status === 'PROCESSED',
                  'bg-red-100 text-red-700 border-red-300': row.status === 'Cancelled'
                }">
                {{ row.status }}
              </span>
            </td>

            <td class="px-4 py-2 text-center">
              <div class="flex justify-center gap-2 flex-wrap">
                <!-- Chỉ hiển thị icon nếu status là 'Completed' -->
                <button v-if="row.status === 'Completed'" 
                  class="p-2 bg-green-500 hover:bg-green-600 text-white rounded transition" 
                  title="Đã hoàn tất">
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

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { getPaymentHistory as fetchPaymentHistory } from '@/services/supplier';
import type { PaymentHistory } from '@/types/supplier';

// Danh sách giao dịch
const paymentList = ref<PaymentHistory[]>([]);
const isLoading = ref(true);

// Hàm load dữ liệu từ API
const loadPaymentHistory = async () => {
  try {
    const res = await fetchPaymentHistory();
    console.log('history payment', res);
    paymentList.value = res;
  } catch (error) {
    console.error('Lỗi khi lấy lịch sử thanh toán:', error);
  } finally {
    isLoading.value = false;
  }
};

onMounted(() => {
  loadPaymentHistory();
});
</script>

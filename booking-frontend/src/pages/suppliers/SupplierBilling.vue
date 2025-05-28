<template>
  <div class="space-y-6 px-4 py-6">
    <!-- Tiêu đề -->
    <div
      class="border rounded-lg p-6 bg-white shadow flex flex-col sm:flex-row sm:justify-between sm:items-center gap-4"
    >
      <h3 class="text-lg font-semibold text-blue-700 flex">
        Quản lý lịch sử giao dịch
      </h3>
    </div>

    <!-- Bảng danh sách -->
    <div class="border rounded-lg p-6 bg-white shadow overflow-x-auto">
      <h3 class="text-lg font-semibold mb-4 text-blue-700 flex">
        <Icon
          icon="mdi:clipboard-list-outline"
          width="24"
          height="24"
          class="mr-2"
        />
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
          <tr v-if="paymentList.length === 0">
            <td class="px-4 py-2 text-center" :colspan="10">
              Không có giao dịch nào
            </td>
          </tr>

          <tr
            v-else
            v-for="(row, index) in paymentList"
            :key="row.paymentId"
            class="border-t"
          >
            <!-- STT tính theo tổng trang (phân trang) -->
            <td class="px-4 py-2">{{ (currentPage * size) + index + 1 }}</td>
            <td class="px-4 py-2 truncate max-w-[180px]" :title="row.customerName">
              {{ row.customerName }}
            </td>
            <td class="px-4 py-2">{{ row.amount.toLocaleString() }}</td>
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
            <td class="px-4 py-2 max-w-[140px] truncate" :title="row.status">
              <span
                class="px-2 py-1 rounded font-semibold border block text-center"
                :class="{
                  'bg-yellow-100 text-yellow-700 border-yellow-300': row.status === 'PAID',
                  'bg-green-100 text-green-700 border-green-300': row.status === 'COMPLETED',
                  'bg-red-100 text-red-700 border-red-300': row.status === 'Cancelled',
                  'bg-gray-100 text-gray-700 border-gray-300': !['PAID', 'COMPLETED', 'Cancelled'].includes(row.status)
                }"
              >
                {{
                  row.status === 'PAID'
                    ? 'Chờ xác nhận'
                    : row.status === 'COMPLETED'
                    ? 'Thành công'
                    : row.status
                }}
              </span>
            </td>
            <td class="px-4 py-2 text-center">
              <div class="flex justify-center gap-2 flex-wrap">
                <button
                  v-if="row.status === 'PAID'"
                  class="p-2 bg-yellow-500 hover:bg-yellow-600 text-white rounded transition"
                  title="Cập nhật trạng thái thành Completed"
                  @click="handleUpdateStatus(row.paymentId, row, 'COMPLETED')"
                >
                  <Icon icon="mdi:check-circle" width="20" height="20" />
                </button>
                <button
                  v-else-if="row.status === 'COMPLETED'"
                  disabled
                  class="p-2 bg-green-500 text-white rounded cursor-not-allowed"
                  title="Đã hoàn tất"
                >
                  <Icon icon="mdi:check-circle" width="20" height="20" />
                </button>
              </div>
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
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { getPaymentHistory as fetchPaymentHistory, updatePaymentStatus } from '@/services/supplier';
import type { PaymentHistory } from '@/types/supplier';
import { toast } from 'vue3-toastify';
import Pagination from '@/components/base/Pagination.vue';

const paymentList = ref<PaymentHistory[]>([]);
const isLoading = ref(true);

// Phân trang
const totalElements = ref(0);
const totalPages = ref(0);
const size = ref(5);          
const currentPage = ref(0);  

// Hàm load dữ liệu theo trang
const loadPaymentHistory = async (page = 0) => {
  isLoading.value = true;
  try {
  
    const res = await fetchPaymentHistory(page, size.value);

    paymentList.value = res.content;
    totalElements.value = res.page.totalElements;
    totalPages.value = res.page.totalPages;
    size.value = res.page.size;
    currentPage.value = res.page.number;
  } catch (error) {
    console.error('Lỗi khi lấy lịch sử thanh toán:', error);
    toast.error('Lấy lịch sử thanh toán thất bại', { autoClose: 5000, position: 'top-right' });
  } finally {
    isLoading.value = false;
  }
};

const handlePageChange = (page: number) => {
  loadPaymentHistory(page - 1);
};

const handleUpdateStatus = async (
  paymentId: number,
  row: PaymentHistory,
  newStatus: string
) => {
  try {
    await updatePaymentStatus(paymentId, newStatus);
    toast.success('Cập nhật trạng thái thành công', { autoClose: 5000, position: 'top-right' });

    const index = paymentList.value.findIndex(p => p.paymentId === paymentId);
    if (index !== -1) {
      paymentList.value[index].status = newStatus;
    }
  } catch (error) {
    console.error('Lỗi khi cập nhật trạng thái:', error);
    toast.error('Cập nhật trạng thái thất bại', { autoClose: 5000, position: 'top-right' });
  }
};

onMounted(() => {
  loadPaymentHistory();
});
</script>

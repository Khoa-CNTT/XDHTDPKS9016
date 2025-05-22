<template>
  <div class="card p-4">
    <!-- Bộ chọn năm và quý -->
    <div class="flex justify-center gap-4 mb-6">
      <select
        v-model="selectedYear"
        class="border rounded px-3 py-1"
      >
        <option
          v-for="year in years"
          :key="year"
          :value="year"
        >
          {{ year }}
        </option>
      </select>

      <select
        v-model="selectedQuarter"
        class="border rounded px-3 py-1"
      >
        <option
          v-for="q in 4"
          :key="q"
          :value="q"
        >
          Quý {{ q }}
        </option>
      </select>

      <button
        @click="fetchStats"
        class="px-4 py-1 bg-blue-500 text-white rounded hover:bg-blue-600"
      >
        Xem thống kê
      </button>
    </div>

    <!-- Nút chọn loại thống kê -->
    <div class="card-header flex justify-center gap-4 mb-6">
      <button
        v-for="key in statKeys"
        :key="key"
        :class="['tab-button', selectedStat === key ? 'active' : '']"
        @click="selectedStat = key"
      >
        {{ statLabels[key] }}
      </button>
    </div>

    <!-- Vùng hiển thị biểu đồ -->
    <div class="card-body w-full flex flex-wrap gap-8 justify-center">
      <div
        v-if="hasData"
        class="grid grid-cols-1 md:grid-cols-2 gap-6 w-full max-w-4xl"
      >
        <div class="flex justify-center">
          <DoughnutChart
            :chartData="chartData"
            :chartOptions="chartOptions"
            class="w-64 h-64"
          />
        </div>
        <div class="flex justify-center">
          <PolarChart
            :chartData="chartData"
            :chartOptions="chartOptions"
            class="w-64 h-64"
          />
        </div>
      </div>
      <div
        v-else
        class="text-center text-red-500 font-semibold"
      >
        Không có dữ liệu cho năm {{ selectedYear }} và quý {{ selectedQuarter }}.
      </div>
      <!-- Bảng hiển thị số liệu -->
<div v-if="dataStats" class="mt-8 overflow-x-auto">
  <table class="min-w-full text-sm text-left border border-gray-300">
    <thead class="bg-gray-100">
      <tr>
        <th class="px-4 py-2 border">Loại thống kê</th>
        <th class="px-4 py-2 border">Giá trị</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="key in statKeys" :key="key">
        <td class="px-4 py-2 border">{{ statLabels[key] }}</td>
        <td class="px-4 py-2 border font-semibold">
          {{ dataStats[key] ?? 'N/A' }}
        </td>
      </tr>
    </tbody>
  </table>
</div>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import DoughnutChart from '@/components/base/DoughnutChart.vue'
import PolarChart from '@/components/base/PolarChart.vue'
import { getAdminStatisticsApi } from '@/services/admin'
import type { DashboardStats } from '@/types/admin'
const hasData = ref(false)
const years = [2023, 2024, 2025] // Hoặc lấy động
const selectedYear = ref(2025)
const selectedQuarter = ref(2)

const dataStats = ref<DashboardStats | null>(null)

// Các key trong dữ liệu để hiển thị nút chọn
const statKeys = ['accountHotel', 'accountCount', 'bookingCount', 'totalPayment'] as const
type StatKey = (typeof statKeys)[number]
const selectedStat = ref<StatKey>('accountHotel')

// Định nghĩa tên hiển thị cho các key
const statLabels: Record<StatKey, string> = {
  accountHotel: 'Tài khoản khách sạn',
  accountCount: 'Tổng số tài khoản đăng ký',
  bookingCount: 'Tài khoản người dùng',
  totalPayment: 'Tổng số tiền thanh toán',
}

// Hàm fetch data theo năm & quý
const fetchStats = async () => {
  try {
    const data = await getAdminStatisticsApi(selectedYear.value, selectedQuarter.value)
    console.log('bieu do',data)
    dataStats.value = data
  } catch (error) {
    console.error('Lỗi khi fetch thống kê:', error)
  }
}

// Gọi fetch lần đầu khi component mount
fetchStats()

const chartData = ref<any>({
  labels: [],
  datasets: [],
})

const chartOptions = {
  responsive: true,
  plugins: {
    legend: {
      position: 'bottom' as const,
    },
  },
}

// Cập nhật dữ liệu biểu đồ khi dataStats hoặc selectedStat thay đổi
watch([dataStats, selectedStat], () => {
  if (!dataStats.value) {
    chartData.value = { labels: [], datasets: [] }
    hasData.value = false
    return
  }

  const label = selectedStat.value
  const value = dataStats.value[label]

  // Nếu không có dữ liệu hoặc bằng 0 thì không hiển thị
  if (!value || value === 0) {
    chartData.value = { labels: [], datasets: [] }
    hasData.value = false
    return
  }

  chartData.value = {
    labels: [statLabels[label], 'Khác'],
    datasets: [
      {
        label: statLabels[label],
        data: [value, 100], // hoặc chỉ đơn giản là [value]
        backgroundColor: ['#36A2EB', '#FF6384'],
        hoverOffset: 30,
      },
    ],
  }

  hasData.value = true
})
</script>

<style scoped>
.tab-button {
  @apply px-4 py-2 border rounded bg-gray-100 hover:bg-gray-200 cursor-pointer;
}
.tab-button.active {
  @apply bg-blue-500 text-white;
}
table {
  border-collapse: collapse;
}

th,
td {
  text-align: left;
}

th {
  background-color: #f3f4f6;
}

</style>

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

    <!-- Vùng hiển thị thông báo hoặc biểu đồ -->
    <div class="card-body w-full flex flex-col items-center gap-4">
      <div
        v-if="noDataMessage"
        class="text-red-600 font-semibold mb-4"
      >
        {{ noDataMessage }}
      </div>

      <div
        v-else
        class="flex flex-wrap gap-8 justify-center"
      >
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6 w-full max-w-4xl">
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
      </div>
      <!-- Bảng số liệu -->

      <div
        v-if="dataStats"
        class="mt-6 w-full max-w-md"
      >
        <h3 class="text-base font-semibold mb-2 text-center">Bảng số liệu</h3>
        <table class="min-w-full table-auto border-collapse border border-gray-300 text-sm">
          <thead>
            <tr class="bg-gray-100">
              <th class="border border-gray-300 px-2 py-1 text-left">Chỉ số</th>
              <th class="border border-gray-300 px-2 py-1 text-right">Giá trị</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="key in statKeys"
              :key="key"
            >
              <td class="border border-gray-300 px-2 py-1">{{ statLabels[key] }}</td>
              <td class="border border-gray-300 px-2 py-1 text-right">
                {{ formatNumber(dataStats[key]) }}
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
import { getHotelStatisticsApi } from '@/services/supplier'
import type { DashboardStatsPartial } from '@/types/supplier'

const years = [2023, 2024, 2025]
const selectedYear = ref(2025)
const selectedQuarter = ref(2)
const formatNumber = (value: number | undefined): string => {
  if (!value) return '0'
  return value.toLocaleString('vi-VN')
}
const dataStats = ref<DashboardStatsPartial | null>(null)
const noDataMessage = ref('')

// Các key trong dữ liệu để hiển thị nút chọn
const statKeys = ['bookingCount', 'totalPayment'] as const
type StatKey = (typeof statKeys)[number]
const selectedStat = ref<StatKey>('bookingCount')

// Định nghĩa tên hiển thị cho các key
const statLabels: Record<StatKey, string> = {
  bookingCount: 'Số lượt đặt chỗ',
  totalPayment: 'Tổng số tiền thanh toán',
}

// Hàm fetch data theo năm & quý
const fetchStats = async () => {
  try {
    const data = await getHotelStatisticsApi(selectedYear.value, selectedQuarter.value)
    dataStats.value = data

    // Kiểm tra data trả về cho thống kê hiện tại
    const label = selectedStat.value
    const value = data ? data[label] : null

    if (!value || value === 0) {
      noDataMessage.value = 'Không có dữ liệu cho năm và quý bạn chọn.'
    } else {
      noDataMessage.value = ''
    }
  } catch (error) {
    console.error('Lỗi khi fetch thống kê:', error)
    noDataMessage.value = 'Lỗi khi lấy dữ liệu thống kê.'
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
    noDataMessage.value = 'Không có dữ liệu.'
    return
  }

  const label = selectedStat.value
  const value = dataStats.value[label]

  if (!value || value === 0) {
    noDataMessage.value = 'Không có dữ liệu cho thống kê này.'
    chartData.value = { labels: [], datasets: [] }
    return
  } else {
    noDataMessage.value = ''
  }

  chartData.value = {
    labels: [statLabels[label], 'Other'],
    datasets: [
      {
        label: statLabels[label],
        data: [value, 100],
        backgroundColor: ['#36A2EB', '#FF6384'],
        hoverOffset: 30,
      },
    ],
  }
})
</script>

<style scoped>
.tab-button {
  @apply px-4 py-2 border rounded bg-gray-100 hover:bg-gray-200 cursor-pointer;
}
.tab-button.active {
  @apply bg-blue-500 text-white;
}
</style>

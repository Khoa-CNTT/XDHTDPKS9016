<template>
  <div class="card">
    <div class="card-header flex justify-center gap-4 mb-6">
      <button
        @click="currentTab = 'count'"
        :class="['tab-button', currentTab === 'count' ? 'active' : '']"
      >
        Số lượng đặt phòng
      </button>
      <button
        @click="currentTab = 'revenue'"
        :class="['tab-button', currentTab === 'revenue' ? 'active' : '']"
      >
        Doanh thu
      </button>
    </div>

    <div class="card-body">
      <!-- Khi chọn Số lượng đặt phòng: hiển thị Doughnut và Polar -->
   <div v-show="currentTab === 'count'" class="grid grid-cols-1 md:grid-cols-2 gap-6 w-full max-w-4xl">
  <div class="flex justify-center">
    <DoughnutChart
      :chartData="bookingCountDoughnutChartData"
      :chartOptions="bookingCountDoughnutChartOptions"
      class="w-64 h-64"
    />
  </div>

  <div class="flex justify-center">
    <PolarAreaChart
      :chartData="bookingCountPolarChartData"
      :chartOptions="bookingCountPolarChartOptions"
      class="w-64 h-64"
    />
  </div>
</div>


      <!-- Khi chọn Doanh thu: hiển thị Doughnut và Polar -->
     <div v-show="currentTab === 'revenue'" class="grid grid-cols-1 md:grid-cols-2 gap-6 w-full max-w-4xl">
  <div class="flex justify-center">
    <DoughnutChart
      :chartData="revenueDoughnutChartData"
      :chartOptions="revenueDoughnutChartOptions"
      class="w-64 h-64"
    />
  </div>

  <div class="flex justify-center">
    <PolarAreaChart
      :chartData="revenuePolarChartData"
      :chartOptions="revenuePolarChartOptions"
      class="w-64 h-64"
    />
  </div>
</div>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import DoughnutChart from '@/components/base/DoughnutChart.vue';
import PolarAreaChart from '@/components/base/PolarChart.vue';

const currentTab = ref<'count' | 'revenue'>('count');

// Dữ liệu cho Báo Cáo Số lượng đặt phòng
// Số lượng phòng đã đặt và số phòng đã hủy trong tháng
const bookingCountDoughnutChartData = {
  labels: ['Đã đặt', 'Đã hủy'],
  datasets: [{
    data: [1500, 200],
    backgroundColor: ['#845EC2', '#D65DB1'],
    borderWidth: 1
  }]
};
const bookingCountDoughnutChartOptions = {
  responsive: true,
  plugins: {
    title: { display: true, text: 'Số lượng đặt phòng vs hủy (Tháng này)' },
    legend: { position: 'top' }
  }
};
const bookingCountPolarChartData = {
  labels: ['Đã đặt', 'Đã hủy'],
  datasets: [{
    data: [1500, 200],
    backgroundColor: ['#4dc9c0', '#ff6347'],
    borderWidth: 1
  }]
};
const bookingCountPolarChartOptions = {
  responsive: true,
  plugins: {
    title: { display: true, text: 'Phân bố đặt phòng vs hủy (Tháng này)' },
    legend: { position: 'top' }
  }
};

// Dữ liệu cho Báo Cáo Doanh thu
// Tổng doanh thu thu về trong tháng
const revenueDoughnutChartData = {
  labels: ['Tổng doanh thu'],
  datasets: [{
    data: [500000000],
    backgroundColor: ['#00C9A7'],
    borderWidth: 1
  }]
};
const revenueDoughnutChartOptions = {
  responsive: true,
  plugins: {
    title: { display: true, text: 'Tổng doanh thu (Tháng này) - VND' },
    legend: { position: 'top' }
  }
};
const revenuePolarChartData = {
  labels: ['Tổng doanh thu'],
  datasets: [{
    data: [500000000],
    backgroundColor: ['#36a2eb'],
    borderWidth: 1
  }]
};
const revenuePolarChartOptions = {
  responsive: true,
  plugins: {
    title: { display: true, text: 'Phân bố doanh thu (Tháng này) - VND' },
    legend: { position: 'top' }
  }
};
</script>

<style scoped>
.card-body {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  flex-wrap: wrap;
  gap: 1rem;
  padding: 20px;
}
.card-header {
  padding: 20px 0;
}
.tab-button {
  padding: 12px 24px;
  background-color: #f0f0f0;
  border: 2px solid #ddd;
  border-radius: 10px;
  color: #333;
  cursor: pointer;
  transition: background-color 0.3s ease, color 0.3s ease, border-color 0.3s ease;
}
.tab-button.active {
  background-color: #36a2eb;
  color: white;
  border-color: #1d7ed2;
}
.tab-button:not(.active):hover {
  background-color: #e0e0e0;
}
</style>
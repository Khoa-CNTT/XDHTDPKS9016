<template>
  <div class="card">
    <div class="card-header flex justify-between items-center">
      <!-- Tab Container -->
      <div class="tabs flex gap-4">
        <button :class="{ 'active': currentTab === 'quarterlyStats' }" @click="currentTab = 'quarterlyStats'"
          class="tab-button">Thống kê theo quý</button>
        <button :class="{ 'active': currentTab === 'mostBookedRoom' }" @click="currentTab = 'mostBookedRoom'"
          class="tab-button">Thống kê loại phòng được đặt nhiều nhất</button>
        <button :class="{ 'active': currentTab === 'bookingRate' }" @click="currentTab = 'bookingRate'"
          class="tab-button">Tỉ lệ đặt phòng</button>
        <button :class="{ 'active': currentTab === 'stayDuration' }" @click="currentTab = 'stayDuration'"
          class="tab-button">Thống kê về thời gian lưu trú</button>
      </div>
    </div>

    <div class="card-body w-full h-auto">
      <!-- Nội dung User -->
      <div v-show="currentTab === 'quarterlyStats'" class="grid grid-cols-1 gap-6 w-full">
        <div class="p-4 border rounded shadow-sm h-96 w-full">
          <BarChart :chart-data="revenueByQuarterChartData" :chart-options="revenueByQuarterChartOptions" />
        </div>
      </div>
      <div v-show="currentTab === 'mostBookedRoom'" class="grid grid-cols-1 gap-6 w-full">
        <div class="p-4 border rounded shadow-sm w-full h-[80vh] flex justify-center items-center">
          <div class="w-[98%] max-w-[1400px] h-full flex justify-center items-center">
            <PolarChart :chartData="roomBookingChartData" :chartOptions="roomBookingChartOptions" />
          </div>
        </div>
      </div>
      <div v-show="currentTab === 'bookingRate'" class="grid grid-cols-1 gap-6 w-full">
        <div class="p-4 border rounded shadow-sm w-full h-[80vh] flex justify-center items-center">
          <div class="w-[98%] max-w-[1400px] h-full flex justify-center items-center">
            <DoughnutChart :chartData="bookingCancelChartData" :chartOptions="bookingCancelChartOptions" />

          </div>
        </div>
      </div>
      <div v-show="currentTab === 'stayDuration'" class="grid grid-cols-1 gap-6 w-full">
        <div class="p-4 border rounded shadow-sm w-full h-[80vh] flex justify-center items-center">
          <div class="w-[98%] max-w-[1400px] h-full flex justify-center items-center">
            <PieChart :chartData="stayDurationChartData" :chartOptions="stayDurationChartOptions" />

          </div>
        </div>
      </div>



    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import BarChart from '@/components/base/BarChart.vue';
import PolarChart from '@/components/base/PolarChart.vue';
import DoughnutChart from '@/components/base/DoughnutChart.vue';
import PieChart from '@/components/base/PieChart.vue';
const currentTab = ref('quarterlyStats');
const revenueByQuarterChartData = {
  labels: [
    'Q1 Năm 1', 'Q2 Năm 1', 'Q3 Năm 1', 'Q4 Năm 1',
    'Q1 Năm 2', 'Q2 Năm 2', 'Q3 Năm 2', 'Q4 Năm 2',
    'Q1 Năm 3', 'Q2 Năm 3', 'Q3 Năm 3', 'Q4 Năm 3',
    'Q1 Năm 4', 'Q2 Năm 4', 'Q3 Năm 4', 'Q4 Năm 4',
    'Q1 Năm 5', 'Q2 Năm 5', 'Q3 Năm 5', 'Q4 Năm 5',
  ],
  datasets: [
    {
      label: 'Doanh thu theo quý (triệu VND)',
      data: [800, 1100, 1400, 1600, 1550, 1750, 2000, 2300, 2450, 2300, 2600, 2700, 2900, 3000, 2800, 3100, 3400, 3600, 3700, 4000],
      backgroundColor: '#42a5f5',
      borderColor: '#1e88e5',
      borderWidth: 1,
    },
  ],
}
const revenueByQuarterChartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    title: {
      display: true,
      text: 'Doanh thu theo quý trong 5 năm gần nhất',
    },
    legend: {
      display: true,
      position: 'top',
    },
  },
  scales: {
    x: {
      ticks: {
        maxRotation: 45,
        minRotation: 30,
      },
    },
    y: {
      beginAtZero: true,
      title: {
        display: true,
        text: 'Doanh thu (triệu VND)',
      },
    },
  },
}
const roomBookingChartData = {
  labels: ['Phòng đơn', 'Phòng đôi', 'Phòng gia đình', 'Phòng cao cấp', 'Phòng tập thể'],
  datasets: [
    {
      label: 'Số lượng booking',
      data: [120, 200, 150, 90, 60],
      backgroundColor: [
        '#42a5f5',
        '#66bb6a',
        '#ffa726',
        '#ab47bc',
        '#ef5350'
      ],
      borderColor: '#fff',
      borderWidth: 1
    }
  ]
}
const roomBookingChartOptions = {
  responsive: true,
  plugins: {
    title: {
      display: true,
      text: 'Tỉ lệ số lượng booking theo loại phòng'
    },
    legend: {
      position: 'right'
    }
  }
}
const bookingCancelChartData = {
  labels: ['Đặt phòng', 'Hủy phòng'],
  datasets: [
    {
      label: 'Tỉ lệ Đặt phòng / Hủy phòng',
      data: [75, 25], // Tỉ lệ đặt phòng và hủy phòng (75% đặt, 25% hủy)
      backgroundColor: ['#42a5f5', '#ef5350'], // Màu sắc cho các phần trong biểu đồ
      borderColor: '#fff',
      borderWidth: 1
    }
  ]
}
const bookingCancelChartOptions = {
  responsive: true,
  plugins: {
    title: {
      display: true,
      text: 'Tỉ lệ Đặt Phòng / Hủy Phòng'
    },
    legend: {
      position: 'top'
    }
  }
}
const stayDurationChartData = {
  labels: ['1 đêm', '2-3 đêm', '4-7 đêm', 'Hơn 7 đêm'],
  datasets: [
    {
      label: 'Thời gian lưu trú',
      data: [40, 30, 20, 10], // Tỷ lệ khách hàng lưu trú trong các khoảng thời gian
      backgroundColor: ['#42a5f5', '#66bb6a', '#ffa726', '#ab47bc'],
      borderColor: '#fff',
      borderWidth: 1
    }
  ]
}

// Tùy chọn biểu đồ
const stayDurationChartOptions = {
  responsive: true,
  plugins: {
    title: {
      display: true,
      text: 'Thống kê về Thời gian Lưu Trú'
    },
    legend: {
      position: 'top'
    }
  }
}
</script>

<style scoped>
.card-body {
  width: 100%;
  padding: 20px;
}

.card-body .grid {
  width: 100%;
  display: flex;
  justify-content: center;
}

.card-body .h-96 {
  height: 60vh;
  width: 100%;
  /* Tăng chiều rộng từ 80% lên 90% */
  max-width: 1200px;
  /* Optional: tránh quá rộng ở màn hình lớn */
}

.tab-button {
  padding: 10px 20px;
  background-color: #f0f0f0;
  border: 1px solid #ddd;
  border-radius: 5px;
  color: #333;
  cursor: pointer;
  transition: background-color 0.3s ease, color 0.3s ease;
}

.tab-button.active {
  background-color: #36a2eb;
  color: white;
  border-color: #1d7ed2;
}

.tab-button:hover {
  background-color: #e0e0e0;
}
</style>

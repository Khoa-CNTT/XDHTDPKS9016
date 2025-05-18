<template>
  <div class="card">
    <div class="card-header flex justify-center gap-4 mb-6">
      <!-- User Button -->
      <button
        @click="currentTab = 'user'"
        :class="['tab-button', currentTab === 'user' ? 'active' : '']"
      >
        Người dùng
      </button>

      <!-- Supplier Button -->
      <button
        @click="currentTab = 'supplier'"
        :class="['tab-button', currentTab === 'supplier' ? 'active' : '']"
      >
        Khách sạn
      </button>
    </div>

    <div class="card-body w-full flex flex-wrap gap-8 justify-center">
      <!-- Nội dung User -->
     <div
  v-show="currentTab === 'user'"
  class="grid grid-cols-1 md:grid-cols-2 gap-6 w-full max-w-4xl"
>
  <div class="flex justify-center">
    <DoughnutChart
      :chartData="userStatusDoughnutChartData"
      :chartOptions="userStatusDoughnutChartOptions"
      class="w-64 h-64"
    />
  </div>

  <div class="flex justify-center">
    <PolarAreaChart
      :chartData="userStatusPolarChartData"
      :chartOptions="userStatusPolarChartOptions"
      class="w-64 h-64"
    />
  </div>
</div>


      <!-- Nội dung Nhà Cung Cấp -->
     <div
  v-show="currentTab === 'supplier'"
  class="grid grid-cols-1 md:grid-cols-2 gap-6 w-full max-w-4xl"
>
  <div class="flex justify-center">
    <DoughnutChart
      :chartData="supplierStatusDoughnutChartData"
      :chartOptions="supplierStatusDoughnutChartOptions"
      class="w-64 h-64"
    />
  </div>

  <div class="flex justify-center">
    <PolarAreaChart
      :chartData="supplierStatusPolarChartData"
      :chartOptions="supplierStatusPolarChartOptions"
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

const currentTab = ref('user');

// Dữ liệu cho User
const userStatusDoughnutChartData = {
  labels: ['Hoạt động', 'Tạm dừng', 'Không hoạt động'],
  datasets: [
    {
      data: [5000, 1200, 800],
      backgroundColor: ['#4dc9c0', '#ff6347', '#f39c12'],
      borderWidth: 1,
    },
  ],
};

const userStatusDoughnutChartOptions = {
  responsive: true,
  plugins: {
    title: {
      display: true,
      text: 'Tỷ lệ người dùng theo trạng thái',
    },
    legend: {
      position: 'top',
    },
  },
};

const userStatusPolarChartData = {
  labels: ['Hoạt động', 'Tạm dừng', 'Không hoạt động'],
  datasets: [
    {
      data: [5000, 1200, 800],
      backgroundColor: ['#4dc9c0', '#ff6347', '#f39c12'],
      borderWidth: 1,
    },
  ],
};

const userStatusPolarChartOptions = {
  responsive: true,
  plugins: {
    title: {
      display: true,
      text: 'Phân bố người dùng theo trạng thái',
    },
    legend: {
      position: 'top',
    },
  },
};

// Dữ liệu cho Nhà Cung Cấp
const supplierStatusDoughnutChartData = {
  labels: ['Hoạt động', 'Tạm dừng', 'Không hoạt động'],
  datasets: [
    {
      data: [3000, 1000, 500],
      backgroundColor: ['#36a2eb', '#ff9f40', '#ffcd56'],
      borderWidth: 1,
    },
  ],
};

const supplierStatusDoughnutChartOptions = {
  responsive: true,
  plugins: {
    title: {
      display: true,
      text: 'Tỷ lệ nhà cung cấp theo trạng thái',
    },
    legend: {
      position: 'top',
    },
  },
};

const supplierStatusPolarChartData = {
  labels: ['Hoạt động', 'Tạm dừng', 'Không hoạt động'],
  datasets: [
    {
      data: [3000, 1000, 500],
      backgroundColor: ['#36a2eb', '#ff9f40', '#ffcd56'],
      borderWidth: 1,
    },
  ],
};

const supplierStatusPolarChartOptions = {
  responsive: true,
  plugins: {
    title: {
      display: true,
      text: 'Phân bố nhà cung cấp theo trạng thái',
    },
    legend: {
      position: 'top',
    },
  },
};
</script>
<style scoped>
.card-body {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding: 20px;
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

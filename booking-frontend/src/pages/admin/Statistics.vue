<template>
  <div class="card">
    <div class="card-header flex justify-between items-center">
      <!-- Tab Container -->
      <div class="tabs flex gap-4">
  <button :class="{'active': currentTab === 'user'}" @click="currentTab = 'user'" class="tab-button">User</button>
  <button :class="{'active': currentTab === 'supplier'}" @click="currentTab = 'supplier'" class="tab-button">Nhà Cung Cấp</button>
</div>
    </div>

    <div class="card-body w-full h-auto">
      <!-- Nội dung User -->
      <div v-show="currentTab === 'user'" class="grid grid-cols-1 gap-6 w-full">
         <!-- BarChart: Số người dùng mới đăng ký theo quý -->
         <div class="p-4 border rounded shadow-sm h-96 w-full">
          <BarChart
            :chart-data="newUserByQuarterChartData"
            :chart-options="newUserByQuarterChartOptions"
          />
        </div>
        <!-- LineChart: Trạng thái người dùng -->
        <div class="p-4 border rounded shadow-sm h-96 w-full">
          <LineChart :chartData="quarterlyUserChartData" :chartOptions="quarterlyUserChartOptions" />
        </div>

       
      </div>

      <!-- Nội dung Nhà Cung Cấp -->
      <div v-show="currentTab === 'supplier'" class="grid grid-cols-1 gap-6 w-full">
        <div class="p-4 border rounded shadow-sm w-full">
          <BarChart
            :chart-data="newSupplierByQuarterChartData"
            :chart-options="newSupplierByQuarterChartOptions"
          />
        </div>
        <div class="p-4 border rounded shadow-sm h-96 w-full">
          <LineChart :chartData="quarterlySupplierChartData" :chartOptions="quarterlySupplierChartOptions" />
        </div>
      </div>
    </div>
  </div>
</template>



<script setup lang="ts">
import { ref } from 'vue';
import LineChart from '@/components/base/LineChart.vue';
import BarChart from '@/components/base/BarChart.vue';
const currentTab = ref('user'); 

const quarterlyUserChartData = {
  labels: ['Q1 Năm 1', 'Q2 Năm 1', 'Q3 Năm 1', 'Q4 Năm 1', 
           'Q1 Năm 2', 'Q2 Năm 2', 'Q3 Năm 2', 'Q4 Năm 2',
           'Q1 Năm 3', 'Q2 Năm 3', 'Q3 Năm 3', 'Q4 Năm 3',
           'Q1 Năm 4', 'Q2 Năm 4', 'Q3 Năm 4', 'Q4 Năm 4',
           'Q1 Năm 5', 'Q2 Năm 5', 'Q3 Năm 5', 'Q4 Năm 5'],
  datasets: [
    {
      label: 'Hoạt động',
      data: [200, 300, 250, 400, 350, 500, 450, 600, 550, 700, 680, 720, 690, 800, 750, 770, 740, 790, 730, 760],
      backgroundColor: '#4dc9c0',
      borderColor: '#4dc9c0',
      fill: false,
      tension: 0.1,
    },
    {
      label: 'Tạm dừng',
      data: [50, 70, 60, 80, 75, 85, 70, 90, 95, 85, 100, 105, 100, 110, 120, 115, 125, 130, 120, 135],
      backgroundColor: '#ff6347',
      borderColor: '#ff6347',
      fill: false,
      tension: 0.1,
    },
    {
      label: 'Không hoạt động',
      data: [20, 30, 25, 35, 40, 38, 45, 50, 48, 55, 58, 60, 57, 65, 62, 70, 68, 75, 72, 78],
      backgroundColor: '#f39c12',
      borderColor: '#f39c12',
      fill: false,
      tension: 0.1,
    },
  ],
}

const quarterlyUserChartOptions = {
  responsive: true,
  maintainAspectRatio: false,  
  plugins: {
    title: {
      display: true,
      text: 'Số lượng tài khoản theo trạng thái (Hoạt động, Tạm dừng, Không hoạt động) theo quý trong 5 năm',
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
    },
  },
}

// Dữ liệu cho biểu đồ Nhà Cung Cấp theo quý trong 5 năm
const quarterlySupplierChartData = {
  labels: ['Q1 Năm 1', 'Q2 Năm 1', 'Q3 Năm 1', 'Q4 Năm 1', 
           'Q1 Năm 2', 'Q2 Năm 2', 'Q3 Năm 2', 'Q4 Năm 2',
           'Q1 Năm 3', 'Q2 Năm 3', 'Q3 Năm 3', 'Q4 Năm 3',
           'Q1 Năm 4', 'Q2 Năm 4', 'Q3 Năm 4', 'Q4 Năm 4',
           'Q1 Năm 5', 'Q2 Năm 5', 'Q3 Năm 5', 'Q4 Năm 5'],
  datasets: [
    {
      label: 'Hoạt động',
      data: [80, 100, 95, 130, 120, 160, 140, 180, 175, 190, 170, 210, 220, 200, 230, 250, 240, 260, 255, 280],
      backgroundColor: '#4dc9c0',
      borderColor: '#4dc9c0',
      fill: false,
      tension: 0.1,
    },
    {
      label: 'Tạm dừng',
      data: [15, 25, 20, 35, 40, 38, 30, 50, 55, 48, 60, 65, 58, 75, 70, 85, 80, 95, 88, 100],
      backgroundColor: '#ff6347',
      borderColor: '#ff6347',
      fill: false,
      tension: 0.1,
    },
    {
      label: 'Không hoạt động',
      data: [5, 12, 10, 18, 20, 22, 30, 28, 32, 35, 33, 38, 40, 42, 45, 50, 55, 53, 58, 60],
      backgroundColor: '#f39c12',
      borderColor: '#f39c12',
      fill: false,
      tension: 0.1,
    },
  ],
}

const quarterlySupplierChartOptions = {
  responsive: true,
  maintainAspectRatio: false,  
  plugins: {
    title: {
      display: true,
      text: 'Số lượng Nhà Cung Cấp theo trạng thái (Hoạt động, Tạm dừng, Không hoạt động) theo quý trong 5 năm',
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
    },
  },
}
const newUserByQuarterChartData = {
  labels: [
    'Q1 Năm 1', 'Q2 Năm 1', 'Q3 Năm 1', 'Q4 Năm 1',
    'Q1 Năm 2', 'Q2 Năm 2', 'Q3 Năm 2', 'Q4 Năm 2',
    'Q1 Năm 3', 'Q2 Năm 3', 'Q3 Năm 3', 'Q4 Năm 3',
    'Q1 Năm 4', 'Q2 Năm 4', 'Q3 Năm 4', 'Q4 Năm 4',
    'Q1 Năm 5', 'Q2 Năm 5', 'Q3 Năm 5', 'Q4 Năm 5',
  ],
  datasets: [
    {
      label: 'Người dùng mới',
      data: [120, 160, 140, 200, 180, 210, 190, 230, 250, 220, 270, 260, 290, 310, 280, 300, 320, 340, 360, 350],
      backgroundColor: '#36a2eb',
      borderColor: '#1d7ed2',
      borderWidth: 1,
    },
  ],
}

const newUserByQuarterChartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    title: {
      display: true,
      text: 'Số người dùng mới đăng ký theo quý trong 5 năm gần nhất',
    },
    legend: {
      display: true,
      position: 'top',
    }
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
        text: 'Số người dùng'
      }
    },
  },
}
const newSupplierByQuarterChartData = {
  labels: [
    'Q1 Năm 1', 'Q2 Năm 1', 'Q3 Năm 1', 'Q4 Năm 1',
    'Q1 Năm 2', 'Q2 Năm 2', 'Q3 Năm 2', 'Q4 Năm 2',
    'Q1 Năm 3', 'Q2 Năm 3', 'Q3 Năm 3', 'Q4 Năm 3',
    'Q1 Năm 4', 'Q2 Năm 4', 'Q3 Năm 4', 'Q4 Năm 4',
    'Q1 Năm 5', 'Q2 Năm 5', 'Q3 Năm 5', 'Q4 Năm 5',
  ],
  datasets: [
    {
      label: 'Nhà cung cấp mới',
      data: [80, 110, 130, 150, 140, 160, 180, 210, 220, 200, 230, 240, 260, 270, 250, 280, 300, 320, 330, 350],
      backgroundColor: '#ff9f40',
      borderColor: '#f56c15',
      borderWidth: 1,
    },
  ],
}

const newSupplierByQuarterChartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    title: {
      display: true,
      text: 'Số nhà cung cấp mới đăng ký theo quý trong 5 năm gần nhất',
    },
    legend: {
      display: true,
      position: 'top',
    }
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
        text: 'Số nhà cung cấp'
      }
    },
  },
}

</script>

  
  <style scoped>
.card-body {
  width: 100%; 
  display: flex;
  justify-content: center;
  align-items: flex-start;
  height: 100vh;
  padding:20px;
}

.card-body .grid {
  width: 100%; 
  max-width: 100vw; 
}

.card-body .h-96 {
  height: 60vh; 
  width: 78vw;  
  margin-top: 0; 
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
  
  
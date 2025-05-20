<template>
    <div>
      <canvas ref="chart"></canvas>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import { Chart } from 'chart.js';
  import { Radar } from 'chart.js';
  
  // Chart.js Radar Chart
  const chart = ref(null);
  
  const props = defineProps({
    chartData: Object,
    chartOptions: Object
  });
  
  onMounted(() => {
    chart.value = new Chart(chart.value, {
      type: 'radar',
      data: props.chartData,
      options: props.chartOptions
    });
  });
  </script>
  
  <style scoped>
  canvas {
    width: 100%;
    height: 100%;
  }
  </style>
  
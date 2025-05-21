<script setup lang="ts">
import { Pie } from 'vue-chartjs'
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  ArcElement,
} from 'chart.js'

ChartJS.register(Title, Tooltip, Legend, ArcElement)

const props = defineProps<{
  chartData: any
  chartOptions?: any
}>()
</script>

<template>
  <Pie :data="chartData" :options="chartOptions" />
</template>

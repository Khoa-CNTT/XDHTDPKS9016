<script setup lang="ts">
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  RadialLinearScale,
  ArcElement,
} from 'chart.js'

import { PolarArea } from 'vue-chartjs' 

ChartJS.register(Title, Tooltip, Legend, RadialLinearScale, ArcElement)

const props = defineProps<{
  chartData: any
  chartOptions?: any
}>()
</script>

<template>
  <PolarArea :data="chartData" :options="chartOptions" />
</template>

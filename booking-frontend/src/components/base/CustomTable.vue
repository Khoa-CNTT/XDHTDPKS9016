
<script setup lang="ts">
import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table'
import { defineProps } from 'vue'

interface TableData {
  [key: string]: string | number
}

const props = defineProps<{
  caption?: string
  headers: string[]
  rows: TableData[]
}>()
</script>

<template>
  <Table>
    <TableCaption>{{ caption || '' }}</TableCaption>
    <TableHeader>
      <TableRow>
        <TableHead
          class="font-bold"
          v-for="(header, index) in headers"
          :key="index"
          :class="index === headers.length - 1 ? 'text-right' : ''"
        >
          {{ header }}
        </TableHead>
      </TableRow>
    </TableHeader>

    <TableBody>
      <TableRow v-for="(row, rowIndex) in rows" :key="rowIndex">
        <TableCell v-for="(value, colIndex) in Object.values(row).slice(0, headers.length - 1)" :key="colIndex">
          {{ value }}
        </TableCell>

        <!-- Cột Hành động - render slot -->
        <TableCell class="text-right">
          <slot name="actions" :row="row" :index="rowIndex" />
        </TableCell>
      </TableRow>
    </TableBody>
  </Table>
</template>


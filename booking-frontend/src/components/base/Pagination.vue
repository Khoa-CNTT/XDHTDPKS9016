<script setup lang="ts">
import {
  Pagination,
  PaginationEllipsis,
  PaginationFirst,
  PaginationLast,
  PaginationList,
  PaginationListItem,
  PaginationNext,
  PaginationPrev,
} from '@/components/ui/pagination'

import { Button } from '@/components/ui/button'
import { defineProps, defineEmits } from 'vue'

const props = defineProps({
  total: {
    type: Number,
    required: true
  },
  itemsPerPage: {
    type: Number,
    default: 10
  },
  defaultPage: {
    type: Number,
    default: 1
  },
  siblingCount: {
    type: Number,
    default: 1
  }
})

const emit = defineEmits(['page-change'])
</script>

<template>
  <Pagination
    v-slot="{ page }"
    :items-per-page="props.itemsPerPage"
    :total="props.total"
    :sibling-count="props.siblingCount"
    show-edges
    :default-page="props.defaultPage"
    @update:page="emit('page-change', $event)"
  >
    <PaginationList v-slot="{ items }" class="flex items-center gap-1">
      <PaginationFirst />
      <PaginationPrev />

      <template v-for="(item, index) in items" :key="index">
        <PaginationListItem
          v-if="item.type === 'page'"
          :value="item.value"
          as-child
        >
          <Button
            class="w-10 h-10 p-0"
            :variant="item.value === page ? 'default' : 'outline'"
          >
            {{ item.value }}
          </Button>
        </PaginationListItem>

        <PaginationEllipsis
          v-else
          :index="index"
        />
      </template>

      <PaginationNext />
      <PaginationLast />
    </PaginationList>
  </Pagination>
</template>

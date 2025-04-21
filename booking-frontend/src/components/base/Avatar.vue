<!-- src/components/ui/Avatar.vue -->
<template>
  <div class="w-10 h-10 rounded-full overflow-hidden bg-gray-200 flex justify-center items-center">
    <img v-if="src" :src="src" :alt="alt" class="w-full h-full object-cover" />
    <span v-else class="text-gray-500">{{ fallback }}</span>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  src?: string
  alt?: string
  fallback?: string
}>()
</script>

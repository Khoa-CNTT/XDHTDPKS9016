<script setup lang="ts">
import SidebarMenu from '@/components/layout/sideBar/SidebarMenu.vue'
import Menu from '@/components/layout/sideBar/Menu.vue'
import Button from '../ui/button/Button.vue'
</script>
<template>
  <div class="w-18 py-3 flex flex-col w-[250px] min-w-[250px] border-r p-4 gap-2">
    <p class="font-semibold text-lg mt-5">
    
    </p>
    <!-- <SidebarMenu /> -->
     <Menu/>
    <Button
      class="mt-auto"
      @click="logout"
      >Logout</Button
    >
  </div>
</template>
<style>
.router-link-exact-active {
  background-color: #f7f7f7;
  color: #344054;
}
</style>

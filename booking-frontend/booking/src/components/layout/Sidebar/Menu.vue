<script setup lang="ts">
const menus = ref([
  {
    title: 'PERSONAL',
    children: [
      {
        class: 'i-solar-user-linear group-hover:i-solar-user-bold-duotone',
        title: 'Home',
        link: '/',
      },
      {
        class:
          'i-solar-users-group-rounded-line-duotone group-hover:i-solar-users-group-rounded-bold-duotone',
        title: 'Hotels',
        link: '/hotels',
      },
      {
        class: 'i-solar-bolt-broken group-hover:i-solar-bolt-bold-duotone',
        title: 'Rooms',
        link: '/rooms',
      },
      {
        class: 'i-solar-bolt-broken group-hover:i-solar-bolt-bold-duotone',
        title: 'About',
        link: '/about',
      },
      {
        class: 'i-solar-bolt-broken group-hover:i-solar-bolt-bold-duotone',
        title: 'Contact',
        link: '/contact',
      },
    ],
  },

])
</script>
<template>
  <div class="mt-5">
    <RouterLink
      to="/"
      class="flex items-center gap-2 group cursor-pointer hover:text-[#0061ff] hover:bg-[#eff7ff] py-3 px-2 rounded-xl transition-all duration-200"
    >
      <span class="group-hover:i-solar-graph-bold-duotone i-solar-graph-linear text-xl"></span>
      <span class="font-medium">Dashboard</span>
    </RouterLink>

    <template
      v-for="menu in menus"
      :key="menu.title"
    >
      <!-- <p class="mt-5 ml-5 text-xs">{{ menu.title }}</p> -->

      <div class="flex flex-col mt-2">
        <RouterLink
          v-for="sub in menu.children"
          :key="sub.title"
          :to="sub.link"
          class="flex items-center gap-2 mb-[2px] group cursor-pointer hover:text-[#0061ff] hover:bg-[#eff7ff] py-3 px-2 rounded-xl transition-all ease-in-out duration-500"
        >
          <span
            class="text-xl"
            :class="sub.class"
          ></span>
          <span class="font-medium">{{ sub.title }}</span>
        </RouterLink>
      </div>
    </template>
  </div>
</template>
<style scoped>
.router-link-active {
  background-color: #eff7ff;
  color: #0061ff;
}
</style>

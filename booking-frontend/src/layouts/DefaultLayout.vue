<script setup lang="ts">
// import SideBar from '@/components/layout/SideBar.vue'
import Header from '@/components/layout/Header.vue'
import Footer from '@/components/layout/Footer.vue'
import TopBar from '@/components/layout/TopBar.vue'
</script>
<template>
  <div class="container-div">
    <Header class="header" />
    <div class="content">
      <slot />
    </div>
    <Footer class="footer" />
  </div>
</template>

<style scoped>
.container-div {
  display: flex;
  flex-direction: column;
  min-height: 100vh; 
  width: 100%;
  overflow: hidden;
}

.header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
}

.content {
  flex: 1;
  margin-top: 80px; 
  padding: 0px;
  overflow-y: auto;
}

.footer {
  width: 100%;
  margin-top: auto; 
}
</style>

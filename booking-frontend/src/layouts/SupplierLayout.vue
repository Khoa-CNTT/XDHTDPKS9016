<template>
    <div class="wrapper">
        <nav class="main-header navbar navbar-expand navbar-white navbar-light">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link" data-widget="pushmenu" href="#"><i class="fas fa-bars"></i></a>
                </li>
            </ul>
        </nav>

        <aside class="main-sidebar bg-text elevation-4 flex-shrink-0 w-[20%] text-white font-bold">
            <RouterLink href="#" class="brand-link">
                <span class="brand-text text-[16px] font-bold uppercase flex items-center gap-2 text-lg text-white">
                    <Icon icon="si:home-line" class="text-white" width="30" height="30" />
                    Nhà cung cấp
                </span>
            </RouterLink>
            <div class="sidebar">
                <nav class="mt-2">
                    <ul class="nav nav-pills nav-sidebar flex-column space-y-1">
                        <li class="nav-item">
                            <router-link to="/supplier/order-room-management"
                                class="nav-link d-flex align-items-center gap-2 text-white font-bold"
                                active-class="bg-blue-500"
                                exact-active-class="bg-blue-600">
                                <Icon icon="material-symbols:hotel-class-rounded" width="24" height="24" />
                                <p class="mb-0">Quản lý đặt phòng</p>
                            </router-link>
                        </li>
                        <li class="nav-item">
                            <router-link to="/supplier/service-control"
                                class="nav-link d-flex align-items-center gap-2 text-white font-bold"
                                active-class="bg-blue-500"
                                exact-active-class="bg-blue-600">
                                <Icon icon="carbon:service-desk" width="24" height="24" />
                                <p class="mb-0">Quản lý dịch vụ</p>
                            </router-link>
                        </li>
                        <li class="nav-item">
                            <router-link to="/supplier/room-management"
                                class="nav-link d-flex align-items-center gap-2 text-white font-bold"
                                active-class="bg-blue-500"
                                exact-active-class="bg-blue-600">
                                <Icon icon="carbon:service-desk" width="24" height="24" />
                                <p class="mb-0">Quản lý phòng</p>
                            </router-link>
                        </li>
                        <li class="nav-item">
                            <router-link to="/supplier/review-manage"
                                class="nav-link d-flex align-items-center gap-2 text-white font-bold"
                                active-class="bg-blue-500"
                                exact-active-class="bg-blue-600">
                                <Icon icon="mdi:comment-multiple-outline" width="24" height="24" />
                                <p class="mb-0">Quản lý bình luận</p>
                            </router-link>
                        </li>
                        <li class="nav-item">
                            <router-link to="/supplier/profile-settings"
                                class="nav-link d-flex align-items-center gap-2 text-white font-bold"
                                active-class="bg-blue-500"
                                exact-active-class="bg-blue-600">
                                <Icon icon="mdi:account-circle-outline" width="24" height="24" />
                                <p class="mb-0">Quản lý tài khoản cá nhân</p>
                            </router-link>
                        </li>
                        <li class="nav-item">
                            <router-link to="/supplier/overview-report"
                                class="nav-link d-flex align-items-center gap-2 text-white font-bold"
                                active-class="bg-blue-500"
                                exact-active-class="bg-blue-600">
                                <Icon icon="mdi:chart-box-outline" width="24" height="24" />
                                <p class="mb-0">Báo cáo và thống kê</p>
                            </router-link>
                        </li>
                    </ul>
                </nav>
            </div>
        </aside>

        <div class="content-wrapper p-3">
            <router-view />
        </div>
    </div>
</template>

<script setup>
</script>

<style scoped>
@import url('https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6.5.1/css/all.min.css');
@import url('https://cdn.jsdelivr.net/npm/admin-lte@3.2/dist/css/adminlte.min.css');

.bg-blue-600 {
  background-color: #1D4ED8;
}

.bg-blue-500 {
  background-color: #3B82F6; 
}
</style>


<script>
import { Icon } from '@iconify/vue'
const loadScript = (src) => {
    const script = document.createElement('script')
    script.src = src
    script.async = true
    document.body.appendChild(script)
}

loadScript('https://cdn.jsdelivr.net/npm/jquery@3.6.4/dist/jquery.min.js')
loadScript('https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js')
loadScript('https://cdn.jsdelivr.net/npm/admin-lte@3.2/dist/js/adminlte.min.js')
</script>
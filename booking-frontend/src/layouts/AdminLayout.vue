<template>
  <div class="flex h-screen overflow-hidden">
    <!-- Sidebar -->
    <aside class="bg-gray-800 text-white w-1/5 flex flex-col">
      <RouterLink to="/" class="flex items-center gap-2 px-4 py-6 text-xl font-bold uppercase">
        <Icon icon="si:home-line" width="30" height="30" />
        Admin
      </RouterLink>

      <nav class="flex-1 px-2 space-y-1">
        <router-link to="/admin/manage-providers"
          class="flex items-center gap-2 px-4 py-3 rounded-lg transition-colors duration-300 hover:bg-blue-600"
          :class="$route.path === '/admin/manage-providers' ? 'bg-blue-500' : ''">
          <Icon icon="carbon:service-desk" width="24" height="24" />
          <span>Quản lý nhà cung cấp</span>
        </router-link>

        <router-link to="/admin/manage-user"
          class="flex items-center gap-2 px-4 py-3 rounded-lg transition-colors duration-300 hover:bg-blue-600"
          :class="$route.path === '/admin/manage-user' ? 'bg-blue-500' : ''">
          <Icon icon="material-symbols:hotel-class-rounded" width="24" height="24" />
          <span>Quản lý người dùng</span>
        </router-link>

        <router-link to="/admin/statistics"
          class="flex items-center gap-2 px-4 py-3 rounded-lg transition-colors duration-300 hover:bg-blue-600"
          :class="$route.path === '/admin/statistics' ? 'bg-blue-500' : ''">
          <Icon icon="mdi:chart-box-outline" width="24" height="24" />
          <span>Báo cáo và thống kê</span>
        </router-link>
      </nav>

      <div class="p-4 mt-auto">
        <button @click="handleLogout"
          class="w-full flex items-center justify-center gap-2 py-2 px-4 rounded-lg border border-white text-white hover:bg-white hover:text-black transition-all">
          <Icon icon="material-symbols:logout-rounded" width="24" height="24" />
          Đăng xuất
        </button>
      </div>
    </aside>

    <!-- Main Content -->
    <div class="flex-1 flex flex-col">
      <!-- Top Bar -->
      <nav class="bg-white px-4 py-2 flex items-center justify-between shadow">
        <!-- Nút menu -->
        <button class="text-gray-600 hover:text-black">
          <i class="fas fa-bars"></i>
        </button>

        <!-- Avatar và tên người dùng -->
        <div class="flex items-center gap-3">
          <img src="https://i.pravatar.cc/40" alt="avatar" class="w-8 h-8 rounded-full object-cover" />
          <span class="text-gray-800 font-semibold">{{ authStore.getUser?.username }}</span>
        </div>
      </nav>

      <!-- Router View -->
      <main class="flex-1 overflow-y-auto p-6 bg-gray-50">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { Icon } from '@iconify/vue'
import { useRouter } from 'vue-router'
import { logoutApi } from '@/services/auth'
import { toast } from 'vue3-toastify'
import { useAuthStore } from '@/stores/auth'
const authStore = useAuthStore()
const router = useRouter()


const handleLogout = async () => {
  try {
    const token = localStorage.getItem('access_token')
    if (!token) throw new Error('Không tìm thấy token trong localStorage')

    await logoutApi({ token })
    localStorage.removeItem('access_token')
    authStore.$reset()
    toast.success('Đăng xuất thành công!', { autoClose: 3000, position: 'top-right' })
    router.push('/login')
  } catch (error) {
    console.error('Lỗi khi logout:', error)
  }
}
</script>

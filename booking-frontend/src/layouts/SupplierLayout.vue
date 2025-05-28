<template>
    <div class="flex h-screen">
       
        <aside class="bg-gray-800 w-[20%] text-white font-bold flex flex-col">
            <nav class="flex-1 overflow-y-auto px-4 mt-4">
                <ul class="space-y-2 p-4">
                    <li>
                        <router-link to="/supplier/order-room-management"
                            class="flex items-center gap-3 px-4 py-2 rounded hover:bg-blue-500 transition"
                            active-class="bg-blue-500" exact-active-class="bg-blue-600">
                            <Icon icon="material-symbols:hotel" width="24" height="24" />
                            <span>Quản lý đặt phòng</span>
                        </router-link>
                    </li>
                    <li>
                        <router-link to="/supplier/service-control"
                            class="flex items-center gap-3 px-4 py-2 rounded hover:bg-blue-500 transition"
                            active-class="bg-blue-500" exact-active-class="bg-blue-600">
                            <Icon icon="mdi:room-service-outline" width="24" height="24" />
                            <span>Quản lý dịch vụ</span>
                        </router-link>
                    </li>
                    <li>
                        <router-link to="/supplier/room-types"
                            class="flex items-center gap-3 px-4 py-2 rounded hover:bg-blue-500 transition"
                            active-class="bg-blue-500" exact-active-class="bg-blue-600">
                            <Icon icon="mdi:bed-outline" width="24" height="24" />
                            <span>Quản lý loại phòng</span>
                        </router-link>
                    </li>
                    <li>
                        <router-link to="/supplier/room-manager"
                            class="flex items-center gap-3 px-4 py-2 rounded hover:bg-blue-500 transition"
                            active-class="bg-blue-500" exact-active-class="bg-blue-600">
                            <Icon icon="mdi:hotel" width="24" height="24" />
                            <span>Quản lý phòng</span>
                        </router-link>
                    </li>
                    <li>
                        <router-link to="/supplier/hotel-info"
                            class="flex items-center gap-3 px-4 py-2 rounded hover:bg-blue-500 transition"
                            active-class="bg-blue-500" exact-active-class="bg-blue-600">
                            <Icon icon="mdi:office-building-outline" width="24" height="24" />
                            <span>Quản lý thông tin khách sạn</span>
                        </router-link>
                    </li>
                    <li>
                        <router-link to="/supplier/billing"
                            class="flex items-center gap-3 px-4 py-2 rounded hover:bg-blue-500 transition"
                            active-class="bg-blue-500" exact-active-class="bg-blue-600">
                            <Icon icon="mdi:credit-card-outline" width="24" height="24" />
                            <span>Quản lý giao dịch thanh toán</span>
                        </router-link>
                    </li>
                    <li>
                        <router-link to="/supplier/profile-settings"
                            class="flex items-center gap-3 px-4 py-2 rounded hover:bg-blue-500 transition"
                            active-class="bg-blue-500" exact-active-class="bg-blue-600">
                            <Icon icon="mdi:account-cog-outline" width="24" height="24" />
                            <span>Quản lý tài khoản cá nhân</span>
                        </router-link>
                    </li>
                    <li>
                        <router-link to="/supplier/overview-report"
                            class="flex items-center gap-3 px-4 py-2 rounded hover:bg-blue-500 transition"
                            active-class="bg-blue-500" exact-active-class="bg-blue-600">
                            <Icon icon="mdi:chart-bar" width="24" height="24" />
                            <span>Báo cáo và thống kê</span>
                        </router-link>
                    </li>
                </ul>
            </nav>

            <div class="p-4 mt-auto">
                <button @click="handleLogout"
                    class="w-full flex items-center justify-center gap-2 border border-white text-white rounded-lg px-4 py-2 hover:bg-white hover:text-black transition duration-300">
                    <Icon icon="material-symbols:logout-rounded" width="24" height="24" />
                    Đăng xuất
                </button>
            </div>
        </aside>

       
        <div class="flex-1 flex flex-col">
        
            <nav class="bg-white px-4 py-2 flex items-center justify-between shadow">
             
                <button class="text-gray-600 hover:text-black">
                    <i class="fas fa-bars"></i>
                </button>

                
                <div class="flex items-center gap-3">
                    <img src="https://i.pravatar.cc/40" alt="avatar" class="w-8 h-8 rounded-full object-cover" />
                    <span class="text-gray-800 font-semibold">{{ authStore.getUser?.username }}</span>
                </div>
            </nav>

    
            <main class="overflow-y-auto">
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
        toast.success('Đăng xuất thành công!', {
            autoClose: 3000,
            position: 'top-right',
        })
        router.push('/login')
    } catch (error) {
        console.error('Lỗi khi logout:', error)
    }
}
</script>


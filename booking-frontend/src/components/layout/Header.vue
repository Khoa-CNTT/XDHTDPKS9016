<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { RouterLink } from 'vue-router'
import { useRouter } from 'vue-router'
import { logoutApi } from '@/services/auth'
import { toast } from 'vue3-toastify'
import { nextTick } from 'vue';
const authStore = useAuthStore()
const showDropdown = ref(false)

const toggleDropdown = () => {
    showDropdown.value = !showDropdown.value
}

// Đóng dropdown khi click bên ngoài
const handleClickOutside = (e: Event) => {
    const dropdown = document.querySelector('.relative')
    if (dropdown && !dropdown.contains(e.target as Node)) {
        showDropdown.value = false
    }
}

const router = useRouter()

const handleLogout = async () => {
    try {
        const token = localStorage.getItem('access_token')
        console.log('Token lấy ra từ localStorage---------------------:', token)
        if (!token) {
            throw new Error('Không tìm thấy token trong localStorage')
        }

        await logoutApi({ token: token })

        // Xóa token sau khi logout thành công
        localStorage.removeItem('access_token')
        toast.success('Đăng xuất thành công!', {
            autoClose: 3000,
            position: 'top-right',
        })
        console.log('Đang chuyển hướng tới /login')
        router.push('/login')

        window.location.reload()
        console.log('Chuyển hướng thành công')
    } catch (error) {
        console.error('Lỗi khi logout:', error)

    }
}
onMounted(() => {
    document.addEventListener('click', handleClickOutside)
})
</script>

<template>
    <div>
        <header class="bg-white shadow-md py-5 px-6 mx-auto flex justify-between">
            <div class="container mx-auto flex justify-between items-center">
                <div class="text-2xl font-bold text-text">Elite<span class="text-gray-400">Booking</span></div>
                <nav class="flex-1 flex justify-center space-x-6 mb-100">
                    <RouterLink to="/" class="text-gray-600 hover:text-blue-600"
                        active-class="text-blue-700 font-semibold" exact-active-class="text-blue-700 font-semibold">
                        Trang chủ
                    </RouterLink>
                    <RouterLink to="/hotels" class="text-gray-600 hover:text-blue-600"
                        active-class="text-blue-700 font-semibold" exact-active-class="text-blue-700 font-semibold">
                        Khách sạn
                    </RouterLink>
                    <RouterLink to="/about" class="text-gray-600 hover:text-blue-600"
                        active-class="text-blue-700 font-semibold" exact-active-class="text-blue-700 font-semibold">
                        Giới thiệu
                    </RouterLink>
                    <RouterLink to="/contact" class="text-gray-600 hover:text-blue-600"
                        active-class="text-blue-700 font-semibold" exact-active-class="text-blue-700 font-semibold">
                        Liên hệ
                    </RouterLink>
                </nav>

                <!-- Avatar và các chức năng khi click vào avatar -->
                <div v-if="authStore.getIsLoggedIn" class="relative" @click="toggleDropdown">
                    <div class="flex items-center space-x-2 cursor-pointer">
                        <img :src="authStore.getUser?.avatar || 'https://i.pravatar.cc/300'" alt="Avatar"
                            class="w-8 h-8 rounded-full border" />
                        <span class="text-gray-700">{{ authStore.getUser?.username }}</span>
                    </div>

                    <!-- Dropdown -->
                    <div v-if="showDropdown"
                        class="absolute right-0 mt-2 w-60 bg-white border border-gray-200 rounded-lg shadow-lg z-50">
                        <ul class="text-sm text-gray-700">
                            <li>
                                <RouterLink to="/profiles" class="block px-4 py-2 hover:bg-gray-100 rounded-t-lg">👤
                                    Quản
                                    lý tài khoản</RouterLink>
                            </li>
                            <li>
                                <RouterLink to="/payment-history" class="block px-4 py-2 hover:bg-gray-100">💳 Lịch sử
                                    thanh toán</RouterLink>
                            </li>
                            <li>
                                <RouterLink to="/change-password"
                                    class="block px-4 py-2 hover:bg-gray-100 rounded-b-lg">🔒 Đổi mật khẩu</RouterLink>
                            </li>
                            <li>
                                <button @click="handleLogout"
                                    class="block w-full text-left px-4 py-2 hover:bg-gray-100 rounded-b-lg">
                                    🔒 Đăng xuất
                                </button>
                            </li>
                        </ul>
                    </div>
                </div>

                <!-- Hiển thị nút login nếu chưa đăng nhập -->
                <div v-else>
                    <RouterLink to="/login">
                        <button class="bg-text text-white px-4 py-2 rounded-lg hover:bg-blue-700">Login</button>
                    </RouterLink>
                </div>
            </div>
        </header>
    </div>
</template>

<style scoped>
/* Style cho dropdown */
.relative {
    position: relative;
}

.dropdown {
    position: absolute;
    top: 100%;
    right: 0;
    z-index: 1000;
}
</style>

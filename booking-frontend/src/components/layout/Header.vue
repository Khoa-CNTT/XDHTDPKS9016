<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { RouterLink } from 'vue-router'
import { useRouter } from 'vue-router'
import { logoutApi } from '@/services/auth'
import { toast } from 'vue3-toastify'
import { nextTick } from 'vue'

const authStore = useAuthStore()
const showDropdown = ref(false)

const toggleDropdown = () => {
  showDropdown.value = !showDropdown.value
}

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
    if (!token) {
      throw new Error('Không tìm thấy token trong localStorage')
    }

    await logoutApi({ token: token })

    localStorage.removeItem('access_token')
    toast.success('Đăng xuất thành công!', {
      autoClose: 3000,
      position: 'top-right',
    })
    router.push('/login')
    window.location.reload()
  } catch (error) {
    void error
  }
}
onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})
</script>

<template>
  <div>
    <header class="bg-white shadow-md py-5 px-6 mx-auto flex justify-between font-sans">
      <div class="container mx-auto flex justify-between items-center">
        <div class="text-3xl font-bold text-text font-sans">
          Elite<span class="text-gray-400">Booking</span>
        </div>
        <nav class="flex-1 flex justify-center space-x-8 mb-100">
          <RouterLink to="/" custom v-slot="{ isExactActive, navigate }">
            <span @click="navigate" :class="[
              'cursor-pointer hover:text-blue-600 text-xl',
              isExactActive ? 'text-blue-700 font-semibold' : 'text-gray-600',
            ]">
              Trang chủ
            </span>
          </RouterLink>

          <RouterLink to="/hotels" custom v-slot="{ isExactActive, navigate }">
            <span @click="navigate" :class="[
              'cursor-pointer hover:text-blue-600 text-xl',
              isExactActive ? 'text-blue-700 font-semibold' : 'text-gray-600',
            ]">
              Khách sạn
            </span>
          </RouterLink>

          <RouterLink to="/about" custom v-slot="{ isExactActive, navigate }">
            <span @click="navigate" :class="[
              'cursor-pointer hover:text-blue-600 text-xl',
              isExactActive ? 'text-blue-700 font-semibold' : 'text-gray-600',
            ]">
              Giới thiệu
            </span>
          </RouterLink>

          <RouterLink to="/contact" custom v-slot="{ isExactActive, navigate }">
            <span @click="navigate" :class="[
              'cursor-pointer hover:text-blue-600 text-xl',
              isExactActive ? 'text-blue-700 font-semibold' : 'text-gray-600',
            ]">
              Liên hệ
            </span>
          </RouterLink>
        </nav>

  
        <div v-if="authStore.getIsLoggedIn" class="relative" @click="toggleDropdown">

          <div class="flex items-center space-x-3 cursor-pointer">
            <img :src="authStore.getUser?.avatar || '/assets/images/avatar.jpg'" alt="Avatar"
              class="w-10 h-10 rounded-full border" />

            <span class="text-gray-700 text-lg">{{ authStore.getUser?.username }}</span>
          </div>
          <!-- Dropdown -->
          <div v-if="showDropdown"
            class="absolute right-0 mt-2 w-60 bg-white border border-gray-200 rounded-lg shadow-lg z-50">
            <ul class="text-base text-gray-700">
              <li>
                <RouterLink to="/profiles" class="block px-4 py-2 hover:bg-gray-100 rounded-t-lg">👤 Quản lý tài khoản
                </RouterLink>
              </li>
              <li>
                <RouterLink to="/payment-history" class="block px-4 py-2 hover:bg-gray-100">💳 Lịch sử thanh toán
                </RouterLink>
              </li>
              <li>
                <RouterLink to="/change-password" class="block px-4 py-2 hover:bg-gray-100 rounded-b-lg">🔒 Đổi mật khẩu
                </RouterLink>
              </li>
              <li>
                <button @click="handleLogout" class="block w-full text-left px-4 py-2 hover:bg-gray-100 rounded-b-lg">
                  🔒 Đăng xuất
                </button>
              </li>
            </ul>
          </div>
        </div>

        <!-- Hiển thị nút login nếu chưa đăng nhập -->
        <div v-else>
          <RouterLink to="/login">
            <button class="bg-text text-white px-6 py-3 text-lg rounded-lg hover:bg-blue-700">
              Login
            </button>
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


body {
  font-family: 'Poppins', sans-serif;
}


header,
.dropdown ul {
  font-family: 'Poppins', sans-serif;
}


.text-xl {
  font-size: 1.25rem;

}

.text-3xl {
  font-size: 1.875rem;

}

.text-lg {
  font-size: 1.125rem;

}
</style>

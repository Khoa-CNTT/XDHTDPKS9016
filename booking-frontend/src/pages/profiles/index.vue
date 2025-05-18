<template>
  <div class="max-w-2xl mx-auto mt-10 p-8 bg-white rounded-xl shadow-md">
    <h2 class="text-2xl font-bold text-center text-gray-800 mb-8">Cập nhật tài khoản cá nhân</h2>
    <form
      @submit.prevent="handleSubmit"
      class="space-y-6"
    >
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Họ và tên</label>
          <input
            v-model="form.full_name"
            type="text"
            placeholder="Nhập họ và tên"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Giới tính</label>
          <select
            v-model="form.gender"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="nam">Nam</option>
            <option value="nu">Nữ</option>
          </select>
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Địa chỉ</label>
          <input
            v-model="form.address"
            type="text"
            placeholder="Nhập địa chỉ"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Email</label>
          <input
            v-model="form.email"
            type="email"
            readonly
            class="w-full px-4 py-2 bg-gray-100 text-gray-700 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400 cursor-not-allowed"
          />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Số điện thoại</label>
          <input
            v-model="form.phone"
            type="text"
            placeholder="Nhập số điện thoại"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Ngày sinh</label>
          <input
            v-model="form.birth_date"
            type="date"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Trạng thái</label>
          <input
            v-model="form.status"
            type="text"
            placeholder="Nhập trạng thái"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <div>
          <label class="block text-sm font-semibold text-gray-700 mb-2">Tên đăng nhập</label>
          <input
            v-model="form.username"
            type="text"
            readonly
            class="w-full px-4 py-2 bg-gray-100 text-gray-700 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400 cursor-not-allowed"
          />
        </div>
      </div>

      <button
        type="submit"
        class="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 px-4 rounded-lg transition duration-300"
      >
        Cập nhật
      </button>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import type { UserInfo } from '@/types/user'
import { getInfoApi, updateUserApi } from '@/services/user'

const form = ref<UserInfo>({
  user_id: 0,
  full_name: '',
  gender: '',
  address: '',
  email: '',
  phone: '',
  birth_date: '',
  status: '',
  username: '',
})

// Chuyển từ dd-MM-yyyy sang yyyy-MM-dd (để hiển thị input type=date)
function convertDateToISO(dateStr: string): string {
  if (!dateStr) return ''
  const parts = dateStr.split('-')
  if (parts.length !== 3) return ''
  const [day, month, year] = parts
  return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`
}

// Chuyển ngược lại yyyy-MM-dd sang dd-MM-yyyy (để gửi API)
function convertDateToAPIFormat(dateStr: string): string {
  if (!dateStr) return ''
  const parts = dateStr.split('-')
  if (parts.length !== 3) return ''
  const [year, month, day] = parts
  return `${day}-${month}-${year}`
}

onMounted(async () => {
  const res = await getInfoApi()
  res.birth_date = convertDateToISO(res.birth_date ?? '')
  form.value = res
})

const handleSubmit = async () => {
  const payload = { ...form.value }
  payload.birth_date = convertDateToAPIFormat(payload.birth_date ?? '')
  await updateUserApi(payload.user_id, payload)
  alert('Cập nhật thành công!')
}
</script>

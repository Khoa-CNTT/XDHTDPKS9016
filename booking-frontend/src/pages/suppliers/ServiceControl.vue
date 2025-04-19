<template>
  <div class="card">
    <div class="card-header flex justify-between items-center">
      <button class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition"
        @click="openAddPopup">
        + Thêm dịch vụ mới
      </button>
      <div v-if="isAddMode"
        class="fixed top-0 left-0 w-full h-full bg-gray-400 bg-opacity-40 flex items-center justify-center z-50">
        <div class="bg-white rounded-xl shadow-xl w-full max-w-2xl p-8 relative">
          <h3 class="text-2xl font-bold text-center text-gray-800 mb-8">➕ Thêm dịch vụ mới</h3>
          <form @submit.prevent="submitAdd" class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label class="block text-sm font-semibold text-gray-700 mb-1">Tên dịch vụ</label>
              <input v-model="newService.serviceName" type="text"
                class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-indigo-500" required />
            </div>
            <div>
              <label class="block text-sm font-semibold text-gray-700 mb-1">Giá</label>
              <input v-model="newService.price" type="text"
                class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-indigo-500" required />
            </div>
            <div>
              <label class="block text-sm font-semibold text-gray-700 mb-1">Mô tả ngắn</label>
              <input v-model="newService.shortDescription" type="text"
                class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-indigo-500" required />
            </div>
            <div>
              <label class="block text-sm font-semibold text-gray-700 mb-1">Mô tả chi tiết</label>
              <input v-model="newService.description" type="text"
                class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-indigo-500" required />
            </div>
            <div class="md:col-span-2">
              <label class="block text-sm font-semibold text-gray-700 mb-1">Thời gian sử dụng</label>
              <input v-model="newService.usageTime" type="text"
                class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-indigo-500" required />
            </div>

            <div class="md:col-span-2 flex justify-end gap-4 mt-4">
              <button type="button" @click="cancelAdd"
                class="px-6 py-2 bg-gray-300 text-gray-700 rounded-md hover:bg-gray-400 transition">
                Hủy
              </button>
              <button type="submit" class="px-6 py-2 bg-green-500 text-white rounded-md hover:bg-green-600 transition">
                Thêm
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>


        <div class="card-body">
          <CustomTable :headers="tableHeaders" :rows="serviceData">
            <template #actions="{ row, index }">
              <div class="flex gap-2 justify-end">
                <button class="px-3 py-1 bg-green-500 hover:bg-green-600 text-white text-sm rounded-md transition"
                  @click="editService(row)">
                  Sửa
                </button>
                <button class="px-3 py-1 bg-red-500 hover:bg-red-600 text-white text-sm rounded-md transition"
                  @click="deleteService(index)">
                  Xóa
                </button>
              </div>
            </template>
          </CustomTable>
        </div>

        <!-- Popup chỉnh sửa -->
        <div v-if="isEditMode"
          class="fixed top-0 left-0 w-full h-full bg- bg-opacity-40 flex items-center justify-center z-50">
          <div class="bg-white rounded-xl shadow-xl w-full max-w-2xl p-8 relative">
            <h3 class="text-2xl font-bold text-center text-gray-800 mb-8">
              ✏️ Chỉnh sửa dịch vụ
            </h3>
            <form @submit.prevent="submitEdit" class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <!-- Tên dịch vụ -->
              <div>
                <label class="block text-sm font-semibold text-gray-700 mb-1">Tên dịch vụ</label>
                <input v-model="formData.serviceName" type="text"
                  class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-indigo-500" required />
              </div>

              <!-- Giá -->
              <div>
                <label class="block text-sm font-semibold text-gray-700 mb-1">Giá</label>
                <input v-model="formData.price" type="text"
                  class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-indigo-500" required />
              </div>

              <!-- Mô tả ngắn -->
              <div>
                <label class="block text-sm font-semibold text-gray-700 mb-1">Mô tả ngắn</label>
                <input v-model="formData.shortDescription" type="text"
                  class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-indigo-500" required />
              </div>

              <!-- Mô tả chi tiết -->
              <div>
                <label class="block text-sm font-semibold text-gray-700 mb-1">Mô tả chi tiết</label>
                <input v-model="formData.description" type="text"
                  class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-indigo-500" required />
              </div>

              <!-- Thời gian sử dụng -->
              <div class="md:col-span-2">
                <label class="block text-sm font-semibold text-gray-700 mb-1">Thời gian sử dụng</label>
                <input v-model="formData.usageTime" type="text"
                  class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-indigo-500" required />
              </div>

              <!-- Nút hành động -->
              <div class="md:col-span-2 flex justify-end gap-4 mt-4">
                <button type="button" @click="cancelEdit"
                  class="px-6 py-2 bg-gray-300 text-gray-700 rounded-md hover:bg-gray-400 transition">
                  Hủy
                </button>
                <button type="submit" class="px-6 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition">
                  Cập nhật
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
</template>
<script setup lang="ts">
import { ref } from 'vue'
import CustomTable from '@/components/base/CustomTable.vue'

const tableHeaders = ['Tên dịch vụ', 'Giá', 'Mô tả ngắn', 'Mô tả chi tiết', 'Thời gian sử dụng', 'Hành động']
const serviceData = ref([
  {
    serviceName: 'Đưa đón sân bay',
    price: '200k',
    shortDescription: 'Xe 4 chỗ',
    description: 'Đưa đón sân bay với xe 4 chỗ',
    usageTime: '30 phút',
  },
  {
    serviceName: 'Ăn sáng buffet',
    price: '150k',
    shortDescription: 'Miễn phí cho 2 người',
    description: 'Buffet sáng phong phú',
    usageTime: '60 phút',
  },
])

const isEditMode = ref(false)
const formData = ref({
  serviceName: '',
  price: '',
  shortDescription: '',
  description: '',
  usageTime: '',
})
const editIndex = ref<number | null>(null)

const editService = (row: any) => {
  isEditMode.value = true
  formData.value = { ...row }
  editIndex.value = serviceData.value.indexOf(row)
}

const deleteService = (index: number) => {
  serviceData.value.splice(index, 1)
}

const submitEdit = () => {
  if (editIndex.value !== null) {
    serviceData.value[editIndex.value] = { ...formData.value }
  }
  cancelEdit()
}

const cancelEdit = () => {
  isEditMode.value = false
  formData.value = {
    serviceName: '',
    price: '',
    shortDescription: '',
    description: '',
    usageTime: '',
  }
  editIndex.value = null
}
const isAddMode = ref(false)

const newService = ref({
  serviceName: '',
  price: '',
  shortDescription: '',
  description: '',
  usageTime: '',
})
const openAddPopup = () => {
  isAddMode.value = true
  // reset dữ liệu khi mở form
  newService.value = {
    serviceName: '',
    price: '',
    shortDescription: '',
    description: '',
    usageTime: '',
  }
}

const cancelAdd = () => {
  isAddMode.value = false
}

const submitAdd = () => {
  serviceData.value.push({ ...newService.value })
  isAddMode.value = false
}
</script>
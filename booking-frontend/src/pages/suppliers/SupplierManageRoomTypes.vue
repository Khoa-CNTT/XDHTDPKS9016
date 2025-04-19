<template>
    <div class="card">
        <!-- Header: nút Thêm loại phòng mới -->
        <div class="card-header flex justify-between items-center">
            <button class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition"
                @click="openAddPopup">
                + Thêm loại phòng mới
            </button>

            <!-- Popup thêm mới -->
            <div v-if="isAddMode"
                class="fixed top-0 left-0 w-full h-full bg-gray-400 bg-opacity-40 flex items-center justify-center z-50">
                <div class="bg-white rounded-xl shadow-xl w-full max-w-2xl p-8 relative">
                    <h3 class="text-2xl font-bold text-center text-gray-800 mb-8">
                        ➕ Thêm loại phòng mới
                    </h3>
                    <form @submit.prevent="submitAdd" class="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <!-- Tên loại phòng -->
                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-1">Tên loại phòng</label>
                            <input v-model="newRoom.roomName" type="text"
                                class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-indigo-500"
                                required />
                        </div>
                        <!-- Giá -->
                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-1">Giá</label>
                            <input v-model="newRoom.price" type="text"
                                class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-indigo-500"
                                required />
                        </div>
                        <!-- Tiện ích -->
                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-1">Tiện ích</label>
                            <input v-model="newRoom.amenities" type="text"
                                class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-indigo-500"
                                required />
                        </div>
                        <!-- Số lượng -->
                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-1">Số lượng</label>
                            <input v-model.number="newRoom.quantity" type="number"
                                class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-indigo-500"
                                required />
                        </div>
                        <!-- Buttons -->
                        <div class="md:col-span-2 flex justify-end gap-4 mt-4">
                            <button type="button" @click="cancelAdd"
                                class="px-6 py-2 bg-gray-300 text-gray-700 rounded-md hover:bg-gray-400 transition">
                                Hủy
                            </button>
                            <button type="submit"
                                class="px-6 py-2 bg-green-500 text-white rounded-md hover:bg-green-600 transition">
                                Thêm
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Body: bảng CustomTable -->
        <div class="card-body">
            <CustomTable :headers="tableHeaders" :rows="roomData">
                <template #actions="{ row, index }">
                    <div class="flex gap-2 justify-end">
                        <button class="px-3 py-1 bg-blue-500 hover:bg-blue-600 text-white text-sm rounded-md transition"
                            @click="editRoom(row)">
                            Sửa
                        </button>
                        <button class="px-3 py-1 bg-red-500 hover:bg-red-600 text-white text-sm rounded-md transition"
                            @click="deleteRoom(index)">
                            Xóa
                        </button>
                    </div>
                </template>
            </CustomTable>
        </div>

        <!-- Popup chỉnh sửa -->
        <div v-if="isEditMode"
            class="fixed top-0 left-0 w-full h-full bg-gray-400 bg-opacity-40 flex items-center justify-center z-50">
            <div class="bg-white rounded-xl shadow-xl w-full max-w-2xl p-8 relative">
                <h3 class="text-2xl font-bold text-center text-gray-800 mb-8">
                    ✏️ Chỉnh sửa loại phòng
                </h3>
                <form @submit.prevent="submitEdit" class="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <!-- Tên loại phòng -->
                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-1">Tên loại phòng</label>
                        <input v-model="formData.roomName" type="text"
                            class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-indigo-500" required />
                    </div>
                    <!-- Giá -->
                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-1">Giá</label>
                        <input v-model="formData.price" type="text"
                            class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-indigo-500" required />
                    </div>
                    <!-- Tiện ích -->
                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-1">Tiện ích</label>
                        <input v-model="formData.amenities" type="text"
                            class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-indigo-500" required />
                    </div>
                    <!-- Số lượng -->
                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-1">Số lượng</label>
                        <input v-model.number="formData.quantity" type="number"
                            class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-indigo-500" required />
                    </div>
                    <!-- Buttons -->
                    <div class="md:col-span-2 flex justify-end gap-4 mt-4">
                        <button type="button" @click="cancelEdit"
                            class="px-6 py-2 bg-gray-300 text-gray-700 rounded-md hover:bg-gray-400 transition">
                            Hủy
                        </button>
                        <button type="submit"
                            class="px-6 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition">
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

const tableHeaders = [
    'STT',
    'Tên loại phòng',
    'Giá',
    'Tiện ích',
    'Số lượng',
    'Hành động'
]

const roomData = ref<Array<Record<string, any>>>([
    { stt: 1, roomName: 'Phòng đơn', price: '500000', amenities: 'Máy lạnh, TV', quantity: 10 },
    { stt: 2, roomName: 'Phòng đôi', price: '700000', amenities: 'Máy lạnh, TV, Tủ lạnh', quantity: 5 }
])

const isAddMode = ref(false)
const isEditMode = ref(false)
const newRoom = ref({ roomName: '', price: '', amenities: '', quantity: 1 })
const formData = ref({ id: null, roomName: '', price: '', amenities: '', quantity: 1 })

// Hàm mở popup thêm
const openAddPopup = () => { isAddMode.value = true }
const cancelAdd = () => { isAddMode.value = false }
// Thêm loại phòng mới
const submitAdd = () => {
    roomData.value.push({
        stt: roomData.value.length + 1,
        roomName: newRoom.value.roomName,
        price: newRoom.value.price,
        amenities: newRoom.value.amenities,
        quantity: newRoom.value.quantity
    })
    newRoom.value = { roomName: '', price: '', amenities: '', quantity: 1 }
    isAddMode.value = false
}

// Sửa loại phòng
const editRoom = (row: any) => {
    formData.value = { ...row }
    isEditMode.value = true
}
const cancelEdit = () => { isEditMode.value = false }
const submitEdit = () => {
    const idx = roomData.value.findIndex(r => r.stt === formData.value.stt)
    if (idx > -1) {
        roomData.value.splice(idx, 1, { ...formData.value })
    }
    isEditMode.value = false
}

// Xóa loại phòng và cập nhật lại STT
const deleteRoom = (index: number) => {
    roomData.value.splice(index, 1)
    roomData.value.forEach((r, i) => { r.stt = i + 1 })
}
</script>

<style scoped>
/* Thêm style tùy chỉnh nếu cần */
</style>
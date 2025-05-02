<template>
    <div class="card">
        <div class="card-header flex justify-between items-center">
            <button @click="showModal = true"
                class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition">
                + Thêm loại phòng mới
            </button>
        </div>

        <div class="card-body">
            <table class="min-w-full table-auto border-collapse">
                <thead>
                    <tr>
                        <th class="border px-4 py-2">STT</th>
                        <th class="border px-4 py-2">Tên loại phòng</th>
                        <th class="border px-4 py-2">Số lượng giường</th>
                        <th class="border px-4 py-2">Số lượng người</th>
                        <th class="border px-4 py-2">Giá</th>
                        <th class="border px-4 py-2">Mô tả</th>
                        <th class="border px-4 py-2">Hình ảnh</th>
                        <th class="border px-4 py-2">Số lượng</th>
                        <th class="border px-4 py-2">Trạng thái</th>
                        <th class="border px-4 py-2">Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(row, index) in roomData" :key="index">
                        <td class="border px-4 py-2">{{ row['STT'] }}</td>
                        <td class="border px-4 py-2">{{ row['Tên loại phòng'] }}</td>
                        <td class="border px-4 py-2">{{ row['Số lượng giường'] }}</td>
                        <td class="border px-4 py-2">{{ row['Số lượng người'] }}</td>
                        <td class="border px-4 py-2">{{ row['Giá'] }}</td>
                        <td class="border px-4 py-2">{{ row['Mô tả'] }}</td>
                        <td class="border px-4 py-2">
                            <img :src="row['Hình ảnh']" alt="room" class="w-16 h-16 object-cover" />
                        </td>
                        <td class="border px-4 py-2">{{ row['Số lượng'] }}</td>
                        <td class="border px-4 py-2">{{ row['Trạng thái'] }}</td>
                        <td class="border px-4 py-2">
                            <div class="flex gap-2 justify-end items-center">
                                <button class="px-2 py-1 bg-blue-500 text-white text-xs rounded-md">Sửa</button>
                                <button @click="askDelete(row['room_type_id'])"
                                    class="px-2 py-1 bg-red-500 text-white text-xs rounded-md">
                                    Xóa
                                </button>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

        <AddRoomTypeModal :isOpen="showModal" @close="showModal = false" @added="handleRoomTypeAdded" />
        <ConfirmDeleteModal v-if="showDeleteConfirm" @cancel="cancelDelete" @confirm="confirmDelete" />
    </div>
</template>

<script setup lang="ts">
import { onMounted, ref, computed } from 'vue'
import { getRoomTypesApi, deleteRoomTypeApi } from '@/services/supplier'
import AddRoomTypeModal from '@/pages/suppliers/AddRoomTypeModal.vue'
import ConfirmDeleteModal from './ConfirmDeleteModal.vue'
import type { RoomType } from '@/types/supplier'
import { toast } from 'vue3-toastify'

const roomTypes = ref<RoomType[]>([])
const isLoading = ref(false)
const error = ref<string | null>(null)
const showModal = ref(false)
const showDeleteConfirm = ref(false)
const selectedRoomTypeId = ref<number | null>(null)

const fetchRoomTypes = async () => {
    isLoading.value = true
    try {
        const response = await getRoomTypesApi()
        roomTypes.value = response.content
        console.log( roomTypes.value);
        
    } catch (err) {
        error.value = 'Không thể tải dữ liệu loại phòng'
        console.error(err)
    } finally {
        isLoading.value = false
    }
}

onMounted(() => {
    fetchRoomTypes()
})

const handleRoomTypeAdded = () => {
    showModal.value = false
    fetchRoomTypes()
}

const roomData = computed(() => {
    return roomTypes.value.map((room, index) => ({
        'STT': index + 1,
        'Tên loại phòng': room.type_name,
        'Số lượng giường': room.number_bed,
        'Số lượng người': room.maximum_people,
        'Giá': `${room.price.toLocaleString()} VNĐ`,
        'Mô tả': room.description,
        'Hình ảnh': room.room_image,
        'Số lượng': room.available_room,
        'Trạng thái': room.status,
        'Hành động': '',
        'room_type_id': room.room_type_id,
    }))
})

const askDelete = (id: number) => {
    selectedRoomTypeId.value = id
    showDeleteConfirm.value = true
}

const confirmDelete = async () => {
    if (selectedRoomTypeId.value !== null) {
        try {
            await deleteRoomTypeApi(selectedRoomTypeId.value)
            toast.error('Xóa loại phòng thành công!', {
                autoClose: 10000,
                position: 'top-right',
            });
            await fetchRoomTypes()
        } catch (err) {
            console.error('Xoá thất bại:', err)
            toast.error('Xóa loại phòng thất bại!', {
                autoClose: 10000,
                position: 'top-right',
            });
        } finally {
            showDeleteConfirm.value = false
            selectedRoomTypeId.value = null
        }
    }
}

const cancelDelete = () => {
    showDeleteConfirm.value = false
    selectedRoomTypeId.value = null
}
</script>
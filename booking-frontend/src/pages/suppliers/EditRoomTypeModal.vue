<template>
    <div v-if="isOpen" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white w-full max-w-xl rounded-lg p-6 relative">
            <h2 class="text-xl font-semibold mb-4">Chỉnh sửa loại phòng</h2>

            <form @submit.prevent="handleUpdate">
                <div class="mb-4">
                    <label class="block font-medium">Tên loại phòng</label>
                    <input v-model="form.type_name" type="text" class="border w-full px-3 py-2 rounded" required />
                </div>

                <div class="mb-4">
                    <label class="block font-medium">Số giường</label>
                    <input v-model.number="form.number_bed" type="number" class="border w-full px-3 py-2 rounded"
                        required />
                </div>

                <div class="mb-4">
                    <label class="block font-medium">Số người tối đa</label>
                    <input v-model.number="form.maximum_people" type="number" class="border w-full px-3 py-2 rounded"
                        required />
                </div>

                <div class="mb-4">
                    <label class="block font-medium">Giá</label>
                    <input v-model.number="form.price" type="number" class="border w-full px-3 py-2 rounded" required />
                </div>

                <div class="mb-4">
                    <label class="block font-medium">Mô tả</label>
                    <textarea v-model="form.description" class="border w-full px-3 py-2 rounded" />
                </div>

                <div class="mb-4">
                    <label class="block font-medium">Hình ảnh (URL)</label>
                    <input v-model="form.room_image" type="text" class="border w-full px-3 py-2 rounded" />
                </div>

                <div class="mb-4">
                    <label class="block font-medium">Số phòng có sẵn</label>
                    <input v-model.number="form.available_room" type="number" class="border w-full px-3 py-2 rounded"
                        required />
                </div>

                <div class="mb-4">
                    <label class="block font-medium">Trạng thái</label>
                    <select v-model="form.status" class="border w-full px-3 py-2 rounded">
                        <option value="ACTIVE">ACTIVE</option>
                        <option value="INACTIVE">INACTIVE</option>
                    </select>
                </div>

                <div class="flex justify-end gap-3">
                    <button type="button" @click="emit('onClose')" class="px-4 py-2 border rounded">Hủy</button>
                    <button type="submit" class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">Cập
                        nhật</button>
                </div>
            </form>
        </div>
    </div>
</template>

<script setup lang="ts">
import { watch, reactive } from 'vue'
import { updateRoomTypeApi } from '@/services/supplier'
import { toast } from 'vue3-toastify'
import type { RoomType, AddRoomType } from '@/types/supplier'

const props = defineProps<{
    isOpen: boolean
    roomType: RoomType | null
    onClose?: () => void
    onUpdated?: () => void
}>()

const form = reactive<AddRoomType>({
    type_name: '',
    number_bed: 1,
    maximum_people: 1,
    price: 0,
    description: '',
    room_image: '',
    available_room: 1,
    status: 'ACTIVE',
})
watch(
    () => props.roomType,
    (newValue) => {
        if (newValue) {
            Object.assign(form, {
                ...newValue,
                status: newValue.status?.toUpperCase() === 'ACTIVE' ? 'ACTIVE' : 'INACTIVE'
            })
        }
    },
    { immediate: true }
)

const emit = defineEmits(['onClose', 'onUpdated']);

const handleUpdate = async () => {
    if (!props.roomType?.room_type_id) return

    try {
        await updateRoomTypeApi(props.roomType.room_type_id, form)
        toast.success('Cập nhật loại phòng thành công!', {
            autoClose: 10000,
            position: 'top-right',
        });
        emit('onUpdated');
        emit('onClose');
    } catch (error) {
        console.error(error)
        toast.error('Cập nhật loại phòng thất bại!', {
            autoClose: 10000,
            position: 'top-right',
        });
    }
}
</script>

<style scoped>
/* Tuỳ chọn thêm animation hoặc style */
</style>
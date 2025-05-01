<template>
    <div class="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50" @click="closePopup">
        <div class="bg-white p-6 rounded-lg w-full max-w-md shadow-lg" @click.stop>
            <h2 class="text-xl font-semibold mb-4 text-center">Sửa thông tin nhà cung cấp</h2>
            <form @submit.prevent="submitEdit" class="space-y-4">
                <div>
                    <label for="name" class="block text-sm font-medium mb-1">Tên nhà cung cấp:</label>
                    <input v-model="supplier.name" id="name" type="text"
                        class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500" />
                </div>

                <div>
                    <label for="address" class="block text-sm font-medium mb-1">Địa chỉ:</label>
                    <input v-model="supplier.address" id="address" type="text"
                        class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500" />
                </div>

                <div>
                    <label for="hotline" class="block text-sm font-medium mb-1">Số điện thoại:</label>
                    <input v-model="supplier.hotline" id="hotline" type="text"
                        class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500" />
                </div>

                <div>
                    <label for="description" class="block text-sm font-medium mb-1">Mô tả:</label>
                    <textarea v-model="supplier.description" id="description" rows="3"
                        class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"></textarea>
                </div>

                <div class="flex justify-end gap-3">
                    <button type="button" class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
                        @click="closePopup">
                        Hủy
                    </button>
                    <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
                        Cập nhật
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, defineProps, defineEmits } from 'vue'
import { Hotel } from '@/types/admin'
import { updateSupplierApi } from '@/services/admin'

const props = defineProps({
    supplier: Object as () => Hotel,
})

const emit = defineEmits(['close', 'updated'])
const supplier = ref({ ...props.supplier })

const closePopup = () => {
    emit('close')
}

const submitEdit = async () => {
    try {
        const hotel_id = supplier.value.hotel_id;
        const name = supplier.value.name ?? ''; 
        const image = supplier.value.image ?? null; 
        const address = supplier.value.address ?? '';  
        const hotline = supplier.value.hotline ?? '';  
        const description = supplier.value.description ?? null; 
        const services = supplier.value.services ?? [];  
        const rooms = supplier.value.rooms ?? []; 
        const supplierData = {
            hotel_id,
            name,
            image,
            address,
            hotline,
            description,
            services,
            rooms
        };
        if (hotel_id) {
            await updateSupplierApi(hotel_id, supplierData);
            emit('updated');
            closePopup();
        } else {
            console.error('Hotel ID is not valid.');
        }
    } catch (error) {
        console.error('Lỗi khi cập nhật nhà cung cấp:', error);
    }
};
</script>
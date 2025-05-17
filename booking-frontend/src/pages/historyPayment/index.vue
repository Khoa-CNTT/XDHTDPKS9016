<template>
    <div class="space-y-6 px-4 py-6">
        <!-- Bảng danh sách -->
        <div class="border rounded-lg p-6 bg-white shadow">
            <h3 class="text-lg font-semibold mb-4 text-blue-700 flex">
                <Icon icon="mdi:clipboard-list-outline" width="24" height="24" class="mr-2" />
                Danh sách đặt phòng
            </h3>

            <div v-if="bookings.length">
                <table class="min-w-full divide-y divide-gray-200 border">
                    <thead class="bg-gray-100">
                        <tr>
                            <th class="px-4 py-2 text-left">STT</th>
                            <th class="px-4 py-2 text-left">Ngày nhận phòng</th>
                            <th class="px-4 py-2 text-left">Ngày trả phòng</th>
                            <th class="px-4 py-2 text-left">Số ngày</th>
                            <th class="px-4 py-2 text-left">Tổng tiền</th>
                            <th class="px-4 py-2 text-left">Trạng thái</th>
                            <th class="px-4 py-2 text-center">Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(booking, index) in bookings" :key="booking.id">
                            <td class="px-4 py-2">{{ index + 1 }}</td>
                            <td class="px-4 py-2">{{ formatDate(booking.checkinDate) }}</td>
                            <td class="px-4 py-2">{{ formatDate(booking.checkoutDate) }}</td>
                            <td class="px-4 py-2">{{ getDays(booking.checkinDate, booking.checkoutDate) }}</td>
                            <td class="px-4 py-2">{{ formatCurrency(booking.totalAmount) }}</td>
                            <td class="px-4 py-2">
                                <span :class="statusClass(booking.status)"
                                    class="px-2 py-1 rounded-full text-sm font-medium inline-block">
                                    {{ booking.status }}
                                </span>
                            </td>
                            <td class="px-4 py-2 text-center">
                                <button @click="viewBooking(booking)"
                                    class="p-2 bg-blue-500 hover:bg-blue-600 text-white rounded transition">
                                    <Icon icon="mdi:eye" width="20" height="20" />
                                </button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <div v-else class="text-center py-6 text-gray-500">
                Không có dữ liệu đặt phòng...
            </div>
        </div>

        <!-- Modal xem chi tiết -->
        <UpdateInfo v-if="showModal" :booking="selectedBooking" @close="showModal = false" />
    </div>
</template>
<script setup lang="ts">
import { ref } from 'vue';
import { Icon } from '@iconify/vue';

const bookings = ref([
    {
        id: 1,
        checkinDate: '2025-05-15',
        checkoutDate: '2025-05-18',
        totalAmount: 3600000,
        status: 'Đã xác nhận'
    },
    {
        id: 2,
        checkinDate: '2025-05-20',
        checkoutDate: '2025-05-22',
        totalAmount: 2400000,
        status: 'Chờ xác nhận'
    }
]);

const showModal = ref(false);
const selectedBooking = ref<any>(null);

function viewBooking(booking: any) {
    selectedBooking.value = booking;
    showModal.value = true;
}

function formatDate(dateStr: string): string {
    const d = new Date(dateStr);
    return d.toLocaleDateString('vi-VN');
}

function getDays(start: string, end: string): number {
    const startDate = new Date(start);
    const endDate = new Date(end);
    const diffTime = Math.abs(endDate.getTime() - startDate.getTime());
    return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
}

function formatCurrency(amount: number): string {
    return amount.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
}

function statusClass(status: string): string {
    switch (status) {
        case 'Đã xác nhận':
            return 'border border-green-500 bg-green-100 text-green-700';
        case 'Chờ xác nhận':
            return 'border border-yellow-500 bg-yellow-100 text-yellow-700';
        case 'Đã hủy':
            return 'border border-red-500 bg-red-100 text-red-700';
        default:
            return 'border border-gray-400 bg-gray-100 text-gray-700';
    }
}
</script>

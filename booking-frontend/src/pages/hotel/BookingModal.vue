<template>
    <div v-if="show" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 pt-20">
        <div class="bg-white rounded-lg w-full max-w-full sm:max-w-xl md:max-w-2xl lg:max-w-3xl p-6 relative shadow-lg max-h-[80vh] overflow-y-auto"
            style="box-sizing: border-box;">
            <button class="absolute top-2 right-2 text-gray-500 hover:text-gray-700" @click="$emit('close')"
                aria-label="Close modal">
                ✖
            </button>

            <!-- Bước chỉ dẫn -->
            <div class="flex flex-wrap justify-between mb-6 border-b pb-3 text-sm sm:text-base">
                <div :class="stepClass(1)">1. Đặt Phòng</div>
                <div :class="stepClass(2)">2. Liên Hệ</div>
            </div>

            <!-- Nội dung theo bước -->
            <div>
                <div v-if="step === 1" class="p-4 border rounded bg-white space-y-6">
                    <!-- Nhập ngày & giờ nhận - trả phòng -->
                    <div class="p-4 border rounded bg-gray-50">
                        <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-4">
                            <div>
                                <label class="block font-medium mb-1">Ngày nhận phòng</label>
                                <input type="date" v-model="body.checkInDate" class="w-full border rounded px-3 py-2" />
                            </div>
                            <div>
                                <label class="block font-medium mb-1">Giờ nhận phòng</label>
                                <input type="time" v-model="body.checkInTime" class="w-full border rounded px-3 py-2" />
                            </div>
                            <div>
                                <label class="block font-medium mb-1">Ngày trả phòng</label>
                                <input type="date" v-model="body.checkOutDate"
                                    class="w-full border rounded px-3 py-2" />
                            </div>
                            <div>
                                <label class="block font-medium mb-1">Giờ trả phòng</label>
                                <input type="time" v-model="body.checkOutTime"
                                    class="w-full border rounded px-3 py-2" />
                            </div>
                        </div>
                    </div>

                    <!-- Chọn loại phòng và số lượng -->
                    <div class="p-4 border rounded bg-gray-50 space-y-4">
                        <h3 class="font-semibold mb-2">Chọn loại phòng và số lượng:</h3>
                        <div v-for="(roomSel, index) in body.roomSelections" :key="index"
                            class="grid grid-cols-1 sm:grid-cols-3 gap-4 items-center">
                            <div>
                                <label class="block font-medium mb-1">Loại phòng</label>
                                <div class="w-full border rounded px-3 py-2 bg-gray-100">
                                    {{ roomType.room_type_id }}
                                </div>
                            </div>
                            <div>
                                <label class="block font-medium mb-1">Số lượng phòng</label>
                                <input type="number" v-model="roomSel.numberOfRooms" min="1"
                                    class="w-full border rounded px-3 py-2" />
                            </div>
                            <div>
                                <label class="block font-medium mb-1">Số khách</label>
                                <input type="number" v-model="body.numberOfPeople" min="1"
                                    class="w-full border rounded px-3 py-2" />
                            </div>
                        </div>
                    </div>

                    <!-- Chọn dịch vụ đi kèm -->
                    <h3 class="text-lg font-semibold mb-3">Chọn dịch vụ đi kèm:</h3>
                    <div class="grid grid-cols-1 sm:grid-cols-2 gap-3 max-h-40 overflow-y-auto mb-4">
                        <label v-for="service in hotel.services" :key="service.service_id"
                            class="flex items-center gap-3 cursor-pointer select-none">
                            <input type="checkbox" :value="service.service_id" v-model="selectedServices"
                                class="w-5 h-5 text-blue-600 rounded border-gray-300 focus:ring-blue-500" />
                            <span class="text-gray-700">{{ service.service_name }}</span>
                        </label>
                    </div>

                    <!-- Tóm tắt dịch vụ đã chọn -->
                    <div class="bg-blue-50 border border-blue-200 rounded p-4 text-blue-800">
                        <p><strong>Dịch vụ đã chọn:</strong></p>
                        <p v-if="selectedServices.length === 0" class="italic text-gray-500">Chưa chọn dịch vụ nào.</p>
                        <ul v-else class="list-disc list-inside">
                            <li v-for="(srv, idx) in selectedServices" :key="idx">{{ srv }}</li>
                        </ul>
                    </div>
                </div>

                <div v-if="step === 2" class="p-4 border rounded bg-white space-y-4">
                    <h2 class="text-xl font-semibold mb-4">Thông tin liên hệ</h2>
                    <div class="space-y-4">
                        <input v-model="contact.bookingId" type="number" placeholder="Mã đặt lịch (Booking ID)"
                            class="w-full border rounded px-4 py-2" />
                        <input v-model="contact.name" type="text" placeholder="Họ và tên"
                            class="w-full border rounded px-4 py-2" />
                        <input v-model="contact.email" type="email" placeholder="Email"
                            class="w-full border rounded px-4 py-2" />
                        <input v-model="contact.phone" type="text" placeholder="Số điện thoại"
                            class="w-full border rounded px-4 py-2" />
                        <input v-model="contact.address" type="text" placeholder="Địa chỉ"
                            class="w-full border rounded px-4 py-2" />
                        <textarea v-model="contact.specialRequests" placeholder="Yêu cầu đặc biệt" rows="3"
                            class="w-full border rounded px-4 py-2"></textarea>
                    </div>
                </div>
            </div>

            <!-- Nút điều hướng -->
            <div class="flex flex-wrap justify-between mt-6 gap-2">
                <button v-if="step > 1" @click="step--"
                    class="px-4 py-2 bg-gray-200 rounded hover:bg-gray-300 transition flex-grow sm:flex-grow-0">
                    Quay lại
                </button>
                <!-- <button v-if="step < 2" @click="step++"
                    class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 ml-auto transition flex-grow sm:flex-grow-0">
                    Tiếp tục
                </button> -->
                <button v-if="step < 2" @click="handleNextStep" :disabled="loading"
                    class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 ml-auto transition flex-grow sm:flex-grow-0 disabled:opacity-50">
                    Tiếp tục
                </button>
                <button v-if="step === 2" @click="submitBooking"
                    class="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 ml-auto transition flex-grow sm:flex-grow-0">
                    Đặt phòng
                </button>
            </div>
        </div>
    </div>
</template>
<script setup>
import { ref } from 'vue'
import { initializeBookingApi, contactInfoPaymentApi } from '@/services/booking'
const props = defineProps({
    show: Boolean,
    room: Object,
    hotel: Object,
    roomType: Object
})
defineEmits(['close'])

const step = ref(1)
const contact = ref({
    bookingId: null,
    name: '',
    phone: '',
    email: '',
    address: '',
    specialRequests: ''
})
const selectedServices = ref([])

const stepClass = (s) => {
    return `font-medium px-2 py-1 rounded ${step.value === s ? 'text-blue-700 font-bold border-b-2 border-blue-700' : 'text-gray-500'
        }`
}

const body = ref({
    checkInDate: '',
    checkOutDate: '',
    checkInTime: '',
    checkOutTime: '',
    numberOfPeople: 1,
    roomSelections: [
        {
            roomTypeId: props.roomType.room_type_id,
            numberOfRooms: 1
        }
    ],
    serviceIds: []
})
const loading = ref(false)
function formatTimeToHHMMSS(time) {
  // time có thể là "14:00" hoặc "14:00:00"
  if (!time) return '' 
  if (time.length === 5) {
    // dạng "HH:mm" => thêm ":00"
    return time + ':00'
  }
  return time
}
async function handleNextStep() {
    console.log('handleNextStep được gọi')

    if (step.value === 1) {
         body.value.checkInTime = formatTimeToHHMMSS(body.value.checkInTime)
        body.value.checkOutTime = formatTimeToHHMMSS(body.value.checkOutTime)
        // 1. Gán serviceIds từ checkbox người dùng đã chọn
        body.value.serviceIds = [...selectedServices.value]
        console.log('Dữ liệu gửi API:', JSON.stringify(body.value, null, 2))

        // 2. Kiểm tra xem đã chọn ngày giờ chưa
        if (
            !body.value.checkInDate ||
            !body.value.checkInTime ||
            !body.value.checkOutDate ||
            !body.value.checkOutTime
        ) {
            console.warn('Ngày giờ check-in hoặc check-out bị thiếu')
            return
        }


        // 4. Gọi API
        loading.value = true
        try {
            const res = await initializeBookingApi(body.value)
            console.log('API trả về:', res)

            // 5. Lưu bookingId từ API vào contact
            contact.value.bookingId = res.bookingId
            step.value++ // sang bước 2
        } catch (error) {
            console.error('Lỗi khi gọi initializeBookingApi:', error)
        } finally {
            loading.value = false
        }
    }
}
</script>
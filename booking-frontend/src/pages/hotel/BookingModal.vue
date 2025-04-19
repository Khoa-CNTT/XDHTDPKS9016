<template>
    <div v-if="show" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white rounded-lg w-[95%] max-w-2xl p-6 relative">
            <button class="absolute top-2 right-2 text-gray-500" @click="$emit('close')">✖</button>

            <!-- Bước chỉ dẫn -->
            <div class="flex justify-between mb-6 border-b pb-3">
                <div :class="stepClass(1)">1. Đặt Phòng</div>
                <div :class="stepClass(2)">2. Liên Hệ</div>
                <div :class="stepClass(3)">3. Thanh Toán</div>
            </div>

            <!-- Nội dung theo bước -->
            <div>
                <div v-if="step === 1">
                    <h2 class="text-xl font-semibold mb-2">Thông tin đặt phòng: {{ room?.name }}</h2>
                    <p class="mb-2 text-gray-600">{{ room?.description }}</p>
                    <ul class="list-disc list-inside text-sm text-gray-600 mb-4">
                        <li v-for="(feature, index) in room?.features" :key="index">{{ feature }}</li>
                    </ul>

                    <h3 class="text-lg font-semibold mb-2">Chọn dịch vụ đi kèm:</h3>
                    <div class="grid grid-cols-2 gap-2 mb-4">
                        <label v-for="(service, index) in services" :key="index" class="flex items-center gap-2">
                            <input type="checkbox" :value="service" v-model="selectedServices" />
                            <span>{{ service }}</span>
                        </label>
                    </div>
                </div>


                <div v-if="step === 2">
                    <h2 class="text-xl font-semibold mb-4">Thông tin liên hệ</h2>
                    <div class="space-y-4">
                        <input v-model="contact.name" type="text" placeholder="Họ và tên"
                            class="w-full border rounded px-4 py-2" />
                        <input v-model="contact.phone" type="text" placeholder="Số điện thoại"
                            class="w-full border rounded px-4 py-2" />
                        <input v-model="contact.email" type="email" placeholder="Email"
                            class="w-full border rounded px-4 py-2" />
                    </div>
                </div>

                <div v-if="step === 3">
                    <h2 class="text-xl font-semibold mb-4">Thông tin thanh toán</h2>
                    <div class="space-y-4">
                        <select v-model="paymentMethod" class="w-full border rounded px-4 py-2">
                            <option value="">Chọn phương thức thanh toán</option>
                            <option value="cash">Thanh toán khi nhận phòng</option>
                            <option value="credit">Thẻ tín dụng</option>
                            <option value="momo">Momo (chuyển khoản)</option>
                            <option value="bank">Chuyển khoản ngân hàng</option>
                            <option value="vnpay">Thanh toán VNPAY</option>
                        </select>

                        <textarea v-model="note" class="w-full border rounded px-4 py-2"
                            placeholder="Ghi chú thêm (nếu có)" />

                        <!-- Hiện mã QR nếu chọn VNPAY -->
                        <!-- Hiện mã QR nếu chọn Momo hoặc VNPAY -->
                        <div v-if="paymentMethod === 'momo' || paymentMethod === 'vnpay'" class="text-center mt-4">
                            <p class="text-gray-700 font-medium mb-2">
                                Quét mã QR để thanh toán qua {{ paymentMethod === 'momo' ? 'Momo' : 'nnVNPAY' }}
                            </p>
                            <img v-if="qrCodeSrc" :src="qrCodeSrc" alt="QR Code"
                                class="mx-auto w-48 h-48 object-contain rounded shadow" />
                            <p class="text-sm text-gray-500 mt-2">
                                Vui lòng thanh toán theo mã QR trên ứng dụng
                                {{ paymentMethod === 'momo' ? 'Momo' : 'VNPAY' }}.
                            </p>
                        </div>

                    </div>
                </div>
            </div>

            <!-- Nút điều hướng -->
            <div class="flex justify-between mt-6">
                <button v-if="step > 1" @click="step--" class="px-4 py-2 bg-gray-200 rounded hover:bg-gray-300">Quay
                    lại</button>
                <button v-if="step < 3" @click="step++"
                    class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 ml-auto">Tiếp tục</button>
                <button v-if="step === 3" @click="submitBooking"
                    class="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 ml-auto">Đặt phòng</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import QRCode from 'qrcode'
import { ref, watch } from 'vue'

defineProps({
    show: Boolean,
    room: Object,
})
defineEmits(['close'])

const step = ref(1)
const contact = ref({
    name: '',
    phone: '',
    email: '',
})
const paymentMethod = ref('')
const note = ref('')
const qrCodeSrc = ref('')

// CSS cho bước chỉ dẫn
const stepClass = (s) => {
    return `font-medium px-2 py-1 rounded ${step.value === s
        ? 'text-blue-700 font-bold border-b-2 border-blue-700'
        : 'text-gray-500'
        }`
}

// Hàm submit đặt phòng
const submitBooking = () => {
    console.log('Đặt phòng:', {
        room: room.value,
        contact: contact.value,
        payment: paymentMethod.value,
        note: note.value,
    })
    alert('Đặt phòng thành công!')
}

// Theo dõi thay đổi phương thức thanh toán để tạo QR tương ứng
watch(paymentMethod, async (newMethod) => {
    if (newMethod === 'momo') {
        await createMomoQrCode()
    } else if (newMethod === 'vnpay') {
        await createVnPayQrCode()
    } else {
        qrCodeSrc.value = ''
    }
})

// Tạo mã QR cho Momo
const createMomoQrCode = async () => {
    try {
        // Ví dụ URL giả lập từ Momo (thực tế bạn nên lấy từ API của Momo)
        const momoUrl =
            'https://momo.vn/pay?amount=100000&orderInfo=Thanh+toan+qua+momo'
        qrCodeSrc.value = await QRCode.toDataURL(momoUrl)
    } catch (error) {
        console.error('Lỗi tạo mã QR Momo:', error)
    }
}

// Tạo mã QR cho VNPAY
const createVnPayQrCode = async () => {
    try {
        const url =
            'https://sandbox.vnpayment.vn/vnpayepay.html?amount=100000&orderInfo=Thanh+toan+vnpay'
        qrCodeSrc.value = await QRCode.toDataURL(url)
    } catch (error) {
        console.error('Lỗi tạo mã QR VNPAY:', error)
    }
}
const services = [
    'Spa',
    'Golf',
    'Massage',
    'Tennis',
    'Hồ bơi',
    'Gym',
    'Buffet sáng',
    'Xe đưa đón'
]
const selectedServices = ref([])
</script>
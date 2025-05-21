<template>
  <div
    v-if="show"
    class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 pt-20"
  >
    <div
      class="bg-white rounded-lg w-full max-w-full sm:max-w-xl md:max-w-2xl lg:max-w-3xl p-6 relative shadow-lg max-h-[80vh] overflow-y-auto"
      style="box-sizing: border-box"
    >
      <button
        class="absolute top-2 right-2 text-gray-500 hover:text-gray-700"
        @click="$emit('close')"
        aria-label="Close modal"
      >
        ✖
      </button>

      <!-- Bước chỉ dẫn -->
      <div class="flex flex-wrap justify-between mb-6 border-b pb-3 text-sm sm:text-base">
        <div :class="stepClass(1)">1. Đặt Phòng</div>
        <div :class="stepClass(2)">2. Liên Hệ</div>
      </div>

      <!-- Nội dung theo bước -->
      <div>
        <div
          v-if="step === 1"
          class="p-4 border rounded bg-white space-y-6"
        >
          <!-- Nhập ngày & giờ nhận - trả phòng -->
          <div class="p-4 border rounded bg-gray-50">
            <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-4">
              <div>
                <label class="block font-medium mb-1">Ngày nhận phòng</label>
                <input
                  :min="today"
                  type="date"
                  v-model="body.checkInDate"
                  class="w-full border rounded px-3 py-2"
                />
                <p
                  v-if="errorCheckInDate"
                  class="text-red-600 text-sm mt-1"
                >
                  {{ errorCheckInDate }}
                </p>
              </div>
              <div>
                <label class="block font-medium mb-1">Giờ nhận phòng</label>
                <input
                  :min="today"
                  type="time"
                  v-model="body.checkInTime"
                  class="w-full border rounded px-3 py-2"
                />
              </div>
              <div>
                <label class="block font-medium mb-1">Ngày trả phòng</label>
                <input
                  :min="body.checkInDate || today"
                  type="date"
                  v-model="body.checkOutDate"
                  class="w-full border rounded px-3 py-2"
                />
                <p
                  v-if="errorCheckOutDate"
                  class="text-red-600 text-sm mt-1"
                >
                  {{ errorCheckOutDate }}
                </p>
              </div>
              <div>
                <label class="block font-medium mb-1">Giờ trả phòng</label>
                <input
                  type="time"
                  v-model="body.checkOutTime"
                  class="w-full border rounded px-3 py-2"
                />
              </div>
            </div>
          </div>

          <!-- Chọn loại phòng và số lượng -->
          <div class="p-4 border rounded bg-gray-50 space-y-4">
            <div
              v-for="(roomSel, index) in body.roomSelections"
              :key="index"
              class="grid grid-cols-1 sm:grid-cols-3 gap-4 items-start"
            >
              <div class="w-full">
                <div class="flex gap-4">
                  <!-- Loại phòng -->
                  <div class="flex-1">
                    <div class="text-sm text-gray-500 mb-1">Loại phòng</div>
                    <div class="border rounded px-3 py-2 bg-gray-100 text-sm">
                      {{ roomType.room_type_id }}
                    </div>
                  </div>

                  <!-- Số phòng -->
                  <div class="flex-1">
                    <div class="text-sm text-gray-500 mb-1">Số phòng</div>
                    <div class="border rounded px-3 py-2 bg-gray-100 text-sm">
                      {{ room.id_room }}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Chọn dịch vụ đi kèm -->
          <h3 class="text-lg font-semibold mb-3">Chọn dịch vụ đi kèm:</h3>
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-3 max-h-40 overflow-y-auto mb-4">
            <label
              v-for="service in hotel.services"
              :key="service.service_id"
              class="flex items-center gap-3 cursor-pointer select-none"
            >
              <input
                type="checkbox"
                :value="service.service_id"
                v-model="selectedServices"
                class="w-5 h-5 text-blue-600 rounded border-gray-300 focus:ring-blue-500"
              />
              <span class="text-gray-700">{{ service.service_name }}</span>
            </label>
          </div>

          <!-- Tóm tắt dịch vụ đã chọn -->
          <div class="bg-blue-50 border border-blue-200 rounded p-4 text-blue-800">
            <p><strong>Dịch vụ đã chọn:</strong></p>
            <p
              v-if="selectedServices.length === 0"
              class="italic text-gray-500"
            >
              Chưa chọn dịch vụ nào.
            </p>
            <ul
              v-else
              class="list-disc list-inside"
            >
              <li
                v-for="(srv, idx) in selectedServices"
                :key="idx"
              >
                {{ srv }}
              </li>
            </ul>
          </div>
        </div>

        <div
          v-if="step === 2"
          class="p-4 border rounded bg-white space-y-4"
        >
          <h2 class="text-xl font-semibold mb-4">Thông tin liên hệ</h2>
          <div class="space-y-4">
            <input
              v-model="contact.bookingId"
              type="number"
              placeholder="Mã đặt lịch (Booking ID)"
              class="w-full border rounded px-4 py-2"
            />
            <input
              v-model="contact.contactName"
              type="text"
              placeholder="Họ và tên"
              class="w-full border rounded px-4 py-2"
            />
            <input
              v-model="contact.contactEmail"
              type="email"
              placeholder="Email"
              class="w-full border rounded px-4 py-2"
            />
            <input
              v-model="contact.contactPhone"
              type="text"
              placeholder="Số điện thoại"
              class="w-full border rounded px-4 py-2"
            />
            <input
              v-model="contact.contactAddress"
              type="text"
              placeholder="Địa chỉ"
              class="w-full border rounded px-4 py-2"
            />
            <textarea
              v-model="contact.specialRequests"
              placeholder="Yêu cầu đặc biệt"
              rows="3"
              class="w-full border rounded px-4 py-2"
            ></textarea>
          </div>
        </div>
      </div>

      <!-- Nút điều hướng -->
      <div class="flex flex-wrap justify-between mt-6 gap-2">
        <button
          v-if="step > 1"
          @click="step--"
          class="px-4 py-2 bg-gray-200 rounded hover:bg-gray-300 transition flex-grow sm:flex-grow-0"
        >
          Quay lại
        </button>

        <button
          v-if="step < 2"
          @click="handleNextStep"
          :disabled="loading"
          class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 ml-auto transition flex-grow sm:flex-grow-0 disabled:opacity-50"
        >
          Tiếp tục
        </button>
        <button
          v-if="step === 2"
          @click="submitBooking"
          class="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 ml-auto transition flex-grow sm:flex-grow-0"
        >
          Đặt phòng
        </button>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, watch, onMounted } from 'vue'
import { initializeBookingApi, contactInfoPaymentApi } from '@/services/booking'
import { toast } from 'vue3-toastify'
const props = defineProps({
  show: Boolean,
  room: Object,
  hotel: Object,
  roomType: Object,
})

const emit = defineEmits(['close'])

const step = ref(1)
const contact = ref({
  bookingId: null,
  contactName: '',
  contactPhone: '',
  contactEmail: '',
  contactAddress: '',
  specialRequests: '',
})
const selectedServices = ref([])

const today = new Date().toISOString().split('T')[0]
const errorCheckInDate = ref('')
const errorCheckOutDate = ref('')
const loading = ref(false)

const body = ref({
  checkInDate: '',
  checkOutDate: '',
  checkInTime: '',
  checkOutTime: '',
  numberOfPeople: 1,
  roomSelections: [
    {
      roomTypeId: null,
      roomId: null,
    },
  ],
  serviceIds: [],
})
watch(
  () => body.value.checkInDate,
  (newVal) => {
    if (!newVal) {
      errorCheckInDate.value = 'Ngày nhận phòng không được để trống.'
    } else if (newVal < today) {
      errorCheckInDate.value = 'Ngày nhận phòng không được chọn ngày trong quá khứ.'
    } else {
      errorCheckInDate.value = ''
    }

    // Nếu ngày trả phòng nhỏ hơn ngày nhận phòng thì reset hoặc báo lỗi
    if (body.value.checkOutDate && body.value.checkOutDate < newVal) {
      errorCheckOutDate.value = 'Ngày trả phòng phải bằng hoặc sau ngày nhận phòng.'
    } else {
      errorCheckOutDate.value = ''
    }
  },
)

watch(
  () => body.value.checkOutDate,
  (newVal) => {
    if (!newVal) {
      errorCheckOutDate.value = 'Ngày trả phòng không được để trống.'
    } else if (newVal < body.value.checkInDate) {
      errorCheckOutDate.value = 'Ngày trả phòng phải bằng hoặc sau ngày nhận phòng.'
    } else {
      errorCheckOutDate.value = ''
    }
  },
)
// Watch để cập nhật roomTypeId khi props thay đổi
watch(
  [() => props.room, () => props.roomType],
  ([newRoom, newRoomType]) => {
    if (newRoom?.id_room) {
      body.value.roomSelections[0].roomId = newRoom.id_room
    }
    if (newRoomType?.room_type_id) {
      body.value.roomSelections[0].roomTypeId = newRoomType.room_type_id
    }
    console.log('✅ roomSelections đã được cập nhật:', body.value.roomSelections)
  },
  { immediate: true },
)

function formatTimeToHHMMSS(time) {
  if (!time) return ''
  if (time.length === 5) return time + ':00'
  return time
}

const stepClass = (s) => {
  return `font-medium px-2 py-1 rounded ${
    step.value === s ? 'text-blue-700 font-bold border-b-2 border-blue-700' : 'text-gray-500'
  }`
}

async function handleNextStep() {
  console.log('handleNextStep được gọi')
  if (!body.value.checkInDate) errorCheckInDate.value = 'Ngày nhận phòng không được để trống.'
  else errorCheckInDate.value = ''

  if (!body.value.checkOutDate) errorCheckOutDate.value = 'Ngày trả phòng không được để trống.'
  else errorCheckOutDate.value = ''
  if (step.value === 1) {
    // Format lại thời gian check-in/out về định dạng HH:mm:ss
    body.value.checkInTime = formatTimeToHHMMSS(body.value.checkInTime)
    body.value.checkOutTime = formatTimeToHHMMSS(body.value.checkOutTime)

    // Gán dịch vụ đã chọn từ checkbox
    body.value.serviceIds = [...selectedServices.value]

    // Kiểm tra ngày và giờ có được nhập không
    if (
      !body.value.checkInDate ||
      !body.value.checkInTime ||
      !body.value.checkOutDate ||
      !body.value.checkOutTime
    ) {
      console.warn('Vui lòng nhập đầy đủ ngày và giờ check-in/check-out')
      return
    }

    console.log('Dữ liệu gửi initializeBookingApi:', JSON.stringify(body.value, null, 2))

    loading.value = true
    try {
      const res = await initializeBookingApi(body.value)
      console.log('API trả về:', res)

      // Gán bookingId từ API cho biến contact
      if (res && res.bookingId) {
        contact.value.bookingId = res.bookingId
        step.value++ // Chuyển sang bước tiếp theo
      } else {
        console.error('Không nhận được bookingId từ API')
      }
    } catch (error) {
      console.error('Lỗi khi gọi initializeBookingApi:', error)
    } finally {
      loading.value = false
    }
  }
}
async function submitBooking() {
  loading.value = true

  try {
    const res = await contactInfoPaymentApi(contact.value)

    if (res && res.paymentUrl) {
      // ✅ Hiển thị toast thành công
      toast.success('🎉 Đặt phòng thành công! Đang chuyển hướng đến trang thanh toán...', {
        autoClose: 1500, // thời gian hiển thị toast
        position: 'top-center',
      })

      // ✅ Sau 1.5s thì mở paymentUrl và đóng modal
      setTimeout(() => {
        window.open(res.paymentUrl, '_blank') // mở tab mới
        resetForm()
        emit('close') // đóng modal
      }, 1500)
    } else {
      toast.error('Không nhận được link thanh toán từ hệ thống.')
    }
  } catch (err) {
    console.error('Lỗi khi gọi contactInfoPaymentApi:', err)
    toast.error('❌ Đặt phòng thất bại. Vui lòng thử lại sau.')
  } finally {
    loading.value = false
  }
}
function resetForm() {
  step.value = 1

  contact.value = {
    bookingId: null,
    contactName: '',
    contactPhone: '',
    contactEmail: '',
    contactAddress: '',
    specialRequests: '',
  }

  selectedServices.value = []

  body.value = {
    checkInDate: '',
    checkOutDate: '',
    checkInTime: '',
    checkOutTime: '',
    numberOfPeople: 1,
    roomSelections: [
      {
        roomTypeId: null,
        roomId: null,
      },
    ],
    serviceIds: [],
  }

  errorCheckInDate.value = ''
  errorCheckOutDate.value = ''
}
</script>

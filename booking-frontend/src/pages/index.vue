<template>
  <div class="content relative bg-gradient-primary">
    <div class="hero text-center min-h-[350px] flex flex-col justify-start relative">
      <!-- Hình ảnh nền -->
      <div class="absolute inset-0 w-full h-[350px]">
        <img
          src="https://cms-origin.gotadi.com/wp-content/uploads/2025/04/sl38-t4-2025-voucher-100k-ks-vivumuale-web-1.png"
          alt="Banner khách sạn"
          class="w-full h-full object-cover object-center"
        />
        <!-- Lớp overlay để làm tối hình ảnh -->
        <!-- <div class="absolute inset-0 bg-black bg-opacity-40"></div> -->
      </div>

      <!-- Nội dung hero -->
      <div class="relative z-10 pt-12">
        <!-- <h1 class="text-3xl text-white font-bold uppercase">
          Rong chơi bốn phương, giá vẫn "yêu thương"
        </h1> -->

        <div class="search-box w-full md:w-4/5 lg:w-3/4 mx-auto z-20 relative mt-36">
          <!-- Tabs chọn loại dịch vụ -->
          <div class="flex overflow-x-auto bg-white rounded-t-lg shadow-md">
            <div class="flex items-center p-4 cursor-pointer border-b-2 border-blue-500">
              <span class="text-blue-500 font-medium mr-2">🏨</span>
              <span class="text-blue-500 font-medium">Khách sạn</span>
            </div>
          </div>

          <!-- Chọn loại lưu trú -->
          <div class="flex bg-white p-5 border-b border-gray-200">
            <div class="mr-4 cursor-pointer">
              <button class="px-6 py-3 bg-blue-500 text-white rounded-full">Chỗ Ở Qua Đêm</button>
            </div>
          </div>

          <!-- Form tìm kiếm -->
          <!-- <div class="bg-white p-6 rounded-b-lg shadow-xl">
            <div class="grid grid-cols-1 md:grid-cols-1 gap-6">
           
              <div class="flex items-center bg-gray-50 border border-gray-200 rounded-lg p-4 w-full">
                <span class="mr-3 text-xl">🔍</span>
                <input type="text" placeholder="Sea Sand 2 Hotel Da Nang"
                  class="w-full bg-transparent outline-none text-lg" v-model="location" />
              </div>
            </div>
          
            <div class="flex justify-center mt-6">
              <button
                class="bg-blue-500 text-white font-bold py-4 px-16 rounded-md text-xl uppercase hover:bg-blue-600 transition duration-300">
                TÌM
              </button>
            </div>
          </div> -->
          <div class="bg-white p-6 rounded-b-lg shadow-xl">
            <div class="grid grid-cols-1 md:grid-cols-1 gap-6">
              <!-- Ô input tìm kiếm -->
              <div
                class="flex items-center bg-gray-50 border border-gray-200 rounded-lg p-4 w-full"
              >
                <span class="mr-3 text-xl">🔍</span>
                <input
                  v-model="location"
                  type="text"
                  placeholder="Sea Sand 2 Hotel Da Nang"
                  class="w-full bg-transparent outline-none text-lg"
                />
              </div>
            </div>

            <!-- Nút tìm kiếm -->
            <div class="flex justify-center mt-6">
              <!-- <button @click="handleSearch"
                class="bg-blue-500 text-white font-bold py-4 px-16 rounded-md text-xl uppercase hover:bg-blue-600 transition duration-300">
                TÌM
              </button> -->
            </div>

            <!-- Kết quả tìm kiếm -->
            <div
              v-if="searchResults.length"
              class="mt-8"
            >
              <h2 class="text-2xl font-semibold mb-4 text-gray-800">🔎 Kết quả tìm kiếm:</h2>
              <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                <div
                  v-for="hotel in searchResults"
                  :key="hotel.hotel_id"
                  @click="viewHotelDetail(hotel.hotel_id)"
                  class="border rounded-lg overflow-hidden shadow-md hover:shadow-lg transition cursor-pointer"
                >
                  <img
                    :src="`${BASE_URL}${hotel.image}`"
                    alt="Hotel Image"
                    class="w-full h-48 object-cover"
                  />
                  <div class="p-4">
                    <h3 class="text-xl font-bold text-gray-800 mb-1">{{ hotel.name }}</h3>
                    <p class="text-gray-600 text-sm mb-1">
                      <strong>Địa chỉ:</strong> {{ hotel.address }}
                    </p>
                    <p class="text-gray-600 text-sm">
                      <strong>Hotline:</strong> {{ hotel.hotline }}
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- Thông báo ưu đãi và quảng cáo -->
          <div
            class="absolute top-4 right-4 bg-red-500 text-white px-4 py-2 rounded-lg shadow-lg flex items-center space-x-2"
          >
            <span class="text-lg">🔥</span>
            <span class="text-sm font-semibold">Ưu đãi: Đặt gói tiết kiệm - Mới!</span>
          </div>
        </div>
      </div>
    </div>

    <div
      v-if="groupedHotels.length > 0"
      class="container mx-auto p-4 pt-32"
    >
      <div
        v-for="group in groupedHotels"
        :key="group.location"
        class="mb-8"
      >
        <h2 class="text-2xl font-bold mb-4">{{ group.location }}</h2>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          <div
            v-for="hotel in group.hotels"
            :key="hotel.id"
            class="border p-4 rounded shadow"
          >
            <img
              :src="hotel.image"
              class="w-full h-48 object-cover mb-2"
            />
            <h3 class="text-lg font-bold">{{ hotel.name }}</h3>
            <p class="text-gray-600">{{ hotel.description }}</p>
            <button
              class="mt-2 px-4 py-2 bg-blue-600 text-white rounded"
              @click="goToDetail(hotel.id)"
            >
              Xem chi tiết
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="container mx-auto p-4 pt-14">
      <h1 class="text-2xl text-text font-bold mb-2 uppercase">Điểm đến nổi bật</h1>
      <hr class="mb-4 border-t-2 border-text" />
      <div class="grid grid-cols-2 gap-4 mb-4">
        <div
          v-for="(city, index) in topCities"
          :key="index"
          class="relative"
        >
          <img
            :src="city.image"
            :alt="city.name"
            class="w-full h-48 object-cover rounded-lg"
          />
          <div
            class="absolute top-0 left-0 bg-black bg-opacity-50 text-white text-sm p-1 rounded flex"
          >
            <img
              :src="city.img"
              :alt="city.name"
              class="w-8 h-5 object-cover rounded-lg"
            />
            {{ city.name }}
          </div>
        </div>
      </div>
      <div class="grid grid-cols-3 gap-4 pb-30">
        <div
          v-for="(city, index) in bottomCities"
          :key="index"
          class="relative"
        >
          <img
            :src="city.image"
            :alt="city.name"
            class="w-full h-48 object-cover rounded"
          />
          <div
            class="absolute top-0 left-0 bg-black bg-opacity-50 text-white text-sm p-1 rounded flex"
          >
            <img
              :src="city.img"
              :alt="city.name"
              class="w-8 h-5 object-cover rounded-lg"
            />
            {{ city.name }}
          </div>
        </div>
      </div>
    </div>
    <div class="container mx-auto p-4 pt-14">
      <h1 class="text-2xl text-text font-bold mb-2 uppercase">Chương trình khuyến mãi</h1>
      <hr class="mb-4 border-t-2 border-text" />
      <swiper
        :slides-per-view="3"
        :space-between="20"
        :loop="true"
        :autoplay="{ delay: 3000 }"
        navigation
        class="rounded-lg relative"
      >
        <swiper-slide
          v-for="(img, i) in promoImages"
          :key="i"
        >
          <img
            :src="img"
            alt="Khuyến mãi"
            class="rounded-xl h-40 w-full object-cover"
          />
        </swiper-slide>
      </swiper>
    </div>
    <div class="container mx-auto p-4">
      <h1 class="text-2xl text-text font-bold mb-2 uppercase">Khách sạn nổi bật</h1>
      <hr class="mb-4 border-t-2 border-text" />
      <div class="grid grid-cols-2 md:grid-cols-3 gap-4">
        <div
          v-for="(hotel, index) in hotelList.slice(0, displayCount)"
          :key="hotel.hotel_id"
          class="bg-white p-3 shadow-md rounded-lg"
        >
          <img
            v-if="hotel.image"
            :src="'http://localhost:8080' + hotel.image"
            alt="Hotel Image"
            class="w-full h-48 object-cover rounded-md"
          />
          <div class="flex justify-between mt-2">
            <div>
              <h2 class="text-lg font-semibold">{{ hotel.name }}</h2>
              <p class="text-sm text-gray-600">{{ hotel.address }}</p>
            </div>
            <button
              @click="viewHotelDetail(hotel.hotel_id)"
              class="mt-2 px-4 py-2 bg-text text-white rounded-md self-start"
            >
              Xem chi tiết
            </button>
          </div>
        </div>
      </div>
      <div class="flex justify-center mt-4">
        <button
          @click="toggleView"
          class="px-4 py-2 bg-blue-600 text-white rounded-full text-sm font-semibold hover:bg-blue-700 transition-all duration-200 ease-in-out"
        >
          {{ isExpanded ? 'Thu gọn' : 'Xem thêm' }}
        </button>
      </div>
    </div>
    <div class="container mx-auto p-4">
      <img
        src="https://top3.vn/uploads/source/levanty/saigonbptourist/banner-cat-2.jpg"
        alt="Hình khuyến mãi"
        class="w-full h-64 object-cover rounded-xl shadow-md"
      />
    </div>

    <div class="container mx-auto p-4 text-center">
      <hr class="mb-4 border-t-2 border-text" />
      <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
        <div
          v-for="about in about"
          :key="about.id"
          class="p-6 flex flex-col items-center"
        >
          <img
            :src="about.image"
            alt="Hotel Image"
            class="w-24 h-24 object-cover rounded-full mb-4"
          />
          <h2 class="text-lg font-semibold mb-2">{{ about.name }}</h2>
          <p class="text-sm text-gray-600 mb-4">{{ about.description }}</p>
        </div>
      </div>
      <hr class="mb-4 border-t-2 border-text" />
    </div>
    <div class="container mx-auto p-4 flex flex-col md:flex-row items-center">
      <div class="md:w-1/2 text-left">
        <h1 class="text-2xl text-blue-900 font-bold mb-2 uppercase">Đăng ký nơi nghỉ của bạn</h1>
        <hr class="mb-4 border-t-2 border-blue-900 w-3/4" />
        <p class="text-gray-600">
          Tiếp cận hàng triệu khách hàng tiềm năng và nâng tầm doanh nghiệp của bạn với chúng tôi.
        </p>
      </div>
      <div class="md:w-1/2 flex justify-center mt-6 md:mt-0">
        <img
          src="https://homesun.com.vn/wp-content/uploads/2022/05/Tieu-chuan-sao-cua-khach-san-Viet-Nam.jpg"
          alt="Hotel Image"
          class="rounded-lg shadow-lg"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { getHotelListApi, searchHotelsApi } from '@/services/home'
import { Swiper, SwiperSlide } from 'swiper/vue'
import 'swiper/css'
import 'swiper/css/navigation'
import { Navigation, Autoplay } from 'swiper/modules'
import 'swiper/css/autoplay'
import { BASE_URL } from '@/utils/imageHelper'
const hotelList = ref([])
const displayCount = ref(6)
const isExpanded = ref(false)
const router = useRouter()

const fetchHotelList = async () => {
  try {
    const response = await getHotelListApi()
    hotelList.value = response.content
    console.log('Hotel list response:', response.content)
  } catch (error) {
    console.error('Error fetching hotel list:', error)
  }
}

const loadMore = () => {
  displayCount.value += 6
}

const toggleView = () => {
  if (isExpanded.value) {
    displayCount.value = 6
  } else {
    displayCount.value += 6
  }
  isExpanded.value = !isExpanded.value
}

const viewHotelDetail = (hotelId) => {
  console.log('Hotel ID clicked:', hotelId)
  router.push({ name: 'HotelDetail', params: { id: hotelId } })
}

// Biến chứa từ khóa tìm kiếm và kết quả
const location = ref('')
const searchResults = ref([]) // Kết quả từ API
let debounceTimer = null
watch(location, (newVal) => {
  if (debounceTimer) clearTimeout(debounceTimer)

  if (!newVal || !newVal.trim()) {
    searchResults.value = []
    return
  }

  debounceTimer = setTimeout(async () => {
    try {
      const resp = await searchHotelsApi(newVal.trim())
      // Nếu API trả về resp.content thì dùng resp.content
      searchResults.value = resp.content || resp || []
    } catch (err) {
      console.error('Lỗi khi tìm kiếm khách sạn:', err)
    }
  }, 300)
})
onMounted(() => {
  fetchHotelList()
})

const showChat = ref(false)
const chatMessage = ref('')

function toggleChat() {
  showChat.value = !showChat.value
}

const checkIn = ref('')
const checkOut = ref('')
const quantity = ref(1)
const hotels = ref([])

const promoImages = [
  'https://cdn6.agoda.net/images/WebCampaign/pulse_globalcampaign_midnightmadness/home_banner_web2/vi-vn.png',
  'https://cdn6.agoda.net/images/WebCampaign/pulse_globalcampaign_fromasiatotheworld/home_banner_web2/vi-vn.png',
  'https://cdn6.agoda.net/images/WebCampaign/20250403_ka_eliteescapes/home_banner_web/en-us.png',
]
const groupedHotels = ref([])

const goToDetail = (hotelId) => {
  router.push({ name: 'HotelDetail', params: { id: hotelId } })
}
const topCities = ref([
  { name: 'Hồ Chí Minh', image: '/assets/images/1.png', img: '/assets/images/coVietNam.jpg' },
  { name: 'Đà Nẵng', image: '/assets/images/2.jpg', img: '/assets/images/coVietNam.jpg' },
])

const bottomCities = ref([
  { name: 'Hà Nội', image: '/assets/images/3.jpg', img: '/assets/images/coVietNam.jpg' },
  { name: 'Hội An', image: '/assets/images/4.jpg', img: '/assets/images/coVietNam.jpg' },
  { name: 'Thừa Thiên Huế', image: '/assets/images/5.jpg', img: '/assets/images/coVietNam.jpg' },
])

const about = [
  {
    id: 1,
    name: 'Tìm dễ dàng',
    description: 'Tìm trong số 5 triệu khách sạn chỉ sau vài giây.',
    image: '/assets/images/Search.svg',
  },
  {
    id: 2,
    name: 'Tự tin so sánh',
    description: 'So sánh giá phòng từ nhiều trang web cùng lúc.',
    image: '/assets/images/Compare.svg',
  },
  {
    id: 3,
    name: 'Tiết kiệm nhiều',
    description: 'Khám phá ưu đãi hấp dẫn dễ đặt trên trang web đối tác của chúng tôi.',
    image: '/assets/images/Save.svg',
  },
]
</script>

<style scoped>
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.3s ease;
}

.fade-slide-enter-from {
  opacity: 0;
  transform: translateY(20px);
}

.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(20px);
}
</style>

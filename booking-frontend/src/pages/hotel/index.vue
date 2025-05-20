<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-100 via-white to-purple-100 py-10">
    <div class="container mx-auto px-4">
      <main class="grid grid-cols-1 md:grid-cols-4 gap-6">
        <!-- Sidebar -->
        <aside class="col-span-1 bg-white rounded-3xl shadow-lg p-5 border border-blue-200">
          <h2 class="text-xl font-bold text-blue-700 mb-5">Tìm kiếm theo địa điểm</h2>

          <!-- Thanh tìm kiếm -->
          <input v-model="searchCity" type="text" placeholder="Tìm kiếm thành phố..."
            class="w-full mb-4 px-4 py-2 border border-blue-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400" />

          <div class="space-y-3">
            <div v-for="(city, index) in filteredCities" :key="index"
              class="cursor-pointer bg-blue-50 hover:bg-blue-100 text-blue-700 hover:text-blue-900 text-sm font-medium px-4 py-2 rounded-lg border border-blue-200 transition text-center"
              @click="filterHotels(city)">
              {{ city }}
            </div>
          </div>
        </aside>


        <!-- Danh sách khách sạn -->
        <section class="col-span-1 md:col-span-3 space-y-6">
          <article v-for="(hotel, index) in hotelList" :key="index"
            class="flex flex-col md:flex-row bg-white rounded-3xl border border-indigo-200 shadow-lg hover:shadow-2xl transition overflow-hidden h-auto md:h-80">
            <!-- Hình ảnh -->
            <div class="w-full md:w-1/3 h-64 md:h-full overflow-hidden">
              <img v-if="hotel.image" :src="'http://157.66.101.165:8080' + hotel.image" alt="Hotel Image"
                class="w-full h-full object-cover" />
            </div>

            <!-- Nội dung -->
            <div class="flex-1 p-5 bg-gradient-to-r from-white to-indigo-50">
              <div>
                <h2 class="text-xl font-bold text-indigo-700 hover:text-indigo-900 transition cursor-pointer">
                  {{ hotel.name }}
                </h2>
                <p class="text-gray-600 text-sm mt-1 italic">{{ hotel.description }}</p>

                <p class="mt-2 text-indigo-600 font-medium">Địa điểm: {{ hotel.address }}</p>

                <!-- Badge tiện ích -->
                <div class="mt-3 flex flex-wrap gap-2">
                  <span v-for="(amenity, i) in hotel.amenities" :key="i"
                    class="bg-indigo-100 text-indigo-700 px-3 py-1 rounded-full text-xs font-semibold shadow">
                    {{ amenity }}
                  </span>
                </div>
              </div>

              <div class="mt-5 text-right">
                <button @click="viewHotelDetail(hotel.hotel_id)"
                  class="bg-gradient-to-r from-purple-500 to-indigo-500 hover:from-purple-600 hover:to-indigo-600 text-white px-6 py-2 rounded-xl font-medium shadow-md hover:shadow-lg transition">
                  Xem chi tiết
                </button>
              </div>
            </div>
          </article>
        </section>


      </main>
    </div>
    <div class="container mx-auto px-4 py-10 text-center">
      <h2 class="text-3xl font-bold mb-6">Dịch Vụ Đi Kèm</h2>
      <p class="text-gray-600 mb-8">
        Chúng tôi cung cấp nhiều tiện ích để nâng cao trải nghiệm của bạn.
      </p>
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 pb-20">
        <div v-for="service in services" :key="service.id"
          class="relative overflow-hidden rounded-2xl shadow-lg bg-cover bg-center h-48 transition-transform duration-300 ease-in-out transform hover:scale-105 hover:brightness-110"
          :style="{ backgroundImage: `url(${service.image})` }">
          <div
            class="absolute inset-0 bg-black bg-opacity-50 flex flex-col justify-center items-center text-white p-4 transition-opacity duration-300 hover:bg-opacity-30">
            <h3 class="text-xl font-semibold mb-2">{{ service.title }}</h3>
            <p class="text-sm">{{ service.description }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { defineComponent } from 'vue'
import { Swiper, SwiperSlide } from 'swiper/vue'
import 'swiper/css'
import 'swiper/css/navigation'
import { Navigation, Autoplay } from 'swiper/modules'
import 'swiper/css/autoplay'
import { useRouter } from 'vue-router'
import { getHotelListApi } from '@/services/home'

const hotelList = ref([])
const displayCount = ref(6)
const isExpanded = ref(false)
const router = useRouter()

const fetchHotelList = async () => {
  try {
    const response = await getHotelListApi();
    hotelList.value = response.content;
    console.log('Hotel list response:', response.content);
  } catch (error) {
    console.error('Error fetching hotel list:', error);
  }
};
const viewHotelDetail = (hotelId) => {
  console.log('Hotel ID clicked:', hotelId)
  router.push({ name: 'HotelDetail', params: { id: hotelId } })
}
onMounted(() => {
  fetchHotelList()
})
const services = ref([
  {
    id: 1,
    title: 'Spa',
    description: 'Thư giãn với các liệu pháp spa chuyên nghiệp.',
    image: '/assets/images/spa.jpg',
  },
  {
    id: 2,
    title: 'Massage',
    description: 'Massage trị liệu giúp bạn thư giãn.',
    image: '/assets/images/massage.jpg',
  },
  {
    id: 3,
    title: 'Golf',
    description: 'Sân golf đạt tiêu chuẩn quốc tế.',
    image: '/assets/images/golf.jpg',
  },
  {
    id: 4,
    title: 'Tennis',
    description: 'Sân tennis chất lượng cao.',
    image: '/assets/images/tennis.jpg',
  },
  {
    id: 5,
    title: 'Gym',
    description: 'Phòng tập với trang thiết bị hiện đại.',
    image: '/assets/images/gym.jpg',
  },
  {
    id: 6,
    title: 'Bể bơi',
    description: 'Hồ bơi ngoài trời với không gian rộng rãi.',
    image: '/assets/images/hoboi.jpg',
  },
  {
    id: 7,
    title: 'Nhà hàng',
    description: 'Thưởng thức ẩm thực đặc sắc.',
    image: '/assets/images/restaurant.jpg',
  },
  {
    id: 8,
    title: 'Dịch vụ đưa đón',
    description: 'Dịch vụ xe đưa đón tận nơi.',
    image: '/assets/images/car.jpg',
  },
])

</script>

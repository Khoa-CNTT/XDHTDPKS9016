<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-100 via-white to-purple-100 py-10">
    <div class="container mx-auto px-4">
      <main class="grid grid-cols-1 md:grid-cols-4 gap-6">
        <!-- Sidebar -->
        <aside class="col-span-1 bg-white rounded-3xl shadow-lg p-5 border border-blue-200">
          <h2 class="text-xl font-bold text-blue-700 mb-5">Địa điểm</h2>
          <div class="space-y-3">
            <div
              v-for="(city, index) in cities"
              :key="index"
              class="cursor-pointer bg-blue-50 hover:bg-blue-100 text-blue-700 hover:text-blue-900 text-sm font-medium px-4 py-2 rounded-lg border border-blue-200 transition text-center"
              @click="filterHotels(city)"
            >
              {{ city }}
            </div>
          </div>
        </aside>

        <!-- Danh sách khách sạn -->
        <section class="col-span-1 md:col-span-3 space-y-6">
          <article
            v-for="(hotel, index) in filteredHotels"
            :key="index"
            class="flex flex-col md:flex-row bg-white rounded-3xl border border-indigo-200 shadow-lg hover:shadow-2xl transition overflow-hidden h-80"
          >
            <!-- Hình ảnh -->
            <img
              :src="hotel.image || '/assets/images/img-hotel-2.png'"
              :alt="hotel.name"
              class="w-full md:w-80 h-64 md:h-full object-cover rounded-t-3xl md:rounded-l-3xl md:rounded-tr-none"
            />

            <!-- Nội dung -->
            <div class="flex-1 p-5 bg-gradient-to-r from-white to-indigo-50">
              <div>
                <h2
                  class="text-xl font-bold text-indigo-700 hover:text-indigo-900 transition cursor-pointer"
                >
                  {{ hotel.name }}
                </h2>
                <p class="text-gray-600 text-sm mt-1 italic">{{ hotel.description }}</p>

                <p class="mt-2 text-indigo-600 font-medium">Địa điểm: {{ hotel.address }}</p>

                <!-- Badge tiện ích -->
                <div class="mt-3 flex flex-wrap gap-2">
                  <span
                    v-for="(amenity, i) in hotel.amenities"
                    :key="i"
                    class="bg-indigo-100 text-indigo-700 px-3 py-1 rounded-full text-xs font-semibold shadow"
                  >
                    {{ amenity }}
                  </span>
                </div>

                <!-- Rating + đánh giá -->
                <div class="mt-3 flex items-center space-x-2 text-yellow-500 font-semibold">
                  <span>{{ hotel.rating }} ★</span>
                  <span class="text-gray-500 text-sm">({{ hotel.reviews }} đánh giá)</span>
                </div>

                <!-- Giá & số phòng -->
                <div class="mt-3 flex items-center justify-between">
                  <p class="text-lg font-bold text-green-600">{{ hotel.price }}k / đêm</p>
                  <p class="text-sm text-gray-600">Còn lại: {{ hotel.roomsLeft }}</p>
                </div>
              </div>

              <div class="mt-5 text-right">
                <button
                  @click="viewHotelDetail(hotel.id)"
                  class="bg-gradient-to-r from-purple-500 to-indigo-500 hover:from-purple-600 hover:to-indigo-600 text-white px-6 py-2 rounded-xl font-medium shadow-md hover:shadow-lg transition"
                >
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
        <div
          v-for="service in services"
          :key="service.id"
          class="relative overflow-hidden rounded-2xl shadow-lg bg-cover bg-center h-48 transition-transform duration-300 ease-in-out transform hover:scale-105 hover:brightness-110"
          :style="{ backgroundImage: `url(${service.image})` }"
        >
          <div
            class="absolute inset-0 bg-black bg-opacity-50 flex flex-col justify-center items-center text-white p-4 transition-opacity duration-300 hover:bg-opacity-30"
          >
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
    const response = await getHotelListApi()
    hotelList.value = response.content
  } catch (error) {
    void error
  }
}

onMounted(() => {
  fetchHotelList()
})
onMounted(() => {
  fetchHotelList()
})
const viewHotelDetail = (hotelId) => {
  router.push({ name: 'HotelDetail', params: { id: hotelId } })
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

const selectedCity = ref(null)
const filteredHotels = computed(() => {
  return selectedCity.value
    ? hotels.value.filter((h) => h.city === selectedCity.value)
    : hotels.value
})

const cities = computed(() => {
  return [...new Set(hotels.value.map((h) => h.city))]
})

const filterHotels = (city) => {
  selectedCity.value = city
}
</script>

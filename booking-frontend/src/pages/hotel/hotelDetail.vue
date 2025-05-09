<template>
  <div v-if="hotel" class="p-8 bg-gradient-to-r from-gray-100 to-blue-100">
    <div class="container mx-auto px-8 grid grid-cols-1 md:grid-cols-3 gap-6 py-2">
      <div class="md:col-span-2 space-y-6">
        <div class="bg-white p-6 rounded-lg shadow-lg text-center">
          <h1 class="text-2xl font-bold text-text flex items-center justify-center gap-2">
            <i class="fas fa-hotel"></i> {{ hotel.name }}
          </h1>
          <p class="text-text flex items-center justify-center gap-2 mt-2">
            <i class="fas fa-map-marker-alt"></i>{{ hotel.address }}
          </p>
        </div>
        <div class="flex gap-4">
          <div class="w-2/3">
            <img src="/assets/images/rooms.webp" class="w-full h-64 object-cover rounded-lg shadow-md">
          </div>
          <div class="w-1/3 grid grid-cols-2 gap-2">
            <img src="/assets/images/img-hotel-6.jpeg"
              class="w-full h-32 object-cover rounded-lg hover:scale-105 transition">
            <img src="/assets/images/restaurant.jpg"
              class="w-full h-32 object-cover rounded-lg hover:scale-105 transition">
            <img src="/assets/images/tennis.jpg" class="w-full h-32 object-cover rounded-lg hover:scale-105 transition">
            <img src="/assets/images/massage.jpg"
              class="w-full h-32 object-cover rounded-lg hover:scale-105 transition">
          </div>
        </div>
        <div class="bg-white p-6 rounded-lg shadow-lg">
          <h2 class="text-xl font-bold text-text">Giới thiệu</h2>
          <div class="w-12 h-1 bg-text my-2"></div>
          <p class="text-text leading-relaxed">
            {{ hotel.description }}

          </p>
          <div class="bg-white p-6 rounded-lg shadow-lg pb-2" v-if="hotel?.services?.length">
            <h2 class="text-xl font-bold text-blue-700">Dịch vụ & Tiện ích</h2>
            <ul class="list-disc pl-5 text-gray-700">
              <li v-for="(service, index) in hotel.services" :key="index">
                {{ service.service_name }}
              </li>
            </ul>
          </div>
          <!-- <div class="bg-white p-6 rounded-lg shadow-lg pb-2" v-if="hotel?.roomTypes?.length">
            <h2 class="text-xl font-bold text-blue-700">Các loại phòng</h2>
            <ul class="list-disc pl-5 text-gray-700">
              <li v-for="(roomTypes, index) in hotel.roomTypes" :key="index">
                {{ roomTypes.type_name }}
                <p>Số lượng:{{ roomTypes.number_bed }}</p>
                <p>Số lượng người tối đa:{{ roomTypes.maximum_people }}</p>
                <p>Mô tả:{{ roomTypes.description }}</p>
                <p>Phòng trống:{{ roomTypes.available_room }}</p>
              </li>

            </ul>
          </div> -->

        </div>
      </div>

      <div class="bg-white p-6 rounded-lg shadow-lg">
        <h2 class="text-xl font-bold text-text">Khách sạn tương tự</h2>
        <div class="w-12 h-1 bg-text my-2"></div>
        <div class="space-y-4">
          <!-- Lặp qua danh sách khách sạn từ API -->
          <div v-for="(hotel, index) in hotelList.slice(1, showAllHotels ? hotelList.length : 4)"
            class="bg-white rounded-lg shadow-md hover:scale-105 transition cursor-pointer ">
            <img :src="hotel.image || '/assets/images/img-hotel-6.jpeg'" class="w-full h-32 object-cover rounded-t-lg"
              :alt="hotel.name">
            <h3
              class="text-center text-text font-semibold p-3 hover:text-blue-600 cursor-pointer transition-colors duration-200">
              {{ hotel.name }}</h3>
            <p class="text-center text-text font-semibold p-1">{{ hotel.address }}</p>
          </div>
          <div v-if="hotelList.length > 4">
            <button @click="toggleShowAllHotels"
              class="w-full text-blue-600 font-semibold py-2 border-gray-300 hover:bg-gray-100">
              {{ showAllHotels ? 'Ẩn bớt' : 'Xem thêm' }}
            </button>
          </div>
        </div>
      </div>

    </div>

    <div class="container mx-auto px-8">
      <h2 class="text-xl font-bold text-text">Phòng</h2>
      <div class="w-12 h-1 bg-blue-700 mb-6"></div>
      <swiper :modules="[Navigation, Pagination, Autoplay]" :slides-per-view="5" :space-between="16"
        :autoplay="{ delay: 500, disableOnInteraction: false }" :loop="true" class="pb-4">
        <swiper-slide v-for="(room, index) in rooms" :key="index">
          <div class="bg-white rounded-lg shadow-md hover:scale-105 transition">
            <img :src="room.image" class="w-full h-32 object-cover rounded-t-lg">
            <h3 class="text-center text-text font-semibold p-3">{{ room.name }}</h3>
          </div>
        </swiper-slide>
      </swiper>
    </div>
    <div class="container mx-auto px-8 pt-4" v-if="hotel?.roomTypes?.length">
      <h2 class="text-xl font-bold text-text">Bảng giá phòng</h2>
      <div class="w-12 h-1 bg-blue-700 mb-6"></div>
      <table class="w-full border-collapse border border-gray-800 mt-2 text-sm md:text-base text-center">
        <thead>
          <tr class="bg-gray-200 text-gray-900">
            <th class="p-3 border border-gray-800">Loại phòng</th>
            <th class="p-3 border border-gray-800">Giá (VND)</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(room, index) in hotel.roomTypes" :key="index" class="border-b border-gray-800">
            <td class="p-3 border border-gray-800">{{ room.type_name }}</td>
            <td class="p-3 text-blue-700 border border-gray-800">{{ room.price }} VND</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="container mx-auto px-8 pt-4" v-if="hotel?.roomTypes?.length">
      <h2 class="text-2xl font-bold text-blue-700 mb-4">Các loại phòng</h2>
      <div class="w-full border text-sm md:text-base flex flex-wrap gap-6 p-6 bg-gray-100 rounded-lg">
        <div v-for="(room, index) in hotel.roomTypes" :key="index"
          class="w-full md:w-[32%] bg-white border rounded-lg shadow-md overflow-hidden flex flex-col">
          <img :src="room.room_image || require('@/assets/images/room-1.jpg')" :alt="room.type_name"
            class="w-full h-48 object-cover" />
          <div class="p-4 flex flex-col justify-between h-full">
            <div>
              <h2 class="text-xl font-semibold mb-2 text-blue-800">{{ room.type_name }}</h2>
              <p class="text-gray-600 mb-2">{{ room.description }}</p>
              <ul class="text-sm text-gray-600 list-disc list-inside mb-4">
                <li>🛏️ Số giường: {{ room.number_bed }}</li>
                <li>👥 Tối đa: {{ room.maximum_people }} người</li>
                <li>📦 Phòng trống: {{ room.available_room }}</li>
              </ul>
            </div>
            <button  @click="showBookingModal = true"
              class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 w-full mt-auto">
              Đặt Phòng
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="container mx-auto px-8 pt-4">
      <h2 class="text-xl font-bold text-text">Đánh giá khách hàng</h2>
      <div class="w-12 h-1 bg-blue-700 mb-6"></div>

      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div v-for="review in reviews" :key="review.id"
          class="bg-white p-6 rounded-lg shadow-lg hover:shadow-xl transition-all">
          <div class="flex items-center space-x-3 mb-4">
            <div
              class="w-12 h-12 bg-blue-500 text-white rounded-full flex items-center justify-center text-lg font-semibold">
              {{ review.name.charAt(0) }}
            </div>
            <div>
              <p class="font-bold text-lg text-gray-900">{{ review.name }}</p>
              <p class="text-sm text-gray-500">{{ review.date }}</p>
            </div>
          </div>
          <p class="text-gray-600 italic">"{{ review.comment }}"</p>
          <div class="mt-3 flex justify-center">
            <span v-for="star in review.rating" :key="star" class="text-yellow-400 text-lg">★</span>
          </div>
        </div>
      </div>
    </div>

    <div class="container mx-auto px-8 pt-4">
      <iframe class="w-full h-72 md:h-96 rounded-lg shadow-lg" src="https://www.google.com/maps/embed?..."
        allowfullscreen></iframe>
    </div>

    <div class="container mx-auto  p-6  my-6 text-white text-center">
      <div class="bg-white text-gray-800 p-5 rounded-lg shadow-md">
        <h2 class="text-2xl font-bold tracking-wide mb-4 flex items-center justify-center gap-2">
          🎁 Ưu đãi & Khuyến mãi 🎉
        </h2>
        <ul class="space-y-3">
          <li class="flex items-center gap-3">
            <span class="text-green-500 text-xl">✅</span> Giảm <span class="text-red-500 font-bold">20%</span> khi đặt
            phòng online
          </li>
          <li class="flex items-center gap-3">
            <span class="text-green-500 text-xl">✅</span> Tặng <span class="text-blue-500 font-bold">buffet sáng miễn
              phí</span>
          </li>
          <li class="flex items-center gap-3">
            <span class="text-green-500 text-xl">✅</span> Check-in sớm & check-out muộn miễn phí
          </li>
          <li class="flex items-center gap-3">
            <span class="text-green-500 text-xl">✅</span> Nhận ưu đãi khi đặt từ 2 đêm trở lên
          </li>
        </ul>
      </div>
    </div>
    <BookingModal v-if="showBookingModal" :show="showBookingModal" @close="showBookingModal = false" />
    <router-link
  :to="{ name: 'HotelReview', params: { id: hotelId } }"
  class="inline-block"
>
  <button
    class="px-4 py-2 bg-gradient-to-r from-indigo-500 to-purple-600 text-white font-semibold rounded-lg shadow hover:scale-105 hover:shadow-lg transition-transform duration-200"
  >
    Đánh Giá
  </button>
</router-link>
    <router-view />
  </div>
</template>
<script setup>
import BookingModal from '@/pages/hotel/BookingModal.vue';
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { getHotelByIdApi } from '@/services/home';
import { getHotelListApi } from '@/services/home';

const route = useRoute();
const hotel = ref(null);
const hotelList = ref([]);
const displayCount = ref(6);
const isExpanded = ref(false);
const router = useRouter();
const showBookingModal = ref(false)
const showAllHotels = ref(false);
const hotelId = route.params.id
onMounted(async () => {
  const hotelId = route.params.id;
  try {
    const response = await getHotelByIdApi(hotelId);
    hotel.value = response;
    console.log('----////', hotel.value);

  } catch (error) {
    console.error('Error fetching hotel details:', error);
  }
});

const fetchHotelList = async () => {
  try {
    const response = await getHotelListApi();
    hotelList.value = response.content;
    console.log('Hotel list response:', response.content);
  } catch (error) {
    console.error('Error fetching hotel list:', error);
  }
};

const toggleShowAllHotels = () => {
  showAllHotels.value = !showAllHotels.value;
};
onMounted(() => {
  fetchHotelList();
});














import { Swiper, SwiperSlide } from 'swiper/vue';
import { Navigation, Pagination, Autoplay } from 'swiper/modules';
import 'swiper/css';
import 'swiper/css/navigation';
import 'swiper/css/pagination';
import 'swiper/css/autoplay';

const showModal = ref(false)
const selectedRoom = ref(null)

const openModal = (room) => {
  selectedRoom.value = room
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
  selectedRoom.value = null
}

const roomsType = [
  {
    id: 1,
    name: 'Phòng Deluxe',
    description: 'Phòng hiện đại dành cho 2 người, có ban công và ánh sáng tự nhiên.',
    features: [
      'Giường đôi cao cấp',
      'Máy lạnh, tủ lạnh mini',
      'TV màn hình phẳng',
      'Wi-Fi miễn phí'
    ],
    image: '/assets/images/room-4.jpg'
  },
  {
    id: 2,
    name: 'Phòng Suite',
    description: 'Không gian rộng rãi, có phòng khách riêng và tầm nhìn đẹp.',
    features: [
      'Bồn tắm & phòng tắm đứng',
      'Ghế sofa & bàn làm việc',
      'Mini Bar cao cấp',
      'Bữa sáng miễn phí'
    ],
    image: '/assets/images/room-5.jpg'
  },
  {
    id: 3,
    name: 'Phòng VIP',
    description: 'Dành cho khách hàng cao cấp với không gian sang trọng đẳng cấp.',
    features: [
      'Phòng rộng >60m²',
      'Dịch vụ đưa đón sân bay',
      'Phòng tắm đá cẩm thạch',
      'Hồ bơi riêng (nếu có)'
    ],
    image: '/assets/images/room-6.jpg'
  }
]
const rooms = [
  { name: "Phòng A", image: "/assets/images/room-1.jpg" },
  { name: "Phòng B", image: "/assets/images/room-2.jpg" },
  { name: "Phòng C", image: "/assets/images/room-3.jpg" },
  { name: "Phòng D", image: "/assets/images/room-4.jpg" },
  { name: "Phòng E", image: "/assets/images/room-5.jpg" },
  { name: "Phòng F", image: "/assets/images/room-6.jpg" },
  { name: "Phòng G", image: "/assets/images/room-7.jpg" },
  { name: "Phòng F", image: "/assets/images/room-8.jpg" },
  { name: "Phòng G", image: "/assets/images/room-10.jpg" },
];
const roomPrices = ref([
  { type: "Phòng Deluxe", price: "1,500,000" },
  { type: "Phòng Suite", price: "2,500,000" },
  { type: "Phòng VIP", price: "5,000,000" }
]);
const reviews = ref([
  { id: 1, name: "Nguyễn Văn A", comment: "Khách sạn rất đẹp và tiện nghi. Dịch vụ tuyệt vời!" },
  { id: 2, name: "Trần Thị B", comment: "Phòng sạch sẽ, view đẹp, nhân viên nhiệt tình. Sẽ quay lại!" },
  { id: 3, name: "Lê Văn C", comment: "Giá cả hợp lý, ăn sáng ngon, vị trí thuận tiện." }
]);
</script>
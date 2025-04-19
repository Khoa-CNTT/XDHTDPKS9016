<template>
  <div class="content relative bg-gradient-primary">
    <div class="hero text-center min-h-[350px] flex flex-col justify-center bg-text">
      <h1 class="text-3xl text-white font-bold">Tìm & đặt phòng khách sạn với <br />các bước đơn giản!</h1>
      <p class="text-lg text-white mt-2">Khám phá ngay những ưu đãi tốt nhất dành cho bạn tại <b>Booking</b>!</p>
    </div>

    <div class="search-box grid grid-cols-1 md:grid-cols-3 gap-4 p-4 bg-white shadow-lg 
  w-10/12 md:w-1/2 mx-auto absolute top-80 left-0 right-0 border border-gray-300 rounded-lg">
  
  <!-- Địa điểm -->
  <div class="input-group flex flex-col">
    <label class="font-semibold mb-1">Địa điểm / Tên khách sạn</label>
    <input type="text" v-model="location" placeholder="Ví dụ: Đà Nẵng, Khách sạn Mường Thanh"
      class="border p-2 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-300" />
  </div>

  <!-- Loại khách sạn -->
  <div class="input-group flex flex-col">
    <label class="font-semibold mb-1">Loại khách sạn</label>
    <select v-model="hotelType" class="border p-2 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-300">
      <option value="">Tất cả</option>
      <option value="resort">Resort</option>
      <option value="hotel">Khách sạn</option>
      <option value="homestay">Homestay</option>
    </select>
  </div>

  <!-- Đánh giá sao -->
  <div class="input-group flex flex-col">
    <label class="font-semibold mb-1">Đánh giá</label>
    <select v-model="rating" class="border p-2 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-300">
      <option value="">Tất cả</option>
      <option value="5">5 sao</option>
      <option value="4">4 sao trở lên</option>
      <option value="3">3 sao trở lên</option>
    </select>
  </div>

  <!-- Nút tìm kiếm -->
  <div class="md:col-span-3 flex justify-end">
    <button @click="searchHotels"
      class="w-full md:w-auto bg-orange-400 text-white py-2 px-6 rounded-md hover:bg-orange-300 transition-all">
      Tìm kiếm
    </button>
  </div>
</div>



    <div v-if="groupedHotels.length > 0" class="container mx-auto p-4 pt-32">
      <div v-for="group in groupedHotels" :key="group.location" class="mb-8 ">
        <h2 class="text-2xl font-bold mb-4">{{ group.location }}</h2>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          <div v-for="hotel in group.hotels" :key="hotel.id" class="border p-4 rounded shadow">
            <img :src="hotel.image" class="w-full h-48 object-cover mb-2" />
            <h3 class="text-lg font-bold">{{ hotel.name }}</h3>
            <p class="text-gray-600">{{ hotel.description }}</p>
            <button class="mt-2 px-4 py-2 bg-blue-600 text-white rounded" @click="goToDetail(hotel.id)">
              Xem chi tiết
            </button>
          </div>
        </div>
      </div>
    </div>

    <p v-else class="text-center text-red-500 mt-4">Không tìm thấy khách sạn phù hợp</p>

    <div class="fixed bottom-6 right-6 z-50">
      <button @click="toggleChat"
        class="bg-blue-600 text-white p-5 rounded-full shadow-lg hover:bg-blue-500 transition-all duration-300 animate-shake">
        <Icon icon="lucide:message-circle" class="h-8 w-8" />
      </button>
    </div>
    <!-- Chat Form -->
    <transition name="fade-slide">
      <div v-if="showChat"
        class="fixed bottom-20 right-6 w-80 bg-white shadow-lg rounded-lg border border-gray-200 z-50">
        <div class="p-4 border-b font-semibold bg-blue-600 text-white rounded-t-lg">
          Chat với chúng tôi
          <button @click="toggleChat" class="float-right text-white font-bold">✕</button>
        </div>
        <div class="p-4 max-h-60 overflow-y-auto text-sm text-gray-800">
          <p>Xin chào! Tôi có thể giúp gì cho bạn?</p>
        </div>
        <div class="p-2 border-t">
          <input type="text" v-model="chatMessage" placeholder="Nhập tin nhắn..."
            class="w-full border rounded p-2 text-sm" />
        </div>
      </div>
    </transition>


    <div class="container mx-auto p-4 pt-32">
      <h1 class="text-2xl text-text font-bold mb-2 uppercase">Điểm đến nổi bật</h1>
      <hr class="mb-4 border-t-2 border-text" />
      <div class="grid grid-cols-2 gap-4 mb-4">
        <div v-for="(city, index) in topCities" :key="index" class="relative">
          <img :src="city.image" :alt="city.name" class="w-full h-48 object-cover rounded-lg" />
          <div class="absolute top-0 left-0 bg-black bg-opacity-50 text-white text-sm p-1 rounded flex">
            <img :src="city.img" :alt="city.name" class="w-8 h-5 object-cover rounded-lg" />
            {{ city.name }}
          </div>
        </div>
      </div>
      <div class="grid grid-cols-3 gap-4 pb-30">
        <div v-for="(city, index) in bottomCities" :key="index" class="relative">
          <img :src="city.image" :alt="city.name" class="w-full h-32 object-cover rounded" />
          <div class="absolute top-0 left-0 bg-black bg-opacity-50 text-white text-sm p-1 rounded flex">
            <img :src="city.img" :alt="city.name" class="w-8 h-5 object-cover rounded-lg" />
            {{ city.name }}
          </div>
        </div>
      </div>
    </div>
    <div class="container mx-auto p-4">
      <h1 class="text-2xl text-text font-bold mb-2 uppercase">Khách sạn nổi bật</h1>
      <hr class="mb-4 border-t-2 border-text" />
      <div class="grid grid-cols-2 md:grid-cols-3 gap-4">
        <div v-for="hotel in hotel" :key="hotel.id" class="bg-white p-3 shadow-md rounded-lg">
          <img :src="hotel.image" alt="Hotel Image" class="w-full h-48 object-cover rounded-md" />
          <div class="flex justify-between mt-2">
            <div>
              <h2 class="text-lg font-semibold">{{ hotel.name }}</h2>
              <p class="text-sm text-gray-600">{{ hotel.description }}</p>
            </div>
            <button class="mt-2 px-4 py-2 bg-text text-white rounded-md self-start">Xem chi tiết</button>
          </div>
        </div>
      </div>
    </div>

    <div class="container mx-auto p-4 text-center">
      <hr class="mb-4 border-t-2 border-text" />
      <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
        <div v-for="about in about" :key="about.id" class="p-6 flex flex-col items-center">
          <img :src="about.image" alt="Hotel Image" class="w-24 h-24 object-cover rounded-full mb-4" />
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
        <p class="text-gray-600">Tiếp cận hàng triệu khách hàng tiềm năng và nâng tầm doanh nghiệp của bạn với chúng
          tôi.</p>
      </div>
      <div class="md:w-1/2 flex justify-center mt-6 md:mt-0">
        <img src="/assets/images/rooms.webp" alt="Hotel Image" class="rounded-lg shadow-lg" />
      </div>
    </div>

  </div>

</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router'

const showChat = ref(false)
const chatMessage = ref('')

function toggleChat() {
  showChat.value = !showChat.value
}
const router = useRouter()

const location = ref('')
const checkIn = ref('')
const checkOut = ref('')
const quantity = ref(1)
const hotels = ref([]);
const defaultHotels = [
  {
    id: 1,
    location: 'Đà Nẵng',
    hotels: [
      {
        id: 101,
        name: 'Seaside Villa',
        image: "/assets/images/img-hotel-6.jpeg",
        description: 'Villa gần biển, phù hợp gia đình',
        available: true,
        maxGuests: 6,
        dates: ['2025-04-10', '2025-04-11', '2025-04-12']
      },
      {
        id: 102,
        name: 'Beachfront Paradise',
        image: "/assets/images/img-hotel-12.jpeg",
        description: 'Hướng biển, bãi tắm riêng',
        available: true,
        maxGuests: 5,
        dates: ['2025-04-11', '2025-04-13']
      },
    ]
  },
  {
    id: 2,
    location: 'Hà Nội',
    hotels: [
      {
        id: 201,
        name: 'Sunrise Hotel',
        image: "/assets/images/img-hotel-9.jpeg",
        description: 'Khách sạn trung tâm Hà Nội, tiện nghi, giá tốt',
        available: true,
        maxGuests: 4,
        dates: ['2025-04-10', '2025-04-11', '2025-04-12']
      },
      {
        id: 202,
        name: 'Old Quarter Hotel',
        image: "/assets/images/img-hotel-10.jpeg",
        description: 'Khách sạn gần phố cổ, thuận tiện di chuyển',
        available: true,
        maxGuests: 3,
        dates: ['2025-04-10', '2025-04-12']
      }
    ]
  },
  {
    id: 3,
    location: 'Đà Lạt',
    hotels: [
      {
        id: 301,
        name: 'Mountain Retreat',
        image: "/assets/images/img-hotel-8.jpeg",
        description: 'Nghỉ dưỡng trong rừng thông, yên bình',
        available: true,
        maxGuests: 2,
        dates: ['2025-04-11', '2025-04-14']
      },
      {
        id: 302,
        name: 'Pine Valley Homestay',
        image: "/assets/images/img-hotel-7.jpeg",
        description: 'Homestay ấm cúng giữa rừng thông',
        available: true,
        maxGuests: 3,
        dates: ['2025-04-10', '2025-04-13']
      },
    ]
  }
];


const groupedHotels = ref([])

const searchHotels = () => {
  groupedHotels.value = []
  const results = []

  for (const group of defaultHotels) {
    const filteredHotels = group.hotels.filter(hotel => {
      const matchLocation = hotel.name.toLowerCase().includes(location.value.toLowerCase()) ||
        group.location.toLowerCase().includes(location.value.toLowerCase())
      const matchGuest = quantity.value <= hotel.maxGuests
      const matchDate = checkIn.value && checkOut.value
        ? hotel.dates.includes(checkIn.value) && hotel.dates.includes(checkOut.value)
        : true

      return matchLocation && matchGuest && matchDate
    })

    if (filteredHotels.length > 0) {
      results.push({
        location: group.location,
        hotels: filteredHotels
      })
    }
  }

  groupedHotels.value = results
  hotels.value = results.flatMap(g => g.hotels)
}
// onMounted(() => {
//   hotels.value = [
//     { id: 1, name: "Hotel A", description: "Mô tả khách sạn A", image: "/assets/images/img-hotel-6.jpeg" },
//     { id: 2, name: "Hotel B", description: "Mô tả khách sạn B", image: "/assets/images/img-hotel-7.jpeg" },
//     { id: 3, name: "Hotel C", description: "Mô tả khách sạn C", image: "/assets/images/img-hotel-8.jpeg" },
//     { id: 4, name: "Hotel D", description: "Mô tả khách sạn D", image: "/assets/images/img-hotel-9.jpeg" },
//     { id: 5, name: "Hotel E", description: "Mô tả khách sạn E", image: "/assets/images/img-hotel-10.jpeg" },
//     { id: 6, name: "Hotel F", description: "Mô tả khách sạn F", image: "/assets/images/img-hotel-12.jpeg" }
//   ];
// });

const goToDetail = (hotelId) => {
  router.push({ name: 'HotelDetail', params: { id: hotelId } })
}
const topCities = ref([
  { name: 'Hồ Chí Minh', image: '/assets/images/1.png', img: '/assets/images/coVietNam.jpg' },
  { name: 'Đà Nẵng', image: '/assets/images/2.jpg', img: '/assets/images/coVietNam.jpg' },
]);

const bottomCities = ref([
  { name: 'Đà Nẵng', image: '/assets/images/3.jpg', img: '/assets/images/coVietNam.jpg' },
  { name: 'Hội An', image: '/assets/images/4.jpg', img: '/assets/images/coVietNam.jpg' },
  { name: 'Thừa Thiên Huế', image: '/assets/images/5.jpg', img: '/assets/images/coVietNam.jpg' },
]);
const hotel = [
  { id: 1, name: "Hotel A", description: "Mô tả khách sạn A", image: "/assets/images/img-hotel-6.jpeg" },
  { id: 2, name: "Hotel B", description: "Mô tả khách sạn B", image: "/assets/images/img-hotel-7.jpeg" },
  { id: 3, name: "Hotel C", description: "Mô tả khách sạn C", image: "/assets/images/img-hotel-8.jpeg" },
  { id: 4, name: "Hotel D", description: "Mô tả khách sạn D", image: "/assets/images/img-hotel-9.jpeg" },
  { id: 5, name: "Hotel E", description: "Mô tả khách sạn E", image: "/assets/images/img-hotel-10.jpeg" },
  { id: 6, name: "Hotel F", description: "Mô tả khách sạn F", image: "/assets/images/img-hotel-12.jpeg" }
];
const about = [
  { id: 1, name: "Tìm dễ dàng", description: "Tìm trong số 5 triệu khách sạn chỉ sau vài giây.", image: "/assets/images/Search.svg" },
  { id: 2, name: "Tự tin so sánh", description: "So sánh giá phòng từ nhiều trang web cùng lúc.", image: "/assets/images/Compare.svg" },
  { id: 3, name: "Tiết kiệm nhiều", description: "Khám phá ưu đãi hấp dẫn dễ đặt trên trang web đối tác của chúng tôi.", image: "/assets/images/Save.svg" }
];
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
<template>
  <div class="p-8 bg-gradient-to-r from-gray-100 to-blue-100">
    <div class="container mx-auto px-8 grid grid-cols-1 md:grid-cols-3 gap-6 py-2">
      <div class="md:col-span-2 space-y-6">
        <div class="bg-white p-6 rounded-lg shadow-lg text-center">
          <h1 class="text-2xl font-bold text-text flex items-center justify-center gap-2">
            <i class="fas fa-hotel"></i> Tên khách sạn
          </h1>
          <p class="text-text flex items-center justify-center gap-2 mt-2">
            <i class="fas fa-map-marker-alt"></i> Địa chỉ khách sạn
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
            La Vela Saigon Hotel là khách sạn 5 sao sang trọng, tọa lạc ngay trung tâm TP. Hồ Chí Minh, mang
            đến không gian nghỉ dưỡng hoàn hảo với phong cách thiết kế tinh tế, đẳng cấp. Với vị trí thuận
            lợi, du khách có thể dễ dàng di chuyển đến các địa điểm nổi tiếng như Nhà thờ Đức Bà, Chợ Bến
            Thành hay Phố đi bộ Nguyễn Huệ.
            <br><br>
            Khách sạn cung cấp hệ thống phòng nghỉ đa dạng từ tiêu chuẩn đến cao cấp, mỗi phòng đều được
            thiết kế theo phong cách hiện đại, kết hợp với nội thất sang trọng, tạo nên một không gian nghỉ
            dưỡng thoải mái và tiện nghi. Đặc biệt, phòng có tầm nhìn bao quát thành phố, giúp du khách tận
            hưởng khung cảnh tuyệt đẹp về đêm.
            <br><br>
            Một trong những điểm nhấn của La Vela Saigon Hotel chính là hồ bơi vô cực trên tầng thượng, nơi
            du khách có thể thư giãn trong làn nước mát lạnh và chiêm ngưỡng toàn cảnh thành phố từ trên
            cao. Ngoài ra, khách sạn còn có hệ thống nhà hàng sang trọng phục vụ các món ăn Á - Âu đa dạng,
            quầy bar với không gian lãng mạn, trung tâm spa cao cấp giúp bạn thư giãn sau những giờ làm việc
            căng thẳng.
            <br><br>
            Đối với những ai yêu thích thể thao và chăm sóc sức khỏe, phòng gym hiện đại được trang bị đầy
            đủ thiết bị luyện tập sẽ là một lựa chọn lý tưởng. Bên cạnh đó, trung tâm hội nghị với sức chứa
            lớn, trang bị hệ thống âm thanh, ánh sáng hiện đại sẽ là địa điểm lý tưởng để tổ chức các sự
            kiện, hội nghị hay tiệc cưới đẳng cấp.
            <br><br>
            Đội ngũ nhân viên chuyên nghiệp, tận tình luôn sẵn sàng phục vụ 24/7, đảm bảo mang đến cho du
            khách những trải nghiệm đáng nhớ nhất. Nếu bạn đang tìm kiếm một nơi lưu trú hoàn hảo với dịch
            vụ đẳng cấp, La Vela Saigon Hotel chính là lựa chọn lý tưởng cho bạn.

          </p>
          <div class="bg-white p-6 rounded-lg shadow-lg pb-2">
            <h2 class="text-xl font-bold text-blue-700">Dịch vụ & Tiện ích</h2>
            <ul class="list-disc pl-5 text-gray-700">
              <li>Hồ bơi vô cực</li>
              <li>Trung tâm spa & gym</li>
              <li>Nhà hàng sang trọng</li>
              <li>Phòng hội nghị</li>
            </ul>
          </div>
        </div>
      </div>

      <div class="bg-white p-6 rounded-lg shadow-lg">
        <h2 class="text-xl font-bold text-text">Khách sạn tương tự</h2>
        <div class="w-12 h-1 bg-text my-2"></div>
        <div class="space-y-4">
          <div class="bg-white rounded-lg shadow-md hover:scale-105 transition">
            <img src="/assets/images/img-hotel-6.jpeg" class="w-full h-32 object-cover rounded-t-lg">
            <h3 class="text-center text-text font-semibold p-3">Khách sạn A</h3>
          </div>
          <div class="bg-white rounded-lg shadow-md hover:scale-105 transition">
            <img src="/assets/images/img-hotel-2.png" class="w-full h-32 object-cover rounded-t-lg">
            <h3 class="text-center text-text font-semibold p-3">Khách sạn B</h3>
          </div>
          <div class="bg-white rounded-lg shadow-md hover:scale-105 transition">
            <img src="/assets/images/img-hotel-3.png" class="w-full h-32 object-cover rounded-t-lg">
            <h3 class="text-center text-text font-semibold p-3">Khách sạn C</h3>
          </div>
          <div class="bg-white rounded-lg shadow-md hover:scale-105 transition">
            <img src="/assets/images/img-hotel-4.png" class="w-full h-32 object-cover rounded-t-lg">
            <h3 class="text-center text-text font-semibold p-3">Khách sạn B</h3>
          </div>
          <div class="bg-white rounded-lg shadow-md hover:scale-105 transition">
            <img src="/assets/images/img-hotel-5.png" class="w-full h-32 object-cover rounded-t-lg">
            <h3 class="text-center text-text font-semibold p-3">Khách sạn C</h3>
          </div>
        </div>
      </div>
    </div>

    <div class="container mx-auto px-8 pt-4">
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
    <div class="container mx-auto px-8 pt-4">
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
          <tr v-for="room in roomPrices" :key="room.type" class="border-b border-gray-800">
            <td class="p-3 border border-gray-800">{{ room.type }}</td>
            <td class="p-3 text-blue-700 border border-gray-800">{{ room.price }} VND</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="container mx-auto px-8 pt-4">
    <div class="w-full border mt-2 text-sm md:text-base flex flex-wrap gap-6 p-6 bg-gray-100 rounded-lg">
      <div
        v-for="room in roomsType"
        :key="room.id"
        class="w-full md:w-[32%] bg-white border rounded-lg shadow-md overflow-hidden flex flex-col"
      >
        <img :src="room.image" :alt="room.name" class="w-full h-48 object-cover" />
        <div class="p-4 flex flex-col justify-between h-full">
          <div>
            <h2 class="text-xl font-semibold mb-2 text-blue-800">{{ room.name }}</h2>
            <p class="text-gray-600 mb-2">{{ room.description }}</p>
            <ul class="text-sm text-gray-600 list-disc list-inside mb-4">
              <li v-for="(feature, index) in room.features" :key="index">{{ feature }}</li>
            </ul>
          </div>
          <button
            @click="openModal(room)"
            class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 w-full mt-auto"
          >
            Đặt Phòng
          </button>
        </div>
      </div>
    </div>

    <!-- Modal -->
    <BookingModal :show="showModal" :room="selectedRoom" @close="closeModal" />
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
    <ModalBooking v-model:showModal="showModal" />
  </div>
</template>
<script setup>
import { Swiper, SwiperSlide } from 'swiper/vue';
import { Navigation, Pagination, Autoplay } from 'swiper/modules';
import BookingModal from './BookingModal.vue';
import 'swiper/css';
import 'swiper/css/navigation';
import 'swiper/css/pagination';
import 'swiper/css/autoplay';
import { ref } from 'vue';
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
// Mảng chứa thông tin các phòng
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
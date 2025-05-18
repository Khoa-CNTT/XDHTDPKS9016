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
        </div>
      </div>

      <div class="bg-white p-6 rounded-lg shadow-lg">
        <h2 class="text-xl font-bold text-text">Khách sạn tương tự</h2>
        <div class="w-12 h-1 bg-text my-2"></div>
        <div class="space-y-4">
          <!-- Lặp qua danh sách khách sạn từ API -->
          <div v-for="(hotel, index) in hotelList.slice(0, showAllHotels ? hotelList.length : 4)"
            class="bg-white rounded-lg shadow-md hover:scale-105 transition cursor-pointer ">
            <img v-if="hotel.image" :src="'http://157.66.101.165:8080' + hotel.image" alt="Hotel Image"
              class="w-full h-full object-cover" />
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
    <div class="container mx-auto px-8 pt-4" v-if="hotel?.roomTypes?.length">
      <h2 class="text-xl font-bold text-text">Bảng loại phòng</h2>
      <div class="w-12 h-1 bg-blue-700 mb-6"></div>
      <table class="w-full border-collapse border border-gray-800 mt-2 text-sm md:text-base text-center">
        <thead>
          <tr class="bg-gray-200 text-gray-900">
            <th class="p-3 border border-gray-800">Loại phòng</th>
            <!-- <th class="p-3 border border-gray-800">Giá (VND)</th> -->
          </tr>
        </thead>
        <tbody>
          <tr v-for="(room, index) in hotel.roomTypes" :key="index" class="border-b border-gray-800">
            <td class="p-3 border border-gray-800">{{ room.type_name }}</td>
            <!-- <td class="p-3 text-blue-700 border border-gray-800">{{ room.price }} VND</td> -->
          </tr>
        </tbody>
      </table>
    </div>
    <div class="container mx-auto px-8 pt-4" v-if="hotel?.roomTypes?.length">
      <h2 class="text-2xl font-bold text-blue-700 mb-4">Các loại phòng</h2>
      <div class="w-full border text-sm md:text-base flex flex-wrap gap-6 p-6 bg-gray-100 rounded-lg">
        <div v-for="(room, index) in hotel.roomTypes" :key="index"
          class="w-full sm:w-[48%] md:w-[32%] bg-white border rounded-2xl shadow-lg overflow-hidden flex flex-col hover:shadow-xl transition-shadow">
          <img
            :src="room.room_image ? 'http://157.66.101.165:8080' + room.room_image : 'http://157.66.101.165:8080/api/v1/images/view/b3a94d55-e4a4-42cf-8e9d-9e785d4488a7_room-9.jpg'"
            alt="Hotel Image" class="w-full h-[220px] object-cover rounded-t-2xl" />

          <div class="p-4 flex flex-col justify-between h-full">
            <div>
              <h2 class="text-xl font-semibold mb-2 text-blue-800">{{ room.type_name }}</h2>
              <p class="text-gray-600 mb-2">{{ room.description }}</p>

              <ul class="text-sm text-gray-700 list-disc list-inside mb-2">
                <li>📦 Phòng trống: {{ room.number_room }}</li>
              </ul>

              <ul class="text-sm text-gray-700 list-disc list-inside mb-4">
                <li v-for="r in room.rooms" :key="r.id_room">
                  🛏️ {{ r.number_bed }} giường
                </li>
              </ul>
            </div>

            <button @click="goToRooms"
              class="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 w-full mt-auto">
              Xem Phòng
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
    <div class="container mx-auto  px-8 my-8 text-white text-center">
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
    <div class="container mx-auto px-8">
      <h2 class="text-xl font-bold text-text">Phòng</h2>
      <div class="w-12 h-1 bg-blue-700 mb-6"></div>
    </div>
    <div class="bg-gradient-to-r from-gray-100 to-blue-100 py-1">
      <div class="container mx-auto px-4 md:px-8">
        <div class="flex flex-col md:flex-row gap-8 border border-gray-300 p-6 rounded-xl shadow-lg">
          <!-- Cột trái - Hình ảnh và mô tả khách sạn -->
          <div class="md:w-1/3 bg-white rounded-xl shadow-md p-4">
            <img :src="`http://157.66.101.165:8080${hotel.image}`" alt="Main room image" class="rounded-lg w-full" />
            <div class="flex gap-2 mt-3">
              <img v-for="(roomType, i) in hotel.roomTypes.slice(0, 2)" :key="i"
                :src="`http://157.66.101.165:8080${roomType.room_image}`" class="rounded-md w-1/2" />
            </div>
            <a href="#" class="text-blue-600 text-sm mt-2 inline-block">📷 Xem ảnh và chi tiết</a>

            <h4 class="text-lg font-semibold mt-4 mb-2">🏨 {{ hotel.name }}</h4>
            <p class="text-sm text-gray-700 mb-2">{{ hotel.address }}</p>
            <p class="text-sm text-gray-700">📞 {{ hotel.hotline }}</p>
            <!-- Hiển thị danh sách đánh giá -->
            <div class="mt-4">
              <h5 class="text-md font-semibold mb-2 text-blue-600 text-[20px]">💬 Đánh giá</h5>
              <ul class="space-y-3 max-h-48 overflow-y-auto">
                <li v-for="comment in comments" :key="comment.comment_id" class="border rounded p-3 bg-gray-50">
                  <p class="text-gray-800 mb-1">{{ comment.content }}</p>
                  <div class="text-xs text-gray-500">
                    <span>{{ comment.comment_author || 'Khách ẩn danh' }}</span> ·
                    <span>{{ comment.comment_date }} {{ comment.comment_time }}</span>
                  </div>
                </li>
              </ul>
            </div>
             <!-- Form thêm bình luận -->
    <form @submit.prevent="addComment" class="mt-4">
      <textarea
        v-model="newComment"
        placeholder="Viết bình luận..."
        class="w-full border rounded p-2 resize-none"
        rows="3"
        required
      ></textarea>
      <button
        type="submit"
        class="mt-2 bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
      >
        Gửi bình luận
      </button>
    </form>
          </div>

          <!-- Cột phải - Danh sách các phòng còn chỗ -->
          <div class="md:w-2/3 space-y-6">
            <template v-for="(roomType, index) in hotel.roomTypes" :key="index">
              <template v-for="(room, idx) in roomType.rooms" :key="room.id_room">
                <div
                  class="flex flex-col sm:flex-row border border-gray-300 rounded-lg overflow-hidden bg-white shadow-sm transition duration-300 ease-in-out transform hover:scale-105 hover:shadow-[0_10px_20px_rgba(168,85,247,0.4)] hover:ring-2 hover:ring-purple-300 cursor-pointer">

                  <!-- Thông tin loại phòng -->
                  <div class="sm:w-1/3 p-4 border-b sm:border-b-0 sm:border-r border-gray-300">
                    <strong class="block mb-2">{{ roomType.type_name }}</strong>
                    <ul class="list-none space-y-1 text-sm">
                      <li>🛏️ Số giường: {{ room.number_bed }}</li>
                      <li>{{ roomType.description }}</li>
                      <li>{{ roomType.room_type_id }}</li>
                      <li>✔ Có bữa sáng tuyệt hảo (519.481 ₫ /người)</li>
                      <li>✘ Không hoàn tiền (Giá thấp!)</li>
                      <li>✔ Đặt và trả tiền ngay</li>
                    </ul>
                  </div>

                  <!-- Biểu tượng -->
                  <div
                    class="sm:w-1/12 flex items-center justify-center text-xl border-b sm:border-b-0 sm:border-r border-gray-300">
                    👥 ℹ️
                  </div>

                  <!-- Giá -->
                  <div class="sm:w-1/4 p-4 text-red-700 border-b sm:border-b-0 sm:border-r border-gray-300">
                    <div class="bg-red-100 text-red-600 text-sm font-bold p-1 mb-1">
                      📍 Giá tốt nhất hiện tại
                    </div>
                    <div class="line-through text-gray-600 text-sm">
                      {{ room.price + 100 }} ₫ <span class="text-red-500 ml-1">-20%</span>
                    </div>
                    <div class="text-xl font-bold">{{ room.price.toLocaleString() }} ₫</div>
                    <div class="text-xs text-gray-500">Giá mỗi đêm chưa gồm thuế và phí</div>
                  </div>

                  <!-- Số lượng còn lại -->
                  <div
                    class="sm:w-1/12 flex items-center justify-center border-b sm:border-b-0 sm:border-r border-gray-300">
                    {{ roomType.number_room }}
                  </div>

                  <!-- Nút đặt -->
                  <div class="sm:w-1/4 flex flex-col items-center justify-center px-4 py-2">
                    <button @click="openBooking(room, roomType)"
                      class="bg-blue-600 text-white px-4 py-2 rounded mb-1 w-full">Đặt ngay</button>
                    <div class="text-green-600 text-sm font-semibold">Còn phòng</div>
                  </div>
                </div>
              </template>
            </template>
          </div>
        </div>
      </div>
    </div>
    <BookingModal :show="showBooking" :room="selectedRoom" :hotel="hotel" @close="showBooking = false"
      :roomType="selectedRoomType" />
    <!-- <ListRoom /> -->
    <!-- <BookingModal v-if="showBookingModal" :show="showBookingModal" @close="showBookingModal = false" /> -->
    <router-link :to="{ name: 'HotelReview', params: { id: hotelId } }" class="inline-block">
      <button
        class="px-4 py-2 bg-gradient-to-r from-indigo-500 to-purple-600 text-white font-semibold rounded-lg shadow hover:scale-105 hover:shadow-lg transition-transform duration-200">
        Đánh Giá
      </button>
    </router-link>
    <router-view />
  </div>
</template>
<script setup>
import ListRoom from './ListRoom.vue';
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { getHotelByIdApi } from '@/services/home';
import { getHotelListApi } from '@/services/home';
import BookingModal from '@/pages/hotel/BookingModal.vue';
const route = useRoute();
const hotel = ref(null);
const hotelList = ref([]);
const displayCount = ref(6);
const isExpanded = ref(false);
const router = useRouter();
const showBookingModal = ref(false)
const showAllHotels = ref(false);
const hotelId = route.params.id
const comments = [
  {
    comment_id: 1,
    content: "Phòng rất đẹp và sạch sẽ lamas xzxzc",
    comment_author: null,
    comment_date: "2025-05-17",
    comment_time: "12:42:51",
  },
  {
    comment_id: 2,
    content: "Dịch vụ thân thiện, tiện nghi đầy đủ.",
    comment_author: "Nguyễn Văn A",
    comment_date: "2025-05-15",
    comment_time: "10:20:00",
  },
  {
    comment_id: 3,
    content: "Vị trí khách sạn thuận tiện, dễ đi lại.",
    comment_author: "Trần Thị B",
    comment_date: "2025-05-16",
    comment_time: "14:05:30",
  }
];

onMounted(async () => {
  const hotelId = route.params.id;
  console.log('=======', hotelId);

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
const selectedRoomType = ref(null)
const showBooking = ref(false)
const selectedRoom = ref(null)
const openBooking = (room, roomType) => {
  selectedRoom.value = room
  selectedRoomType.value = roomType // nếu bạn muốn lưu thêm loại phòng

  console.log('Room:', selectedRoom.value)
  console.log('RoomType ID:', roomType.room_type_id)

  showBooking.value = true
}
onMounted(() => {
  fetchHotelList();
});


const goToRooms = () => {
  const hotelId = route.params.hotelId
  showBookingModal.value = true
  router.push({ name: 'HotelRooms' })
}

</script>
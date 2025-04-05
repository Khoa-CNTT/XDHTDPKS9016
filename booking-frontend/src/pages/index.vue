<template>
  <div class="content relative bg-gradient-primary">
    <div class="hero text-center min-h-[350px] flex flex-col justify-center bg-text">
      <h1 class="text-3xl text-white font-bold">Tìm & đặt phòng khách sạn với <br />các bước đơn giản!</h1>
      <p class="text-lg text-white mt-2">Khám phá ngay những ưu đãi tốt nhất dành cho bạn tại <b>Booking</b>!</p>
    </div>

    <div class="search-box grid grid-cols-1 md:grid-cols-5 gap-4 p-4 bg-white shadow-lg 
    w-10/12 md:w-3/4 mx-auto absolute top-80 left-0 right-0 border border-gray-300 rounded-lg">
      <div class="input-group flex flex-col">
        <label class="font-semibold">Địa điểm tên khách sạn</label>
        <input type="text" placeholder="Nhập địa điểm hoặc tên khách sạn" class="border p-2 rounded-md" />
      </div>
      <div class="input-group flex flex-col">
        <label class="font-semibold">Ngày nhận phòng</label>
        <input type="date" class="border p-2 rounded-md" />
      </div>
      <div class="input-group flex flex-col">
        <label class="font-semibold">Ngày trả phòng</label>
        <input type="date" class="border p-2 rounded-md" />
      </div>
      <div class="input-group flex flex-col">
        <label class="font-semibold">Số lượng</label>
        <input type="number" min="1" value="1" class="border p-2 rounded-md" />
      </div>
      <button class="search-btn bg-orange-400 text-white py-1 px-2 text-sm rounded-md hover:bg-orange-300">Tìm
        kiếm</button>
    </div>

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
        <div v-for="hotel in hotels" :key="hotel.id" class="bg-white p-3 shadow-md rounded-lg">
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

const topCities = ref([
  { name: 'Hồ Chí Minh', image: '/assets/images/1.png', img: '/assets/images/coVietNam.jpg' },
  { name: 'Đà Nẵng', image: '/assets/images/2.jpg', img: '/assets/images/coVietNam.jpg' },
]);

const bottomCities = ref([
  { name: 'Đà Nẵng', image: '/assets/images/3.jpg', img: '/assets/images/coVietNam.jpg' },
  { name: 'Hội An', image: '/assets/images/4.jpg', img: '/assets/images/coVietNam.jpg' },
  { name: 'Thừa Thiên Huế', image: '/assets/images/5.jpg', img: '/assets/images/coVietNam.jpg' },
]);
const hotels = [
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

<style lang="scss" scoped></style>
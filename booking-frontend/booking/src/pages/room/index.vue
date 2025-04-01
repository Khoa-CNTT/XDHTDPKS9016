<template>

 <div class="w-full pb-30">
  <div class="max-w-6xl mx-auto bg-white p-6 rounded-lg shadow-lg mb-32">
    <div class="flex items-center gap-2 text-yellow-500">
      <span class="text-lg font-semibold">★★★★★</span>
      <span class="bg-yellow-300 text-sm px-2 py-1 rounded">{{ resort.transport }}</span>
    </div>
    <h1 class="text-2xl font-bold mt-2">{{ resort.name }}</h1>
    <p class="text-blue-600 mt-1"> {{ resort.location }} - <a href="#" class="font-semibold">Vị trí tuyệt vời</a></p>

    <div class="grid grid-cols-3 gap-2 mt-4">
      <div class="col-span-2">
        <img class="w-full h-64 object-cover rounded" :src="resort.images[0]" alt="Resort">
        <div class="grid grid-cols-2 gap-2 mt-2">
          <img v-for="(img, index) in resort.images.slice(1, 3)" :key="index" class="w-full h-32 object-cover rounded"
            :src="img" alt="Resort Image">
        </div>
      </div>

      <div class="bg-gray-100 p-4 rounded-lg w-100">
        <h2 class="text-xl font-semibold">Tuyệt vời <span class="text-blue-600">{{ resort.rating }}</span></h2>
        <p class="text-gray-600">{{ resort.reviews }} đánh giá</p>
        <button class="mt-4 bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 w-full">Đặt ngay</button>
      </div>
    </div>

    <div class="mt-6 p-4 bg-gray-100 rounded-lg">
      <h2 class="text-xl font-bold">Về Mekong Lodge Resort</h2>
      <p class="text-gray-700 mt-2">Mekong Lodge Resort là một khu nghỉ dưỡng ven sông yên bình, mang đến trải nghiệm
        gần gũi với thiên nhiên. Với hồ bơi ngoài trời tuyệt đẹp, phòng nghỉ tiện nghi và không gian thoáng đãng, đây là
        điểm đến lý tưởng cho những ai muốn tận hưởng kỳ nghỉ thư giãn tại miền Tây sông nước.</p>
    </div>

    <div class="mt-6 p-4 bg-gray-100 rounded-lg">
      <h2 class="text-xl font-bold">Tiện ích và dịch vụ</h2>
      <ul class="list-disc list-inside text-gray-700 mt-2">
        <li>Hồ bơi ngoài trời</li>
        <li>WiFi miễn phí</li>
        <li>Xe đưa đón sân bay</li>
        <li>Phòng không hút thuốc</li>
        <li>Trung tâm Spa & chăm sóc sức khỏe</li>
        <li>Dịch vụ phòng</li>
        <li>Nhà hàng</li>
        <li>Chỗ đỗ xe miễn phí</li>
        <li>Quầy bar</li>
      </ul>
    </div>

    <div class="mt-6 p-4 bg-gray-100 rounded-lg ">
      <h2 class="text-xl font-bold">Vị trí trên bản đồ</h2>
      <div class="w-full h-64">
        <iframe width="100%" height="100%" frameborder="0" style="border:0" allowfullscreen="" aria-hidden="false"
          tabindex="0" :src="resort.mapUrl">
        </iframe>
      </div>
    </div>

  </div>
 </div>

</template>
<script setup>
import { ref } from 'vue';
const resort = ref({
  name: "Mekong Lodge Resort",
  transport: "Xe đưa đón sân bay",
  location: "An Hoa, Dong Hoa Hiep, Cái Bè, Việt Nam",
  rating: 8.6,
  reviews: 1072,
  reviewText: "Như về quê, buổi chiều chill chill tắm hồ bơi ngắm hoàng hôn...",
  images: [
    "/assets/images/restaurant.jpg",
    "/assets/images/restaurant.jpg",
    "/assets/images/restaurant.jpg"
  ],
  mapUrl: "https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d61349.64700641605!2d108.16550641911964!3d16.047164814898174!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x314219c792252a13%3A0xfc14e3a044436487!2zxJDDoCBO4bq1bmcsIEjhuqNpIENow6J1LCDEkMOgIE7hurVuZywgVmnhu4d0IE5hbQ!5e0!3m2!1svi!2s!4v1742134613406!5m2!1svi!2s"
});
</script>
<template>
    <div class="flex flex-col min-h-screen w-full bg-[linear-gradient(135deg,_#F4F5F7,_#DCE3F2)]">
        <div class="container mx-auto flex-1">
            <div class="py-5">
                <h1 class="text-2xl font-bold text-gray-800">Danh sách khách sạn</h1>
            </div>

            <main class="grid grid-cols-4 gap-6 p-5">
                <aside class="shadow-lg rounded-lg p-4 col-span-1 bg-white">
                    <h2 class="text-lg font-bold text-gray-800 mb-3">Địa điểm</h2>
                    <ul class="space-y-2">
                        <li v-for="(city, index) in cities" :key="index"
                            class="cursor-pointer text-[#1E40AF] hover:underline" @click="filterHotels(city)">
                            {{ city }}
                        </li>
                    </ul>
                </aside>

                <section class="col-span-3 space-y-4">
                    <article v-for="(hotel, index) in filteredHotels" :key="index"
                        class="flex bg-white shadow-md rounded-lg p-4 w-full">
                        <img :src="hotel.image" :alt="hotel.name" class="w-60 h-40 rounded-lg mr-4">
                        <div class="flex-1">
                            <h2 class="text-lg font-bold text-gray-800">{{ hotel.name }}</h2>
                            <p class="text-gray-600">{{ hotel.description }}</p>
                            <p class="text-sm text-gray-500">Địa điểm: {{ hotel.city }}</p>
                            <p class="text-yellow-500 text-sm">⭐ {{ hotel.rating }}/5</p>
                            <p class="text-red-500 font-bold">Giá: {{ hotel.price }} VNĐ/đêm</p>
                            <p class="text-green-600">✔ {{ hotel.cancellation }}</p>
                            <p class="text-green-600">✔ {{ hotel.payment }}</p>
                            <p class="text-gray-600">Tiện ích: {{ hotel.amenities.join(', ') }}</p>
                            <a href="#"
                                class="mt-3 inline-block bg-[#1E40AF] text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition">
                                Xem chi tiết
                            </a>
                        </div>
                    </article>
                </section>
            </main>
        </div>

        <div class="container mx-auto py-10 text-center">
            <h2 class="text-3xl font-bold text-gray-800">Our Top Hotels</h2>
            <p class="text-gray-500 mt-2">Explore the best hotels with top ratings and affordable prices.</p>

            <swiper :modules="[Autoplay]" :autoplay="{ delay: 2000, disableOnInteraction: false }" :loop="true"
                :space-between="5" :slides-per-view="1" :breakpoints="{
                    640: { slidesPerView: 1, spaceBetween: 5 },
                    768: { slidesPerView: 2, spaceBetween: 5 },
                    1024: { slidesPerView: 3, spaceBetween: 10 }
                }" class="mt-6">
                <swiper-slide v-for="(hotel, index) in hotel" :key="index" class="p-1">
                    <div class="bg-white rounded-lg shadow-md overflow-hidden w-[95%] mx-auto relative">
                        <div
                            class="absolute top-2 left-2 flex items-center bg-gray-500/80 text-white text-xs font-semibold px-3 py-1 rounded-lg shadow-md">
                            <img src="/assets/images/coVietNam.jpg" alt="VN Flag" class="w-4 h-4 mr-2 rounded">
                            {{ hotel.address }}
                        </div>
                        <img :src="hotel.image" alt="Hotel Image" class="w-full h-52 object-cover">
                        <div class="p-4">
                            <h3 class="text-lg font-semibold">{{ hotel.name }}</h3>
                            <p class="text-gray-600 text-sm">${{ hotel.price }}/Day</p>
                            <div class="flex items-center justify-between mt-1">
                                <span class="text-yellow-500 text-sm">⭐ {{ hotel.rating }}</span>
                            </div>
                        </div>
                    </div>
                </swiper-slide>
            </swiper>
        </div>

        <div class="container mx-auto py-5 text-center">
            <h2 class="text-3xl font-bold text-gray-800">🎉 Ưu đãi khách sạn cực hot!</h2>
            <p class="text-gray-500 mt-2">Sử dụng mã giảm giá để đặt phòng với mức giá tốt nhất.</p>

            <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4 mt-6">
                <div v-for="voucher in vouchers" :key="voucher.id"
                    class="bg-yellow-100 border border-yellow-300 shadow-md p-4 rounded-lg relative text-left">
                    <h3 class="text-sm font-semibold text-yellow-700">{{ voucher.title }}</h3>
                    <p class="text-red-500 text-lg font-bold mt-1">Giảm {{ voucher.discount }}</p>
                    <p class="text-gray-600 text-xs mt-1">{{ voucher.description }}</p>
                    <p class="text-gray-500 text-xs mt-1">⏳ Hạn: {{ voucher.expiry }}</p>

                    <div class="flex items-center justify-between mt-3 bg-white border border-gray-300 rounded-md p-1">
                        <span class="font-mono text-xs text-gray-800">{{ voucher.code }}</span>
                        <button @click="copyCode(voucher.code)" class="text-[#1E40AF] text-xs font-semibold">
                            Sao chép
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <div class="container mx-auto px-4 py-10 text-center">
            <h2 class="text-3xl font-bold mb-6">Dịch Vụ Đi Kèm</h2>
            <p class="text-gray-600 mb-8">Chúng tôi cung cấp nhiều tiện ích để nâng cao trải nghiệm của bạn.</p>
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
import { ref, computed } from 'vue';
import { defineComponent } from 'vue';
import { Swiper, SwiperSlide } from 'swiper/vue';
import 'swiper/css';
import 'swiper/css/navigation';
import { Navigation, Autoplay } from 'swiper/modules';
import 'swiper/css/autoplay';
const services = ref([
    { id: 1, title: 'Spa', description: 'Thư giãn với các liệu pháp spa chuyên nghiệp.', image: '/assets/images/spa.jpg' },
    { id: 2, title: 'Massage', description: 'Massage trị liệu giúp bạn thư giãn.', image: '/assets/images/massage.jpg' },
    { id: 3, title: 'Golf', description: 'Sân golf đạt tiêu chuẩn quốc tế.', image: '/assets/images/golf.jpg' },
    { id: 4, title: 'Tennis', description: 'Sân tennis chất lượng cao.', image: '/assets/images/tennis.jpg' },
    { id: 5, title: 'Gym', description: 'Phòng tập với trang thiết bị hiện đại.', image: '/assets/images/gym.jpg' },
    { id: 6, title: 'Bể bơi', description: 'Hồ bơi ngoài trời với không gian rộng rãi.', image: '/assets/images/hoboi.jpg' },
    { id: 7, title: 'Nhà hàng', description: 'Thưởng thức ẩm thực đặc sắc.', image: '/assets/images/restaurant.jpg' },
    { id: 8, title: 'Dịch vụ đưa đón', description: 'Dịch vụ xe đưa đón tận nơi.', image: '/assets/images/car.jpg' }
]);
const vouchers = ref([
    { id: 1, code: "HOTEL50", discount: "50%", description: "Giảm 50% khi đặt phòng tại khách sạn 5 sao", expiry: "31/12/2024" },
    { id: 2, code: "STAY3PAY2", discount: "1 Đêm miễn phí", description: "Ở 3 đêm, trả tiền 2 đêm", expiry: "30/09/2024" },
    { id: 3, code: "LUXURY10", discount: "10%", description: "Ưu đãi 10% cho khách sạn cao cấp", expiry: "15/11/2024" },
    { id: 4, code: "EARLYBIRD20", discount: "20%", description: "Giảm 20% khi đặt phòng trước 1 tháng", expiry: "30/10/2024" },
]);

const copyCode = (code) => {
    navigator.clipboard.writeText(code);
    alert(`Đã sao chép mã: ${code}`);
};
const hotel = ref([
    { id: 1, name: 'Khách sạn Rex', address: 'TP.HCM', price: 1200, image: '/assets/images/img-hotel-6.jpeg', rating: 4.9 },
    { id: 2, name: 'Khách sạn Majestic', address: 'TP.HCM', price: 900, image: '/assets/images/img-hotel-7.jpeg', rating: 4.8 },
    { id: 3, name: 'Khách sạn Lotte', address: 'Hà Nội', price: 2000, image: '/assets/images/img-hotel-8.jpeg', rating: 4.9 },
    { id: 4, name: 'Khách sạn InterContinental', address: 'Đà Nẵng', price: 2500, image: '/assets/images/img-hotel-9.jpeg', rating: 5.0 },
    { id: 5, name: 'Khách sạn Vinpearl', address: 'Nha Trang', price: 1800, image: '/assets/images/img-hotel-10.jpeg', rating: 4.7 },
    { id: 6, name: 'Khách sạn Fusion Resort', address: 'Phú Quốc', price: 2300, image: '/assets/images/img-hotel-11.jpeg', rating: 4.8 },
    { id: 7, name: 'Khách sạn Metropole', address: 'Hà Nội', price: 3000, image: '/assets/images/img-hotel-12.jpeg', rating: 5.0 },
    { id: 8, name: 'Khách sạn Novotel', address: 'Đà Nẵng', price: 1500, image: '/assets/images/img-hotel-13.jpeg', rating: 4.6 },
    { id: 9, name: 'Khách sạn The Anam', address: 'Cam Ranh', price: 1700, image: '/assets/images/img-hotel-14.jpeg', rating: 4.7 },
]);
// Danh sách khách sạn mẫu

const hotels = ref([
    {
        name: "The Reverie Saigon",
        description: "Khách sạn 6 sao sang trọng bậc nhất tại TP. Hồ Chí Minh, mang phong cách hoàng gia với nội thất đẳng cấp, hồ bơi ngoài trời và dịch vụ spa sang trọng.",
        price: "4.500.000",
        cancellation: "Miễn phí hủy",
        payment: "Thanh toán trước hoặc tại chỗ nghỉ",
        image: "/assets/images/img-hotel-1.png",
        city: "TP. Hồ Chí Minh",
        rating: 5,
        amenities: ["Hồ bơi", "Spa", "Nhà hàng", "Quầy bar", "WiFi miễn phí"]
    },
    {
        name: "InterContinental Hanoi Landmark72",
        description: "Khách sạn cao cấp tọa lạc tại tòa nhà cao nhất Hà Nội, cung cấp tầm nhìn toàn cảnh thành phố, dịch vụ đẳng cấp và ẩm thực quốc tế.",
        price: "3.800.000",
        cancellation: "Miễn phí hủy",
        payment: "Thanh toán trước hoặc tại chỗ nghỉ",
        image: "/assets/images/img-hotel-2.png",
        city: "Hà Nội",
        rating: 5,
        amenities: ["Phòng gym", "Nhà hàng", "WiFi miễn phí", "Dịch vụ đưa đón"]
    },
    {
        name: "Fusion Resort Cam Ranh",
        description: "Khu nghỉ dưỡng bên bờ biển tuyệt đẹp với hồ bơi riêng, biệt thự sang trọng và chương trình chăm sóc sức khỏe miễn phí hàng ngày.",
        price: "5.200.000",
        cancellation: "Miễn phí hủy",
        payment: "Thanh toán trước hoặc tại chỗ nghỉ",
        image: "/assets/images/img-hotel-3.png",
        city: "Khánh Hòa",
        rating: 4.5,
        amenities: ["Hồ bơi riêng", "Bữa sáng miễn phí", "Spa", "Bãi biển riêng"]
    },
    {
        name: "Premier Village Danang Resort",
        description: "Khu nghỉ dưỡng cao cấp với bãi biển riêng, biệt thự sang trọng có hồ bơi riêng, nhà hàng phục vụ ẩm thực đa dạng.",
        price: "6.000.000",
        cancellation: "Miễn phí hủy",
        payment: "Thanh toán trước hoặc tại chỗ nghỉ",
        image: "/assets/images/img-hotel-4.png",
        city: "Đà Nẵng",
        rating: 5,
        amenities: ["Hồ bơi riêng", "Nhà hàng", "Spa", "WiFi miễn phí"]
    },
    {
        name: "Azerai La Residence Hue",
        description: "Khách sạn boutique với kiến trúc cổ điển, tầm nhìn ra sông Hương thơ mộng, mang lại trải nghiệm nghỉ dưỡng thanh bình.",
        price: "2.800.000",
        cancellation: "Miễn phí hủy",
        payment: "Thanh toán trước hoặc tại chỗ nghỉ",
        image: "/assets/images/img-hotel-2.png",
        city: "Huế",
        rating: 4.5,
        amenities: ["Nhà hàng", "WiFi miễn phí", "Bể bơi", "Dịch vụ spa"]
    },
    {
        name: "Vinpearl Resort Nha Trang",
        description: "Khu nghỉ dưỡng 5 sao nằm trên đảo Hòn Tre, kết hợp bãi biển tuyệt đẹp và công viên giải trí VinWonders đẳng cấp quốc tế.",
        price: "4.000.000",
        cancellation: "Miễn phí hủy",
        payment: "Thanh toán trước hoặc tại chỗ nghỉ",
        image: "/assets/images/img-hotel-3.png",
        city: "Nha Trang",
        rating: 5,
        amenities: ["Công viên nước", "Bể bơi", "Nhà hàng", "Quầy bar", "WiFi miễn phí"]
    }
]);

// Lọc danh sách khách sạn theo thành phố
const selectedCity = ref(null);
const filteredHotels = computed(() => {
    return selectedCity.value ? hotels.value.filter(h => h.city === selectedCity.value) : hotels.value;
});

// Danh sách địa điểm (dựa trên danh sách khách sạn)
const cities = computed(() => {
    return [...new Set(hotels.value.map(h => h.city))];
});

// Hàm lọc khách sạn theo thành phố
const filterHotels = (city) => {
    selectedCity.value = city;
};
</script>
<template>
  <div>
    <!-- Phần tiêu đề và mô tả -->
    <div class="bg-[#1e3a8a] text-white py-16 text-center rounded-b-3xl">
      <h1 class="text-4xl font-bold">LIÊN HỆ CHÚNG TÔI</h1>
      <p class="mt-4 text-xl">Chúng tôi luôn sẵn sàng hỗ trợ bạn, dù bạn ở bất cứ nơi đâu!</p>
    </div>

    <!-- Phần nội dung liên hệ -->
    <div
      class="py-16"
      style="background: linear-gradient(135deg, #f4f5f7, #dce3f2)"
    >
      <div class="container mx-auto px-6 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-12">
        <!-- Thông tin tổng đài hỗ trợ và công ty -->
        <div class="space-y-8 md:col-span-2 lg:col-span-2">
          <div
            class="bg-white shadow-xl rounded-2xl p-8 transform transition-transform hover:scale-105 duration-300 ease-in-out"
          >
            <h2 class="text-2xl font-bold text-blue-900">Tổng đài hỗ trợ</h2>
            <p class="text-gray-600 text-lg mt-2">Áp dụng cước phí nhà mạng thông thường</p>
            <p class="text-blue-600 text-xl font-semibold mt-4">Hotline: 028 3827 0404</p>
          </div>

          <div
            class="bg-white shadow-xl rounded-2xl p-8 transform transition-transform hover:scale-105 duration-300 ease-in-out"
          >
            <h2 class="text-2xl font-bold text-blue-900">Công Ty Cổ Phần Việt Nam Booking</h2>
            <div class="mt-6 space-y-4">
              <div>
                <h3 class="text-xl font-semibold text-blue-700">📍 Hồ Chí Minh</h3>
                <p class="text-gray-600">164 Lê Thánh Tôn, Bến Thành, Quận 1</p>
                <p class="text-gray-600">190-192 Trần Quý, Phường 7, Quận 11</p>
              </div>
              <div>
                <h3 class="text-xl font-semibold text-blue-700">📍 Hà Nội</h3>
                <p class="text-gray-600">11B P. Nguyễn Siêu, Hàng Buồm, Hoàn Kiếm</p>
              </div>
              <div>
                <h3 class="text-xl font-semibold text-blue-700">📍 Đà Nẵng</h3>
                <p class="text-gray-600">12 Phạm Phú Thứ, Hải Châu 1, Hải Châu</p>
              </div>
            </div>
          </div>
        </div>

        <!-- Phần bản đồ -->
        <div class="flex flex-col h-full">
          <div class="bg-white shadow-xl rounded-2xl overflow-hidden flex-1">
            <iframe
              class="w-full h-full rounded-xl"
              src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3724.856988863329!2d105.81817631540299!3d21.00151899399368!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3135abce7cce1bdf%3A0x62f78a3a4b6e1b3d!2zUGjhuqdtIG3hu5tpIFF14bqjbmcgWHVsaXQgZ2nDqkMgZWhyIEV6Q2xvdWQ!5e0!3m2!1svi!2s!4v1678258886206"
              allowfullscreen
              loading="lazy"
            ></iframe>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* Tùy chỉnh màu sắc và hiệu ứng hover */
.bg-[#1e3a8a] {
  background-color: #1e3a8a;
}
.bg-white {
  background-color: #ffffff;
}
.text-blue-900 {
  color: #1e3a8a;
}
.text-blue-600 {
  color: #1e40af;
}
.text-blue-700 {
  color: #1e3a8a;
}
.rounded-2xl {
  border-radius: 1rem;
}
.shadow-xl {
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}
</style>

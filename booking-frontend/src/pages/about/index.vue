<template>
  <main class="pt-20 pb-16 text-center bg-[linear-gradient(135deg,#F4F5F7,#DCE3F2)]">
    <div class="container mx-auto px-4">
      <h1 class="text-4xl font-extrabold text-blue-900 uppercase mb-5">Giới Thiệu Về Chúng Tôi</h1>
      <p class="text-lg text-gray-700 leading-relaxed mb-10">
        Chào mừng bạn đến với <span class="text-red-500 font-bold">EliteBooking</span>! Chúng tôi
        cung cấp dịch vụ đặt phòng khách sạn hàng đầu với trải nghiệm người dùng mượt mà, tiện lợi
        và nhanh chóng.
      </p>

      <!-- Sứ Mệnh, Tầm Nhìn, Giá Trị Cốt Lõi -->
      <section class="flex flex-col md:flex-row gap-8 justify-center mt-10">
        <div
          class="flex-1 bg-white p-8 rounded-xl shadow-xl transition-transform transform hover:-translate-y-2 hover:scale-105 text-center"
        >
          <div class="text-5xl text-red-500 mb-3">🚀</div>
          <h2 class="text-xl font-bold text-blue-900 mb-2">Sứ Mệnh</h2>
          <p class="text-gray-700">
            Đem đến cho khách hàng dịch vụ đặt phòng tốt nhất với mức giá hợp lý.
          </p>
        </div>

        <div
          class="flex-1 bg-white p-8 rounded-xl shadow-xl transition-transform transform hover:-translate-y-2 hover:scale-105 text-center"
        >
          <div class="text-5xl text-red-500 mb-3">🌍</div>
          <h2 class="text-xl font-bold text-blue-900 mb-2">Tầm Nhìn</h2>
          <p class="text-gray-700">
            Trở thành nền tảng đặt phòng hàng đầu tại Việt Nam và khu vực Đông Nam Á.
          </p>
        </div>

        <div
          class="flex-1 bg-white p-8 rounded-xl shadow-xl transition-transform transform hover:-translate-y-2 hover:scale-105 text-center"
        >
          <div class="text-5xl text-red-500 mb-3">💎</div>
          <h2 class="text-xl font-bold text-blue-900 mb-2">Giá Trị Cốt Lõi</h2>
          <ul class="text-gray-700 space-y-2">
            <li>✔ Chất lượng dịch vụ</li>
            <li>✔ Uy tín và minh bạch</li>
            <li>✔ Hỗ trợ khách hàng tận tâm</li>
          </ul>
        </div>
      </section>

      <!-- Lịch sử phát triển -->
      <section class="mt-14 p-8 bg-white shadow-xl rounded-lg text-left">
        <h2 class="text-2xl font-bold text-blue-900 mb-4">Lịch Sử và Phát Triển</h2>
        <p class="text-lg text-gray-700 leading-relaxed">
          Thành lập từ năm 2010, <strong class="text-blue-900">EliteBooking</strong> đã phát triển
          nhanh chóng và hiện nay là một trong những nền tảng đặt phòng khách sạn uy tín hàng đầu
          tại Việt Nam. Chúng tôi không ngừng nỗ lực để cải tiến dịch vụ và mang lại những trải
          nghiệm tốt nhất cho khách hàng.
        </p>
      </section>

      <!-- Đội ngũ nhân viên -->
      <section class="mt-14 p-8 bg-white shadow-xl rounded-lg text-left">
        <h2 class="text-2xl font-bold text-blue-900 mb-4">Đội Ngũ Nhân Viên</h2>
        <p class="text-lg text-gray-700 leading-relaxed">
          <strong class="text-blue-900">EliteBooking</strong> tự hào về đội ngũ nhân viên nhiệt
          huyết, chuyên nghiệp. Chúng tôi luôn đồng hành cùng khách hàng và mang đến giải pháp tối
          ưu nhất cho mỗi nhu cầu của bạn.
        </p>
      </section>

      <!-- Chứng nhận và đối tác -->
      <section class="mt-14 p-8 bg-white shadow-xl rounded-lg text-left">
        <h2 class="text-2xl font-bold text-blue-900 mb-4">Chứng Nhận và Đối Tác</h2>
        <p class="text-lg text-gray-700 leading-relaxed">
          <strong class="text-blue-900">EliteBooking</strong> đã đạt được nhiều chứng nhận về chất
          lượng dịch vụ và hợp tác với các đối tác lớn trong ngành khách sạn, bảo đảm mang đến cho
          khách hàng dịch vụ tin cậy và an toàn.
        </p>
      </section>

      <!-- Sự kiện sắp tới -->
      <section class="mt-14 p-8 bg-white shadow-xl rounded-lg text-left">
        <h2 class="text-2xl font-bold text-blue-900 mb-4">Sự Kiện Sắp Tới</h2>
        <p class="text-lg text-gray-700 leading-relaxed">
          Chúng tôi sẽ tổ chức sự kiện "Ngày hội du lịch EliteBooking" vào cuối tháng này, với nhiều
          ưu đãi hấp dẫn dành cho khách hàng tham gia. Đừng bỏ lỡ cơ hội!
        </p>
      </section>
    </div>
  </main>
</template>

<script>
export default {
  name: 'AboutUsPage',
}
</script>

<style scoped>
/* Thêm các hiệu ứng cho card khi hover */
@keyframes cardHover {
  from {
    transform: translateY(0);
  }
  to {
    transform: translateY(-10px);
  }
}

.card:hover {
  animation: cardHover 0.3s ease-in-out;
}
</style>

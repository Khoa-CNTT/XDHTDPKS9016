<template>
  <div class="max-w-2xl mx-auto mt-12 p-6 bg-white shadow-xl rounded-lg border border-gray-200">
    <!-- Tiêu đề -->
    <h1 class="text-2xl font-bold text-gray-800 mb-6 text-center">Chỉnh sửa thông tin khách sạn</h1>

    <!-- Ảnh đại diện -->
    <div class="mb-6">
      <!-- <img
        src="/api/v1/images/view/984efdf2-6407-415e-8e90-d391f370ae47_sofitel.jpg"
        alt="Hotel Image"
        class="w-full h-56 object-cover rounded-md border"
      /> -->
    </div>

    <!-- Form -->
    <form class="space-y-5">
      <!-- Tên khách sạn -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Tên khách sạn</label>
        <input
          type="text"
          value="Khách sạn Sài Gòn bbbbb"
          class="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-400 focus:outline-none"
        />
      </div>

      <!-- Hotline -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Hotline</label>
        <input
          type="text"
          value="0901239999"
          class="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-400 focus:outline-none"
        />
      </div>

      <!-- Địa chỉ -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Địa chỉ</label>
        <input
          type="text"
          value="123 Đường Lê Lợi, Quận 1, TP.HCM"
          class="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-400 focus:outline-none"
        />
      </div>

      <!-- Mô tả -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Mô tả</label>
        <textarea
          rows="4"
          class="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-400 focus:outline-none"
        >Khách sạn 5 sao nằm tại trung tâm thành phố.</textarea>
      </div>

      <!-- Nút lưu -->
      <div>
        <button
          type="submit"
          class="w-full bg-blue-600 text-white py-3 rounded-md hover:bg-blue-700 transition duration-300 text-lg font-medium"
        >
          Cập nhật
        </button>
      </div>
    </form>
  </div>
</template>

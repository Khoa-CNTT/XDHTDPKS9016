<template>
  <div class="max-w-2xl mx-auto mt-10 p-8 bg-white rounded-xl shadow-md">
    <h2 class="text-2xl font-bold text-center text-gray-800 mb-8">Cập nhật tài khoản cá nhân</h2>
    <form class="space-y-6">
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <!-- <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">User ID</label>
          <input v-model="form.user_id" type="text" placeholder="User ID" 
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            readonly />
        </div> -->

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Họ và tên</label>
          <input type="text" placeholder="Nhập họ và tên"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Giới tính</label>
          <select
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
            <option value="nam">Nam</option>
            <option value="nu">Nữ</option>
            <!-- <option value="khac">Khác</option> -->
          </select>
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Địa chỉ</label>
          <input type="text" placeholder="Nhập địa chỉ"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Email</label>
          <input type="email" placeholder="Nhập email" readonly
            class="w-full px-4 py-2 bg-gray-100 text-gray-700 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400 cursor-not-allowed" />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Số điện thoại</label>
          <input type="text" placeholder="Nhập số điện thoại"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Ngày sinh</label>
          <input type="date"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Trạng thái</label>
          <input type="text" placeholder="Nhập trạng thái (ví dụ: hoạt động)"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
        </div>
        <div>
          <label class="block text-sm font-semibold text-gray-700 mb-2">
            Tên đăng nhập
          </label>
          <input type="text" placeholder="Tên đăng nhập" readonly
            class="w-full px-4 py-2 bg-gray-100 text-gray-700 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400 cursor-not-allowed" />
        </div>
      </div>

      <button type="submit"
        class="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 px-4 rounded-lg transition duration-300">
        Cập nhật
      </button>
    </form>
  </div>
</template>


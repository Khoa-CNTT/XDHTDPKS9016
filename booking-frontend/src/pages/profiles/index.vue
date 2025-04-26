<template>
  <div class="max-w-2xl mx-auto mt-10 p-8 bg-white rounded-xl shadow-md">
    <h2 class="text-2xl font-bold text-center text-gray-800 mb-8">Cập nhật tài khoản cá nhân</h2>
    <form class="space-y-6" @submit.prevent="onSubmit">
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <!-- <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">User ID</label>
          <input v-model="form.user_id" type="text" placeholder="User ID" 
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            readonly />
        </div> -->

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Họ và tên</label>
          <input v-model="form.full_name" type="text" placeholder="Nhập họ và tên"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Giới tính</label>
          <select v-model="form.gender"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
            <option value="nam">Nam</option>
            <option value="nu">Nữ</option>
            <!-- <option value="khac">Khác</option> -->
          </select>
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Địa chỉ</label>
          <input v-model="form.address" type="text" placeholder="Nhập địa chỉ"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Email</label>
          <input v-model="form.email" type="email" placeholder="Nhập email" readonly
            class="w-full px-4 py-2 bg-gray-100 text-gray-700 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400 cursor-not-allowed" />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Số điện thoại</label>
          <input v-model="form.phone" type="text" placeholder="Nhập số điện thoại"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Ngày sinh</label>
          <input v-model="form.birth_date" type="date"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Trạng thái</label>
          <input v-model="form.status" type="text" placeholder="Nhập trạng thái (ví dụ: hoạt động)"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
        </div>
        <div>
          <label class="block text-sm font-semibold text-gray-700 mb-2">
            Tên đăng nhập
          </label>
          <input v-model="form.username" type="text" placeholder="Tên đăng nhập" readonly
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

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { updateUserApi, getInfoApi } from '@/services/user'; 
import { toast } from 'vue3-toastify';
import { UserProfile, UserInfo } from "@/types/user";
import { formatDateToDDMMYYYY } from '@/utils/dateUtils';

// Khai báo form
const form = ref({
  // user_id:0,
  full_name: '',
  gender: '',
  address: '',
  email: '',
  phone: '',
  birth_date: '',
  status: '',
  username: '',
});

const loading = ref(false);
const errorMessage = ref('');

const authStore = useAuthStore();

// Gọi thông tin người dùng khi component được mount
onMounted(async () => {
  const savedToken = localStorage.getItem('access_token');
  if (savedToken) {
    try {
      await authStore.setupAuth();
      const userId = authStore.user?.user_id;
      console.log('---------------------',userId);
      
      // form.value.user_id = authStore.user?.user_id ?? '';
      form.value.email = authStore.user?.email ?? '';
      form.value.username = authStore.user?.username ?? '';
      form.value.status = authStore.user?.status ?? '';

      if (userId) {
        await fetchUserProfile(String(userId));
      } else {
        console.error('Không tìm thấy user_id trong authStore.user!');
      }
    } catch (error) {
      console.error('Lỗi tự động đăng nhập:', error);
      localStorage.removeItem('access_token');
    }
  }
});

// Fetch thông tin người dùng từ API
const fetchUserProfile = async (id: string) => {
  loading.value = true;
  try {
    const response = await getInfoApi(id);
    if (response.data) {
      const data: UserInfo = response.data;
      // Format ngày sinh tại đây
      form.value = {
        // user_id: data.user_id ?? 0,
        full_name: data.full_name ?? '',
        gender: data.gender ?? '',
        address: data.address ?? '',
        email: data.email ?? '',
        phone: data.phone ?? '',
        birth_date: formatDateToDDMMYYYY(data.birth_date ?? ''), 
        status: String(data.status ?? ''),
        username: data.username ?? '',
      };
      toast.success('Lấy thông tin người dùng thành công!', {
        autoClose: 3000,
        position: 'top-right',
      });
    } else {
      errorMessage.value = 'Không có dữ liệu người dùng.';
      toast.error(errorMessage.value, {
        autoClose: 3000,
        position: 'top-right',
      });
    }
  } catch (error) {
    console.error('Lỗi khi lấy thông tin người dùng:', error);
    errorMessage.value = 'Đã có lỗi xảy ra khi lấy thông tin người dùng!';
    toast.error(errorMessage.value, {
      autoClose: 3000,
      position: 'top-right',
    });
  } finally {
    loading.value = false;
  }
};


const onSubmit = async () => {
  loading.value = true;
  try {
    const userId = authStore.user?.user_id;
    if (!userId) {
      toast.error('Không tìm thấy user_id để cập nhật!');
      return;
    }
    const formattedBirthDate = formatDateToDDMMYYYY(form.value.birth_date);

    const response = await updateUserApi(userId, {
      ...form.value,
      birth_date: formattedBirthDate, 
    });
    if (response.data) {
      toast.success('Cập nhật thông tin thành công!', {
        autoClose: 3000,
        position: 'top-right',
      });
    } else {
      toast.error('Cập nhật thất bại. Vui lòng thử lại!', {
        autoClose: 3000,
        position: 'top-right',
      });
    }
  } catch (error) {
    console.error('Lỗi khi cập nhật thông tin người dùng:', error);
    toast.error('Có lỗi xảy ra khi cập nhật!', {
      autoClose: 3000,
      position: 'top-right',
    });
  } finally {
    loading.value = false;
  }
};
</script>

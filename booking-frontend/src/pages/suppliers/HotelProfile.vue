<template>
  <div class="p-8 max-w-5xl mx-auto space-y-6">
    <div class="flex justify-between items-center">
      <h1 class="text-2xl font-bold text-text">Quản lý thông tin khách sạn</h1>
      <button @click="editMode ? saveHotel() : editMode = true"
        class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
        {{ editMode ? 'Lưu' : 'Chỉnh sửa' }}
      </button>
    </div>
    <!-- <div class="bg-white p-4 rounded shadow mb-4">
      <img v-if="book.image" :src="`http://103.69.97.39:8080/${book.image}`" alt="Image hotel"
        style="max-width: 50px; max-height: 50px;" />
      <span v-else class="text-gray-500">No image available</span>
    </div> -->
    <!-- Tên & địa chỉ -->
    <div class="bg-white p-4 rounded shadow space-y-2">
      <div>
        <label class="font-medium block">Tên khách sạn:</label>
        <template v-if="editMode">
          <input v-model="hotel.name" class="w-full border p-2 rounded" />
        </template>
        <template v-else>
          <p>{{ hotel.name }}</p>
        </template>
      </div>
      <div>
        <label class="font-medium block">Địa chỉ:</label>
        <template v-if="editMode">
          <input v-model="hotel.address" class="w-full border p-2 rounded" />
        </template>
        <template v-else>
          <p>{{ hotel.address }}</p>
        </template>
      </div>
    </div>

    <!-- Mô tả -->
    <div class="bg-white p-4 rounded shadow">
      <label class="font-medium block mb-2">Giới thiệu:</label>
      <template v-if="editMode">
        <textarea v-model="hotel.description" rows="10" class="w-full border p-2 rounded"></textarea>
      </template>
      <template v-else>
        <p class="whitespace-pre-line">{{ hotel.description }}</p>
      </template>
    </div>

    <!-- Dịch vụ -->
    <div class="bg-white p-4 rounded shadow">
      <label class="font-medium block mb-2">Dịch vụ & Tiện ích:</label>
      <ul class="list-disc pl-5">
        <li v-for="(service, i) in hotel.services" :key="i">
          <template v-if="editMode">
            <input v-model="hotel.services[i].serviceName" class="w-full border p-1 mb-1 rounded" />
          </template>
          <template v-else>{{ service.serviceName }} - {{ service.servicePrice.toLocaleString() }} VND</template>
        </li>
      </ul>
    </div>

    <!-- Bảng giá -->
    <div class="bg-white p-4 rounded shadow">
      <label class="font-medium block mb-2">Bảng giá phòng:</label>
      <table class="w-full border border-gray-400 text-sm">
        <thead>
          <tr class="bg-gray-100 text-left">
            <th class="p-2 border border-gray-300">Loại phòng</th>
            <th class="p-2 border border-gray-300">Giá (VND)</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(room, i) in hotel.roomTypes" :key="i">
            <td class="p-2 border border-gray-300">
              <template v-if="editMode">
                <input v-model="room.typeName" class="w-full border p-1 rounded" />
              </template>
              <template v-else>{{ room.typeName }}</template>
            </td>
            <td class="p-2 border border-gray-300">
              <template v-if="editMode">
                <input type="number" v-model="room.price" class="w-full border p-1 rounded" />
              </template>
              <template v-else>{{ room.price.toLocaleString() }} VND</template>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getInfoHotelApi, updateHotelInfoApi } from '@/services/supplier'
import { toast } from 'vue3-toastify';
const hotel = ref({
  name: "",
  image: "",
  address: "",
  hotline: "",
  description: "",
  services: [],
  roomTypes: []
});

const fetchHotelInfo = async () => {
  try {
    const data = await getInfoHotelApi();
    hotel.value = {
      name: data.name,
      image: data.image,
      address: data.address,
      hotline: data.hotline,
      description: data.description,
      services: data.services,
      roomTypes: data.roomTypes
    };
    console.log(hotel.value.image);
  } catch (error) {
    console.error('Lỗi khi lấy thông tin khách sạn:', error);
  }
};

onMounted(() => {
  fetchHotelInfo();

});

const editMode = ref(false)

const saveHotel = async () => {
  try {
    const res = await updateHotelInfoApi(hotel.value);
    console.log("====>",res);
    
    toast.success('Cập nhật thông tin khách sạn thành công', {
      autoClose: 10000,
      position: 'top-right',
    });
    editMode.value = false;
  } catch (error) {
    toast.error('Lỗi khi cập nhật thông tin khách sạn!', {
      autoClose: 10000,
      position: 'top-right',
    });
  }
};
</script>

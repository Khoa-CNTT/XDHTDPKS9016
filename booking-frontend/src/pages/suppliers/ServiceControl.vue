<template>
  <div class="space-y-6 px-4 py-6">
    <div class="border rounded-lg p-6 bg-white shadow flex justify-between items-center">
      <h3 class="text-lg font-semibold text-blue-700 flex">
        <Icon icon="mdi:tools" class="mr-2 text-xl" width="24" height="24" />
        Quản lý dịch vụ
      </h3>
      <button
        @click="showAddModal = true"
        class="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 transition flex items-center"
      >
        <Icon icon="mdi:plus-box" class="text-xl pr-2" width="24" height="24" />
        Thêm dịch vụ mới
      </button>
    </div>

    <div class="border rounded-lg p-6 bg-white shadow overflow-x-auto">
      <table class="min-w-full divide-y divide-gray-200 border">
        <thead class="bg-gray-100">
          <tr>
            <th class="px-4 py-2 text-left">STT</th>
            <th class="px-4 py-2 text-left">Tên dịch vụ</th>
            <th class="px-4 py-2 text-left">Giá</th>
            <th class="px-4 py-2 text-left max-w-xs">Mô tả chi tiết</th>
            <th class="px-4 py-2 text-center">Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(row, index) in services" :key="row.id" class="border-t hover:bg-gray-50">
            <td class="px-4 py-2">{{ index + 1 }}</td>
            <td class="px-4 py-2">{{ row.name }}</td>
            <td class="px-4 py-2">{{ row.price }} đ</td>
            <td class="px-4 py-2 max-w-xs truncate">{{ row.description }}</td>
            <td class="px-4 py-2 text-center">
              <div class="flex gap-2 justify-center">
                <button class="p-2 bg-blue-500 hover:bg-blue-600 text-white rounded transition">
                  <Icon icon="mdi:eye" width="20" height="20" />
                </button>
                <button
                  @click="handleEdit(row)"
                  class="p-2 bg-green-500 hover:bg-green-600 text-white rounded transition"
                >
                  <Icon icon="mdi:pencil" width="20" height="20" />
                </button>
                <button class="p-2 bg-red-500 hover:bg-red-600 text-white rounded transition">
                  <Icon icon="mdi:trash-can" width="20" height="20" />
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal thêm dịch vụ -->
    <ModalAddService
      v-if="showAddModal"
      @close="showAddModal = false"
      @serviceAdded="handleServiceAdded"
    />

    <!-- Modal cập nhật dịch vụ -->
    <!-- <UpdateService
      v-if="showEditModal"
      :service="editingService"
      @close="showEditModal = false"
      @serviceUpdated="handleServiceUpdated"
    /> -->
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import ModalAddService from './ModalAddService.vue';
import UpdateService from './UpdateService.vue';
import { getServiceListApi } from '@/services/supplier';
import { GetService } from '@/types/supplier';
import { toast } from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';

const services = ref<GetService[]>([]);
const showAddModal = ref(false);
const showEditModal = ref(false);
const editingService = ref<GetService | null>(null);

const fetchServices = async () => {
  try {
    const res = await getServiceListApi();
    services.value = res;
  } catch (error) {
    toast.error('Lỗi tải danh sách dịch vụ');
  }
};

const handleServiceAdded = async () => {
  await fetchServices();
  toast.success('Thêm dịch vụ thành công!');
  showAddModal.value = false;
};

const handleEdit = (service: GetService) => {
  // Tạo bản sao để tránh sửa trực tiếp vào list
  editingService.value = { ...service };
  showEditModal.value = true;
};

const handleServiceUpdated = async () => {
  await fetchServices();
  toast.success('Cập nhật dịch vụ thành công!');
  showEditModal.value = false;
};

fetchServices();
</script>

<template>
  <div class="space-y-6 px-4 py-6">
    <!-- Tiêu đề + nút thêm dịch vụ -->
    <div class="border rounded-lg p-6 bg-white shadow flex justify-between items-center">
      <h3 class="text-lg font-semibold text-blue-700 flex">
        <Icon icon="mdi:tools" class="mr-2 text-xl" width="24" height="24" /> Quản lý dịch vụ
      </h3>
      <button @click="showAddModal = true"
        class="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 transition flex items-center">
        <Icon icon="mdi:plus-box" class="text-xl pr-2" width="24" height="24" /> Thêm dịch vụ mới
      </button>
    </div>

    <!-- Bảng danh sách dịch vụ -->
    <div class="border rounded-lg p-6 bg-white shadow overflow-x-auto">
      <table class="min-w-full divide-y divide-gray-200 border">
        <thead class="bg-gray-100">
          <tr>
            <th class="px-4 py-2 text-left">STT</th>
            <th class="px-4 py-2 text-left">Tên dịch vụ</th>
            <th class="px-4 py-2 text-left">Giá</th>
            <th class="px-4 py-2 text-left">Hình ảnh</th>
            <th class="px-4 py-2 text-left">Mô tả chi tiết</th>
            <th class="px-4 py-2 text-center">Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(row, index) in tableRows" :key="index" class="border-t hover:bg-gray-50">
            <td class="px-4 py-2">{{ row['STT'] }}</td>
            <td class="px-4 py-2">{{ row['Tên dịch vụ'] }}</td>
            <td class="px-4 py-2">{{ row['Giá'] }} đ</td>
            <td class="px-4 py-2">
              <img :src="row['Hình ảnh']" alt="service" class="w-16 h-16 object-cover rounded-md shadow" />
            </td>
            <td class="px-4 py-2 max-w-xs truncate" :title="row['Mô tả chi tiết']">
              {{ row['Mô tả chi tiết'] }}
            </td>
            <td class="px-4 py-2 text-center">
              <div class="flex gap-2 justify-center">
                <!-- Icon Xem -->
                <button @click="viewService(Number(row['service_id']))"
                  class="p-2 bg-blue-500 hover:bg-blue-600 text-white rounded transition">
                  <Icon icon="mdi:eye" width="20" height="20" />
                </button>

                <!-- Icon Sửa -->
                <button @click="editService(Number(row['service_id']))"
                  class="p-2 bg-green-500 hover:bg-green-600 text-white rounded transition">
                  <Icon icon="mdi:pencil" width="20" height="20" />
                </button>

                <!-- Icon Xóa -->
                <button @click="confirmDelete(Number(row['service_id']))"
                  class="p-2 bg-red-500 hover:bg-red-600 text-white rounded transition">
                  <Icon icon="mdi:trash-can" width="20" height="20" />
                </button>
              </div>
            </td>

          </tr>
        </tbody>
      </table>
    </div>

    <!-- Dialog xác nhận xoá -->
    <ConfirmDialog v-if="showConfirmDialog" @cancel="cancelDelete" @confirm="handleConfirmedDelete" />

    <!-- Modal thêm dịch vụ -->
    <ModalAddService v-if="showAddModal" @close="showAddModal = false" @serviceAdded="handleServiceAdded"
      :fetchServices="fetchServices" />

    <!-- Modal chi tiết dịch vụ -->
    <ModalServiceDetail v-if="isOpenDetailModal" :service="selectedService" @close="isOpenDetailModal = false" />

    <!-- Modal edit dịch vụ -->
    <UpdateService v-if="showPopupEdit" :service="selectedService" @close="showPopupEdit = false"
      @updated="fetchServices" />
  </div>
</template>



<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import CustomTable from '@/components/base/CustomTable.vue';
import ModalAddService from './ModalAddService.vue';
import ModalServiceDetail from './ModalServiceDetail.vue';
import UpdateService from './UpdateService.vue';
import { getServiceListApi, getServiceByIdApi, deleteServiceApi } from '@/services/supplier';
import { ServiceRes, Service } from '@/types/supplier';
import ConfirmDialog from './ConfirmDialog.vue';
const services = ref<Service[]>([]);
const loading = ref(false);
const error = ref<string | null>(null);
const showAddModal = ref(false);
const isOpenDetailModal = ref(false);
const selectedService = ref<Service | null>(null);
const showConfirmDialog = ref(false);
const showPopupEdit = ref(false);
const serviceIdToDelete = ref<number | null>(null);
const tableHeaders = ['STT', 'Tên dịch vụ', 'Giá', 'Hình ảnh', 'Mô tả chi tiết', 'Hành động'];

const fetchServices = async () => {
  loading.value = true;
  error.value = null;
  try {
    const res = await getServiceListApi();
    services.value = res.content;
  } catch (err: any) {
    error.value = err.message || 'Đã xảy ra lỗi khi tải dịch vụ';
  } finally {
    loading.value = false;
  }
};

const tableRows = computed(() => {
  return services.value.map((service, index) => ({
    'STT': index + 1,
    'Tên dịch vụ': service.service_name,
    'Giá': service.service_price,
    'Hình ảnh': service.service_image ?? 'null',
    'Mô tả chi tiết': service.description,
    'Hành động': '',
    'service_id': service.service_id
  }));
});

const viewService = async (serviceId: number) => {
  try {
    const response = await getServiceByIdApi(serviceId);
    selectedService.value = response;
    isOpenDetailModal.value = true;
  } catch (err: any) {
    error.value = err.message || 'Đã xảy ra lỗi khi tải chi tiết dịch vụ';
  }
};

const confirmDelete = (id: number) => {
  serviceIdToDelete.value = id;
  showConfirmDialog.value = true;
};

const cancelDelete = () => {
  serviceIdToDelete.value = null;
  showConfirmDialog.value = false;
};

const handleConfirmedDelete = async () => {
  if (serviceIdToDelete.value !== null) {
    try {
      await deleteServiceApi(serviceIdToDelete.value);
      fetchServices();
    } catch (err: any) {
      error.value = err.message || 'Đã xảy ra lỗi khi xóa dịch vụ';
    } finally {
      showConfirmDialog.value = false;
      serviceIdToDelete.value = null;
    }
  }
};

const handleServiceAdded = (newService: Service) => {
  services.value.push(newService);
};
const editService = async (id: number) => {
  try {
    const res = await getServiceByIdApi(id);
    selectedService.value = res;
    showPopupEdit.value = true;
  } catch (err: any) {
    error.value = 'Không thể tải thông tin dịch vụ để sửa.';
  }
};

onMounted(() => {
  fetchServices();
});
</script>

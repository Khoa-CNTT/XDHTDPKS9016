<template>
  <div class="card">
    <div class="card-header flex justify-between items-center">
      <button @click="showAddModal = true"
        class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition">
        + Thêm dịch vụ mới
      </button>
    </div>
    <div class="card-body">
      <CustomTable :headers="tableHeaders" :rows="tableRows">
        <template #actions="{ row, index }">
          <div class="flex gap-2 justify-end">
            <button @click="viewService(Number(row['service_id']))"
              class="px-3 py-1 bg-blue-500 hover:bg-blue-600 text-white text-sm rounded-md transition">
              Xem chi tiết
            </button>
            <button class="px-3 py-1 bg-green-500 hover:bg-green-600 text-white text-sm rounded-md transition">
              Sửa
            </button>
            <button @click="confirmDelete(Number(row['service_id']))"
              class="px-3 py-1 bg-red-500 hover:bg-red-600 text-white text-sm rounded-md transition">
              Xóa
            </button>
          </div>
        </template>
      </CustomTable>
    </div>
    <ConfirmDialog v-if="showConfirmDialog" @cancel="cancelDelete" @confirm="handleConfirmedDelete" />
    <ModalAddService v-if="showAddModal" @close="showAddModal = false" @serviceAdded="handleServiceAdded"
      :fetchServices="fetchServices" />
    <ModalServiceDetail v-if="isOpenDetailModal" :service="selectedService" @close="isOpenDetailModal = false" />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import CustomTable from '@/components/base/CustomTable.vue';
import ModalAddService from './ModalAddService.vue';
import ModalServiceDetail from './ModalServiceDetail.vue';
import { getServiceListApi, getServiceByIdApi, deleteServiceApi, createServiceApi } from '@/services/supplier';
import { ServiceRes, Service } from '@/types/supplier';
import ConfirmDialog from './ConfirmDialog.vue';
const services = ref<Service[]>([]);
const loading = ref(false);
const error = ref<string | null>(null);
const showAddModal = ref(false);
const isOpenDetailModal = ref(false);
const selectedService = ref<Service | null>(null);
const showConfirmDialog = ref(false);
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
  services.value.push(newService); // Thêm dịch vụ mới vào danh sách
};

onMounted(() => {
  fetchServices();
});
</script>

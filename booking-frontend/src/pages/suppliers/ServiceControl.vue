<template>
  <div class="card">
    <div class="card-header flex justify-between items-center">
      <button class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition"
        @click="isOpenModal = true">
        + Thêm dịch vụ mới
      </button>
    </div>
    <div class="card-body">
      <CustomTable :headers="tableHeaders" :rows="tableRows">
        <template #actions="{ row, index }">
          <div class="flex gap-2 justify-end">
            <button
              @click="viewService(Number(row['service_id']))"
            class="px-3 py-1 bg-blue-500 hover:bg-blue-600 text-white text-sm rounded-md transition">
              Xem chi tiết
            </button>
            <button 
          
            class="px-3 py-1 bg-green-500 hover:bg-green-600 text-white text-sm rounded-md transition">
              Sửa
            </button>
            <button @click="deleteService(Number(row['service_id']))"
              class="px-3 py-1 bg-red-500 hover:bg-red-600 text-white text-sm rounded-md transition">
              Xóa
            </button>
          </div>
        </template>
      </CustomTable>
    </div>
    <ModalCreateService v-if="isOpenModal" @close="isOpenModal = false" @save="handleSaveService" />
     <ModalServiceDetail v-if="isOpenDetailModal" :service="selectedService" @close="isOpenDetailModal = false" />
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import CustomTable from '@/components/base/CustomTable.vue'
import { getServiceListApi, getServiceByIdApi, deleteServiceApi, updateServiceApi, createServiceApi } from '@/services/supplier'
import { ServiceRes, Service } from '@/types/supplier';
import ModalCreateService from './ModalCreateService.vue';
import ModalServiceDetail from './ModalServiceDetail.vue';

const isOpenModal = ref(false);
const services = ref<Service[]>([]);
const loading = ref(false);
const error = ref<string | null>(null);
const tableHeaders = ['STT', 'Tên dịch vụ', 'Giá', 'Hình ảnh', 'Mô tả chi tiết', 'Hành động']

const fetchServices = async () => {
  loading.value = true;
  error.value = null;
  try {
    const res = await getServiceListApi();
    console.log("ne-----------:",res);
    
    services.value = res.content;
  } catch (err: any) {
    error.value = err.message || 'Đã xảy ra lỗi khi tải dịch vụ';
  } finally {
    loading.value = false;
  }
};
const selectedService = ref<Service | null>(null);
  const isOpenDetailModal = ref(false); 
const viewService = async (serviceId: number) => {
  try {
    const response = await getServiceByIdApi(serviceId);
    selectedService.value = response;
    isOpenDetailModal.value = true;
  } catch (err: any) {
    error.value = err.message || 'Đã xảy ra lỗi khi tải chi tiết dịch vụ';
  }
};
const deleteService = async (serviceId: number) => {
  try {
    await deleteServiceApi(serviceId);
    fetchServices();
  } catch (err: any) {
    error.value = err.message || 'Đã xảy ra lỗi khi xóa dịch vụ';
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

const handleSaveService = async (service: Service) => {
  try {
    console.log("Service to be created:", service); 
    const response = await createServiceApi(service);
    console.log('API Response:', response);
    fetchServices();
    isOpenModal.value = false;
  } catch (err: any) {
    error.value = err.message || 'Đã xảy ra lỗi khi thêm dịch vụ';
    console.error("Error:", err);
  }
}
onMounted(() => {
  fetchServices();
});
</script>

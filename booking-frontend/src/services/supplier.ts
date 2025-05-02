

import { $api } from '@/api/ofetch'
import { ServiceResponse, AddService, Service } from "@/types/supplier"
import { RoomTypeResponse,AddRoomType,RoomType} from "@/types/supplier"
export const getServiceListApi = async (): Promise<ServiceResponse> => {
  return $api('/management-service', { method: 'GET' }, true);
};

export const getServiceByIdApi = async (serviceId: number): Promise<Service> => {
  return $api(`/management-service/${serviceId}`, { method: 'GET' }, true);
};


export const deleteServiceApi = async (serviceId: number): Promise<void> => {
  await $api(`/management-service/${serviceId}`, { method: 'DELETE' }, true);
};


export const updateServiceApi = async (serviceId: number, serviceData: AddService): Promise<Service> => {
  return $api(`/management-service/${serviceId}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(serviceData),
  }, true);
};

// Thêm dịch vụ mới
export const createServiceApi = async (serviceData: AddService): Promise<Service> => {
  return $api('/management-service', {
    method: 'POST',
    body: serviceData,
  }, true);
};

export const getRoomTypesApi = async (): Promise<RoomTypeResponse> => {
  return $api('/room-types', { method: 'GET' }, true);
};

export const createRoomTypeApi = async (roomTypeData: AddRoomType): Promise<RoomType> => {
  return $api('/room-types', {
    method: 'POST',
    body: roomTypeData,
  }, true);
};


export const uploadImageApi = async (file: File): Promise<string> => {
  const formData = new FormData();
  formData.append('file', file); 

  const response = await $api('/images/upload', {
    method: 'POST',
    body: formData,
  }, true);

  return response; 
};

export const deleteRoomTypeApi = async (roomTypeId: number): Promise<void> => {
  await $api(`/room-types/${roomTypeId}`, { method: 'DELETE' }, true);
};
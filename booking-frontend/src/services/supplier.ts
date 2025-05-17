

import { $api } from '@/api/ofetch'
import { GetService , AddService, Service,NewService,AddServiceResponse } from "@/types/supplier"
import { RoomTypeResponse,RoomTypeSummary,RoomType,RoomTypeDetail,UpdateRoomTypeBody} from "@/types/supplier"
import { HotelInfo,InfoHotel} from "@/types/supplier"
//service
export const getServiceListApi = async (): Promise<GetService[]> => {
  return $api('/management-service', { method: 'GET' }, true);
};

export const getServiceByIdApi = async (serviceId: number): Promise<Service> => {
  return $api(`/management-service/${serviceId}`, { method: 'GET' }, true);
};


export const deleteServiceApi = async (serviceId: number): Promise<void> => {
  await $api(`/management-service/${serviceId}`, { method: 'DELETE' }, true);
};


export const updateServiceApi = async (serviceId: number, serviceData: NewService): Promise<AddServiceResponse> => {
  return $api(`/management-service/${serviceId}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(serviceData),
  }, true);
};

export const createServiceApi = async (serviceData: NewService): Promise<AddServiceResponse> => {
  return $api('/management-service', {
    method: 'POST',
    body: serviceData,
  }, true);
};

// export const getRoomTypesApi = async (): Promise<RoomTypeResponse> => {
//   return $api('/room-types', { method: 'GET' }, true);
// };




export const uploadImageApi = async (file: File): Promise<string> => {
  const formData = new FormData();
  formData.append('file', file); 

  const response = await $api('/images/upload', {
    method: 'POST',
    body: formData,
  }, true);

  return response; 
};





export const getInfoHotelApi = async (): Promise<HotelInfo> => {
  return $api('/hotel-info', { method: 'GET' }, true);
};


export const updateHotelInfoApi = async (hotelData: InfoHotel): Promise<HotelInfo> => {
  return $api('/hotel-info', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(hotelData),
  }, true);
};

///type room
export const getRoomTypesApi = async (): Promise<RoomTypeResponse> => {
  return $api('/room-types', { method: 'GET' }, true);
};
export const createRoomTypeApi = async (roomTypeData: RoomTypeDetail): Promise<RoomType> => {
  return $api('/room-types', {
    method: 'POST',
    body: roomTypeData,
  }, true);
};
export const deleteRoomTypeApi = async (roomTypeId: number): Promise<void> => {
  await $api(`/room-types/${roomTypeId}`, { method: 'DELETE' }, true);
};
export const updateRoomTypeApi = async (
  roomTypeId: number,
  roomTypeData: UpdateRoomTypeBody
): Promise<RoomTypeSummary> => {
  return $api(`/room-types/${roomTypeId}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(roomTypeData),
  }, true);
};
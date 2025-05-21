

import { $api } from '@/api/ofetch'
import { GetService , AddService, Service,NewService,AddServiceResponse, Room } from "@/types/supplier"
import { RoomTypeResponse,RoomTypeSummary,RoomType,RoomTypeDetail,UpdateRoomTypeBody} from "@/types/supplier"
import { HotelInfo,InfoHotel,HotelProfile, HotelDetail} from "@/types/supplier"
import { IBookingList,BookingList,DashboardStatsPartial} from "@/types/supplier"
import { PaymentHistory} from "@/types/supplier"
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
// export const getRoomTypesApi = async (): Promise<RoomTypeResponse> => {
//   return $api('/room-types', { method: 'GET' }, true);
// };
export const getRoomTypesApi = async (page = 0, size = 5): Promise<RoomTypeResponse> => {
  return $api(`/room-types?page=${page}&size=${size}`, { method: 'GET' }, true);
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

export const getAllBookings = async (): Promise<IBookingList[]> => {
  return $api('/hotel-info', { method: 'GET' }, true);
};
export const getComment = async (): Promise<Comment[]> => {
  const response = await $api('/hotel-info', { method: 'GET' }, true);
  return response as Comment[];
};

//create info hotel
export const createHotel = async (roomTypeData: HotelProfile): Promise<HotelDetail> => {
  return $api('/management-supplier', {
    method: 'POST',
    body: roomTypeData,
  }, true);
};
// manager booking
export const getBookingsManager = async (): Promise<BookingList> => {
  return $api('/bookings/hotel', { method: 'GET' }, true);
};

export const getRoomsApi = async (): Promise<Room[]> => {
  return $api('/rooms-v1', { method: 'GET' }, true);
}
export const updateRoomApi = async (id_room: number, roomData: Partial<Room>): Promise<Room> => {
  return $api(`/rooms-v1/${id_room}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(roomData),
  }, true);
};

export const deleteRoomApi = async (id_room: number): Promise<void> => {
  return $api(`/rooms-v1/${id_room}`, {
    method: 'DELETE',
  }, true);
};

export const createRoomApi = async (roomData: Partial<Room>): Promise<Room> => {
  return $api('/rooms-v1', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(roomData),
  }, true);
}
//history payment
export const getPaymentHistory = async (): Promise<PaymentHistory[]> => {
  return $api('/payments/history', { method: 'GET' }, true);
};

//thong ke
export const getHotelStatisticsApi = async (
  year: number,
  quarter: number
): Promise<DashboardStatsPartial> => {
  return $api(
    `/management-statistical/hotel?year=${year}&quarter=${quarter}`,
    {
      method: 'GET',
    },
    true
  );
};
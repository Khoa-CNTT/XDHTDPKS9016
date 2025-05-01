import { $api } from '@/api/ofetch'
import { HotelResponse, Hotel} from "@/types/home"

export const getHotelListApi = async (): Promise<HotelResponse> => {
  return $api('/hotels', { method: 'GET' }, true);
};

export const getHotelByIdApi = async (hotelId: number): Promise<Hotel> => {
    return $api(`/hotels/${hotelId}`, { method: 'GET' }, true);
  };
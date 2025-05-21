import { $api } from '@/api/ofetch'
import { HotelResponse, Hotel, HotelSearchResponse,Comment } from '@/types/home'

export const getHotelListApi = async (): Promise<HotelResponse> => {
  return $api('/hotels', { method: 'GET' }, true)
}

// export const getHotelByIdApi = async (hotelId: number): Promise<Hotel> => {
//   return $api(`/hotels/${hotelId}`, { method: 'GET' }, true)
// }
export const getHotelByIdApi = async (
  hotelId: number,
  checkIn: string,
  checkOut: string
): Promise<Hotel> => {
  const params = new URLSearchParams({ checkIn, checkOut }).toString();
  const url = `/hotels/${hotelId}?${params}`;
  return $api(url, { method: 'GET' }, true);
};

export const searchHotelsApi = async (query: string): Promise<HotelSearchResponse> => {
  return $api(`/hotels/search?name=${encodeURIComponent(query)}`, { method: 'GET' }, true)
}


export const getCommentPublicApi = async (hotelId: number): Promise<Comment[]> => {
  return $api(`/public-comments/${hotelId}`, { method: 'GET' }, true)
}
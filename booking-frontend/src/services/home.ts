import { $api } from '@/api/ofetch'
import { HotelResponse, Hotel, HotelSearchResponse } from '@/types/home'

export const getHotelListApi = async (): Promise<HotelResponse> => {
  return $api('/hotels', { method: 'GET' }, true)
}

export const getHotelByIdApi = async (hotelId: number): Promise<Hotel> => {
  return $api(`/hotels/${hotelId}`, { method: 'GET' }, true)
}
export const searchHotelsApi = async (query: string): Promise<HotelSearchResponse> => {
  return $api(`/hotels/search?name=${encodeURIComponent(query)}`, { method: 'GET' }, true)
}

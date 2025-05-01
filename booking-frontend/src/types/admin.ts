export interface PageInfo {
  totalElements: number
  totalPages: number
  number: number
  size: number
}
export interface User {
  user_id: number
  full_name: string | null
  gender: string | null
  address: string | null
  email: string
  phone: string | null
  birth_date: string | null
  status: string | null
  bookings: any[]
  comments: any[]
  ratings: any[]
}

export interface Service {
  service_id: number
  service_name: string
  service_price: number
  service_image: string | null
  description: string
  bookings: any[]
}

export interface Hotel {
  hotel_id: number
  name: string
  image: string | null
  address: string
  hotline: string
  description: string | null
  services: Service[]
  rooms: any[]
}

export interface ManagementSupplierResponse {
  content: Hotel[]
  page: PageInfo
}
export interface CreateHotelPayload {
  name: string
  image: string | null
  address: string
  hotline: string
  description: string | null
}
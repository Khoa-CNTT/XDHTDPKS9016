// --- Đặt Booking nếu cần, tạm thời để trống ---
export interface Booking {}

// --- Service & Response ---
export interface Service {
  service_id: number
  service_name: string
  service_price: number
  service_image: string | null
  description: string
  bookings: Booking[]
}

export interface AddService {
  service_name: string
  service_price: number
  service_image: string | null
  description: string
}

export interface ServiceResponse {
  content: Service[]
  page: Page
}
export interface GetService {
  id: number
  name: string
  description: string
  price: number
}
export interface NewService {
  service_name: string
  service_price: number
  service_image: string
  description: string
}

export interface AddServiceResponse extends NewService {
  service_id: number
}

export interface ServiceRes {
  content: Service
}

// --- RoomType & Related ---
export interface RoomType {
  room_type_id: number
  type_name: string
  number_bed: number
  maximum_people: number
  price: number
  description: string
  room_image: string
  available_room: number
  status: 'ACTIVE' | 'INACTIVE' | string
}

export interface AddRoomType {
  type_name: string
  number_bed: number
  maximum_people: number
  price: number
  description: string
  room_image: string
  available_room: number
  status: 'ACTIVE' | 'INACTIVE' | string
}

export interface RoomTypeSummary {
  room_type_id: number
  type_name: string
  number_room: number
  description: string
  room_image: string
}

export interface RoomTypeResponse {
  content: RoomTypeSummary[]
  page: PageInfo
}

// --- Hotel ---
export interface HotelInfo {
  idHotel: number
  name: string
  image: string
  address: string
  hotline: string
  description: string
  services: ServiceInfo[]
  roomTypes: RoomTypeInfo[]
}

export interface InfoHotel {
  name: string
  image: string
  address: string
  hotline: string
  description: string
}

// --- Service & RoomType (Simple dùng trong HotelInfo) ---
export interface ServiceInfo {
  serviceId: number
  serviceName: string
  servicePrice: number
  serviceImage: string
  description: string
}

export interface RoomInfo {
  roomId: number
  numberBeds: number
  price: number
  status: string
  booked: boolean
}

export interface RoomTypeInfo {
  roomTypeId: number
  typeName: string
  description: string
  roomImage: string
  numberRoom: number
  averagePrice: number
  rooms: RoomInfo[]
}

// --- Simple display version (không nên bị trùng) ---
export interface SimpleService {
  serviceName: string
  servicePrice: number
}

export interface SimpleRoomType {
  typeName: string
  price: number
}

// --- Page Info ---
export interface Page {
  totalElements: number
  totalPages: number
  number: number
  size: number
}

export interface PageInfo {
  totalElements: number
  totalPages: number
  number: number
  size: number
}
export interface RoomTypeDetail {
  type_name: string
  number_bed: number
  maximum_people: number
  price: number
  description: string
  room_image: string
  available_room: number
  // status: 'ACTIVE' | 'INACTIVE';
}

export interface UpdateRoomTypeBody {
  type_name: string
  number_room: number
  description: string
  room_image: string
}

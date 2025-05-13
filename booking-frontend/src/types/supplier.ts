export interface Booking {}

export interface Service {
  service_id: number
  service_name: string
  service_price: number
  service_image: string | null
  description: string
  bookings: Booking[]
}

export interface Page {
  totalElements: number
  totalPages: number
  number: number
  size: number
}

export interface ServiceResponse {
  content: Service[]
  page: Page
}
export interface ServiceRes {
  content: Service;
}
export interface AddService {
  service_name: string;
  service_price: number;
  service_image: string | null;
  description: string;
}

export interface RoomType {
  room_type_id: number;
  type_name: string;
  number_bed: number;
  maximum_people: number;
  price: number;
  description: string;
  room_image: string;
  available_room: number;
  status: 'ACTIVE' | 'INACTIVE' | string; 
}

export interface PageInfo {
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

export interface RoomTypeResponse {
  content: RoomType[];
  page: PageInfo;
}
export interface AddRoomType {
  type_name: string;
  number_bed: number;
  maximum_people: number;
  price: number;
  description: string;
  room_image: string;
  available_room: number;
  status: 'ACTIVE' | 'INACTIVE' | string;
}

export interface SimpleService {
  serviceName: string;
  servicePrice: number;
}

export interface SimpleRoomType {
  typeName: string;
  price: number;
}

export interface HotelInfo {
  name: string;
  image: string;
  address: string;
  hotline: string;
  description: string;
  services: SimpleService[];
  roomTypes: SimpleRoomType[];
}

export interface HotelProfile {
  name: string;
  image: string;
  address: string;
  hotline: string;
  description: string;
}
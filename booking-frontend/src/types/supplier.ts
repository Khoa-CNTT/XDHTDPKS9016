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


export interface IHotelInfo {
  hotelId: number;
  name: string;
  address: string;
  email: string;
  phone: string;
  description: string;
}

export interface IRoomInfo {
  roomId: number;
  roomTypeId: number;
  roomTypeName: string;
  numberOfRooms: number;
  numberBeds: number;
  pricePerRoom: number;
  totalPrice: number;
}

export interface IServiceInfo {
  id: number;
  name: string;
  description: string;
  price: number;
}

export interface IBillInfo {
  billId: number;
  roomTotal: number;
  serviceTotal: number;
  total: number;
  deposit: number;
  numberOfDays: number;
}

export interface IUserInfo {
  user_id: number;
  full_name: string;
  gender: string;
  address: string;
  email: string;
  phone: string;
  birth_date: string;
  status: string;
  username: string;
}

export interface IBookingItem {
  bookingId: number;
  checkInDate: string;
  checkOutDate: string;
  checkInTime: string;
  checkOutTime: string;
  numberPeople: number;
  status: string;
  statusDisplay: string;
  hotel: IHotelInfo;
  rooms: IRoomInfo[];
  services: IServiceInfo[];
  bill: IBillInfo;
  user: IUserInfo;
  contactName: string;
  contactEmail: string;
  contactPhone: string;
  contactAddress: string;
  specialRequests: string;
}

export type IBookingList = IBookingItem[];
export interface Comment {
  content: string;
  comment_id: number;
  comment_author: string | null;
  comment_date: string; 
  comment_time: string; 
}
//info hotel
export interface HotelProfile {
  name: string;
  email: string;
  image: string | null;
  address: string;
  hotline: string;
  description: string;
  username: string;
  password: string;
  sendEmail: boolean;
}
export interface HotelAccount {
  account_id: number;
  username: string | null;
  email: string | null;
}

export interface HotelDetail {
  hotel_id: number;
  name: string;
  image: string | null;
  address: string;
  hotline: string;
  description: string | null;
  account: HotelAccount;
}
//manager bôking
// BookingInterfaces.ts

export interface Booking {
  bookingId: number;
  checkInDate: string;      // YYYY-MM-DD
  checkOutDate: string;     // YYYY-MM-DD
  checkInTime: string;      // HH:mm:ss
  checkOutTime: string;     // HH:mm:ss
  numberPeople: number;
  status: string;
  hotel: HotelBooking;
  rooms: BookingRoom[];
  services: BookingService[];
  bill: BookingBill;
  user: BookingUser;
  contactName: string;
  contactEmail: string;
  contactPhone: string;
  contactAddress: string;
  specialRequests: string;
  statusDisplay: string;
}

export interface HotelBooking {
  hotelId: number;
  name: string;
  address: string;
  email: string;
  phone: string;
  description: string;
}

export interface BookingRoom {
  roomId: number;
  roomTypeId: number;
  roomTypeName: string;
  numberOfRooms: number;
  numberBeds: number;
  pricePerRoom: number;
  totalPrice: number;
}

export interface BookingService {
  id: number;
  name: string;
  description: string;
  price: number;
}

export interface BookingBill {
  billId: number;
  roomTotal: number;
  serviceTotal: number;
  total: number;
  deposit: number;
  numberOfDays: number;
}

export interface BookingUser {
  user_id: number;
  full_name: string;
  gender: string;
  address: string;
  email: string;
  phone: string;
  birth_date: string;  
  status: string;
  username: string;
}
export type BookingList = Booking[];
//history payment
export interface PaymentHistory {
  paymentId: number;
  bookingId: number;
  billId: number;
  amount: number;
  paymentMethod: string;
  transactionId: string;
  status: string;
  paymentDate: string; 
  paymentTime: string; 
  customerName: string;
  accountNumber: string;
  statusDisplay: string | null;
}
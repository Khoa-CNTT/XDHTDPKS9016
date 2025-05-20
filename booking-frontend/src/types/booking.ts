interface RoomSelection {
  roomTypeId: number
  numberOfRooms: number
}

export interface BookingRequest {
  checkInDate: string
  checkOutDate: string
  checkInTime: string
  checkOutTime: string
  numberOfPeople: number
  roomSelections: RoomSelection[]
  serviceIds: number[]
}

export interface Hotel {
  hotelId: number
  name: string
  address: string
  email: string
  phone: string
  description: string
}

export interface Room {
  roomId: number
  roomTypeId: number
  roomTypeName: string
  numberOfRooms: number
  numberBeds: number
  pricePerRoom: number
  totalPrice: number
}

export interface Service {
  id: number
  name: string
  description: string
  price: number
}

export interface Bill {
  billId: number
  roomTotal: number
  serviceTotal: number
  total: number
  deposit: number
  numberOfDays: number
}

export interface User {
  user_id: number
  full_name: string
  gender: string
  address: string
  email: string
  phone: string
  birth_date: string
  status: string
  username: string
}

export interface BookingResponse {
  bookingId: number
  checkInDate: string
  checkOutDate: string
  checkInTime: string
  checkOutTime: string
  numberPeople: number
  status: string
  hotel: Hotel
  rooms: Room[]
  services: Service[]
  bill: Bill
  user: User
  contactName: string | null
  contactEmail: string | null
  contactPhone: string | null
  contactAddress: string | null
  specialRequests: string | null
  statusDisplay: string | null
}
export interface BookingBody {
  bookingId: number
  contactName: string
  contactEmail: string
  contactPhone: string
  contactAddress: string
  specialRequests: string
}

export interface User {
  user_id: number
  full_name: string
  gender: string
  address: string
  email: string
  phone: string
  birth_date: string
  status: string
  username: string
}

export interface Booking {
  bookingId: number
  checkInDate: string
  checkOutDate: string
  checkInTime: string
  checkOutTime: string
  numberPeople: number
  status: string
  hotel: Hotel
  rooms: Room[]
  services: Service[]
  bill: Bill
  user: User
  contactName: string
  contactEmail: string
  contactPhone: string
  contactAddress: string
  specialRequests: string
  statusDisplay: string | null
}

export interface BookingResponsePayment {
  booking: Booking
  paymentUrl: string
}
export interface BookingContact {
  bookingId: number
  contactName: string
  contactEmail: string
  contactPhone: string
  contactAddress: string
  specialRequests: string
}

export interface BookingListItem {
  bookingId: number
  checkInDate: string // ISO date string, vd: "2025-05-18"
  checkOutDate: string // ISO date string
  checkInTime: string // vd: "12:00:00"
  checkOutTime: string // vd: "12:00:00"
  numberPeople: number
  status: string // vd: "PAID"
  statusDisplay: string // vd: "Đã thanh toán"

  hotel: {
    hotelId: number
    name: string
    address: string
    email: string
    phone: string
    description: string
  }

  rooms: Array<{
    roomId: number
    roomTypeId: number
    roomTypeName: string
    numberOfRooms: number
    numberBeds: number
    pricePerRoom: number
    totalPrice: number
  }>

  services: Array<{
    id: number
    name: string
    description: string
    price: number
  }>

  bill: {
    billId: number
    roomTotal: number
    serviceTotal: number
    total: number
    deposit: number
    numberOfDays: number
  }

  user: {
    user_id: number
    full_name: string
    gender: string
    address: string
    email: string
    phone: string
    birth_date: string // format "dd-MM-yyyy"
    status: string
    username: string
  }

  contactName: string
  contactEmail: string
  contactPhone: string
  contactAddress: string
  specialRequests: string
}

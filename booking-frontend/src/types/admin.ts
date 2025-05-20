// export interface PageInfo {
//   totalElements: number
//   totalPages: number
//   number: number
//   size: number
// }
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
export interface ApiUserResponse {
  content: User[]
  page: Page
}

// export interface Service {
//   service_id: number
//   service_name: string
//   service_price: number
//   service_image: string | null
//   description: string
//   bookings: any[]
// }

// export interface Hotel {
//   hotel_id: number
//   name: string
//   image: string | null
//   address: string
//   hotline: string
//   description: string | null
//   services: Service[]
//   rooms: any[]
// }

// export interface ManagementSupplierResponse {
//   content: Hotel[]
//   page: PageInfo
// }
// export interface CreateHotelPayload {
//   name: string
//   image: string | null
//   address: string
//   hotline: string
//   description: string | null
// }


/////////////////////////
export interface ApiResponse {
  content: Hotel[];
  page: Page;
}

export interface Hotel {
  idHotel: number;
  name: string;
  image: string;
  address: string;
  hotline: string;
  description: string;
  services: Service[];
  roomTypes: RoomType[];
}

export interface Service {
  serviceId: number;
  serviceName: string;
  servicePrice: number;
  serviceImage: string | null;
  description: string;
}

export interface RoomType {
  roomTypeId: number;
  typeName: string;
  description: string;
  roomImage: string;
  numberRoom: number;
  averagePrice: number;
  rooms: Room[];
}

export interface Room {
  roomId: number;
  numberBeds: number;
  price: number;
  status: "AVAILABLE" | "BOOKED" | string;
  booked: boolean;
}

export interface Page {
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}
export interface Residence {
  name: string;
  image: string;
  address: string;
  hotline: string;
  description: string;
}

//hotel
export interface InfoHotel {
  name: string;
  image: string | null;
  address: string;
  hotline: string;
  description: string | null;
}

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


// thong ke
export interface DashboardStats {
  accountHotel: number;
  accountCount: number;
  bookingCount: number;
  totalPayment: number | null;
}
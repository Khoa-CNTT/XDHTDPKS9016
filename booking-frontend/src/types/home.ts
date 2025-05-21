// Định nghĩa kiểu cho từng đối tượng
export interface Booking {

  }
  
  export interface Service {
    service_id: number;
    service_name: string;
    service_price: number;
    service_image: string | null;
    description: string;
    bookings: Booking[];
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
    status: string; 
  }
  
  export interface Hotel {
    hotel_id: number;
    name: string;
    image: string | null;
    address: string;
    hotline: string;
    description: string;
    services: Service[];
    roomTypes: RoomType[];
  }
  
  export interface PageInfo {
    totalElements: number;
    totalPages: number;
    number: number;
    size: number;
  }
  
  export interface HotelResponse {
    content: Hotel[];
    page: PageInfo;
  }
  export interface HotelSearchResult {
  hotel_id: number;
  name: string;
  image: string;
  address: string;
  hotline: string;
  description: string;
}

export type HotelSearchResponse = HotelSearchResult[];
export interface Comment {
  content: string;
  comment_id: number;
  comment_author: string;
  comment_time: string;  // Format: "HH:mm:ss"
  comment_date: string;  // Format: "YYYY-MM-DD"
}

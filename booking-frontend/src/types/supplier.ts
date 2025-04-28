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
import { $api } from '@/api/ofetch'
import {
  BookingRequest,
  BookingResponse,
  BookingContact,
  BookingResponsePayment,
  BookingListItem,
} from '@/types/booking'
export const initializeBookingApi = async (
  bookingRequest: BookingRequest,
): Promise<BookingResponse> => {
  return $api(
    '/bookings/initialize',
    {
      method: 'POST',
      body: bookingRequest,
    },
    true,
  )
}

export const contactInfoPaymentApi = async (
  contactInfo: BookingContact,
): Promise<BookingResponsePayment> => {
  return $api(
    '/bookings/contact-info-payment',
    {
      method: 'POST',
      body: contactInfo,
    },
    true,
  )
}

export const getBookingListApi = async (): Promise<BookingListItem[]> => {
  return $api(
    '/bookings/user',
    {
      method: 'GET',
    },
    true,
  )
}

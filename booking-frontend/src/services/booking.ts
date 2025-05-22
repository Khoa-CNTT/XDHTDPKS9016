import { $api } from '@/api/ofetch'
import {
  BookingRequest,
  BookingResponse,
  BookingContact,
  BookingResponsePayment,
  BookingListResponse,
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

export const getBookingListApi = async (): Promise<BookingListResponse> => {
  return $api(
    '/bookings/user',
    {
      method: 'GET',
    },
    true,
  )
}

export const postCommentApi = async (roomId: number, comment: string): Promise<void> => {
  return $api(
    `/comments/room/${roomId}`,
    {
      method: 'POST',
      body: { comment },
    },
    true,
  )
}

export const postRatingApi = async (roomId: number, score: number): Promise<void> => {
  return $api(
    `/ratings/${roomId}`,
    {
      method: 'POST',
      body: { score },
    },
    true,
  )
}

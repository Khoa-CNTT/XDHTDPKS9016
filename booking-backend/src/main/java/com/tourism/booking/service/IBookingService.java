package com.tourism.booking.service;

import com.tourism.booking.dto.booking.BookingRequestDTO;
import com.tourism.booking.dto.booking.BookingResponseDTO;
import com.tourism.booking.dto.booking.ContactInfoDTO;
import com.tourism.booking.model.Booking;
import com.tourism.booking.model.Services;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface IBookingService {
    BookingResponseDTO createBooking(BookingRequestDTO bookingRequest);

    BookingResponseDTO getBookingById(Long id);

    Booking getBookingEntityById(Long id);

    Booking saveBooking(Booking booking);

    Set<Services> getServicesByBookingId(Long bookingId);

    List<BookingResponseDTO> getBookingsByDateRangeAndStatus(
            LocalDate checkInFrom, LocalDate checkInTo,
            LocalDate checkOutFrom, LocalDate checkOutTo,
            String status);

    BookingResponseDTO initializeBooking(BookingRequestDTO request, Principal principal);

    BookingResponseDTO updateContactInfo(ContactInfoDTO contactInfo);

    List<BookingResponseDTO> getBookingsByUserId(Long userId);

    List<BookingResponseDTO> getBookingsByHotelId(Long hotelId);

    List<BookingResponseDTO> getBookingsByHotelIdAndStatus(Long hotelId, String status);

    BookingResponseDTO updateBookingStatus(Long id, String status);

    boolean isBookingInTemporaryStorage(Long bookingId);

    BookingResponseDTO finalizeBooking(Long bookingId, Principal principal);

    List<BookingResponseDTO> getRecentBookings(int limit);
}
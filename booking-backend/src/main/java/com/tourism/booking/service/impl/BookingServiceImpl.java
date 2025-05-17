package com.tourism.booking.service.impl;

import com.tourism.booking.dto.booking.BookedRoomDTO;
import com.tourism.booking.dto.booking.BookingRequestDTO;
import com.tourism.booking.dto.booking.BookingResponseDTO;
import com.tourism.booking.dto.booking.ContactInfoDTO;
import com.tourism.booking.dto.booking.RoomSelectionDTO;
import com.tourism.booking.exception.ApiException;
import com.tourism.booking.exception.ErrorCode;
import com.tourism.booking.model.*;
import com.tourism.booking.repository.BookingRepository;
import com.tourism.booking.repository.RoomRepository;
import com.tourism.booking.repository.RoomTypeRepository;
import com.tourism.booking.repository.UserRepository;
import com.tourism.booking.service.IBookingService;
import com.tourism.booking.service.IRoomAvailabilityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements IBookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final UserRepository userRepository;
    private final IRoomAvailabilityService roomAvailabilityService;

    @Override
    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO bookingRequest) {
        // Validate availability for each room type
        for (RoomSelectionDTO roomSelection : bookingRequest.getRoomSelections()) {
            int availableCount = roomAvailabilityService.getAvailableRoomsCount(
                    roomSelection.getRoomTypeId(),
                    bookingRequest.getCheckInDate(),
                    bookingRequest.getCheckOutDate());

            if (availableCount < roomSelection.getNumberOfRooms()) {
                throw new ApiException(ErrorCode.INSUFFICIENT_ROOMS_AVAILABLE);
            }
        }

        // Create new booking
        Booking booking = new Booking();
        booking.setCheck_in_date(bookingRequest.getCheckInDate());
        booking.setCheck_out_date(bookingRequest.getCheckOutDate());
        booking.setCheck_in_time(
                bookingRequest.getCheckInTime() != null ? bookingRequest.getCheckInTime() : LocalTime.of(14, 0));
        booking.setCheck_out_time(
                bookingRequest.getCheckOutTime() != null ? bookingRequest.getCheckOutTime() : LocalTime.of(12, 0));
        booking.setNumber_people(bookingRequest.getNumberOfPeople());
        booking.setStatus("PAID");
        booking.setContact_name(bookingRequest.getContactName());
        booking.setContact_email(bookingRequest.getContactEmail());
        booking.setContact_phone(bookingRequest.getContactPhone());
        booking.setContact_address(bookingRequest.getContactAddress());
        booking.setSpecial_requests(bookingRequest.getSpecialRequests());

        // Set user if provided
        if (bookingRequest.getUserId() != null) {
            UserProfile user = userRepository.findById(bookingRequest.getUserId())
                    .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
            booking.setUser_profile(user);
        }

        // Save the booking to get an ID
        booking = bookingRepository.save(booking);

        // Process room selections
        List<BookedRoomDTO> bookedRooms = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (RoomSelectionDTO roomSelection : bookingRequest.getRoomSelections()) {
            RoomType roomType = roomTypeRepository.findById(roomSelection.getRoomTypeId())
                    .orElseThrow(() -> new ApiException(ErrorCode.ROOM_TYPE_NOT_FOUND));

            // Find available rooms of this type
            List<Room> availableRooms = roomRepository.findAvailableRoomsByTypeAndDateRange(
                    roomType.getRoom_type_id(),
                    bookingRequest.getCheckInDate(),
                    bookingRequest.getCheckOutDate(),
                    roomSelection.getNumberOfRooms());

            if (availableRooms.size() < roomSelection.getNumberOfRooms()) {
                throw new ApiException(ErrorCode.INSUFFICIENT_ROOMS_AVAILABLE);
            }

            // Create booking rooms - FIXED: Instead of creating multiple entries with
            // numberOfRooms=1,
            // create a single entry with the correct numberOfRooms value
            Room room = availableRooms.get(0);
            BookingRoom bookingRoom = new BookingRoom();
            bookingRoom.setBooking(booking);
            bookingRoom.setRoom(room);
            bookingRoom.setNumberOfRooms(roomSelection.getNumberOfRooms()); // FIXED: Use user-selected room count
            bookingRoom.setRoomTypeId(roomType.getRoom_type_id());
            booking.getBookingRooms().add(bookingRoom);

            // Add to total amount - multiply by the number of rooms
            totalAmount = totalAmount
                    .add(room.getPrice().multiply(BigDecimal.valueOf(roomSelection.getNumberOfRooms())));

            // Create DTO for response
            BookedRoomDTO bookedRoomDTO = new BookedRoomDTO();
            bookedRoomDTO.setRoomTypeId(roomType.getRoom_type_id());
            bookedRoomDTO.setRoomTypeName(roomType.getType_name());
            bookedRoomDTO.setNumberOfRooms(roomSelection.getNumberOfRooms());
            bookedRoomDTO.setNumberBeds(availableRooms.get(0).getNumber_bed());
            bookedRoomDTO.setPricePerRoom(availableRooms.get(0).getPrice());
            bookedRoomDTO.setTotalPrice(
                    availableRooms.get(0).getPrice().multiply(BigDecimal.valueOf(roomSelection.getNumberOfRooms())));

            bookedRooms.add(bookedRoomDTO);
        }

        // Update and save booking
        booking = bookingRepository.save(booking);

        // Create response DTO
        BookingResponseDTO responseDTO = new BookingResponseDTO();
        responseDTO.setBookingId(booking.getId_booking());
        responseDTO.setCheckInDate(booking.getCheck_in_date());
        responseDTO.setCheckOutDate(booking.getCheck_out_date());
        responseDTO.setCheckInTime(booking.getCheck_in_time());
        responseDTO.setCheckOutTime(booking.getCheck_out_time());
        responseDTO.setNumberPeople(booking.getNumber_people());
        responseDTO.setStatus(booking.getStatus());
        responseDTO.setContactName(booking.getContact_name());
        responseDTO.setContactEmail(booking.getContact_email());
        responseDTO.setContactPhone(booking.getContact_phone());
        responseDTO.setContactAddress(booking.getContact_address());
        responseDTO.setSpecialRequests(booking.getSpecial_requests());
        responseDTO.setRooms(bookedRooms);
        responseDTO.setTotalAmount(totalAmount);

        if (booking.getUser_profile() != null) {
            responseDTO.setUserId(booking.getUser_profile().getUser_id());
        }

        return responseDTO;
    }

    @Override
    public BookingResponseDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.BOOKING_NOT_FOUND));

        return mapBookingToDTO(booking);
    }

    @Override
    public List<BookingResponseDTO> getBookingsByUserId(Long userId) {
        List<Booking> bookings = bookingRepository.findByUserProfileId(userId);
        return bookings.stream()
                .map(this::mapBookingToDTO)
                .collect(Collectors.toList());
    }

    private BookingResponseDTO mapBookingToDTO(Booking booking) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setBookingId(booking.getId_booking());
        dto.setCheckInDate(booking.getCheck_in_date());
        dto.setCheckOutDate(booking.getCheck_out_date());
        dto.setCheckInTime(booking.getCheck_in_time());
        dto.setCheckOutTime(booking.getCheck_out_time());
        dto.setNumberPeople(booking.getNumber_people());
        dto.setStatus(booking.getStatus());
        dto.setContactName(booking.getContact_name());
        dto.setContactEmail(booking.getContact_email());
        dto.setContactPhone(booking.getContact_phone());
        dto.setContactAddress(booking.getContact_address());
        dto.setSpecialRequests(booking.getSpecial_requests());

        List<BookedRoomDTO> bookedRooms = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (BookingRoom bookingRoom : booking.getBookingRooms()) {
            Room room = bookingRoom.getRoom();
            RoomType roomType = room.getRoom_type();

            BookedRoomDTO bookedRoomDTO = new BookedRoomDTO();
            bookedRoomDTO.setRoomId(room.getId_room());
            bookedRoomDTO.setRoomTypeId(roomType.getRoom_type_id());
            bookedRoomDTO.setRoomTypeName(roomType.getType_name());
            bookedRoomDTO.setNumberOfRooms(bookingRoom.getNumberOfRooms());
            bookedRoomDTO.setNumberBeds(room.getNumber_bed());
            bookedRoomDTO.setPricePerRoom(room.getPrice());
            bookedRoomDTO.setTotalPrice(room.getPrice().multiply(BigDecimal.valueOf(bookingRoom.getNumberOfRooms())));

            totalAmount = totalAmount.add(bookedRoomDTO.getTotalPrice());
            bookedRooms.add(bookedRoomDTO);
        }

        dto.setRooms(bookedRooms);
        dto.setTotalAmount(totalAmount);

        if (booking.getUser_profile() != null) {
            dto.setUserId(booking.getUser_profile().getUser_id());
        }

        return dto;
    }

    @Override
    public Booking getBookingEntityById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.BOOKING_NOT_FOUND));
    }

    @Override
    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public Set<Services> getServicesByBookingId(Long bookingId) {
        Booking booking = getBookingEntityById(bookingId);
        return booking.getServices();
    }

    @Override
    public List<BookingResponseDTO> getBookingsByDateRangeAndStatus(
            LocalDate checkInFrom, LocalDate checkInTo,
            LocalDate checkOutFrom, LocalDate checkOutTo,
            String status) {
        // Implementation needed
        return new ArrayList<>();
    }

    @Override
    public BookingResponseDTO initializeBooking(BookingRequestDTO request, Principal principal) {
        // Implementation needed
        return new BookingResponseDTO();
    }

    @Override
    public BookingResponseDTO updateContactInfo(ContactInfoDTO contactInfo) {
        // Implementation needed
        return new BookingResponseDTO();
    }

    @Override
    public List<BookingResponseDTO> getBookingsByHotelId(Long hotelId) {
        // Implementation needed
        return new ArrayList<>();
    }

    @Override
    public List<BookingResponseDTO> getBookingsByHotelIdAndStatus(Long hotelId, String status) {
        // Implementation needed
        return new ArrayList<>();
    }

    @Override
    public BookingResponseDTO updateBookingStatus(Long id, String status) {
        // Implementation needed
        return new BookingResponseDTO();
    }

    @Override
    public boolean isBookingInTemporaryStorage(Long bookingId) {
        // Implementation needed
        return false;
    }

    @Override
    public BookingResponseDTO finalizeBooking(Long bookingId, Principal principal) {
        // Implementation needed
        return new BookingResponseDTO();
    }

    @Override
    public List<BookingResponseDTO> getRecentBookings(int limit) {
        // Implementation needed
        return new ArrayList<>();
    }
}
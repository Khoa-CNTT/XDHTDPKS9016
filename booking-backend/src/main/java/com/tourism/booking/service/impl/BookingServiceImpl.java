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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        if (bookingRequest.getUserId() != null) {
            UserProfile user = userRepository.findById(bookingRequest.getUserId())
                    .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
            booking.setUser_profile(user);
        }
        booking = bookingRepository.save(booking);
        List<BookedRoomDTO> bookedRooms = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (RoomSelectionDTO roomSelection : bookingRequest.getRoomSelections()) {
            Room room = roomRepository.findById(roomSelection.getRoomId())
                    .orElseThrow(() -> new ApiException(ErrorCode.ROOM_NOT_FOUND));

            if (!room.getRoom_type().getRoom_type_id().equals(roomSelection.getRoomTypeId())) {
                throw new ApiException(ErrorCode.ROOM_TYPE_MISMATCH);
            }

            List<Room> availableRooms = roomRepository.findAvailableRoomsByDateRange(
                    roomSelection.getRoomTypeId(),
                    bookingRequest.getCheckInDate(),
                    bookingRequest.getCheckOutDate());

            if (!availableRooms.stream().anyMatch(r -> r.getId_room().equals(roomSelection.getRoomId()))) {
                throw new ApiException(ErrorCode.ROOM_NOT_AVAILABLE);
            }

            BookingRoom bookingRoom = new BookingRoom();
            bookingRoom.setBooking(booking);
            bookingRoom.setRoom(room);
            bookingRoom.setRoomTypeId(room.getRoom_type().getRoom_type_id());
            booking.getBookingRooms().add(bookingRoom);

            totalAmount = totalAmount.add(room.getPrice());

            BookedRoomDTO bookedRoomDTO = new BookedRoomDTO();
            bookedRoomDTO.setRoomId(room.getId_room());
            bookedRoomDTO.setRoomTypeId(room.getRoom_type().getRoom_type_id());
            bookedRoomDTO.setRoomTypeName(room.getRoom_type().getType_name());
            bookedRoomDTO.setNumberBeds(room.getNumber_bed());
            bookedRoomDTO.setPricePerRoom(room.getPrice());
            bookedRoomDTO.setTotalPrice(room.getPrice());

            bookedRooms.add(bookedRoomDTO);
        }

        booking = bookingRepository.save(booking);

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
    public Page<BookingResponseDTO> getBookingsByUserId(Pageable pageable, Long userId) {
        Page<Booking> bookings = bookingRepository.findByUserProfileId(pageable, userId);
        return bookings.map(this::mapBookingToDTO);
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
        return new ArrayList<>();
    }

    @Override
    public BookingResponseDTO initializeBooking(BookingRequestDTO request, Principal principal) {
        return new BookingResponseDTO();
    }

    @Override
    public BookingResponseDTO updateContactInfo(ContactInfoDTO contactInfo) {

        return new BookingResponseDTO();
    }

    @Override
    public Page<BookingResponseDTO> getBookingsByHotelId(Pageable pageable, Long hotelId) {
        return null;
    }

    @Override
    public List<BookingResponseDTO> getBookingsByHotelIdAndStatus(Long hotelId, String status) {
        return new ArrayList<>();
    }

    @Override
    public BookingResponseDTO updateBookingStatus(Long id, String status) {
        return new BookingResponseDTO();
    }

    @Override
    public boolean isBookingInTemporaryStorage(Long bookingId) {
        return false;
    }

    @Override
    public BookingResponseDTO finalizeBooking(Long bookingId, Principal principal) {
        return new BookingResponseDTO();
    }

    @Override
    public List<BookingResponseDTO> getRecentBookings(int limit) {
        return new ArrayList<>();
    }
}
package com.tourism.booking.service.impl;

import com.tourism.booking.dto.room.*;
import com.tourism.booking.model.*;
import com.tourism.booking.repository.*;
import com.tourism.booking.service.IRoomManagementService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoomManagementServiceImpl implements IRoomManagementService {

    private static final Logger logger = LoggerFactory.getLogger(RoomManagementServiceImpl.class);

    @Autowired
    private IRoomRepository roomRepository;

    @Autowired
    private IRoomTypeRepository roomTypeRepository;

    @Autowired
    private IHotelRepository hotelRepository;

    @Autowired
    private IBookingRoomRepository bookingRoomRepository;

    @Autowired
    private IUserProfileRepository userProfileRepository;

    @Override
    public RoomManagementResponseDTO getRoomManagementByHotelId(Long hotelId) {
        logger.info("Fetching room management data for hotel ID: {}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with ID: " + hotelId));
        RoomManagementResponseDTO response = new RoomManagementResponseDTO();
        response.setHotelId(hotel.getHotel_id());
        response.setHotelName(hotel.getName());
        response.setHotelAddress(hotel.getAddress());
        List<RoomType> roomTypes = roomTypeRepository.findAllRoomTypesByHotelId(hotelId);
        logger.info("Found {} room types for hotel ID: {}", roomTypes.size(), hotelId);

        List<RoomTypeManagementDTO> roomTypeDTOs = new ArrayList<>();
        int totalRooms = 0;
        int totalBookedRooms = 0;

        for (RoomType roomType : roomTypes) {
            RoomTypeManagementDTO roomTypeDTO = new RoomTypeManagementDTO();
            roomTypeDTO.setRoomTypeId(roomType.getRoom_type_id());
            roomTypeDTO.setTypeName(roomType.getType_name());
            roomTypeDTO.setDescription(roomType.getDescription());
            roomTypeDTO.setRoomImage(roomType.getRoom_image());
            List<Room> rooms = roomRepository.findByRoomTypeRoomTypeId(roomType.getRoom_type_id());
            int roomCount = rooms.size();
            roomTypeDTO.setTotalRoomsInType(roomCount);
            totalRooms += roomCount;
            BigDecimal averagePrice = rooms.stream()
                    .map(Room::getPrice)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(Math.max(1, roomCount)), 2, RoundingMode.HALF_UP);
            roomTypeDTO.setAveragePrice(averagePrice);
            Integer bookedRoomsCount = bookingRoomRepository.countBookedRoomsByRoomTypeId(roomType.getRoom_type_id());
            int bookedCount = bookedRoomsCount != null ? bookedRoomsCount : 0;
            roomTypeDTO.setBookedRoomsCount(bookedCount);
            totalBookedRooms += bookedCount;
            int availableCount = Math.max(0, roomCount - bookedCount);
            roomTypeDTO.setAvailableRoomsCount(availableCount);
            List<RoomDetailDTO> roomDetailDTOs = rooms.stream()
                    .map(room -> mapRoomToDTO(room))
                    .collect(Collectors.toList());
            roomTypeDTO.setRooms(roomDetailDTOs);

            roomTypeDTOs.add(roomTypeDTO);
        }

        response.setRoomTypes(roomTypeDTOs);
        response.setTotalRooms(totalRooms);
        response.setTotalBookedRooms(totalBookedRooms);
        response.setTotalAvailableRooms(totalRooms - totalBookedRooms);

        logger.info("Room management data retrieved successfully for hotel ID: {}", hotelId);
        return response;
    }

    @Override
    public RoomManagementResponseDTO getRoomManagementByHotelIdAndDateRange(Long hotelId, LocalDate checkInDate,
            LocalDate checkOutDate) {
        logger.info("Fetching room management data for hotel ID: {} from {} to {}", hotelId, checkInDate, checkOutDate);
        RoomManagementResponseDTO response = getRoomManagementByHotelId(hotelId);
        List<BookingRoom> bookedRooms = bookingRoomRepository.findBookedRoomsByHotelIdAndDateRange(
                hotelId, checkInDate, checkOutDate);
        Map<Long, Integer> bookedCountByRoomType = new HashMap<>();
        for (BookingRoom bookingRoom : bookedRooms) {
            Long roomTypeId = bookingRoom.getRoomTypeId();
            bookedCountByRoomType.put(
                    roomTypeId,
                    bookedCountByRoomType.getOrDefault(roomTypeId, 0) + bookingRoom.getNumberOfRooms());
        }

        int totalBookedInTimeRange = 0;
        for (RoomTypeManagementDTO roomTypeDTO : response.getRoomTypes()) {
            int bookedInTimeRange = bookedCountByRoomType.getOrDefault(roomTypeDTO.getRoomTypeId(), 0);
            roomTypeDTO.setBookedRoomsCount(bookedInTimeRange);
            roomTypeDTO.setAvailableRoomsCount(roomTypeDTO.getTotalRoomsInType() - bookedInTimeRange);
            totalBookedInTimeRange += bookedInTimeRange;
            updateRoomBookingDetails(roomTypeDTO.getRooms(), bookedRooms);
        }

        response.setTotalBookedRooms(totalBookedInTimeRange);
        response.setTotalAvailableRooms(response.getTotalRooms() - totalBookedInTimeRange);

        logger.info("Room management data with date range retrieved successfully for hotel ID: {}", hotelId);
        return response;
    }

    @Override
    @Transactional
    public RoomType createRoomType(CreateRoomTypeRequest request) {
        logger.info("Creating new room type for hotel ID: {}", request.getHotelId());
        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with ID: " + request.getHotelId()));
        RoomType roomType = new RoomType();
        roomType.setType_name(request.getTypeName());
        roomType.setNumber_room(request.getNumberRoom());
        roomType.setDescription(request.getDescription());
        roomType.setRoom_image(request.getRoomImage());
        roomType.setHotel(hotel);
        RoomType savedRoomType = roomTypeRepository.save(roomType);
        logger.info("Successfully created room type with ID: {}", savedRoomType.getRoom_type_id());

        return savedRoomType;
    }

    @Override
    @Transactional
    public Room createRoom(CreateRoomRequest request) {
        logger.info("Creating new room for room type ID: {}", request.getRoomTypeId());
        RoomType roomType = roomTypeRepository.findById(request.getRoomTypeId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Room type not found with ID: " + request.getRoomTypeId()));
        UserProfile userProfile = userProfileRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + request.getUserId()));

        Room room = new Room();
        room.setNumber_bed(request.getNumberBeds());
        room.setPrice(request.getPrice());
        room.setStatus(request.getStatus());
        room.setRoom_type(roomType);
        room.setUser_profile(userProfile);

        Room savedRoom = roomRepository.save(room);
        logger.info("Successfully created room with ID: {}", savedRoom.getId_room());

        return savedRoom;
    }

    private RoomDetailDTO mapRoomToDTO(Room room) {
        RoomDetailDTO dto = new RoomDetailDTO();
        dto.setRoomId(room.getId_room());
        dto.setNumberBeds(room.getNumber_bed());
        dto.setPrice(room.getPrice());
        dto.setStatus(room.getStatus());

        List<BookingRoom> bookingRooms = bookingRoomRepository.findActiveBookingsByRoomId(room.getId_room());
        dto.setBooked(!bookingRooms.isEmpty());

        List<BookingDetailDTO> bookingDetails = bookingRooms.stream()
                .map(this::mapBookingRoomToDTO)
                .collect(Collectors.toList());
        dto.setBookings(bookingDetails);

        return dto;
    }

    private BookingDetailDTO mapBookingRoomToDTO(BookingRoom bookingRoom) {
        BookingDetailDTO dto = new BookingDetailDTO();
        Booking booking = bookingRoom.getBooking();

        dto.setBookingId(booking.getId_booking());
        dto.setCheckInDate(booking.getCheck_in_date());
        dto.setCheckOutDate(booking.getCheck_out_date());
        dto.setCheckInTime(booking.getCheck_in_time());
        dto.setCheckOutTime(booking.getCheck_out_time());
        dto.setStatus(booking.getStatus());
        dto.setContactName(booking.getContact_name());
        dto.setContactPhone(booking.getContact_phone());
        dto.setNumberOfRooms(bookingRoom.getNumberOfRooms());

        return dto;
    }

    private void updateRoomBookingDetails(List<RoomDetailDTO> roomDetails, List<BookingRoom> bookedRooms) {
        Map<Long, List<BookingRoom>> bookingsByRoomId = bookedRooms.stream()
                .collect(Collectors.groupingBy(br -> br.getRoom().getId_room()));

        for (RoomDetailDTO roomDetail : roomDetails) {
            List<BookingRoom> roomBookings = bookingsByRoomId.get(roomDetail.getRoomId());
            if (roomBookings != null && !roomBookings.isEmpty()) {
                roomDetail.setBooked(true);
                roomDetail.setBookings(roomBookings.stream()
                        .map(this::mapBookingRoomToDTO)
                        .collect(Collectors.toList()));
            } else {
                roomDetail.setBooked(false);
                roomDetail.setBookings(Collections.emptyList());
            }
        }
    }
}
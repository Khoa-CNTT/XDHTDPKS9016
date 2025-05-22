package com.tourism.booking.service.impl;

import com.tourism.booking.dto.publicHotel.*;
import com.tourism.booking.exception.ApiException;
import com.tourism.booking.exception.ErrorCode;
import com.tourism.booking.model.Hotel;
import com.tourism.booking.model.Room;
import com.tourism.booking.model.RoomType;
import com.tourism.booking.repository.IPublicHotelRepository;
import com.tourism.booking.repository.IRoomRepository;
import com.tourism.booking.service.IPublicHotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicHotelService implements IPublicHotelService {
        private final IPublicHotelRepository hotelRepository;
        private final IRoomRepository roomRepository;

        @Override
        @Transactional(readOnly = true)
        public Optional<HotelDetailPublicDTO> getHotelDetail(Long hotelId, LocalDate checkIn, LocalDate checkOut) {
                // 1) validate
                if (checkIn == null || checkOut == null || !checkIn.isBefore(checkOut)) {
                        throw new ApiException(ErrorCode.INVALID_DATE_RANGE);
                }

                // 2) lấy hotel (nếu không tìm thấy sẽ throw)
                Hotel hotel = hotelRepository.findById(hotelId)
                                .orElseThrow(() -> new ApiException(ErrorCode.HOTEL_NOT_EXIST));

                // 3) lấy danh sách phòng trống
                List<Room> availableRooms = roomRepository.findAvailableRoomsByHotelAndDateRange(
                                hotelId, checkIn, checkOut);

                // 4) nhóm theo roomType
                Map<RoomType, List<Room>> roomsGroupedByType = availableRooms.stream()
                                .collect(Collectors.groupingBy(Room::getRoom_type));

                // 5) tạo danh sách RoomTypePublicDTO
                List<RoomTypePublicDTO> roomTypeDTOs = roomsGroupedByType.entrySet().stream()
                                .map(entry -> {
                                        RoomType roomType = entry.getKey();
                                        List<Room> rooms = entry.getValue();

                                        List<RoomPublicDTO> roomDTOs = rooms.stream()
                                                        .map(r -> RoomPublicDTO.builder()
                                                                        .id_room(r.getId_room())
                                                                        .number_bed(r.getNumber_bed())
                                                                        .number_rooms(r.getNumber_rooms())
                                                                        .price(r.getPrice())
                                                                        .build())
                                                        .collect(Collectors.toList());

                                        return RoomTypePublicDTO.builder()
                                                        .room_type_id(roomType.getRoom_type_id())
                                                        .type_name(roomType.getType_name())
                                                        .description(roomType.getDescription())
                                                        .number_room(roomDTOs.size())
                                                        .room_image(roomType.getRoom_image())
                                                        .rooms(roomDTOs)
                                                        .build();
                                }).collect(Collectors.toList());

                // 6) map services
                List<ServicePublicDTO> services = hotel.getServices() != null ? hotel.getServices().stream()
                                .map(s -> ServicePublicDTO.builder()
                                                .service_id(s.getService_id())
                                                .service_name(s.getService_name())
                                                .build())
                                .collect(Collectors.toList()) : List.of();

                // 7) build DTO chính
                HotelDetailPublicDTO dto = HotelDetailPublicDTO.builder()
                                .hotel_id(hotel.getHotel_id())
                                .name(hotel.getName())
                                .image(hotel.getImage())
                                .address(hotel.getAddress())
                                .hotline(hotel.getHotline())
                                .description(hotel.getDescription())
                                .services(services)
                                .roomTypes(roomTypeDTOs)
                                .build();

                return Optional.of(dto);
        }

        @Override
        public Page<HotelPublicDTO> getHotels(Pageable pageable) {
                return hotelRepository.findAll(pageable)
                                .map(this::toDto);
        }

        private HotelPublicDTO toDto(Hotel h) {
                return HotelPublicDTO.builder()
                                .hotel_id(h.getHotel_id())
                                .name(h.getName())
                                .image(h.getImage())
                                .address(h.getAddress())
                                .hotline(h.getHotline())
                                .description(h.getDescription())
                                .build();
        }

}

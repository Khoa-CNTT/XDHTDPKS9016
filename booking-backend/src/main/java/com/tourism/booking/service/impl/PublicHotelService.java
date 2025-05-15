package com.tourism.booking.service.impl;


import com.tourism.booking.dto.publicHotel.*;
import com.tourism.booking.model.Hotel;
import com.tourism.booking.repository.IPublicHotelRepository;
import com.tourism.booking.repository.IRoomRepository;
import com.tourism.booking.service.IPublicHotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicHotelService implements IPublicHotelService {
    private final IPublicHotelRepository hotelRepository;
    private final IRoomRepository roomRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<HotelDetailPublicDTO> getHotelDetail(Long hotelId, LocalDate checkIn, int los) {
        LocalDate checkOut = checkIn.plusDays(los);

        return hotelRepository.findDetailById(hotelId)
                .map(hotel -> {
                    // 1) Map dịch vụ
                    var services = hotel.getServices().stream()
                            .map(s -> ServicePublicDTO.builder()
                                    .service_id(s.getService_id())
                                    .service_name(s.getService_name())
                                    .build())
                            .collect(Collectors.toList());

                    // 2) Map roomTypes + lọc phòng trống theo ngày
                    var roomTypes = hotel.getRoomTypes().stream()
                            .map(rt -> {
                                // Gọi repository lọc phòng trống
                                var availableRooms = roomRepository.findAvailableRoomsByDateRange(
                                        rt.getRoom_type_id(),
                                        checkIn,
                                        checkOut
                                );

                                var roomsDto = availableRooms.stream()
                                        .map(r -> RoomPublicDTO.builder()
                                                .id_room(r.getId_room())
                                                .number_bed(r.getNumber_bed())
                                                .price(r.getPrice())
                                                .build())
                                        .collect(Collectors.toList());

                                return RoomTypePublicDTO.builder()
                                        .room_type_id(rt.getRoom_type_id())
                                        .type_name(rt.getType_name())
                                        .number_room(rt.getNumber_room())
                                        .description(rt.getDescription())
                                        .room_image(rt.getRoom_image())
                                        .rooms(roomsDto)
                                        .build();
                            })
                            .collect(Collectors.toList());

                    // 3) Build DTO tổng
                    return HotelDetailPublicDTO.builder()
                            .id(hotel.getHotel_id())
                            .name(hotel.getName())
                            .image(hotel.getImage())
                            .address(hotel.getAddress())
                            .hotline(hotel.getHotline())
                            .description(hotel.getDescription())
                            .services(services)
                            .roomTypes(roomTypes)
                            .build();
                });
    }

    @Override
    public Page<HotelPublicDTO> getHotels(Pageable pageable) {
        return hotelRepository.findAll(pageable)
                .map(this::toDto);
    }

    private HotelPublicDTO toDto(Hotel h) {
        return HotelPublicDTO.builder()
                .name(h.getName())
                .image(h.getImage())
                .address(h.getAddress())
                .hotline(h.getHotline())
                .description(h.getDescription())
                .build();
    }

}

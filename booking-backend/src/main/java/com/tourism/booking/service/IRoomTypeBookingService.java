package com.tourism.booking.service;

import com.tourism.booking.dto.booking.RoomTypeDTO;

import java.util.List;

public interface IRoomTypeBookingService {
    RoomTypeDTO getRoomTypeById(Long id);

    List<RoomTypeDTO> getRoomTypesByHotelId(Long hotelId);

    List<RoomTypeDTO> getAvailableRoomTypesByHotelId(Long hotelId);
}
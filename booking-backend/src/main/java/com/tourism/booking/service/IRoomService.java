package com.tourism.booking.service;

import com.tourism.booking.dto.RoomDTO;

import java.time.LocalDate;
import java.util.List;

public interface IRoomService {
    RoomDTO getRoomById(Long id);

    List<RoomDTO> getRoomsByHotelId(Long hotelId);

    List<RoomDTO> getRoomsByRoomTypeId(Long roomTypeId);

    List<RoomDTO> getAvailableRooms(Long roomTypeId, LocalDate checkInDate, LocalDate checkOutDate);

    RoomDTO updateRoomStatus(Long id, String status);
}
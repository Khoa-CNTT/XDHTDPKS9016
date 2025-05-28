package com.tourism.booking.service;

import com.tourism.booking.dto.room.CreateRoomRequest;
import com.tourism.booking.dto.room.CreateRoomTypeRequest;
import com.tourism.booking.dto.room.RoomManagementResponseDTO;
import com.tourism.booking.model.Room;
import com.tourism.booking.model.RoomType;

import java.time.LocalDate;

public interface IRoomManagementService {

    RoomManagementResponseDTO getRoomManagementByHotelId(Long hotelId);

    RoomManagementResponseDTO getRoomManagementByHotelIdAndDateRange(
            Long hotelId,
            LocalDate checkInDate,
            LocalDate checkOutDate);

    RoomType createRoomType(CreateRoomTypeRequest request);

    Room createRoom(CreateRoomRequest request);
}
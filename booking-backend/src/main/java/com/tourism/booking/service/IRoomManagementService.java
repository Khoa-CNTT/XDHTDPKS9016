package com.tourism.booking.service;

import com.tourism.booking.dto.room.CreateRoomRequest;
import com.tourism.booking.dto.room.CreateRoomTypeRequest;
import com.tourism.booking.dto.room.RoomManagementResponseDTO;
import com.tourism.booking.model.Room;
import com.tourism.booking.model.RoomType;

import java.time.LocalDate;

public interface IRoomManagementService {

    // Lấy thông tin quản lý phòng theo khách sạn
    RoomManagementResponseDTO getRoomManagementByHotelId(Long hotelId);

    // Lấy thông tin quản lý phòng theo khách sạn và khoảng thời gian
    RoomManagementResponseDTO getRoomManagementByHotelIdAndDateRange(
            Long hotelId,
            LocalDate checkInDate,
            LocalDate checkOutDate);

    // Thêm loại phòng mới
    RoomType createRoomType(CreateRoomTypeRequest request);

    // Thêm phòng mới
    Room createRoom(CreateRoomRequest request);
}
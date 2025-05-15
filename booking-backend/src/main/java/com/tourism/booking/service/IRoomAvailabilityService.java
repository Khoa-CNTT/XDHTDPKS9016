package com.tourism.booking.service;

import com.tourism.booking.dto.room.RoomAvailabilityRequestDTO;
import com.tourism.booking.dto.room.RoomAvailabilityResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface IRoomAvailabilityService {
    List<RoomAvailabilityResponseDTO> checkAvailability(RoomAvailabilityRequestDTO request);

    int getAvailableRoomsCount(Long roomTypeId, LocalDate checkInDate, LocalDate checkOutDate);
}
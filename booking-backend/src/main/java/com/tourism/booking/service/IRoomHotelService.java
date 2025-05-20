package com.tourism.booking.service;

import com.tourism.booking.dto.room.RoomRequest;
import com.tourism.booking.dto.room.RoomResponse;

import java.util.List;

public interface IRoomHotelService {
    RoomResponse create(RoomRequest request);
    RoomResponse update(Long id, RoomRequest request);
    void delete(Long id);
    RoomResponse getById(Long id);
    List<RoomResponse> getRoomsByAccountId(Long accountId);
}

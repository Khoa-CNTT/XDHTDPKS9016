package com.tourism.booking.service;

import com.tourism.booking.dto.room.RoomRequest;
import com.tourism.booking.dto.room.RoomResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRoomHotelService {
    RoomResponse create(RoomRequest request);
    RoomResponse update(Long id, RoomRequest request);
    void delete(Long id);
    RoomResponse getById(Long id);
    Page<RoomResponse> getRoomsByAccountId(Long accountId, Pageable pageable);
}

package com.tourism.booking.service;

import com.tourism.booking.model.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IRoomTypeService {

    Optional<RoomType> getRoomTypeById(Long id);

    RoomType createRoomType(RoomType roomType);

    RoomType updateRoomType(Long id, RoomType roomType);

    void deleteRoomType(Long id);

    Page<RoomType> getRoomTypesByAccountId(Long accountId, Pageable pageable);

}

package com.tourism.booking.service.impl;

import com.tourism.booking.model.RoomType;
import com.tourism.booking.repository.IRoomTypeRepository;
import com.tourism.booking.service.IRoomTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomTypeService implements IRoomTypeService {
    private final IRoomTypeRepository roomTypeRepository;


    @Override
    public Optional<RoomType> getRoomTypeById(Long id) {
        return roomTypeRepository.findById(id);
    }

    @Override
    public RoomType createRoomType(RoomType roomType) {
        return roomTypeRepository.save(roomType);
    }

    @Override
    public RoomType updateRoomType(Long id, RoomType roomType) {
        return roomTypeRepository.findById(id)
                .map(existing -> {
                    existing.setType_name(roomType.getType_name());
                    existing.setDescription(roomType.getDescription());
                    existing.setNumber_room(roomType.getNumber_room());
                    existing.setRoom_image(roomType.getRoom_image());
                    return roomTypeRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("RoomType not found with id: " + id));
    }

    @Override
    public void deleteRoomType(Long id) {
        roomTypeRepository.deleteById(id);
    }

    @Override
    public Page<RoomType> getRoomTypesByAccountId(Long accountId, Pageable pageable) {
        return roomTypeRepository.findRoomTypesByAccountId(accountId, pageable);
    }
}

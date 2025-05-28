package com.tourism.booking.service.impl;

import com.tourism.booking.dto.booking.RoomTypeDTO;
import com.tourism.booking.model.RoomType;
import com.tourism.booking.repository.IRoomTypeRepository;
import com.tourism.booking.repository.IRoomRepository;
import com.tourism.booking.service.IRoomTypeBookingService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class RoomTypeBookingService implements IRoomTypeBookingService {
    IRoomTypeRepository roomTypeRepository;
    IRoomRepository roomRepository;

    @Override
    public RoomTypeDTO getRoomTypeById(Long roomTypeId) {
        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy loại phòng với ID: " + roomTypeId));
        return convertToDTO(roomType);
    }

    @Override
    public List<RoomTypeDTO> getRoomTypesByHotelId(Long hotelId) {
        List<RoomType> roomTypes = roomTypeRepository.findByHotelId(hotelId);
        return roomTypes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<RoomTypeDTO> getAvailableRoomTypesByHotelId(Long hotelId) {
        List<RoomType> roomTypes = roomTypeRepository.findAvailableRoomTypesByHotelId(hotelId);
        return roomTypes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    public void updateAvailableRoom(Long roomTypeId, boolean isBooking) {
        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy loại phòng với ID: " + roomTypeId));

        if (isBooking) {
            if (roomType.getNumber_room() > 0) {
                roomType.setNumber_room(roomType.getNumber_room() - 1);
            } else {
                throw new IllegalStateException("Không còn phòng trống loại này");
            }
        } else {
            roomType.setNumber_room(roomType.getNumber_room() + 1);
        }

        roomTypeRepository.save(roomType);
    }

    private RoomTypeDTO convertToDTO(RoomType roomType) {
        RoomTypeDTO dto = new RoomTypeDTO();
        dto.setId(roomType.getRoom_type_id());
        dto.setName(roomType.getType_name());
        dto.setDescription(roomType.getDescription());
        dto.setTotalRoomCount(roomType.getNumber_room());
        List<RoomTypeDTO.RoomDTO> availableRooms = roomRepository.findByRoomType(roomType).stream()
                .map(room -> {
                    RoomTypeDTO.RoomDTO roomDTO = new RoomTypeDTO.RoomDTO();
                    roomDTO.setId(room.getId_room());
                    roomDTO.setNumberBed(room.getNumber_bed());
                    roomDTO.setPrice(room.getPrice());
                    roomDTO.setStatus(room.getStatus());
                    return roomDTO;
                })
                .collect(Collectors.toList());
        dto.setAvailableRooms(availableRooms);

        return dto;
    }
}

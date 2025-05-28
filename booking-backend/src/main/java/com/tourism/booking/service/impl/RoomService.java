package com.tourism.booking.service.impl;

import com.tourism.booking.dto.RoomDTO;
import com.tourism.booking.model.Room;
import com.tourism.booking.repository.IRoomRepository;
import com.tourism.booking.service.IRoomService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class RoomService implements IRoomService {

    IRoomRepository roomRepository;

    @Override
    public RoomDTO getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Phòng không tồn tại với ID: " + id));
        return convertToDTO(room);
    }

    @Override
    public List<RoomDTO> getRoomsByHotelId(Long hotelId) {
        List<Room> rooms = roomRepository.findByRoomTypeHotelHotelId(hotelId);
        return rooms.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<RoomDTO> getRoomsByRoomTypeId(Long roomTypeId) {
        List<Room> rooms = roomRepository.findByRoomTypeRoomTypeId(roomTypeId);
        return rooms.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<RoomDTO> getAvailableRooms(Long roomTypeId, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Room> rooms = roomRepository.findAvailableRoomsByDateRange(roomTypeId, checkInDate, checkOutDate);
        return rooms.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RoomDTO updateRoomStatus(Long id, String status) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Phòng không tồn tại với ID: " + id));
        room.setStatus(status);
        room = roomRepository.save(room);
        return convertToDTO(room);
    }

    private RoomDTO convertToDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setId(room.getId_room());
        dto.setNumberBed(room.getNumber_bed());
        dto.setPrice(room.getPrice());
        dto.setRoomTypeId(room.getRoom_type().getRoom_type_id());
        dto.setRoomTypeName(room.getRoom_type().getType_name());
        dto.setHotelId(room.getRoom_type().getHotel().getHotel_id());
        dto.setHotelName(room.getRoom_type().getHotel().getName());
        dto.setStatus(room.getStatus());

        dto.setAverageRating(0.0);
        dto.setTotalRatings(0);

        return dto;
    }
}
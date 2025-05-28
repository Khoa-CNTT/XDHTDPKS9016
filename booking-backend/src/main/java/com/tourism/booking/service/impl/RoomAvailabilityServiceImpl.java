package com.tourism.booking.service.impl;

import com.tourism.booking.dto.room.RoomAvailabilityRequestDTO;
import com.tourism.booking.dto.room.RoomAvailabilityResponseDTO;
import com.tourism.booking.model.Room;
import com.tourism.booking.model.RoomType;
import com.tourism.booking.repository.IRoomRepository;
import com.tourism.booking.repository.IRoomTypeRepository;
import com.tourism.booking.service.IRoomAvailabilityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoomAvailabilityServiceImpl implements IRoomAvailabilityService {

        private final IRoomRepository roomRepository;
        private final IRoomTypeRepository roomTypeRepository;

        @Override
        public List<RoomAvailabilityResponseDTO> checkAvailability(RoomAvailabilityRequestDTO request) {
                List<RoomAvailabilityResponseDTO> availabilityList = new ArrayList<>();

                if (request.getRoomTypeId() != null) {
                        RoomType roomType = roomTypeRepository.findById(request.getRoomTypeId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Room type not found: " + request.getRoomTypeId()));

                        int availableRooms = getAvailableRoomsCount(
                                        roomType.getRoom_type_id(),
                                        request.getCheckInDate(),
                                        request.getCheckOutDate());

                        if (availableRooms > 0) {
                                RoomAvailabilityResponseDTO availability = createAvailabilityDTO(
                                                roomType, availableRooms, request.getCheckInDate(),
                                                request.getCheckOutDate());
                                availabilityList.add(availability);
                        }
                } else {
                        List<RoomType> allRoomTypes = roomTypeRepository.findAll();

                        for (RoomType roomType : allRoomTypes) {
                                int availableRooms = getAvailableRoomsCount(
                                                roomType.getRoom_type_id(),
                                                request.getCheckInDate(),
                                                request.getCheckOutDate());

                                if (availableRooms > 0) {
                                        RoomAvailabilityResponseDTO availability = createAvailabilityDTO(
                                                        roomType, availableRooms, request.getCheckInDate(),
                                                        request.getCheckOutDate());
                                        availabilityList.add(availability);
                                }
                        }
                }

                return availabilityList;
        }

        @Override
        public int getAvailableRoomsCount(Long roomTypeId, LocalDate checkInDate, LocalDate checkOutDate) {
                List<Room> availableRooms = roomRepository.findAvailableRoomsByDateRange(
                                roomTypeId, checkInDate, checkOutDate);
                return availableRooms.size();
        }

        private RoomAvailabilityResponseDTO createAvailabilityDTO(
                        RoomType roomType, int availableCount, LocalDate checkInDate, LocalDate checkOutDate) {
                RoomAvailabilityResponseDTO dto = new RoomAvailabilityResponseDTO();
                dto.setRoomTypeId(roomType.getRoom_type_id());
                dto.setRoomTypeName(roomType.getType_name());
                dto.setAvailableRooms(availableCount);
                dto.setCheckInDate(checkInDate);
                dto.setCheckOutDate(checkOutDate);
                return dto;
        }
}
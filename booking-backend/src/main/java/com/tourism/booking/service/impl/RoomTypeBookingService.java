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

    /**
     * Lấy thông tin chi tiết của một loại phòng
     * Nghiệp vụ: Hiển thị thông tin chi tiết của loại phòng cho người dùng
     */
    @Override
    public RoomTypeDTO getRoomTypeById(Long roomTypeId) {
        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy loại phòng với ID: " + roomTypeId));
        return convertToDTO(roomType);
    }

    /**
     * Lấy danh sách các loại phòng của một khách sạn
     * Nghiệp vụ: Hiển thị danh sách các loại phòng có sẵn của khách sạn
     */
    @Override
    public List<RoomTypeDTO> getRoomTypesByHotelId(Long hotelId) {
        List<RoomType> roomTypes = roomTypeRepository.findByHotelId(hotelId);
        return roomTypes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Lấy danh sách các loại phòng còn trống của một khách sạn
     * Nghiệp vụ: Hiển thị cho người dùng chỉ những loại phòng còn khả dụng để đặt
     */
    @Override
    public List<RoomTypeDTO> getAvailableRoomTypesByHotelId(Long hotelId) {
        List<RoomType> roomTypes = roomTypeRepository.findAvailableRoomTypesByHotelId(hotelId);
        return roomTypes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Cập nhật số lượng phòng có sẵn khi đặt phòng thành công
     * Nghiệp vụ: Giảm số lượng phòng có sẵn khi có booking được xác nhận
     */
    public void updateAvailableRoom(Long roomTypeId, boolean isBooking) {
        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy loại phòng với ID: " + roomTypeId));

        if (isBooking) {
            // Giảm số phòng trống khi có booking
            if (roomType.getNumber_room() > 0) {
                roomType.setNumber_room(roomType.getNumber_room() - 1);
            } else {
                throw new IllegalStateException("Không còn phòng trống loại này");
            }
        } else {
            // Tăng số phòng trống khi hủy booking
            roomType.setNumber_room(roomType.getNumber_room() + 1);
        }

        roomTypeRepository.save(roomType);
    }

    private RoomTypeDTO convertToDTO(RoomType roomType) {
        RoomTypeDTO dto = new RoomTypeDTO();
        dto.setId(roomType.getRoom_type_id());
        dto.setName(roomType.getType_name());
        dto.setDescription(roomType.getDescription());

        // Set additional available fields
        dto.setTotalRoomCount(roomType.getNumber_room());

        // Convert available rooms using the nested RoomDTO class
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

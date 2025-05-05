package com.tourism.booking.service.impl;
import com.tourism.booking.dto.booking.RoomTypeDTO;
import com.tourism.booking.model.RoomType;
import com.tourism.booking.repository.IRoomTypeRepository;
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
public class RoomTypeBookingService {
    IRoomTypeRepository roomTypeRepository;

    /**
     * Lấy thông tin chi tiết của một loại phòng
     * Nghiệp vụ: Hiển thị thông tin chi tiết của loại phòng cho người dùng
     */
    public RoomTypeDTO getRoomTypeById(Long roomTypeId) {
        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy loại phòng với ID: " + roomTypeId));
        return convertToDTO(roomType);
    }

    /**
     * Lấy danh sách các loại phòng của một khách sạn
     * Nghiệp vụ: Hiển thị danh sách các loại phòng có sẵn của khách sạn
     */
    public List<RoomTypeDTO> getRoomTypesByHotelId(Long hotelId) {
        List<RoomType> roomTypes = roomTypeRepository.findByHotelId(hotelId);
        return roomTypes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Lấy danh sách các loại phòng còn trống của một khách sạn
     * Nghiệp vụ: Hiển thị cho người dùng chỉ những loại phòng còn khả dụng để đặt
     */
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
            if (roomType.getAvailable_room() > 0) {
                roomType.setAvailable_room(roomType.getAvailable_room() - 1);
            } else {
                throw new IllegalStateException("Không còn phòng trống loại này");
            }
        } else {
            // Tăng số phòng trống khi hủy booking
            roomType.setAvailable_room(roomType.getAvailable_room() + 1);
        }

        roomTypeRepository.save(roomType);
    }

    private RoomTypeDTO convertToDTO(RoomType roomType) {
        RoomTypeDTO dto = new RoomTypeDTO();
        dto.setRoomTypeId(roomType.getRoom_type_id());
        dto.setTypeName(roomType.getType_name());
        dto.setNumberBed(roomType.getNumber_bed());
        dto.setMaximumPeople(roomType.getMaximum_people());
        dto.setPrice(roomType.getPrice());
        dto.setDescription(roomType.getDescription());
        dto.setRoomImage(roomType.getRoom_image());
        dto.setAvailableRoom(roomType.getAvailable_room());
        dto.setStatus(roomType.getStatus());
        dto.setHotelId(roomType.getHotel().getHotel_id());
        return dto;
    }
}

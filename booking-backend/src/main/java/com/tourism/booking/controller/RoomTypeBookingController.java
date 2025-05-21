package com.tourism.booking.controller;

import com.tourism.booking.dto.booking.RoomTypeDTO;
import com.tourism.booking.service.IRoomTypeBookingService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/roomtypes")
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class RoomTypeBookingController {
    IRoomTypeBookingService roomTypeService;

    /**
     * API lấy chi tiết loại phòng theo ID
     * Nghiệp vụ: Hiển thị chi tiết loại phòng cho người dùng
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoomTypeDTO> getRoomTypeById(@PathVariable Long id) {
        RoomTypeDTO roomType = roomTypeService.getRoomTypeById(id);
        return ResponseEntity.ok(roomType);
    }

    /**
     * API lấy danh sách loại phòng theo khách sạn
     * Nghiệp vụ: Hiển thị danh sách loại phòng của khách sạn
     */
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<?> getRoomTypesByHotelId(@PathVariable Long hotelId) {
        List<RoomTypeDTO> roomTypes = roomTypeService.getRoomTypesByHotelId(hotelId);
        return ResponseEntity.ok(roomTypes);
    }

    /**
     * API lấy danh sách loại phòng còn trống theo khách sạn
     * Nghiệp vụ: Hiển thị danh sách loại phòng còn trống của khách sạn
     */
    @GetMapping("/hotel/{hotelId}/available")
    public ResponseEntity<?> getAvailableRoomTypesByHotelId(@PathVariable Long hotelId) {
        List<RoomTypeDTO> roomTypes = roomTypeService.getAvailableRoomTypesByHotelId(hotelId);
        return ResponseEntity.ok(roomTypes);
    }
}

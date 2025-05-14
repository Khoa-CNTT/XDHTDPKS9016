package com.tourism.booking.controller;

import com.tourism.booking.dto.RoomDTO;
import com.tourism.booking.service.IRoomService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/rooms")
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoomController {
    IRoomService roomService;

    /**
     * API lấy chi tiết phòng theo ID
     * Nghiệp vụ: Hiển thị chi tiết phòng cho người dùng
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable Long id) {
        RoomDTO room = roomService.getRoomById(id);
        return ResponseEntity.ok(room);
    }

    /**
     * API lấy danh sách phòng theo khách sạn
     * Nghiệp vụ: Hiển thị danh sách phòng của khách sạn
     */
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<RoomDTO>> getRoomsByHotelId(@PathVariable Long hotelId) {
        List<RoomDTO> rooms = roomService.getRoomsByHotelId(hotelId);
        return ResponseEntity.ok(rooms);
    }

    /**
     * API lấy danh sách phòng theo loại phòng
     * Nghiệp vụ: Hiển thị danh sách phòng của một loại phòng
     */
    @GetMapping("/roomtype/{roomTypeId}")
    public ResponseEntity<List<RoomDTO>> getRoomsByRoomTypeId(@PathVariable Long roomTypeId) {
        List<RoomDTO> rooms = roomService.getRoomsByRoomTypeId(roomTypeId);
        return ResponseEntity.ok(rooms);
    }

    /**
     * API lấy danh sách phòng có sẵn theo loại phòng và thời gian
     * Nghiệp vụ: Tìm phòng còn trống cho việc đặt phòng
     */
    @GetMapping("/available")
    public ResponseEntity<List<RoomDTO>> getAvailableRooms(
            @RequestParam Long roomTypeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate) {
        List<RoomDTO> rooms = roomService.getAvailableRooms(roomTypeId, checkInDate, checkOutDate);
        return ResponseEntity.ok(rooms);
    }

    /**
     * API cập nhật trạng thái phòng
     * Nghiệp vụ: Quản lý đánh dấu phòng đang bảo trì, đang dọn dẹp, sẵn sàng
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateRoomStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        try {
            RoomDTO room = roomService.updateRoomStatus(id, status);
            return ResponseEntity.ok(room);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

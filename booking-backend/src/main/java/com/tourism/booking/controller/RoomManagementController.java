package com.tourism.booking.controller;

import com.tourism.booking.dto.room.CreateRoomRequest;
import com.tourism.booking.dto.room.CreateRoomTypeRequest;
import com.tourism.booking.dto.room.RoomManagementResponseDTO;
import com.tourism.booking.model.Room;
import com.tourism.booking.model.RoomType;
import com.tourism.booking.service.IRoomManagementService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/room-management")
@CrossOrigin(origins = "*")
public class RoomManagementController {

    private static final Logger logger = LoggerFactory.getLogger(RoomManagementController.class);

    @Autowired
    private IRoomManagementService roomManagementService;

    /**
     * Lấy thông tin quản lý phòng theo khách sạn
     */
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<?> getRoomManagementByHotel(@PathVariable Long hotelId) {
        logger.info("Request to get room management for hotel ID: {}", hotelId);

        try {
            RoomManagementResponseDTO response = roomManagementService.getRoomManagementByHotelId(hotelId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting room management: {}", e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Lấy thông tin quản lý phòng theo khách sạn và khoảng thời gian
     */
    @GetMapping("/hotel/{hotelId}/availability")
    public ResponseEntity<?> getRoomManagementWithDateRange(
            @PathVariable Long hotelId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate) {

        logger.info("Request to get room management for hotel ID: {} from {} to {}",
                hotelId, checkInDate, checkOutDate);

        try {
            RoomManagementResponseDTO response = roomManagementService
                    .getRoomManagementByHotelIdAndDateRange(hotelId, checkInDate, checkOutDate);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting room management with date range: {}", e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Tạo loại phòng mới
     */
    @PostMapping("/room-type")
    public ResponseEntity<?> createRoomType(@Valid @RequestBody CreateRoomTypeRequest request) {
        logger.info("Request to create new room type for hotel ID: {}", request.getHotelId());

        try {
            RoomType roomType = roomManagementService.createRoomType(request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Room type created successfully");
            response.put("roomTypeId", roomType.getRoom_type_id());
            response.put("typeName", roomType.getType_name());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error creating room type: {}", e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Tạo phòng mới cho một loại phòng
     */
    @PostMapping("/room")
    public ResponseEntity<?> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        logger.info("Request to create new room for room type ID: {}", request.getRoomTypeId());

        try {
            Room room = roomManagementService.createRoom(request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Room created successfully");
            response.put("roomId", room.getId_room());
            response.put("numberBeds", room.getNumber_bed());
            response.put("price", room.getPrice());
            response.put("status", room.getStatus());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error creating room: {}", e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Tạo loại phòng mới cho khách sạn với ID cụ thể (đơn giản hóa quy trình API)
     */
    @PostMapping("/hotel/{hotelId}/room-type")
    public ResponseEntity<?> createRoomTypeForHotel(
            @PathVariable Long hotelId,
            @Valid @RequestBody CreateRoomTypeRequest request) {

        // Đảm bảo hotelId trong path và request trùng khớp
        request.setHotelId(hotelId);
        logger.info("Request to create new room type for hotel ID (path): {}", hotelId);

        try {
            RoomType roomType = roomManagementService.createRoomType(request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Room type created successfully");
            response.put("roomTypeId", roomType.getRoom_type_id());
            response.put("typeName", roomType.getType_name());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error creating room type: {}", e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
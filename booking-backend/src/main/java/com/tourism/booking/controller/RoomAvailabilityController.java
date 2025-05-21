package com.tourism.booking.controller;

import com.tourism.booking.dto.room.RoomAvailabilityRequestDTO;
import com.tourism.booking.dto.room.RoomAvailabilityResponseDTO;
import com.tourism.booking.service.IRoomAvailabilityService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/room-availability")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class RoomAvailabilityController {

        private final IRoomAvailabilityService roomAvailabilityService;

        @GetMapping("/check")
        public ResponseEntity<List<RoomAvailabilityResponseDTO>> checkAvailability(
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
                        @RequestParam(required = false) Long roomTypeId) {

                RoomAvailabilityRequestDTO request = new RoomAvailabilityRequestDTO(
                                checkInDate, checkOutDate, roomTypeId);

                List<RoomAvailabilityResponseDTO> availability = roomAvailabilityService.checkAvailability(request);

                return ResponseEntity.ok(availability);
        }
//
        @GetMapping("/room-type/{roomTypeId}/count")
        public ResponseEntity<Integer> getAvailableRoomsCount(
                        @PathVariable Long roomTypeId,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate) {

                int availableCount = roomAvailabilityService.getAvailableRoomsCount(
                                roomTypeId, checkInDate, checkOutDate);

                return ResponseEntity.ok(availableCount);
        }
}
package com.tourism.booking.controller;

import com.tourism.booking.dto.page.PageReponse;
import com.tourism.booking.model.Hotel;
import com.tourism.booking.model.RoomType;
import com.tourism.booking.service.IHotelService;
import com.tourism.booking.service.IRoomTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@PreAuthorize("hasRole('SUPPLIER')")
@RestController
@RequestMapping("${api.prefix}/room-types")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class RoomTypeController {
    private final IRoomTypeService roomTypeService;
    private final IHotelService hotelService;

    @GetMapping("/{id}")
    public ResponseEntity<RoomType> getRoomTypeById(@PathVariable Long id) {
        return roomTypeService.getRoomTypeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RoomType> createRoomType(
            Principal principal,
            @RequestBody RoomType roomType
    ) {
        Long accountId = Long.parseLong(principal.getName());
        Hotel hotel = hotelService.getHotelByAccountId(accountId)
                .orElseThrow(() -> new RuntimeException("Hotel not found for account " + accountId));
        roomType.setHotel(hotel);
        RoomType saved = roomTypeService.createRoomType(roomType);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomType> updateRoomType(
            @PathVariable Long id,
            Principal principal,
            @RequestBody RoomType roomType
    ) {
        Long accountId = Long.parseLong(principal.getName());
        if (!hotelService.isOwnerOfRoomType(accountId, id)) {
            return ResponseEntity.status(403).build();
        }
        RoomType updated = roomTypeService.updateRoomType(id, roomType);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomType(
            @PathVariable Long id,
            Principal principal
    ) {
        Long accountId = Long.parseLong(principal.getName());
        if (!hotelService.isOwnerOfRoomType(accountId, id)) {
            return ResponseEntity.status(403).build();
        }
        roomTypeService.deleteRoomType(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public ResponseEntity<?> getMyRoomTypes(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Long accountId = Long.parseLong(principal.getName());
        Pageable pageable = PageRequest.of(page, size);
        Page<RoomType> roomTypes = roomTypeService.getRoomTypesByAccountId(accountId, pageable);
        return ResponseEntity.ok(new PageReponse<>(roomTypes)) ;
    }
}

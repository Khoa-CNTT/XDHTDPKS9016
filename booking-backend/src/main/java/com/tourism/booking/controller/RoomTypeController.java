package com.tourism.booking.controller;

import com.tourism.booking.dto.page.PageReponse;
import com.tourism.booking.exception.ApiException;
import com.tourism.booking.exception.ErrorCode;
import com.tourism.booking.model.Account;
import com.tourism.booking.model.Hotel;
import com.tourism.booking.model.RoomType;
import com.tourism.booking.service.IAccountService;
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

//@PreAuthorize("hasRole('SUPPLIER')")
@RestController
@RequestMapping("${api.prefix}/room-types")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class RoomTypeController {
    private final IRoomTypeService roomTypeService;
    private final IHotelService hotelService;
    private final IAccountService accountService;


    @GetMapping("/{id}")
    public ResponseEntity<RoomType> getRoomTypeById(@PathVariable Long id) {
        return roomTypeService.getRoomTypeById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_EXIST));
    }

    @PostMapping
    public ResponseEntity<RoomType> createRoomType(Principal principal, @RequestBody RoomType roomType) {
        Account acc = accountService.getAccountByUsername(principal.getName());
        Hotel hotel = hotelService.getHotelByAccountId(acc.getAccount_id())
                .orElseThrow(() -> new ApiException(ErrorCode.ROOM_NOT_EXIST));
        roomType.setHotel(hotel);
        RoomType saved = roomTypeService.createRoomType(roomType);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoomType(@PathVariable Long id, Principal principal,
            @RequestBody RoomType roomType) {
        Account acc = accountService.getAccountByUsername(principal.getName());
        if (!hotelService.isOwnerOfRoomType(acc.getAccount_id(), id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room Type is not found");
        }
        RoomType updated = roomTypeService.updateRoomType(id, roomType);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoomType(@PathVariable Long id, Principal principal) {
        Account acc = accountService.getAccountByUsername(principal.getName());
        if (!hotelService.isOwnerOfRoomType(acc.getAccount_id(), id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room Type is not found");
        }
        roomTypeService.deleteRoomType(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public ResponseEntity<?> getMyRoomTypes(Principal principal, @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        Account acc = accountService.getAccountByUsername(principal.getName());
        Pageable pageable = PageRequest.of(page, size);
        Page<RoomType> roomTypes = roomTypeService.getRoomTypesByAccountId(acc.getAccount_id(), pageable);
        return ResponseEntity.ok(new PageReponse<>(roomTypes)) ;
    }
}

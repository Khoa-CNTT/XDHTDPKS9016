package com.tourism.booking.controller;

import com.tourism.booking.dto.page.PageReponse;
import com.tourism.booking.dto.roomType.RoomTypeResponse;
import com.tourism.booking.exception.ApiException;
import com.tourism.booking.exception.ErrorCode;
import com.tourism.booking.mapper.IRoomTypeMapper;
import com.tourism.booking.model.Account;
import com.tourism.booking.model.Hotel;
import com.tourism.booking.model.RoomType;
import com.tourism.booking.service.IAccountService;
import com.tourism.booking.service.IHotelService;
import com.tourism.booking.service.IRoomTypeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("${api.prefix}/room-types")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoomTypeManagementController {

    IRoomTypeService roomTypeService;
    IHotelService hotelService;
    IAccountService accountService;
    IRoomTypeMapper mapper;


    @GetMapping("/{id}")
    public ResponseEntity<RoomTypeResponse> getRoomTypeById(@PathVariable Long id) {
        RoomType rt = roomTypeService.getRoomTypeById(id).orElseThrow(() -> new ApiException(ErrorCode.ROOM_TYPE_NOT_FOUND));
        return ResponseEntity.ok(mapper.toResponse(rt));
    }

    @PostMapping
    public ResponseEntity<RoomTypeResponse> createRoomType(Principal principal, @RequestBody RoomTypeResponse request) {
        Account acc = accountService.getAccountByUsername(principal.getName());
        Hotel hotel = hotelService.getHotelByAccountId(acc.getAccount_id()).orElseThrow(() -> new ApiException(ErrorCode.HOTEL_NOT_EXIST));

        RoomType entity = mapper.toEntity(request);
        entity.setHotel(hotel);

        RoomType saved = roomTypeService.createRoomType(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomTypeResponse> updateRoomType(@PathVariable Long id, Principal principal, @RequestBody RoomTypeResponse request) {
        Account acc = accountService.getAccountByUsername(principal.getName());
        if (!hotelService.isOwnerOfRoomType(acc.getAccount_id(), id)) {
            throw new ApiException(ErrorCode.ROOM_TYPE_NOT_FOUND);
        }

        RoomType update = mapper.toEntity(request);
        RoomType updated = roomTypeService.updateRoomType(id, update);
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomType(@PathVariable Long id, Principal principal) {
        Account acc = accountService.getAccountByUsername(principal.getName());
        if (!hotelService.isOwnerOfRoomType(acc.getAccount_id(), id)) {
            throw new ApiException(ErrorCode.ROOM_TYPE_NOT_FOUND);
        }
        roomTypeService.deleteRoomType(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PageReponse<RoomTypeResponse>> getMyRoomTypes(Principal principal, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {

        Account acc = accountService.getAccountByUsername(principal.getName());
        Pageable pageable = PageRequest.of(page, size);
        Page<RoomType> roomTypes = roomTypeService.getRoomTypesByAccountId(acc.getAccount_id(), pageable);

        PageReponse<RoomTypeResponse> response = new PageReponse<>(roomTypes.map(mapper::toResponse));
        return ResponseEntity.ok(response);
    }
}

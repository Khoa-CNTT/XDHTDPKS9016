package com.tourism.booking.controller;


import com.tourism.booking.dto.page.PageReponse;
import com.tourism.booking.dto.room.RoomRequest;
import com.tourism.booking.dto.room.RoomResponse;
import com.tourism.booking.service.IAccountService;
import com.tourism.booking.service.impl.RoomHotelService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("${api.prefix}/rooms-v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoomHotelController {
    RoomHotelService roomHotelService;
    IAccountService accountService;

        @GetMapping
        public ResponseEntity<PageReponse<RoomResponse>> getMyRooms(Principal principal, @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "50") int size) {
            Long accountId = accountService.getAccountByUsername(principal.getName()).getAccount_id();
            Pageable pageable = PageRequest.of(page, size);

            Page<RoomResponse> rooms = roomHotelService.getRoomsByAccountId(accountId, pageable);
            return ResponseEntity.ok(new PageReponse<>(rooms));
        }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(roomHotelService.getById(id));
    }


    @PostMapping
    public ResponseEntity<RoomResponse> create(@RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomHotelService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> update(@PathVariable Long id,
                                               @RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomHotelService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roomHotelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

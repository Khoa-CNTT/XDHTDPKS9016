package com.tourism.booking.controller;

import com.tourism.booking.dto.hotel.HotelInfoResponse;
import com.tourism.booking.exception.ApiException;
import com.tourism.booking.exception.ErrorCode;
import com.tourism.booking.mapper.IHotelInfoMapper;
import com.tourism.booking.model.Account;
import com.tourism.booking.model.Hotel;
import com.tourism.booking.service.IAccountService;
import com.tourism.booking.service.IHotelService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("${api.prefix}/hotel-info")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin(origins = "http://localhost:5173")
public class HotelInformationController {

     IHotelService hotelService;
     IAccountService accountService;
     IHotelInfoMapper mapper;

    @GetMapping
    public ResponseEntity<HotelInfoResponse> getMyHotel(Principal principal) {
        Account acc = accountService.getAccountByUsername(principal.getName());

        HotelInfoResponse dto = hotelService
                .getHotelByAccountId(acc.getAccount_id())
                .map(mapper::toDto)
                .orElseThrow(() -> new ApiException(ErrorCode.HOTEL_NOT_EXIST));
        return ResponseEntity.ok(dto);
    }

    @PutMapping
    public ResponseEntity<HotelInfoResponse> updateMyHotel(Principal principal, @RequestBody Hotel hotel) {
        Account acc = accountService.getAccountByUsername(principal.getName());
        HotelInfoResponse dto = hotelService.updateHotelInfo(acc.getAccount_id(), hotel);
        return ResponseEntity.ok(dto);
    }
}

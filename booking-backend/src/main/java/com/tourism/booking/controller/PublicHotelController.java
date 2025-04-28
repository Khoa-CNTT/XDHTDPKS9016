package com.tourism.booking.controller;

import com.tourism.booking.dto.page.PageReponse;
import com.tourism.booking.exception.ApiException;
import com.tourism.booking.exception.ErrorCode;
import com.tourism.booking.service.IHotelService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/hotels")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@CrossOrigin("http://localhost:5173/")
public class PublicHotelController {
    IHotelService hotelService;

    @GetMapping
    public ResponseEntity<?> getHotels(Pageable pageable) {
        return ResponseEntity.ok(new PageReponse<>(hotelService.getHotels(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        return hotelService.getHotelById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ApiException(ErrorCode.HOTEL_NOT_EXIST));
    }
}


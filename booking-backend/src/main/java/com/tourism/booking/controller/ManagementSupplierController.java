package com.tourism.booking.controller;

import com.tourism.booking.dto.hotel.CreateHotelRequest;
import com.tourism.booking.dto.hotel.HotelResponse;
import com.tourism.booking.dto.hotel.UpdateHotelRequest;
import com.tourism.booking.dto.page.PageReponse;
import com.tourism.booking.exception.ApiException;
import com.tourism.booking.exception.ErrorCode;
import com.tourism.booking.model.Hotel;
import com.tourism.booking.service.IHotelService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/management-supplier")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@CrossOrigin("http://localhost:5173/")
public class ManagementSupplierController {
    IHotelService hotelService;

    @GetMapping
    public ResponseEntity<?> getAllHotels(Pageable pageable) {
        return ResponseEntity.ok(new PageReponse<>(hotelService.getHotels(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHotelById(@PathVariable("id") Long id) {
        return hotelService.getHotelById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ApiException(ErrorCode.HOTEL_NOT_EXIST));
    }

    @PostMapping
    public ResponseEntity<?> createHotelWithAccount(@RequestBody CreateHotelRequest request) {
        try {
            HotelResponse createdHotel = hotelService.createHotel(request);
            return new ResponseEntity<>(createdHotel, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHotel(@PathVariable("id") Long id, @RequestBody UpdateHotelRequest request) {
        return ResponseEntity.ok(hotelService.updateBasicHotelInfo(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHotel(@PathVariable("id") Long id) {
        hotelService.getHotelById(id).orElseThrow(() -> new ApiException(ErrorCode.HOTEL_NOT_EXIST));
        hotelService.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }
}

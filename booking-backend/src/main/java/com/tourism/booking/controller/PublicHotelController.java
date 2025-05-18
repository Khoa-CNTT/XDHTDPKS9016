package com.tourism.booking.controller;

import com.tourism.booking.dto.page.PageReponse;
import com.tourism.booking.dto.publicHotel.HotelDetailPublicDTO;
import com.tourism.booking.exception.ApiException;
import com.tourism.booking.exception.ErrorCode;
import com.tourism.booking.service.IPublicHotelService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@PreAuthorize("permitAll()")
@RestController
@RequestMapping("${api.prefix}/hotels")
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @AllArgsConstructor
@CrossOrigin(origins = "*")
public class PublicHotelController {
    IPublicHotelService hotelService;

    @GetMapping
    public ResponseEntity<?> getHotels(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(new PageReponse<>(hotelService.getHotels(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDetailPublicDTO> getHotelDetail(
            @PathVariable Long id,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam(defaultValue = "1") int los
    ) {
        LocalDate effectiveCheckIn = (checkIn != null) ? checkIn : LocalDate.now();

        return hotelService.getHotelDetail(id, effectiveCheckIn, los)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ApiException(ErrorCode.HOTEL_NOT_EXIST));
    }
}


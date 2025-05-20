package com.tourism.booking.controller;
import com.tourism.booking.dto.booking.ServiceDTO;
import com.tourism.booking.dto.page.PageReponse;
import com.tourism.booking.service.impl.ServiceBookingService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/services")
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class ServiceBookingController {

     ServiceBookingService serviceService;

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<?> getServicesByHotelId(Pageable pageable, @PathVariable Long hotelId) {
        Page<ServiceDTO> services = serviceService.getServicesByHotelId(pageable, hotelId);
        return ResponseEntity.ok(new PageReponse<>(services));
    }
}

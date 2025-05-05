package com.tourism.booking.controller;
import com.tourism.booking.dto.booking.ServiceDTO;
import com.tourism.booking.service.impl.ServiceBookingService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * API lấy danh sách dịch vụ theo khách sạn
     * Nghiệp vụ: Hiển thị danh sách dịch vụ có thể thêm khi đặt phòng
     */
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<?> getServicesByHotelId(@PathVariable Long hotelId) {
        List<ServiceDTO> services = serviceService.getServicesByHotelId(hotelId);
        return ResponseEntity.ok(services);
    }
}

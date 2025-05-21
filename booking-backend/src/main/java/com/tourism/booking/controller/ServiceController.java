package com.tourism.booking.controller;

import com.tourism.booking.dto.booking.ServiceDTO;
import com.tourism.booking.dto.hotel.HotelInfoResponse;
import com.tourism.booking.dto.page.PageReponse;
import com.tourism.booking.dto.service.ServiceRequest;
import com.tourism.booking.dto.service.ServiceResponse;
import com.tourism.booking.exception.ApiException;
import com.tourism.booking.exception.ErrorCode;
import com.tourism.booking.mapper.IServiceMapper;
import com.tourism.booking.model.Account;
import com.tourism.booking.model.Hotel;
import com.tourism.booking.model.Services;
import com.tourism.booking.model.UserProfile;
import com.tourism.booking.service.IAccountService;
import com.tourism.booking.service.IHotelService;
import com.tourism.booking.service.IServiceService;
import com.tourism.booking.service.impl.ServiceBookingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/management-service")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = { "Authorization", "Content-Type" }, methods = {
        RequestMethod.GET, RequestMethod.OPTIONS })
public class ServiceController {
    IServiceService serviceService;
    IAccountService accountService;
    IHotelService hotelService;
    IServiceMapper serviceMapper;
    ServiceBookingService servicesService;

    @GetMapping
    public ResponseEntity<?> getServicesByHotelId(@PageableDefault(size = 5) Pageable pageable, Principal principal) {
        Account acc = accountService.getAccountByUsername(principal.getName());
        Hotel hotel = hotelService.getHotelByAccountId(acc.getAccount_id())
                .orElseThrow(() -> new ApiException(ErrorCode.HOTEL_NOT_EXIST));
        Page<ServiceDTO> services = servicesService.getServicesByHotelId(pageable, hotel.getHotel_id());
        return ResponseEntity.ok(new PageReponse<>(services));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getServiceById(@PathVariable("id") Long id) {
        Services service = serviceService.getServiceById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.SERVICE_NOT_EXIST));
        return ResponseEntity.ok(serviceMapper.ServiceToServiceResponse(service));
    }

    @PostMapping
    public ResponseEntity<?> createService(@RequestBody ServiceRequest serviceRequest, Principal principal) {
        Account acc = accountService.getAccountByUsername(principal.getName());
        Hotel hotel = hotelService.getHotelByAccountId(acc.getAccount_id())
                .orElseThrow(() -> new ApiException(ErrorCode.HOTEL_NOT_EXIST));
        Services service = new Services();
        service.setService_name(serviceRequest.getService_name());
        service.setService_price(serviceRequest.getService_price());
        service.setService_image(serviceRequest.getService_image());
        service.setDescription(serviceRequest.getDescription());
        service.setHotel(hotel);
        Services saved = serviceService.saveService(service);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(serviceMapper.ServiceToServiceResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateService(@PathVariable("id") Long id, @RequestBody ServiceRequest serviceRequest,
            Principal principal) {
        Account acc = accountService.getAccountByUsername(principal.getName());
        Hotel hotel = hotelService.getHotelByAccountId(acc.getAccount_id())
                .orElseThrow(() -> new ApiException(ErrorCode.HOTEL_NOT_EXIST));
        Services services = serviceService.getServiceById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.SERVICE_NOT_EXIST));
        services.setService_id(id);
        services.setService_name(serviceRequest.getService_name());
        services.setService_price(serviceRequest.getService_price());
        services.setService_image(serviceRequest.getService_image());
        services.setDescription(serviceRequest.getDescription());
        services.setHotel(hotel);
        Services updated = serviceService.saveService(services);

        return ResponseEntity.ok(serviceMapper.ServiceToServiceResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteService(@PathVariable("id") Long id) {
        serviceService.getServiceById(id).orElseThrow(() -> new ApiException(ErrorCode.SERVICE_NOT_EXIST));
        serviceService.deleteServiceById(id);
        return ResponseEntity.noContent().build();
    }
}

package com.tourism.booking.controller;

import com.tourism.booking.dto.page.PageReponse;
import com.tourism.booking.exception.ApiException;
import com.tourism.booking.exception.ErrorCode;
import com.tourism.booking.model.Account;
import com.tourism.booking.model.Hotel;
import com.tourism.booking.model.Services;
import com.tourism.booking.service.IAccountService;
import com.tourism.booking.service.IHotelService;
import com.tourism.booking.service.IServiceService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
//@PreAuthorize("hasRole('USER')")
@RequestMapping("${api.prefix}/management-service")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@CrossOrigin(
        origins = "http://localhost:5173",
        allowedHeaders = {"Authorization", "Content-Type"},
        methods = {RequestMethod.GET, RequestMethod.OPTIONS})
public class ServiceController {
    IServiceService serviceService;
    IAccountService accountService;
    IHotelService hotelService;

    @GetMapping
    public ResponseEntity<?> getAllServiceByHotelId(Pageable pageable, Principal principal) {
        Account acc = accountService.getAccountByUsername(principal.getName());
        return ResponseEntity.ok(new PageReponse<>(serviceService.getServicesByIdHotel(acc.getAccount_id(),pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getServiceById(@PathVariable("id") Long id) {
        return serviceService.getServiceById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ApiException(ErrorCode.SERVICE_NOT_EXIST));
    }

    @PostMapping
    public ResponseEntity<?> createService(@RequestBody Services service, Principal principal) {
        Account acc = accountService.getAccountByUsername(principal.getName());
        System.out.println(acc.getAccount_id());
        Hotel hotel = hotelService.getHotelByAccountId(acc.getAccount_id())
                .orElseThrow(() -> new ApiException(ErrorCode.HOTEL_NOT_EXIST));

        service.setHotel(hotel);
        Services saved = serviceService.saveService(service);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateService(@PathVariable("id") Long id, @RequestBody Services service, Principal principal) {
        Account acc = accountService.getAccountByUsername(principal.getName());
        System.out.println(acc.getAccount_id());
        Hotel hotel = hotelService.getHotelByAccountId(acc.getAccount_id())
                .orElseThrow(() -> new ApiException(ErrorCode.HOTEL_NOT_EXIST));
        service.setService_id(id);
        service.setHotel(hotel);
        return ResponseEntity.ok(serviceService.saveService(service));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteService(@PathVariable("id") Long id) {
        serviceService.getServiceById(id).orElseThrow(() -> new ApiException(ErrorCode.SERVICE_NOT_EXIST));
        serviceService.deleteServiceById(id);
        return ResponseEntity.noContent().build();
    }
}

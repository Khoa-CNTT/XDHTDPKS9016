package com.tourism.booking.controller;


import com.tourism.booking.service.IStatisticalService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/management-statistical")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class StatisticalController {
    IStatisticalService statisticalService;

    @GetMapping
    public ResponseEntity<?> getStatistical() {
        return ResponseEntity.ok(statisticalService.getAll());
    }
}

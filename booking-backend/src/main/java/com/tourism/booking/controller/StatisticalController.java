package com.tourism.booking.controller;


import com.tourism.booking.dto.statistical.QuarterlyStatDTO;
import com.tourism.booking.dto.statistical.StatisticalDTO;
import com.tourism.booking.model.Account;
import com.tourism.booking.service.IAccountService;
import com.tourism.booking.service.IStatisticalService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("${api.prefix}/management-statistical")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class StatisticalController {
    IStatisticalService statisticalService;
    IAccountService accountService;

    @GetMapping("/admin")
    public ResponseEntity<StatisticalDTO> getStatisticalByQuarter(
            @RequestParam int year,
            @RequestParam int quarter) {
        if (quarter < 1 || quarter > 4) {
            return ResponseEntity.badRequest().build();
        }
        StatisticalDTO dto = statisticalService.getByQuarter(year, quarter);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/hotel")
    public ResponseEntity<QuarterlyStatDTO> getStatisticalByHotel(
            @RequestParam int year,
            @RequestParam int quarter,
            Principal principal
    ){
        Account acc = accountService.getAccountByUsername(principal.getName());
        if (quarter < 1 || quarter > 4) {
            return ResponseEntity.badRequest().build();
        }
        QuarterlyStatDTO dto = statisticalService.quarterlyStat(acc.getAccount_id() ,year, quarter);
        return ResponseEntity.ok(dto);
    }

}

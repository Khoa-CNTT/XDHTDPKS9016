package com.tourism.booking.dto.booking;


import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BillDTO {
    private Long billId;
    private BigDecimal totalAmount;
    private BigDecimal deposit;
    private LocalDate printDate;
    private LocalTime printTime;
}
package com.tourism.booking.dto.booking;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BillDTO {
    private Long billId;
    private BigDecimal roomTotal;
    private BigDecimal serviceTotal;
    private BigDecimal total;
    private BigDecimal deposit;
    private int numberOfDays;
}
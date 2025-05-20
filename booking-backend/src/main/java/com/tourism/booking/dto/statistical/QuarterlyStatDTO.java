package com.tourism.booking.dto.statistical;

import java.math.BigDecimal;

public interface QuarterlyStatDTO {
    Long getBookingCount();
    BigDecimal getTotalPayment();
}

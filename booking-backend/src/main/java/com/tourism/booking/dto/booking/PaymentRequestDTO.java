package com.tourism.booking.dto.booking;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequestDTO {
    private Long billId;
    private Long bookingId;
    private BigDecimal amount;
    private String paymentMethod;
    private String transactionId;
    private boolean noBillReference;
}
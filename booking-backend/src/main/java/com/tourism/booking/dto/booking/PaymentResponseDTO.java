package com.tourism.booking.dto.booking;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class PaymentResponseDTO {
    private Long paymentId;
    private Long billId;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private LocalTime paymentTime;
    private String paymentMethod;
    private String transactionId;

    // Thông tin thêm cho UI
    private String customerName;
    private String accountNumber;
    private String status;
}
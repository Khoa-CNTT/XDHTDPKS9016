package com.tourism.booking.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
    private Long paymentId;
    private Long bookingId;
    private Long billId;
    private BigDecimal amount;
    private String paymentMethod;
    private String transactionId;
    private String status;

    // Thay Date bằng LocalDate để phù hợp với entity
    private LocalDate paymentDate;
    private LocalTime paymentTime;

    // Thêm các trường còn thiếu
    private String customerName;
    private String accountNumber;

    // Thêm thông tin hiển thị trạng thái nếu cần
    private String statusDisplay;
}
package com.tourism.booking.controller;

import com.tourism.booking.dto.booking.PaymentRequestDTO;
import com.tourism.booking.dto.booking.PaymentResponseDTO;
import com.tourism.booking.dto.page.PageReponse;
import com.tourism.booking.service.IPaymentService;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Arrays;

@RestController
@RequestMapping("${api.prefix}/payments")
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentBookingController {
    IPaymentService paymentService;
    private static final Logger logger = LoggerFactory.getLogger(PaymentBookingController.class);

    @GetMapping("/history")
    public ResponseEntity<?> getAllPayments(@PageableDefault(size = 5) Pageable pageable) {
        Page<PaymentResponseDTO> payments = paymentService.getAllPayments(pageable);
        return ResponseEntity.ok(new PageReponse<>(payments));
    }

    @PutMapping("/{paymentId}/status")
    public ResponseEntity<?> updatePaymentStatus(
            @PathVariable Long paymentId,
            @RequestParam String status) {
        try {
            logger.info("Nhận request cập nhật trạng thái thanh toán: paymentId={}, status={}", paymentId, status);
            if (!isValidPaymentStatus(status)) {
                logger.warn("Trạng thái không hợp lệ: {}", status);
                return ResponseEntity
                        .badRequest()
                        .body(Map.of(
                                "error",
                                "Trạng thái không hợp lệ. Các trạng thái hợp lệ: PAID, COMPLETED, CANCELLED",
                                "requested_status", status,
                                "valid_statuses", Arrays.asList("PAID", "COMPLETED", "CANCELLED")));
            }

            logger.info("Gọi service để cập nhật trạng thái");
            PaymentResponseDTO payment = paymentService.updatePaymentStatus(paymentId, status);
            logger.info("Cập nhật thành công: {}", payment);
            return ResponseEntity.ok(payment);
        } catch (EntityNotFoundException e) {
            logger.error("Không tìm thấy payment với ID: {}", paymentId);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Lỗi cập nhật trạng thái thanh toán: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Lỗi cập nhật trạng thái thanh toán: " + e.getMessage(),
                            "stack_trace", e.getStackTrace()));
        }
    }


    private boolean isValidPaymentStatus(String status) {
        return Arrays.asList("PAID", "COMPLETED", "CANCELLED").contains(status);
    }

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> processPayment(@RequestBody PaymentRequestDTO request) {
        PaymentResponseDTO payment = paymentService.processPayment(request);
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }

    @GetMapping("/bill/{billId}")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByBillId(@PathVariable Long billId) {
        List<PaymentResponseDTO> payments = paymentService.getPaymentsByBillId(billId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/booking/{bookingId}/status")
    public ResponseEntity<Boolean> isBookingFullyPaid(@PathVariable Long bookingId) {
        boolean isFullyPaid = paymentService.isFullyPaid(bookingId);
        return ResponseEntity.ok(isFullyPaid);
    }
}

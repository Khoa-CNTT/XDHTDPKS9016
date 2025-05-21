package com.tourism.booking.exception;

import com.tourism.booking.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandle {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandle.class);

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> handleApiException(ApiException e) {
        ErrorCode ec = e.getErrorCode();
        ApiResponse body = ApiResponse.builder()
                .code(ec.getCode())
                .message(ec.getMessage())
                .build();
        return ResponseEntity.status(ec.getStatus()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .data(errors)
                .message("Invalid input")
                .code(4000)
                .build());
    }

    // 2) Bắt riêng MissingServletRequestPartException (thiếu part 'file')
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ApiResponse> handleMissingPart(MissingServletRequestPartException e) {
        ApiResponse body = ApiResponse.builder()
                .code(5000)
                .message("Thiếu phần 'file' trong form-data.")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 3) Handle payment-related exceptions specifically
    @ExceptionHandler(PaymentProcessingException.class)
    public ResponseEntity<ApiResponse> handlePaymentException(PaymentProcessingException e) {
        logger.error("Payment processing error: {}", e.getMessage(), e);
        ApiResponse body = ApiResponse.builder()
                .code(5001)
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    // 4) Handle booking-related exceptions
    @ExceptionHandler(BookingProcessingException.class)
    public ResponseEntity<ApiResponse> handleBookingException(BookingProcessingException e) {
        logger.error("Booking processing error: {}", e.getMessage(), e);
        ApiResponse body = ApiResponse.builder()
                .code(5002)
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    // 5) Handle all other exceptions with detailed logging
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleAllOtherExceptions(Exception e) {
        logger.error("Unexpected error: {}", e.getMessage(), e);
        String errorMessage = String.format("Có lỗi xảy ra trên server: %s - %s",
                e.getClass().getSimpleName(), e.getMessage());
        ApiResponse body = ApiResponse.builder()
                .code(5000)
                .message(errorMessage)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ApiResponse> handleMultipart(MultipartException ex) {
        ApiResponse body = ApiResponse.builder()
                .code(5001)
                .message("Có lỗi hình ảnh.")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);

    }
}

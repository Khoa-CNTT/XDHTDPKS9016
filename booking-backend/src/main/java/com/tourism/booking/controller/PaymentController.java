package com.tourism.booking.controller;

import com.tourism.booking.config.VNPayConfig;
import com.tourism.booking.dto.booking.BookingResponseDTO;
import com.tourism.booking.dto.booking.PaymentRequestDTO;
import com.tourism.booking.dto.booking.PaymentResponseDTO;
import com.tourism.booking.model.Payment;
import com.tourism.booking.model.Booking;
import com.tourism.booking.model.Services;
import com.tourism.booking.model.UserProfile;
import com.tourism.booking.repository.IPaymentRepository;
import com.tourism.booking.service.IBookingService;
import com.tourism.booking.service.IPaymentService;
import com.tourism.booking.service.IUserProfileService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/payment")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private IBookingService bookingService;

    @Autowired
    private IPaymentService paymentService;

    @Autowired
    private IPaymentRepository paymentRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private IUserProfileService userProfileService;

    @GetMapping("/booking-payment")
    public ResponseEntity<String> createBookingPayment(@RequestParam("bookingId") Long bookingId) {
        logger.info("Creating payment URL for booking: {}", bookingId);

        try {
            if (bookingId == null) {
                return ResponseEntity.badRequest().body("Error: bookingId must not be null");
            }

            // Get booking information
            BookingResponseDTO booking = bookingService.getBookingById(bookingId);
            if (booking == null) {
                return ResponseEntity.notFound().build();
            }

            // Get payment amount (deposit or full amount)
            long amount = getPaymentAmount(booking);
            if (amount <= 0) {
                return ResponseEntity.badRequest().body("Error: Invalid payment amount");
            }

            // Create VNPay payment URL
            String paymentUrl = createVNPayUrl(bookingId, amount);
            logger.info("Payment URL created successfully for booking: {}", bookingId);

            return ResponseEntity.ok(paymentUrl);
        } catch (Exception e) {
            logger.error("Error creating payment URL: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating payment URL: " + e.getMessage());
        }
    }

    @GetMapping("/payment-callback")
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public ResponseEntity<Map<String, Object>> paymentCallback(
            @RequestParam Map<String, String> queryParams,
            @RequestParam(value = "bookingId", required = false) Long bookingId,
            @RequestParam(value = "userId", required = false) Long userId) {

        logger.info("Payment callback received for booking {}: {}", bookingId, queryParams);
        Map<String, Object> response = new HashMap<>();

        // Check if transaction already processed
        String vnp_TransactionNo = queryParams.get("vnp_TransactionNo");
        if (isTransactionProcessed(vnp_TransactionNo)) {
            logger.info("Transaction already processed: {}", vnp_TransactionNo);
            return createSuccessResponse(vnp_TransactionNo, bookingId);
        }

        try {
            String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");

            if ("00".equals(vnp_ResponseCode)) {
                // 1. Get the temporary booking with services
                BookingResponseDTO tempBookingDTO = bookingService.getBookingById(bookingId);
                if (tempBookingDTO == null) {
                    logger.error("Temporary booking not found: {}", bookingId);
                    return createErrorResponse("Booking not found");
                }

                // Get services before finalizing
                Set<Services> services = bookingService.getServicesByBookingId(bookingId);

                // 2. Create a mock Principal with userId
                Principal mockPrincipal = new Principal() {
                    @Override
                    public String getName() {
                        UserProfile user = userProfileService.findUserProfileEntityById(userId)
                                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
                        return user.getAccount().getUsername();
                    }
                };

                // 3. Finalize booking with mock Principal
                BookingResponseDTO finalizedBooking = bookingService.finalizeBooking(bookingId, mockPrincipal);
                logger.info("Booking finalized successfully: {}", bookingId);

                // 4. Get the finalized booking and save services
                Booking booking = bookingService.getBookingEntityById(finalizedBooking.getBookingId());
                booking.setServices(services);
                bookingService.saveBooking(booking);
                logger.info("Booking services saved successfully for booking: {}", bookingId);

                // 5. Create payment record
                PaymentRequestDTO paymentRequest = createPaymentRequest(finalizedBooking, queryParams);
                PaymentResponseDTO payment = paymentService.processPayment(paymentRequest);
                logger.info("Payment processed successfully: {}", payment.getPaymentId());

                return createSuccessResponse(payment, finalizedBooking);
            } else if ("24".equals(vnp_ResponseCode)) {
                logger.info("Payment cancelled by user for booking: {}", bookingId);
                return handleCancelledPayment(bookingId, vnp_TransactionNo);
            } else {
                logger.warn("Payment failed with code: {} for booking: {}", vnp_ResponseCode, bookingId);
                return handleFailedPayment(vnp_ResponseCode, bookingId, vnp_TransactionNo);
            }
        } catch (Exception e) {
            logger.error("Error processing payment callback: {}", e.getMessage(), e);
            return createErrorResponse(e.getMessage());
        }
    }

    private boolean isTransactionProcessed(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId) != null;
    }

    private ResponseEntity<Map<String, Object>> processSuccessfulPayment(
            Long bookingId, Map<String, String> queryParams, TransactionStatus status, Principal principal) {

        try {
            // 1. Finalize booking
            BookingResponseDTO finalizedBooking = bookingService.finalizeBooking(bookingId, principal);

            // 2. Create payment record
            PaymentRequestDTO paymentRequest = createPaymentRequest(finalizedBooking, queryParams);
            PaymentResponseDTO payment = paymentService.processPayment(paymentRequest);

            // 3. Commit transaction
            transactionManager.commit(status);

            return createSuccessResponse(payment, finalizedBooking);
        } catch (Exception e) {
            transactionManager.rollback(status);
            logger.error("Error processing successful payment: {}", e.getMessage(), e);
            throw e;
        }
    }

    private PaymentRequestDTO createPaymentRequest(BookingResponseDTO booking, Map<String, String> queryParams) {
        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setBookingId(booking.getBookingId());
        request.setBillId(booking.getBill().getBillId());

        String vnp_Amount = queryParams.get("vnp_Amount");
        if (vnp_Amount != null) {
            long amount = Long.parseLong(vnp_Amount) / 100;
            request.setAmount(new BigDecimal(amount));
        }

        request.setPaymentMethod("VNPAY");
        request.setTransactionId(queryParams.get("vnp_TransactionNo"));
        return request;
    }

    private ResponseEntity<Map<String, Object>> handleCancelledPayment(Long bookingId, String transactionNo) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "cancelled");
        response.put("message", "Payment was cancelled by user");
        response.put("bookingId", bookingId);
        response.put("transactionId", transactionNo);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Map<String, Object>> handleFailedPayment(
            String responseCode, Long bookingId, String transactionNo) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "failed");
        response.put("message", "Payment failed with code: " + responseCode);
        response.put("bookingId", bookingId);
        response.put("transactionId", transactionNo);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Map<String, Object>> createSuccessResponse(String transactionNo, Long bookingId) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Payment was already processed");
        response.put("transactionId", transactionNo);
        response.put("bookingId", bookingId);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Map<String, Object>> createSuccessResponse(
            PaymentResponseDTO payment, BookingResponseDTO booking) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Payment processed successfully");
        response.put("payment", payment);
        response.put("booking", booking);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(String errorMessage) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", errorMessage);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private long getPaymentAmount(BookingResponseDTO booking) {
        if (booking.getBill() != null) {
            if (booking.getBill().getDeposit() != null) {
                return booking.getBill().getDeposit().longValue();
            } else if (booking.getBill().getTotal() != null) {
                return booking.getBill().getTotal().longValue();
            }
        }
        return 0;
    }

    private String createVNPayUrl(Long bookingId, long amount) throws UnsupportedEncodingException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        // Get booking information to include user ID
        BookingResponseDTO booking = bookingService.getBookingById(bookingId);
        Long userId = booking.getUser() != null ? booking.getUser().getUser_id() : null;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don dat phong #" + bookingId);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        // Add userId to return URL
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl + "?bookingId=" + bookingId + "&userId=" + userId);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();

        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        return VNPayConfig.vnp_PayUrl + "?" + queryUrl;
    }
}

package com.tourism.booking.controller;

import com.tourism.booking.config.VNPayConfig;
import com.tourism.booking.dto.booking.BookedRoomDTO;
import com.tourism.booking.dto.booking.BookingResponseDTO;
import com.tourism.booking.dto.booking.PaymentRequestDTO;
import com.tourism.booking.dto.booking.PaymentResponseDTO;
import com.tourism.booking.exception.BookingProcessingException;
import com.tourism.booking.exception.PaymentProcessingException;
import com.tourism.booking.model.*;
import com.tourism.booking.repository.IPaymentRepository;
import com.tourism.booking.repository.IRoomRepository;
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
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Collections;

@Controller
@RequestMapping("${api.prefix}/payment")
@CrossOrigin(origins = "*")
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

    @Autowired
    private IRoomRepository roomRepository;

    @GetMapping("/booking-payment")
    @ResponseBody
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

    @GetMapping(value = "/payment-callback")
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public String paymentCallback(
            @RequestParam Map<String, String> queryParams,
            @RequestParam(value = "bookingId", required = false) Long bookingId,
            @RequestParam(value = "userId", required = false) Long userId,
            Model model) {

        logger.info("Payment callback received for booking {}: {}", bookingId, queryParams);

        try {
            // 2. Check if transaction already processed
            String vnp_TransactionNo = queryParams.get("vnp_TransactionNo");
            if (isTransactionProcessed(vnp_TransactionNo)) {
                logger.info("Transaction already processed: {}", vnp_TransactionNo);
                try {
                    // Get booking information for the success page
                    BookingResponseDTO booking = bookingService.getBookingById(bookingId);
                    if (booking != null) {
                        model.addAttribute("contactName", booking.getContactName());
                        model.addAttribute("amount", Long.parseLong(queryParams.get("vnp_Amount")) / 100);
                        model.addAttribute("paymentTime",
                                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));

                        // Add hotel information if available
                        if (booking.getHotel() != null) {
                            model.addAttribute("hotelName", booking.getHotel().getName());
                            model.addAttribute("hotelAddress", booking.getHotel().getAddress());
                        } else {
                            model.addAttribute("hotelName", "Không có thông tin");
                            model.addAttribute("hotelAddress", "Không có thông tin");
                        }
                        return "bookingsuccess";
                    } else {
                        throw new BookingProcessingException(
                                "Không thể tìm thấy thông tin đặt phòng cho giao dịch đã xử lý. BookingId: "
                                        + bookingId);
                    }
                } catch (Exception e) {
                    throw new BookingProcessingException("Lỗi khi lấy thông tin đặt phòng cho giao dịch đã xử lý", e);
                }
            }

            // 1. Validate required parameters
            String[] requiredParams = {
                    "vnp_Amount",
                    "vnp_BankCode",
                    "vnp_BankTranNo",
                    "vnp_CardType",
                    "vnp_OrderInfo",
                    "vnp_PayDate",
                    "vnp_ResponseCode",
                    "vnp_TmnCode",
                    "vnp_TransactionNo",
                    "vnp_TransactionStatus",
                    "vnp_TxnRef"
            };

            for (String param : requiredParams) {
                if (!queryParams.containsKey(param)) {
                    throw new PaymentProcessingException("Thiếu thông tin thanh toán bắt buộc: " + param);
                }
            }

            // 3. Process based on response code
            String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");
            String vnp_TransactionStatus = queryParams.get("vnp_TransactionStatus");

            // Log transaction details
            logger.info("Processing transaction: ResponseCode={}, Status={}, Amount={}, BookingId={}",
                    vnp_ResponseCode, vnp_TransactionStatus, queryParams.get("vnp_Amount"), bookingId);

            if ("00".equals(vnp_ResponseCode) && "00".equals(vnp_TransactionStatus)) {
                try {
                    // 1. Get the temporary booking with services
                    BookingResponseDTO tempBookingDTO = bookingService.getBookingById(bookingId);
                    if (tempBookingDTO == null) {
                        throw new BookingProcessingException("Không tìm thấy thông tin đặt phòng: " + bookingId);
                    }

                    // Get services before finalizing
                    Set<Services> services = bookingService.getServicesByBookingId(bookingId);
                    // Handle null or empty services set
                    if (services != null) {
                        logger.info("Retrieved {} services for booking {}", services.size(), bookingId);
                    } else {
                        logger.info("No services for booking {}", bookingId);
                        services = Collections.emptySet(); // Initialize as empty set to avoid null pointer
                    }

                    // 2. Create a mock Principal with userId
                    Principal mockPrincipal = createMockPrincipal(userId);

                    // 3. Finalize booking with mock Principal
                    BookingResponseDTO finalizedBooking = bookingService.finalizeBooking(bookingId, mockPrincipal);
                    logger.info("Booking finalized successfully: {}", finalizedBooking.getBookingId());

                    // 4. Get the finalized booking entity and update its status
                    Booking booking = bookingService.getBookingEntityById(finalizedBooking.getBookingId());
                    booking.setStatus("PAID");

                    // 5. Save services if any
                    if (services != null && !services.isEmpty()) {
                        booking.setServices(services);
                        logger.info("Added {} services to finalized booking", services.size());
                    }

                    // 6. Save the updated booking
                    try {
                        booking = bookingService.saveBooking(booking);
                        logger.info("Booking saved with status PAID and services attached");
                    } catch (Exception e) {
                        throw new BookingProcessingException("Lỗi khi lưu thông tin đặt phòng", e);
                    }

                    // 7. Create payment record
                    try {
                        PaymentRequestDTO paymentRequest = createPaymentRequest(finalizedBooking, queryParams);
                        paymentRequest.setStatus("SUCCESS");
                        paymentRequest.setPaymentDate(LocalDate.now());
                        paymentRequest.setPaymentTime(LocalTime.now());
                        paymentRequest.setBookingId(booking.getId_booking());

                        PaymentResponseDTO payment = paymentService.processPayment(paymentRequest);
                        logger.info("Payment processed successfully: {}", payment.getPaymentId());
                    } catch (Exception e) {
                        logger.error("Error processing payment record: {}", e.getMessage(), e);
                        // Continue to show success page even if payment record creation fails
                    }

                    // 8. Prepare success view
                    model.addAttribute("contactName", finalizedBooking.getContactName());
                    model.addAttribute("amount", Long.parseLong(queryParams.get("vnp_Amount")) / 100);
                    model.addAttribute("paymentTime", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));

                    if (finalizedBooking.getHotel() != null) {
                        model.addAttribute("hotelName", finalizedBooking.getHotel().getName());
                        model.addAttribute("hotelAddress", finalizedBooking.getHotel().getAddress());
                    } else {
                        model.addAttribute("hotelName", "Không có thông tin");
                        model.addAttribute("hotelAddress", "Không có thông tin");
                    }
                    return "bookingsuccess";

                } catch (Exception e) {
                    throw new PaymentProcessingException("Lỗi trong quá trình xử lý thanh toán: " + e.getMessage(), e);
                }
            } else if ("24".equals(vnp_ResponseCode)) {
                model.addAttribute("error", "Giao dịch đã bị hủy bởi người dùng");
                return "error";
            } else {
                model.addAttribute("error", "Giao dịch thất bại với mã lỗi: " + vnp_ResponseCode);
                return "error";
            }

        } catch (PaymentProcessingException | BookingProcessingException e) {
            logger.error("Payment/Booking processing error: {}", e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            return "error";
        } catch (Exception e) {
            logger.error("Unexpected error in payment callback: {}", e.getMessage(), e);
            model.addAttribute("error",
                    "Đã xảy ra lỗi không mong muốn trong quá trình xử lý thanh toán: " + e.getMessage());
            return "error";
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

        // Process amount
        String vnp_Amount = queryParams.get("vnp_Amount");
        if (vnp_Amount != null) {
            long amount = Long.parseLong(vnp_Amount) / 100; // VNPay amount is in VND * 100
            request.setAmount(new BigDecimal(amount));
        }

        // Set payment method and transaction details
        request.setPaymentMethod("VNPAY");
        request.setTransactionId(queryParams.get("vnp_TransactionNo"));

        // Set additional VNPay information
        request.setBankCode(queryParams.get("vnp_BankCode"));
        request.setBankTranNo(queryParams.get("vnp_BankTranNo"));
        request.setCardType(queryParams.get("vnp_CardType"));

        // Set payment date from VNPay
        String vnp_PayDate = queryParams.get("vnp_PayDate");
        if (vnp_PayDate != null && vnp_PayDate.length() >= 14) {
            try {
                // Parse VNPay date format (yyyyMMddHHmmss)
                int year = Integer.parseInt(vnp_PayDate.substring(0, 4));
                int month = Integer.parseInt(vnp_PayDate.substring(4, 6));
                int day = Integer.parseInt(vnp_PayDate.substring(6, 8));
                int hour = Integer.parseInt(vnp_PayDate.substring(8, 10));
                int minute = Integer.parseInt(vnp_PayDate.substring(10, 12));
                int second = Integer.parseInt(vnp_PayDate.substring(12, 14));

                request.setPaymentDate(LocalDate.of(year, month, day));
                request.setPaymentTime(LocalTime.of(hour, minute, second));
            } catch (Exception e) {
                logger.error("Error parsing VNPay date: {}", e.getMessage());
                request.setPaymentDate(LocalDate.now());
                request.setPaymentTime(LocalTime.now());
            }
        } else {
            request.setPaymentDate(LocalDate.now());
            request.setPaymentTime(LocalTime.now());
        }

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
            if (booking.getBill().getTotal() != null) {
                return booking.getBill().getTotal().longValue();
            } else if (booking.getBill().getDeposit() != null) {
                return booking.getBill().getDeposit().longValue();
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

    private Principal createMockPrincipal(Long userId) {
        return new Principal() {
            @Override
            public String getName() {
                try {
                    UserProfile user = userProfileService.findUserProfileEntityById(userId)
                            .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
                    return user.getAccount().getUsername();
                } catch (Exception e) {
                    logger.error("Error creating mock principal: {}", e.getMessage());
                    throw new RuntimeException("Failed to create principal", e);
                }
            }
        };
    }

    private boolean validateSecureHash(Map<String, String> queryParams) {
        try {
            // Trong quá trình development/test, return true để bỏ qua việc kiểm tra hash
            if (true) {
                return true;
            }

            String vnp_SecureHash = queryParams.get("vnp_SecureHash");

            // Create a new map without the secure hash for validation
            Map<String, String> validationParams = new HashMap<>(queryParams);
            validationParams.remove("vnp_SecureHash");
            validationParams.remove("bookingId"); // Remove non-VNPay params
            validationParams.remove("userId"); // Remove non-VNPay params

            // Build hash data
            List<String> fieldNames = new ArrayList<>(validationParams.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();

            for (String fieldName : fieldNames) {
                String fieldValue = validationParams.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (fieldNames.indexOf(fieldName) < fieldNames.size() - 1) {
                        hashData.append('&');
                    }
                }
            }

            String calculatedHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
            boolean isValid = calculatedHash.equals(vnp_SecureHash);

            if (!isValid) {
                logger.warn("Hash validation failed. Expected: {}, Got: {}", calculatedHash, vnp_SecureHash);
                logger.warn("Hash data string: {}", hashData.toString());
            }

            return isValid;
        } catch (Exception e) {
            logger.error("Error validating secure hash: {}", e.getMessage(), e);
            return false;
        }
    }
}

package com.tourism.booking.controller;

import com.tourism.booking.config.VNPayConfig;
import com.tourism.booking.dto.booking.BookingResponseDTO;
import com.tourism.booking.dto.booking.PaymentRequestDTO;
import com.tourism.booking.dto.booking.PaymentResponseDTO;
import com.tourism.booking.model.Payment;
import com.tourism.booking.repository.IPaymentRepository;
import com.tourism.booking.service.impl.BookingService;
import com.tourism.booking.service.impl.PaymentService;
import jakarta.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("${api.prefix}/payment")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private BookingService bookingService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private IPaymentRepository paymentRepository;

    @GetMapping("/booking-payment")
    public ResponseEntity<String> createBookingPayment(@PathParam("bookingId") Long bookingId)
            throws UnsupportedEncodingException {
        try {
            // Lấy thông tin booking từ service
            BookingResponseDTO booking = bookingService.getBookingById(bookingId);

            // Lấy số tiền cần thanh toán từ booking (deposit hoặc full amount)
            long price = 0;
            if (booking.getBill() != null) {
                price = booking.getBill().getDeposit().longValue(); // Hoặc booking.getBill().getTotalAmount()
            }

            // Chuyển sang dịch vụ VNPay
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String orderType = "other";
            long amount = price * 100;
            String bankCode = "NCB";

            String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
            String vnp_IpAddr = "127.0.0.1";
            String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");

            vnp_Params.put("vnp_BankCode", "NCB");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", "Thanh toan don dat phong #" + bookingId);
            vnp_Params.put("vnp_OrderType", orderType);

            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl + "?bookingId=" + bookingId);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            List fieldNames = new ArrayList(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    // Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    // Build query
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
            String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

            return ResponseEntity.ok(paymentUrl);
        } catch (Exception e) {
            logger.error("Error creating payment URL: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/payment-callback")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResponseEntity<Map<String, Object>> paymentCallback(
            @RequestParam Map<String, String> queryParams,
            @RequestParam(value = "bookingId", required = false) Long bookingId) {

        Map<String, Object> response = new HashMap<>();
        logger.info("*** PAYMENT CALLBACK START: bookingId={} ***", bookingId);
        logger.info("VNPay params: {}", queryParams);

        // Kiểm tra booking trong bộ nhớ tạm
        boolean inTemporaryStorage = bookingService.isBookingInTemporaryStorage(bookingId);
        logger.info("Booking in temporary storage: {}", inTemporaryStorage);

        if (!inTemporaryStorage) {
            logger.warn("Booking ID {} not found in temporary storage, may be already processed", bookingId);
        }

        // Kiểm tra nếu đã xử lý transaction này rồi
        String vnp_TransactionNo = queryParams.get("vnp_TransactionNo");
        if (vnp_TransactionNo != null) {
            Payment existingPayment = paymentRepository.findByTransactionId(vnp_TransactionNo);
            if (existingPayment != null) {
                // Đã xử lý callback này rồi, trả về kết quả thành công
                logger.info("Transaction {} already processed, returning success", vnp_TransactionNo);

                // Tạo response đơn giản
                response.put("status", "success");
                response.put("message", "Thanh toán đã được xử lý trước đó");
                response.put("transactionId", vnp_TransactionNo);

                // Thêm thông tin về thời gian và số tiền nếu có
                String vnp_PayDate = queryParams.get("vnp_PayDate");
                if (vnp_PayDate != null && vnp_PayDate.length() >= 14) {
                    String paymentDate = vnp_PayDate.substring(0, 8); // yyyyMMdd
                    String paymentTime = vnp_PayDate.substring(8, 14); // HHmmss

                    try {
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");
                        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = inputFormat.parse(paymentDate);
                        paymentDate = outputFormat.format(date);

                        inputFormat = new SimpleDateFormat("HHmmss");
                        outputFormat = new SimpleDateFormat("HH:mm:ss");
                        date = inputFormat.parse(paymentTime);
                        paymentTime = outputFormat.format(date);
                    } catch (Exception e) {
                        // Giữ nguyên định dạng nếu lỗi
                    }

                    response.put("paymentDate", paymentDate);
                    response.put("paymentTime", paymentTime);
                }

                String vnp_Amount = queryParams.get("vnp_Amount");
                if (vnp_Amount != null) {
                    try {
                        long amount = Long.parseLong(vnp_Amount) / 100;
                        response.put("amount", amount);
                    } catch (NumberFormatException e) {
                        // Skip if can't parse
                    }
                }

                return ResponseEntity.ok(response);
            }
        }

        try {
            String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");
            String vnp_BankCode = queryParams.get("vnp_BankCode");
            String vnp_CardType = queryParams.get("vnp_CardType");
            String vnp_PayDate = queryParams.get("vnp_PayDate");
            String vnp_OrderInfo = queryParams.get("vnp_OrderInfo");
            String vnp_Amount = queryParams.get("vnp_Amount");

            logger.info("Response code from VNPay: {}", vnp_ResponseCode);

            // Lấy thông tin booking
            BookingResponseDTO booking = null;
            try {
                booking = bookingService.getBookingById(bookingId);
                response.put("booking", booking);
                logger.info("Found booking for ID: {}, current status: {}", bookingId, booking.getStatus());
            } catch (Exception e) {
                logger.error("Error finding booking: {}", e.getMessage());
                response.put("error", "Không tìm thấy thông tin đặt phòng");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Xử lý định dạng ngày giờ
            String paymentDate = "";
            String paymentTime = "";
            if (vnp_PayDate != null && vnp_PayDate.length() >= 14) {
                paymentDate = vnp_PayDate.substring(0, 8); // yyyyMMdd
                paymentTime = vnp_PayDate.substring(8, 14); // HHmmss

                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = inputFormat.parse(paymentDate);
                    paymentDate = outputFormat.format(date);

                    inputFormat = new SimpleDateFormat("HHmmss");
                    outputFormat = new SimpleDateFormat("HH:mm:ss");
                    date = inputFormat.parse(paymentTime);
                    paymentTime = outputFormat.format(date);
                } catch (Exception e) {
                    // Giữ nguyên định dạng nếu lỗi
                    logger.warn("Error formatting payment date/time: {}", e.getMessage());
                }
            }

            // Xử lý số tiền
            long amount = 0;
            if (vnp_Amount != null) {
                try {
                    amount = Long.parseLong(vnp_Amount) / 100;
                } catch (NumberFormatException e) {
                    logger.warn("Error parsing amount: {}", e.getMessage());
                }
            }

            // Xử lý các trường hợp thanh toán
            if ("00".equals(vnp_ResponseCode)) {
                // Thanh toán thành công
                try {
                    // Hoàn tất booking và tạo hóa đơn - CHỈ lưu booking vào database khi thanh toán
                    // thành công
                    logger.info("Payment successful with code 00 for booking id: {}", bookingId);
                    logger.info("Saving booking to database and creating payment record");

                    BookingResponseDTO finalizedBooking = bookingService.finalizeBooking(bookingId);
                    // Lưu ý ID trong finalizedBooking có thể khác với bookingId ban đầu
                    Long savedBookingId = finalizedBooking.getBookingId();
                    logger.info("Booking finalized and saved with ID: {}", savedBookingId);

                    response.put("booking", finalizedBooking);

                    // Tạo payment record
                    PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
                    paymentRequest.setBillId(finalizedBooking.getBill().getBillId());
                    paymentRequest.setAmount(finalizedBooking.getBill().getDeposit());
                    paymentRequest.setPaymentMethod("VNPAY");
                    paymentRequest.setTransactionId(vnp_TransactionNo);

                    PaymentResponseDTO payment = paymentService.processPayment(paymentRequest);
                    response.put("payment", payment);

                    // Tạo response đơn giản
                    Map<String, Object> simplifiedResponse = new HashMap<>();
                    simplifiedResponse.put("status", "success");
                    simplifiedResponse.put("message", "Thanh toán thành công");
                    simplifiedResponse.put("transactionId", vnp_TransactionNo);
                    simplifiedResponse.put("bookingId", savedBookingId); // Dùng ID mới
                    simplifiedResponse.put("paymentDate", paymentDate);
                    simplifiedResponse.put("paymentTime", paymentTime);
                    simplifiedResponse.put("amount", amount);

                    logger.info("*** PAYMENT CALLBACK COMPLETED SUCCESSFULLY ***");
                    return ResponseEntity.ok(simplifiedResponse);
                } catch (Exception e) {
                    logger.error("Error processing successful payment: {}", e.getMessage(), e);
                    response.put("status", "error");
                    response.put("message", "Lỗi xử lý thanh toán: " + e.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                }
            } else if ("24".equals(vnp_ResponseCode)) {
                // Người dùng hủy giao dịch
                response.put("status", "cancelled");
                response.put("message", "Bạn đã hủy giao dịch thanh toán");
                response.put("transactionId", vnp_TransactionNo);
                response.put("bookingId", bookingId);
                return ResponseEntity.ok(response);
            } else {
                // Thanh toán thất bại với mã khác
                response.put("status", "failed");
                response.put("message", "Thanh toán thất bại. Mã lỗi: " + vnp_ResponseCode);
                response.put("responseCode", vnp_ResponseCode);
                response.put("transactionId", vnp_TransactionNo);
                response.put("bookingId", bookingId);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            logger.error("Unexpected error in payment callback: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", "Lỗi không xác định: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    // Endpoint API chuyên biệt để phục vụ frontend
    @GetMapping("/payment-result")
    public ResponseEntity<Map<String, Object>> getPaymentResult(
            @RequestParam(value = "bookingId") Long bookingId,
            @RequestParam(value = "transactionId", required = false) String transactionId,
            @RequestParam(value = "bankCode", required = false) String bankCode,
            @RequestParam(value = "cardType", required = false) String cardType,
            @RequestParam(value = "paymentDate", required = false) String paymentDate,
            @RequestParam(value = "paymentTime", required = false) String paymentTime,
            @RequestParam(value = "amount", required = false) Long amount) {

        Map<String, Object> response = new HashMap<>();

        try {
            BookingResponseDTO booking = bookingService.getBookingById(bookingId);

            // Tạo response chi tiết
            response.put("status", "success");
            response.put("booking", booking);
            response.put("transactionId", transactionId);
            response.put("bankCode", bankCode);
            response.put("cardType", cardType);
            response.put("paymentDate", paymentDate);
            response.put("paymentTime", paymentTime);
            response.put("amount", amount);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving payment result: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", "Không tìm thấy thông tin đặt phòng: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}

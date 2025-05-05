package com.tourism.booking.controller;

import com.tourism.booking.dto.booking.BookingRequestDTO;
import com.tourism.booking.dto.booking.BookingResponseDTO;
import com.tourism.booking.dto.booking.ContactInfoDTO;
import com.tourism.booking.model.Account;
import com.tourism.booking.model.Hotel;
import com.tourism.booking.model.UserProfile;
import com.tourism.booking.service.IAccountService;
import com.tourism.booking.service.IHotelService;
import com.tourism.booking.service.impl.BookingService;
import com.tourism.booking.service.impl.RoomTypeBookingService;
import com.tourism.booking.service.impl.UserProfileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

@RestController
@RequestMapping("${api.prefix}/bookings")
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {

    BookingService bookingService;
    PaymentController paymentController;
    IAccountService accountService;
    UserProfileService userProfileService;
    IHotelService hotelService;

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    /**
     * API lấy danh sách booking theo khoảng thời gian và trạng thái
     * Nghiệp vụ: Lọc booking cho UI quản lý
     */
    @GetMapping("/filter")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByFilter(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInTo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutTo,
            @RequestParam(required = false) String status) {

        try {
            List<BookingResponseDTO> bookings = bookingService.getBookingsByDateRangeAndStatus(
                    checkInFrom, checkInTo, checkOutFrom, checkOutTo, status);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    /**
     * API Step 1: Khởi tạo booking với thông tin phòng và dịch vụ
     * Nghiệp vụ: Người dùng chọn phòng và dịch vụ, lưu tạm thời
     */
    @PostMapping("/initialize")
    public ResponseEntity<BookingResponseDTO> initializeBooking(@RequestBody BookingRequestDTO request,
            Principal principal) {
        BookingResponseDTO booking = bookingService.initializeBooking(request, principal);
        return new ResponseEntity<>(booking, HttpStatus.CREATED);
    }

    /**
     * API Step 2: Cập nhật thông tin liên hệ và trả về link thanh toán VNPay
     * Nghiệp vụ: Người dùng nhập thông tin liên hệ và chuyển sang thanh toán VNPay
     */
    @PostMapping("/contact-info-payment")
    public ResponseEntity<?> updateContactInfoWithPayment(
            @RequestBody ContactInfoDTO contactInfo) {
        try {
            // Cập nhật thông tin liên hệ
            BookingResponseDTO booking = bookingService.updateContactInfo(contactInfo);

            // Lấy URL thanh toán VNPay
            ResponseEntity<String> paymentResponse = paymentController.createBookingPayment(booking.getBookingId());
            String paymentUrl = paymentResponse.getBody();

            // Trả về cả thông tin booking và URL thanh toán
            Map<String, Object> response = new HashMap<>();
            response.put("booking", booking);
            response.put("paymentUrl", paymentUrl);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * API lấy danh sách booking của một người dùng
     * Nghiệp vụ: Hiển thị lịch sử đặt phòng cho người dùng
     */
    @GetMapping("/user")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByUserId(Principal principal) {
        try {
            Account acc = accountService.getAccountByUsername(principal.getName());
            UserProfile user = userProfileService.findUserProfileByAccoutId(acc.getAccount_id())
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng"));
            List<BookingResponseDTO> bookings = bookingService.getBookingsByUserId(user.getUser_id());
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    /**
     * API lấy danh sách booking của một khách sạn
     * Nghiệp vụ: Hiển thị danh sách đặt phòng cho quản lý khách sạn
     */
    @GetMapping("/hotel")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByHotelId(Principal principal) {
        Account acc = accountService.getAccountByUsername(principal.getName());
        Hotel hotel = hotelService.getHotelByAccountId(acc.getAccount_id())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khach san"));
        List<BookingResponseDTO> bookings = bookingService.getBookingsByHotelId(hotel.getHotel_id());
        return ResponseEntity.ok(bookings);
    }

    /**
     * API lấy danh sách booking theo trạng thái của một khách sạn
     * Nghiệp vụ: Lọc danh sách đặt phòng theo trạng thái cho quản lý khách sạn
     */
    @GetMapping("/hotel/status/{status}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByHotelIdAndStatus(Principal principal,
            @PathVariable String status) {
        Account acc = accountService.getAccountByUsername(principal.getName());
        Hotel hotel = hotelService.getHotelByAccountId(acc.getAccount_id())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khach san"));
        List<BookingResponseDTO> bookings = bookingService.getBookingsByHotelIdAndStatus(hotel.getHotel_id(), status);
        return ResponseEntity.ok(bookings);
    }

    /**
     * API cập nhật trạng thái booking
     * Nghiệp vụ: Xác nhận hoặc hủy booking từ phía khách sạn
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateBookingStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        try {
            // Kiểm tra tham số status hợp lệ
            if (!isValidBookingStatus(status)) {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("error",
                                "Trạng thái không hợp lệ. Các trạng thái hợp lệ: PENDING, CONFIRMED, COMPLETED, CANCELLED, PAID"));
            }

            BookingResponseDTO booking = bookingService.updateBookingStatus(id, status);
            return ResponseEntity.ok(booking);
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Lỗi cập nhật trạng thái booking: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Lỗi cập nhật trạng thái booking: " + e.getMessage()));
        }
    }

    // Helper method để kiểm tra status hợp lệ
    private boolean isValidBookingStatus(String status) {
        return Arrays.asList("PENDING", "CONFIRMED", "COMPLETED", "CANCELLED", "PAID").contains(status);
    }
}

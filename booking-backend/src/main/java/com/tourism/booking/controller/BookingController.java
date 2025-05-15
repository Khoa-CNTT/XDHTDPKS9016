package com.tourism.booking.controller;

import com.tourism.booking.dto.booking.BookingRequestDTO;
import com.tourism.booking.dto.booking.BookingResponseDTO;
import com.tourism.booking.dto.booking.ContactInfoDTO;
import com.tourism.booking.dto.booking.RoomBookingRequest;
import com.tourism.booking.dto.booking.RoomSelectionDTO;
import com.tourism.booking.model.Account;
import com.tourism.booking.model.Hotel;
import com.tourism.booking.model.UserProfile;
import com.tourism.booking.service.IAccountService;
import com.tourism.booking.service.IBookingService;
import com.tourism.booking.service.IHotelService;
import com.tourism.booking.service.IUserProfileService;
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
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix}/bookings")
@CrossOrigin(origins = "*")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {

    IBookingService bookingService;
    PaymentController paymentController;
    IAccountService accountService;
    IUserProfileService userProfileService;
    IHotelService hotelService;

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

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

    @PostMapping("/initialize")
    public ResponseEntity<BookingResponseDTO> initializeBooking(
            @RequestBody BookingRequestDTO request,
            Principal principal) {
        logger.info("Initializing booking with request: {}", request);

        try {
            validateInitializeRequest(request);

            BookingResponseDTO booking = bookingService.initializeBooking(request, principal);
            logger.info("Booking initialized successfully with ID: {}", booking.getBookingId());

            return new ResponseEntity<>(booking, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid request data: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error initializing booking: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/contact-info-payment")
    public ResponseEntity<?> updateContactInfoWithPayment(
            @RequestBody ContactInfoDTO contactInfo) {
        logger.info("Updating contact info for booking: {}", contactInfo.getBookingId());

        try {
            validateContactInfo(contactInfo);

            BookingResponseDTO booking = bookingService.updateContactInfo(contactInfo);
            Long bookingId = booking.getBookingId();

            ResponseEntity<String> paymentResponse = paymentController.createBookingPayment(bookingId);
            String paymentUrl = paymentResponse.getBody();

            if (paymentUrl == null) {
                throw new RuntimeException("Failed to generate payment URL");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("booking", booking);
            response.put("paymentUrl", paymentUrl);

            logger.info("Contact info updated and payment URL generated for booking: {}", bookingId);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            logger.error("Booking not found: {}", e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Booking not found: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            logger.error("Error processing contact info update: {}", e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    private void validateInitializeRequest(BookingRequestDTO request) {
        List<String> errors = new ArrayList<>();

        if (request.getCheckInDate() == null) {
            errors.add("Check-in date is required");
        }
        if (request.getCheckOutDate() == null) {
            errors.add("Check-out date is required");
        }
        if (request.getNumberOfPeople() <= 0) {
            errors.add("Number of people must be greater than 0");
        }
        if (request.getRoomSelections() == null || request.getRoomSelections().isEmpty()) {
            errors.add("At least one room must be selected");
        } else {
            for (RoomSelectionDTO room : request.getRoomSelections()) {
                if (room.getNumberOfRooms() == null || room.getNumberOfRooms() <= 0) {
                    errors.add("Number of rooms must be greater than 0");
                }
                if (room.getRoomTypeId() == null && room.getRoomId() == null) {
                    errors.add("Either roomTypeId or roomId must be specified");
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join(", ", errors));
        }
    }

    private void validateContactInfo(ContactInfoDTO contactInfo) {
        List<String> errors = new ArrayList<>();

        if (contactInfo.getBookingId() == null) {
            errors.add("Booking ID is required");
        }
        if (StringUtils.isEmpty(contactInfo.getContactName())) {
            errors.add("Contact name is required");
        }
        if (StringUtils.isEmpty(contactInfo.getContactEmail())) {
            errors.add("Contact email is required");
        }
        if (StringUtils.isEmpty(contactInfo.getContactPhone())) {
            errors.add("Contact phone is required");
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join(", ", errors));
        }
    }

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

    @GetMapping("/hotel")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByHotelId(Principal principal) {
        Account acc = accountService.getAccountByUsername(principal.getName());
        Hotel hotel = hotelService.getHotelByAccountId(acc.getAccount_id())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khach san"));
        List<BookingResponseDTO> bookings = bookingService.getBookingsByHotelId(hotel.getHotel_id());
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/hotel/status/{status}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByHotelIdAndStatus(Principal principal,
            @PathVariable String status) {
        Account acc = accountService.getAccountByUsername(principal.getName());
        Hotel hotel = hotelService.getHotelByAccountId(acc.getAccount_id())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khach san"));
        List<BookingResponseDTO> bookings = bookingService.getBookingsByHotelIdAndStatus(hotel.getHotel_id(), status);
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateBookingStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        try {
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

    private boolean isValidBookingStatus(String status) {
        return Arrays.asList("PENDING", "CONFIRMED", "COMPLETED", "CANCELLED", "PAID").contains(status);
    }

    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(@Valid @RequestBody BookingRequestDTO bookingRequest) {
        BookingResponseDTO response = bookingService.createBooking(bookingRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingResponseDTO>> getUserBookings(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getBookingsByUserId(userId));
    }
}

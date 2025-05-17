package com.tourism.booking.service.impl;

import com.tourism.booking.dto.booking.PaymentRequestDTO;
import com.tourism.booking.dto.booking.PaymentResponseDTO;
import com.tourism.booking.model.Bill;
import com.tourism.booking.model.Booking;
import com.tourism.booking.model.BookingRoom;
import com.tourism.booking.model.Payment;
import com.tourism.booking.repository.IBillRepository;
import com.tourism.booking.repository.IBookingRepository;
import com.tourism.booking.repository.IPaymentRepository;
import com.tourism.booking.service.IBookingService;
import com.tourism.booking.service.IPaymentService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.time.temporal.ChronoUnit;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class PaymentService implements IPaymentService {

    IPaymentRepository paymentRepository;
    IBillRepository billRepository;
    IBookingRepository bookingRepository;
    IBookingService bookingService;

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    /**
     * Xử lý thanh toán
     * Nghiệp vụ: Tạo bản ghi thanh toán khi người dùng thanh toán
     */
    @Override
    public PaymentResponseDTO processPayment(PaymentRequestDTO request) {
        logger.info("Processing payment request: {}", request);

        // Check existing payment
        if (request.getTransactionId() != null) {
            Payment existingPayment = paymentRepository.findByTransactionId(request.getTransactionId());
            if (existingPayment != null) {
                return convertToDTO(existingPayment);
            }
        }

        // Get bill
        Bill bill = null;
        if (request.getBillId() != null) {
            bill = billRepository.findById(request.getBillId()).get();
        } else if (request.getBookingId() != null) {
            bill = billRepository.findByBookingId(request.getBookingId());
        }

        // Create and save payment
        Payment payment = new Payment();
        payment.setAmount(request.getAmount());
        payment.setPayment_date(LocalDate.now());
        payment.setPayment_time(LocalTime.now());
        payment.setPayment_method(request.getPaymentMethod());
        payment.setTransaction_id(request.getTransactionId());
        payment.setStatus("PAID");
        payment.setBill(bill);
        payment.setBookingId(request.getBookingId());
        payment = paymentRepository.save(payment);

        // Update booking status
        if (bill != null && bill.getBooking() != null) {
            bookingService.updateBookingStatus(bill.getBooking().getId_booking(), "PAID");
        }

        return convertToDTO(payment);
    }

    private Bill createNewBill(Booking booking) {
        Bill bill = new Bill();
        bill.setBooking(booking);
        bill.setPrint_date(LocalDate.now());
        bill.setPrint_time(LocalTime.now());
        bill.setVersion(0L);

        // Calculate total amount from all booked rooms
        BigDecimal totalAmount = BigDecimal.ZERO;
        if (booking.getBookingRooms() != null && !booking.getBookingRooms().isEmpty()) {
            for (BookingRoom bookingRoom : booking.getBookingRooms()) {
                BigDecimal roomPrice = bookingRoom.getRoom().getPrice();
                int numberOfRooms = bookingRoom.getNumberOfRooms();
                long days = ChronoUnit.DAYS.between(booking.getCheck_in_date(), booking.getCheck_out_date());
                if (days <= 0)
                    days = 1;

                BigDecimal roomTotal = roomPrice
                        .multiply(BigDecimal.valueOf(numberOfRooms))
                        .multiply(BigDecimal.valueOf(days));
                totalAmount = totalAmount.add(roomTotal);
            }
        }

        bill.setTotal_amount(totalAmount);
        bill.setDeposit(totalAmount.multiply(BigDecimal.valueOf(0.3)));

        return billRepository.saveAndFlush(bill);
    }

    /**
     * Xử lý thanh toán độc lập không liên kết với bill
     * Được thiết kế để chạy trong một transaction riêng biệt
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected PaymentResponseDTO processStandalonePayment(PaymentRequestDTO request) {
        // Kiểm tra nếu payment đã tồn tại với transaction ID này
        if (request.getTransactionId() != null) {
            try {
                Payment existingPayment = paymentRepository.findByTransactionId(request.getTransactionId());
                if (existingPayment != null) {
                    System.out.println(
                            "Found existing standalone payment for transaction: " + request.getTransactionId());
                    return convertToDTO(existingPayment);
                }
            } catch (Exception e) {
                System.err.println("Error checking existing payment: " + e.getMessage());
                // Tiếp tục tạo payment mới
            }
        }

        // Tạo một payment độc lập
        Payment payment = new Payment();
        payment.setAmount(request.getAmount());
        payment.setPayment_date(LocalDate.now());
        payment.setPayment_time(LocalTime.now());
        payment.setPayment_method(request.getPaymentMethod());
        payment.setTransaction_id(request.getTransactionId());
        payment.setStatus("PROCESSED");
        // Không liên kết với bill

        // Lưu payment
        try {
            payment = paymentRepository.saveAndFlush(payment);
            System.out.println("Standalone payment saved successfully with ID: " + payment.getPayment_id());
        } catch (Exception e) {
            System.err.println("Error saving standalone payment: " + e.getMessage());
            // Trả về DTO tạm thời
            return createTempPaymentResponseDTO(request);
        }

        return convertToDTO(payment);
    }

    /**
     * Tạo một DTO tạm thời khi không lưu được payment
     */
    private PaymentResponseDTO createTempPaymentResponseDTO(PaymentRequestDTO request) {
        PaymentResponseDTO tempDTO = new PaymentResponseDTO();
        tempDTO.setTransactionId(request.getTransactionId());
        tempDTO.setAmount(request.getAmount());
        tempDTO.setStatus("PAID");
        tempDTO.setPaymentDate(LocalDate.now());
        tempDTO.setPaymentTime(LocalTime.now());
        tempDTO.setCustomerName("Pending Customer");
        tempDTO.setAccountNumber(request.getTransactionId());
        return tempDTO;
    }

    /**
     * Xử lý thanh toán cho booking nằm trong luồng 3 bước
     * Nghiệp vụ: Tạo bản ghi thanh toán và hoàn tất booking
     */
    @Override
    @Transactional
    public PaymentResponseDTO processPaymentWithBookingFinalization(PaymentRequestDTO request, Long bookingId,
            Principal principal) {
        bookingService.finalizeBooking(bookingId, principal);

        // Sau đó xử lý thanh toán như bình thường
        return processPayment(request);
    }

    /**
     * Lấy danh sách tất cả thanh toán cho UI quản lý
     * Nghiệp vụ: Hiển thị danh sách thanh toán cho quản lý
     */
    @Override
    public List<PaymentResponseDTO> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream()
                .map(this::convertToDetailedDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy lịch sử thanh toán của một hóa đơn
     * Nghiệp vụ: Hiển thị lịch sử thanh toán cho người dùng hoặc khách sạn
     */
    @Override
    public List<PaymentResponseDTO> getPaymentsByBillId(Long billId) {
        List<Payment> payments = paymentRepository.findByBillId(billId);
        return payments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Cập nhật trạng thái thanh toán
     * Nghiệp vụ: Xác nhận hoặc huỷ thanh toán
     */
    @Override
    @Transactional
    public PaymentResponseDTO updatePaymentStatus(Long paymentId, String status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy thanh toán với ID: " + paymentId));

        // Cập nhật trạng thái
        payment.setStatus(status);
        // Lưu lại payment
        payment = paymentRepository.save(payment);

        // Nếu xác nhận thanh toán, kiểm tra và cập nhật trạng thái booking nếu cần
        if ("COMPLETED".equals(status)) {
            Bill bill = payment.getBill();
            if (isFullyPaid(bill.getBooking().getId_booking())) {
                bookingService.updateBookingStatus(bill.getBooking().getId_booking(), "PAID");
            }
        }

        return convertToDetailedDTO(payment);
    }

    /**
     * Kiểm tra xem booking đã thanh toán đủ chưa
     * Nghiệp vụ: Kiểm tra tình trạng thanh toán của booking
     */
    @Override
    public boolean isFullyPaid(Long bookingId) {
        // Lấy tổng số tiền cần thanh toán
        Bill bill = billRepository.findByBookingId(bookingId);
        if (bill == null) {
            return false;
        }

        BigDecimal totalAmount = bill.getTotal_amount();

        // Lấy tổng số tiền đã thanh toán
        BigDecimal paidAmount = paymentRepository.getTotalPaidAmountByBillId(bill.getBill_id());
        if (paidAmount == null) {
            paidAmount = BigDecimal.ZERO;
        }

        // So sánh
        return paidAmount.compareTo(totalAmount) >= 0;
    }

    /**
     * Chuyển đổi từ Entity sang DTO
     */
    private PaymentResponseDTO convertToDTO(Payment payment) {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setPaymentId(payment.getPayment_id());
        dto.setBookingId(payment.getBookingId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentDate(payment.getPayment_date());
        dto.setPaymentTime(payment.getPayment_time());
        dto.setPaymentMethod(payment.getPayment_method());
        dto.setTransactionId(payment.getTransaction_id());

        // Kiểm tra null trước khi truy cập
        if (payment.getBill() != null) {
            dto.setBillId(payment.getBill().getBill_id());
        } else {
            // Nếu không có bill, đặt billId thành null
            dto.setBillId(null);
        }

        dto.setStatus(payment.getStatus());
        return dto;
    }

    /**
     * Chuyển đổi từ Entity sang DTO chi tiết cho UI quản lý
     */
    private PaymentResponseDTO convertToDetailedDTO(Payment payment) {
        PaymentResponseDTO dto = convertToDTO(payment);

        // Thêm thông tin chi tiết cho UI nếu có bill
        if (payment.getBill() != null && payment.getBill().getBooking() != null) {
            Booking booking = payment.getBill().getBooking();

            // Xác định tên khách hàng từ booking hoặc user profile
            String customerName = null;
            if (booking.getContact_name() != null && !booking.getContact_name().isEmpty()) {
                customerName = booking.getContact_name();
            } else if (booking.getUser_profile() != null) {
                customerName = booking.getUser_profile().getFull_name();
            } else {
                customerName = "Unknown Customer";
            }

            dto.setCustomerName(customerName);
        } else {
            dto.setCustomerName("Unknown Customer");
        }

        // Sử dụng transaction ID thay cho số tài khoản
        dto.setAccountNumber(payment.getTransaction_id());

        return dto;
    }
}
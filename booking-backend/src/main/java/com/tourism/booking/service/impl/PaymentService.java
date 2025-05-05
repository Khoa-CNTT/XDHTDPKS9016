package com.tourism.booking.service.impl;

import com.tourism.booking.dto.booking.PaymentRequestDTO;
import com.tourism.booking.dto.booking.PaymentResponseDTO;
import com.tourism.booking.model.Bill;
import com.tourism.booking.model.Booking;
import com.tourism.booking.model.Payment;
import com.tourism.booking.repository.IBillRepository;
import com.tourism.booking.repository.IBookingRepository;
import com.tourism.booking.repository.IPaymentRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class PaymentService {

    IPaymentRepository paymentRepository;
    IBillRepository billRepository;
    IBookingRepository bookingRepository;
    BookingService bookingService;

    /**
     * Xử lý thanh toán
     * Nghiệp vụ: Tạo bản ghi thanh toán khi người dùng thanh toán
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PaymentResponseDTO processPayment(PaymentRequestDTO request) {
        // Lấy thông tin Bill
        Bill bill = billRepository.findById(request.getBillId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hóa đơn"));

        // Kiểm tra xem đã có payment cho transaction này chưa
        if (request.getTransactionId() != null) {
            Payment existingPayment = paymentRepository.findByTransactionId(request.getTransactionId());
            if (existingPayment != null) {
                // Đã xử lý payment này rồi, trả về thông tin hiện có
                return convertToDTO(existingPayment);
            }
        }

        // Tạo Payment mới
        Payment payment = new Payment();
        payment.setAmount(request.getAmount());
        payment.setPayment_date(LocalDate.now());
        payment.setPayment_time(LocalTime.now());
        payment.setPayment_method(request.getPaymentMethod());
        payment.setTransaction_id(request.getTransactionId());
        payment.setStatus("PENDING"); // Set initial status
        payment.setBill(bill);

        // Lưu Payment
        payment = paymentRepository.save(payment);

        // Nếu đã thanh toán đủ, cập nhật trạng thái booking
        if (isFullyPaid(bill.getBooking().getId_booking())) {
            Booking booking = bill.getBooking();
            if ("PENDING".equals(booking.getStatus())) {
                // Cập nhật trạng thái
                bookingService.updateBookingStatus(booking.getId_booking(), "PAID");
            }
        }

        // Chuyển đổi sang DTO
        return convertToDTO(payment);
    }

    /**
     * Xử lý thanh toán cho booking nằm trong luồng 3 bước
     * Nghiệp vụ: Tạo bản ghi thanh toán và hoàn tất booking
     */
    @Transactional
    public PaymentResponseDTO processPaymentWithBookingFinalization(PaymentRequestDTO request, Long bookingId) {
        // Hoàn tất booking trước (chuyển từ TEMP sang PENDING và tạo bill)
        bookingService.finalizeBooking(bookingId);

        // Sau đó xử lý thanh toán như bình thường
        return processPayment(request);
    }

    /**
     * Lấy danh sách tất cả thanh toán cho UI quản lý
     * Nghiệp vụ: Hiển thị danh sách thanh toán cho quản lý
     */
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
        dto.setAmount(payment.getAmount());
        dto.setPaymentDate(payment.getPayment_date());
        dto.setPaymentTime(payment.getPayment_time());
        dto.setPaymentMethod(payment.getPayment_method());
        dto.setTransactionId(payment.getTransaction_id());
        dto.setBillId(payment.getBill().getBill_id());
        dto.setStatus(payment.getStatus());
        return dto;
    }

    /**
     * Chuyển đổi từ Entity sang DTO chi tiết cho UI quản lý
     */
    private PaymentResponseDTO convertToDetailedDTO(Payment payment) {
        PaymentResponseDTO dto = convertToDTO(payment);

        // Thêm thông tin chi tiết cho UI
        Booking booking = payment.getBill().getBooking();
        dto.setCustomerName(booking.getContact_name() != null ? booking.getContact_name()
                : booking.getUser_profile().getFull_name());
        dto.setAccountNumber(payment.getTransaction_id()); // Sử dụng transaction ID thay cho số tài khoản

        return dto;
    }
}
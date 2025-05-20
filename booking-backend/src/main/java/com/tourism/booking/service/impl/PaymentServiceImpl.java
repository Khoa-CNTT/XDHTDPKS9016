package com.tourism.booking.service.impl;

import com.tourism.booking.dto.booking.PaymentRequestDTO;
import com.tourism.booking.dto.booking.PaymentResponseDTO;
import com.tourism.booking.model.Bill;
import com.tourism.booking.model.Booking;
import com.tourism.booking.model.Payment;
import com.tourism.booking.repository.IBillRepository;
import com.tourism.booking.repository.IBookingRepository;
import com.tourism.booking.repository.IPaymentRepository;
import com.tourism.booking.service.IBookingService;
import com.tourism.booking.service.IPaymentService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements IPaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private IPaymentRepository paymentRepository;

    @Autowired
    private IBookingRepository bookingRepository;

    @Autowired
    private IBillRepository billRepository;

    @Autowired
    private IBookingService bookingService;

    @Override
    public Page<PaymentResponseDTO> getAllPayments(Pageable pageable) {
        Page<Payment> payments = paymentRepository.findAll(pageable);
        return payments.map(this::convertToDTO);
    }

    @Override
    @Transactional
    public PaymentResponseDTO updatePaymentStatus(Long paymentId, String status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + paymentId));

        payment.setStatus(status);
        payment = paymentRepository.save(payment);

        // Nếu trạng thái thanh toán là thành công, cập nhật trạng thái của booking
        if ("SUCCESS".equals(status)) {
            updateBookingStatus(payment.getBooking().getId_booking(), "PAID");
        }

        return convertToDTO(payment);
    }

    @Override
    @Transactional
    public PaymentResponseDTO processPaymentWithBookingFinalization(PaymentRequestDTO request, Long bookingId,
            Principal principal) {
        // Finalize booking first
        bookingService.finalizeBooking(bookingId, principal);

        // Then process payment
        return processPayment(request);
    }

    @Override
    @Transactional
    public PaymentResponseDTO processPayment(PaymentRequestDTO request) {
        logger.info("Processing payment for booking: {}", request.getBookingId());

        try {
            // Get the booking
            Booking booking = bookingRepository.findById(request.getBookingId())
                    .orElseThrow(
                            () -> new EntityNotFoundException("Booking not found with id: " + request.getBookingId()));

            // Get or create bill if not exists
            Bill bill;
            if (request.getBillId() != null) {
                bill = billRepository.findById(request.getBillId())
                        .orElseThrow(
                                () -> new EntityNotFoundException("Bill not found with id: " + request.getBillId()));
            } else {
                bill = billRepository.findByBookingId(request.getBookingId());
                if (bill == null) {
                    throw new EntityNotFoundException("Bill not found for booking: " + request.getBookingId());
                }
            }

            // Create payment entity
            Payment payment = new Payment();

            // Thiết lập trực tiếp trường bookingId
            logger.info("Setting bookingId directly: {}", booking.getId_booking());
            payment.setBookingId(booking.getId_booking());

            // Thiết lập mối quan hệ cho tham chiếu trong code
            payment.setBooking(booking);
            payment.setBill(bill);

            payment.setAmount(request.getAmount());
            payment.setPayment_method(request.getPaymentMethod());
            payment.setTransaction_id(request.getTransactionId());

            // Set status from request or default to SUCCESS
            payment.setStatus(request.getStatus() != null ? request.getStatus() : "SUCCESS");

            // Set date/time
            if (request.getPaymentDate() != null) {
                payment.setPayment_date(request.getPaymentDate());
                payment.setPayment_time(request.getPaymentTime() != null ? request.getPaymentTime() : LocalTime.now());
            } else {
                payment.setPayment_date(LocalDate.now());
                payment.setPayment_time(LocalTime.now());
            }

            payment = paymentRepository.save(payment);
            logger.info("Payment saved successfully with id: {} for booking: {}",
                    payment.getPayment_id(), payment.getBookingId());

            // Double-check if bookingId was saved correctly, if not try to fix it
            if (payment.getBookingId() == null) {
                logger.warn("BookingId missing in saved payment, attempting to fix");
                payment.setBookingId(booking.getId_booking());
                payment = paymentRepository.save(payment);
                logger.info("Fixed payment with updated bookingId: {}", payment.getBookingId());
            }

            // Update booking status if payment is successful
            if ("SUCCESS".equals(payment.getStatus())) {
                updateBookingStatus(booking.getId_booking(), "PAID");
            }

            return convertToDTO(payment);
        } catch (Exception e) {
            logger.error("Error processing payment: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process payment", e);
        }
    }

    @Override
    public List<PaymentResponseDTO> getPaymentsByBillId(Long billId) {
        List<Payment> payments = paymentRepository.findByBillId(billId);
        return payments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public boolean isFullyPaid(Long bookingId) {
        Bill bill = billRepository.findByBookingId(bookingId);
        if (bill == null) {
            return false;
        }

        List<Payment> payments = paymentRepository.findByBookingId(bookingId);
        if (payments.isEmpty()) {
            return false;
        }

        // Calculate total paid amount
        double totalPaid = payments.stream()
                .filter(p -> "SUCCESS".equals(p.getStatus()))
                .mapToDouble(p -> p.getAmount().doubleValue())
                .sum();

        // Compare with bill total
        return totalPaid >= bill.getTotal_amount().doubleValue();
    }

    private PaymentResponseDTO convertToDTO(Payment payment) {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setPaymentId(payment.getPayment_id());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPayment_method());
        dto.setTransactionId(payment.getTransaction_id());
        dto.setStatus(payment.getStatus());
        dto.setPaymentDate(payment.getPayment_date());
        dto.setPaymentTime(payment.getPayment_time());

        // Đặt bookingId trực tiếp từ entity
        dto.setBookingId(payment.getBookingId());

        // Set billId if available
        if (payment.getBill() != null) {
            dto.setBillId(payment.getBill().getBill_id());

            // Lấy thông tin khách hàng từ booking
            if (payment.getBooking() != null) {
                Booking booking = payment.getBooking();
                String customerName;
                if (booking.getContact_name() != null && !booking.getContact_name().isEmpty()) {
                    customerName = booking.getContact_name();
                } else if (booking.getUser_profile() != null) {
                    customerName = booking.getUser_profile().getFull_name();
                } else {
                    customerName = "Unknown Customer";
                }
                dto.setCustomerName(customerName);
            }
        }

        // Đảm bảo có accountNumber
        dto.setAccountNumber(payment.getTransaction_id());

        // Set status display
        if ("SUCCESS".equals(payment.getStatus()) || "PROCESSED".equals(payment.getStatus())) {
            dto.setStatusDisplay("Thành công");
        } else if ("PAID".equals(payment.getStatus())) {
            dto.setStatusDisplay("Đang xử lý");
        } else if ("FAILED".equals(payment.getStatus())) {
            dto.setStatusDisplay("Thất bại");
        } else {
            dto.setStatusDisplay(payment.getStatus());
        }

        return dto;
    }

    private void updateBookingStatus(Long bookingId, String status) {
        try {
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + bookingId));

            booking.setStatus(status);
            bookingRepository.save(booking);
            logger.info("Booking status updated to {} for booking: {}", status, bookingId);
        } catch (Exception e) {
            logger.error("Error updating booking status: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update booking status", e);
        }
    }
}
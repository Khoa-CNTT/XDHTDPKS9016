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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public PaymentResponseDTO processPayment(PaymentRequestDTO request) {
        logger.info("Processing payment request: {}", request);
        if (request.getTransactionId() != null) {
            Payment existingPayment = paymentRepository.findByTransactionId(request.getTransactionId());
            if (existingPayment != null) {
                return convertToDTO(existingPayment);
            }
        }

        Bill bill = null;
        if (request.getBillId() != null) {
            bill = billRepository.findById(request.getBillId()).get();
        } else if (request.getBookingId() != null) {
            bill = billRepository.findByBookingId(request.getBookingId());
        }

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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected PaymentResponseDTO processStandalonePayment(PaymentRequestDTO request) {
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
            }
        }

        Payment payment = new Payment();
        payment.setAmount(request.getAmount());
        payment.setPayment_date(LocalDate.now());
        payment.setPayment_time(LocalTime.now());
        payment.setPayment_method(request.getPaymentMethod());
        payment.setTransaction_id(request.getTransactionId());
        payment.setStatus("PROCESSED");
        try {
            payment = paymentRepository.saveAndFlush(payment);
            System.out.println("Standalone payment saved successfully with ID: " + payment.getPayment_id());
        } catch (Exception e) {
            System.err.println("Error saving standalone payment: " + e.getMessage());
            return createTempPaymentResponseDTO(request);
        }

        return convertToDTO(payment);
    }
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
    @Override
    @Transactional
    public PaymentResponseDTO processPaymentWithBookingFinalization(PaymentRequestDTO request, Long bookingId,
            Principal principal) {
        bookingService.finalizeBooking(bookingId, principal);
        return processPayment(request);
    }


    @Override
    public Page<PaymentResponseDTO> getAllPayments(Long accountId, Pageable pageable) {
        Page<Payment> payments = paymentRepository.getHistoryPayment(accountId, pageable);
        return payments.map(this::convertToDetailedDTO);
    }

    @Override
    public List<PaymentResponseDTO> getPaymentsByBillId(Long billId) {
        List<Payment> payments = paymentRepository.findByBillId(billId);
        return payments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public PaymentResponseDTO updatePaymentStatus(Long paymentId, String status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy thanh toán với ID: " + paymentId));
        payment.setStatus(status);
        payment = paymentRepository.save(payment);
        if ("COMPLETED".equals(status)) {
            Bill bill = payment.getBill();
            if (isFullyPaid(bill.getBooking().getId_booking())) {
                bookingService.updateBookingStatus(bill.getBooking().getId_booking(), "PAID");
            }
        }

        return convertToDetailedDTO(payment);
    }
    @Override
    public boolean isFullyPaid(Long bookingId) {
        Bill bill = billRepository.findByBookingId(bookingId);
        if (bill == null) {
            return false;
        }

        BigDecimal totalAmount = bill.getTotal_amount();

        BigDecimal paidAmount = paymentRepository.getTotalPaidAmountByBillId(bill.getBill_id());
        if (paidAmount == null) {
            paidAmount = BigDecimal.ZERO;
        }
        return paidAmount.compareTo(totalAmount) >= 0;
    }

    private PaymentResponseDTO convertToDTO(Payment payment) {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setPaymentId(payment.getPayment_id());
        dto.setBookingId(payment.getBookingId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentDate(payment.getPayment_date());
        dto.setPaymentTime(payment.getPayment_time());
        dto.setPaymentMethod(payment.getPayment_method());
        dto.setTransactionId(payment.getTransaction_id());

        if (payment.getBill() != null) {
            dto.setBillId(payment.getBill().getBill_id());
        } else {

            dto.setBillId(null);
        }

        dto.setStatus(payment.getStatus());
        return dto;
    }


    private PaymentResponseDTO convertToDetailedDTO(Payment payment) {
        PaymentResponseDTO dto = convertToDTO(payment);

        if (payment.getBill() != null && payment.getBill().getBooking() != null) {
            Booking booking = payment.getBill().getBooking();

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

        dto.setAccountNumber(payment.getTransaction_id());

        return dto;
    }
}
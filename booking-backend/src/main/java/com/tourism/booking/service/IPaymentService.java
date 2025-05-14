package com.tourism.booking.service;

import com.tourism.booking.dto.booking.PaymentRequestDTO;
import com.tourism.booking.dto.booking.PaymentResponseDTO;

import java.security.Principal;
import java.util.List;

public interface IPaymentService {
    List<PaymentResponseDTO> getAllPayments();

    PaymentResponseDTO updatePaymentStatus(Long paymentId, String status);

    PaymentResponseDTO processPaymentWithBookingFinalization(PaymentRequestDTO request, Long bookingId, Principal principal);

    PaymentResponseDTO processPayment(PaymentRequestDTO request);

    List<PaymentResponseDTO> getPaymentsByBillId(Long billId);

    boolean isFullyPaid(Long bookingId);
}
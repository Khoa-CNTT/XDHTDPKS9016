package com.tourism.booking.repository;

import com.tourism.booking.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface IPaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p WHERE p.bill.bill_id = :billId")
    List<Payment> findByBillId(@Param("billId") Long billId);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.bill.bill_id = :billId")
    BigDecimal getTotalPaidAmountByBillId(@Param("billId") Long billId);

    @Query("SELECT p FROM Payment p WHERE p.transaction_id = :transactionId")
    Payment findByTransactionId(@Param("transactionId") String transactionId);

    @Query("SELECT p FROM Payment p WHERE p.booking.id_booking = :bookingId")
    List<Payment> findByBookingId(@Param("bookingId") Long bookingId);

}

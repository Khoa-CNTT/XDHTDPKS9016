package com.tourism.booking.repository;

import com.tourism.booking.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(value = """
        select p.* from payment p
        join booking_management.booking_room br on p.booking_id = br.booking_id
        join booking_management.room_type rt on br.room_type_id = rt.room_type_id
        join booking_management.hotel h on rt.hotel_id = h.hotel_id
        join booking_management.account a on h.account_id = a.account_id
        where a.account_id = :accountId
        """, nativeQuery = true)
    Page<Payment> getHistoryPayment(@Param("accountId") Long accountId, Pageable pageable);

}

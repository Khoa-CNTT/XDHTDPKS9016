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

    @Query(
            value = """
                    SELECT p.* 
                    FROM payment p
                      JOIN booking_management.booking_room br ON p.booking_id   = br.booking_id
                      JOIN booking_management.room_type   rt ON br.room_type_id = rt.room_type_id
                      JOIN booking_management.hotel       h  ON rt.hotel_id      = h.hotel_id
                      JOIN booking_management.account     a  ON h.account_id     = a.account_id
                    WHERE a.account_id = :accountId
                    """,
            countQuery = """
                    SELECT COUNT(*) 
                    FROM payment p
                      JOIN booking_management.booking_room br ON p.booking_id   = br.booking_id
                      JOIN booking_management.room_type   rt ON br.room_type_id = rt.room_type_id
                      JOIN booking_management.hotel       h  ON rt.hotel_id      = h.hotel_id
                      JOIN booking_management.account     a  ON h.account_id     = a.account_id
                    WHERE a.account_id = :accountId
                    """,
            nativeQuery = true
    )
    Page<Payment> getHistoryPayment(@Param("accountId") Long accountId, Pageable pageable);

}

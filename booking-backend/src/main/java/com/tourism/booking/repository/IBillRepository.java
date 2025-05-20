package com.tourism.booking.repository;

import com.tourism.booking.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IBillRepository extends JpaRepository<Bill, Long> {

    @Query("SELECT b FROM Bill b WHERE b.booking.id_booking = :bookingId")
    Bill findByBookingId(Long bookingId);
}

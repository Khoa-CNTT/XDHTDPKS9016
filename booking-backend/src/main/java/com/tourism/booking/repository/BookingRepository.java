package com.tourism.booking.repository;

import com.tourism.booking.dto.statistical.StatisticalDTO;
import com.tourism.booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.user_profile.id = :userId")
    List<Booking> findByUserProfileId(Long userId);

    @Query(value = """
            SELECT
            (SELECT COUNT(*)   FROM booking_management.booking) AS booking_count,
            (SELECT COUNT(*)   FROM booking_management.account) AS account_count,
            (SELECT SUM(amount) FROM booking_management.payment) AS total_payment,
            (SELECT COUNT(*) FROM booking_management.account a
                join booking_management.role_account rl on a.account_id = rl.account_id
                where rl.role_id = 2) AS account_hotel;
        """, nativeQuery = true)
    StatisticalDTO getStatistical();
}
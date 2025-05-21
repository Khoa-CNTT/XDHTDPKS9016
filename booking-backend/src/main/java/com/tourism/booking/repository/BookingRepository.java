package com.tourism.booking.repository;

import com.tourism.booking.dto.statistical.QuarterlyStatDTO;
import com.tourism.booking.dto.statistical.StatisticalDTO;
import com.tourism.booking.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.user_profile.id = :userId")
    Page<Booking> findByUserProfileId(Pageable pageable, Long userId);

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

    @Query(value = """
            SELECT
              (SELECT COUNT(*)
                 FROM booking_management.booking b
                WHERE YEAR(b.created_at) = :year
                  AND QUARTER(b.created_at) = :quarter
              ) AS bookingCount,

              (SELECT COUNT(*)
                 FROM booking_management.account a
                WHERE YEAR(a.created_at) = :year    
                  AND QUARTER(a.created_at) = :quarter
              ) AS accountCount,

              (SELECT SUM(p.amount)
                 FROM booking_management.payment p
                WHERE YEAR(p.payment_date) = :year
                  AND QUARTER(p.payment_date) = :quarter
              ) AS totalPayment,

              (SELECT COUNT(*)
                 FROM booking_management.account a
                 JOIN booking_management.role_account rl
                   ON a.account_id = rl.account_id
                WHERE rl.role_id   = 2
                  AND YEAR(a.created_at) = :year
                  AND QUARTER(a.created_at) = :quarter
              ) AS accountHotel
            """, nativeQuery = true)
    StatisticalDTO getStatisticalByQuarter(
            @Param("year") int year,
            @Param("quarter") int quarter
    );


    @Query(value = """
            SELECT
              /* alias phải trùng với tên getter (camelCase) */
              COUNT(DISTINCT b.id_booking)       AS bookingCount,
              SUM(p.amount)                      AS totalPayment
            FROM booking_management.booking b
            JOIN booking_management.booking_room br
              ON b.id_booking = br.booking_id
            JOIN booking_management.room_type rt
              ON br.room_type_id = rt.room_type_id
            JOIN booking_management.hotel h
              ON rt.hotel_id = h.hotel_id
            LEFT JOIN booking_management.payment p
              ON p.booking_id = b.id_booking
            WHERE h.account_id = :accountId
              AND YEAR(b.check_in_date)    = :year
              AND QUARTER(b.check_in_date) = :quarter
            """,
            nativeQuery = true)
    QuarterlyStatDTO getHotelStatsByQuarter(
            @Param("accountId") long accountId,
            @Param("year") int year,
            @Param("quarter") int quarter
    );
}
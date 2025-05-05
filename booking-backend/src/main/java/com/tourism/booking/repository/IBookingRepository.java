package com.tourism.booking.repository;

import com.tourism.booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IBookingRepository extends JpaRepository<Booking, Long> {

    // @Query(value = """
    // select b.* from booking b
    // join booking_management.user_profile up on up.user_id = b.user_id
    // where b.user_id = :userId
    // """, nativeQuery = true)
    @Query("SELECT DISTINCT b FROM Booking b " +
            "LEFT JOIN FETCH b.room_type " +
            "LEFT JOIN FETCH b.user_profile " +
            "LEFT JOIN FETCH b.bill " +
            "WHERE b.user_profile.user_id = :userId")
    List<Booking> findByUserProfileId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT b FROM Booking b " +
            "JOIN FETCH b.room_type rt " +
            "JOIN FETCH rt.hotel h " +
            "WHERE h.hotel_id = :hotelId")
    List<Booking> findByHotelId(@Param("hotelId") Long hotelId);

    @Query("SELECT DISTINCT b FROM Booking b " +
            "JOIN FETCH b.room_type rt " +
            "JOIN FETCH rt.hotel h " +
            "WHERE h.hotel_id = :hotelId AND b.status = :status")
    List<Booking> findByHotelIdAndStatus(@Param("hotelId") Long hotelId, @Param("status") String status);

    @Query("SELECT DISTINCT b FROM Booking b " +
            "JOIN FETCH b.room_type rt " +
            "JOIN FETCH rt.hotel h " +
            "WHERE (:checkInFrom IS NULL OR b.check_in_date >= :checkInFrom) " +
            "AND (:checkInTo IS NULL OR b.check_in_date <= :checkInTo) " +
            "AND (:checkOutFrom IS NULL OR b.check_out_date >= :checkOutFrom) " +
            "AND (:checkOutTo IS NULL OR b.check_out_date <= :checkOutTo) " +
            "AND b.status = :status")
    List<Booking> findByDateRangeAndStatus(
            @Param("checkInFrom") LocalDate checkInFrom,
            @Param("checkInTo") LocalDate checkInTo,
            @Param("checkOutFrom") LocalDate checkOutFrom,
            @Param("checkOutTo") LocalDate checkOutTo,
            @Param("status") String status);

    @Query("SELECT DISTINCT b FROM Booking b " +
            "JOIN FETCH b.room_type rt " +
            "JOIN FETCH rt.hotel h " +
            "WHERE (:checkInFrom IS NULL OR b.check_in_date >= :checkInFrom) " +
            "AND (:checkInTo IS NULL OR b.check_in_date <= :checkInTo) " +
            "AND (:checkOutFrom IS NULL OR b.check_out_date >= :checkOutFrom) " +
            "AND (:checkOutTo IS NULL OR b.check_out_date <= :checkOutTo)")
    List<Booking> findByDateRange(
            @Param("checkInFrom") LocalDate checkInFrom,
            @Param("checkInTo") LocalDate checkInTo,
            @Param("checkOutFrom") LocalDate checkOutFrom,
            @Param("checkOutTo") LocalDate checkOutTo);
}

package com.tourism.booking.repository;

import com.tourism.booking.model.Booking;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IBookingRepository extends JpaRepository<Booking, Long> {

        @Query("SELECT DISTINCT b FROM Booking b " +
                        "LEFT JOIN FETCH b.bookingRooms br " +
                        "LEFT JOIN FETCH b.user_profile " +
                        "LEFT JOIN FETCH b.bill " +
                        "WHERE b.user_profile.user_id = :userId")
        List<Booking> findByUserProfileUserId(@Param("userId") Long userId);

        @Query("SELECT DISTINCT b FROM Booking b " +
                        "JOIN FETCH b.bookingRooms br " +
                        "JOIN FETCH br.room r " +
                        "JOIN FETCH r.room_type rt " +
                        "JOIN FETCH rt.hotel h " +
                        "WHERE h.hotel_id = :hotelId")
        List<Booking> findByRoomRoomTypeHotelHotelId(@Param("hotelId") Long hotelId);

        @Query("SELECT DISTINCT b FROM Booking b " +
                        "JOIN b.bookingRooms br " +
                        "JOIN br.room r " +
                        "JOIN r.room_type rt " +
                        "JOIN rt.hotel h " +
                        "WHERE h.hotel_id = :hotelId AND b.status = :status")
        List<Booking> findByRoomRoomTypeHotelHotelIdAndStatus(Long hotelId, String status);

        @Query("SELECT DISTINCT b FROM Booking b " +
                        "JOIN FETCH b.bookingRooms br " +
                        "JOIN FETCH br.room r " +
                        "JOIN FETCH r.room_type rt " +
                        "WHERE (:checkInFrom IS NULL OR b.check_in_date >= :checkInFrom) " +
                        "AND (:checkInTo IS NULL OR b.check_in_date <= :checkInTo) " +
                        "AND (:checkOutFrom IS NULL OR b.check_out_date >= :checkOutFrom) " +
                        "AND (:checkOutTo IS NULL OR b.check_out_date <= :checkOutTo) " +
                        "AND b.status = :status")
        List<Booking> findByStatusAndDateRange(
                        @Param("status") String status,
                        @Param("checkInFrom") LocalDate checkInFrom,
                        @Param("checkInTo") LocalDate checkInTo,
                        @Param("checkOutFrom") LocalDate checkOutFrom,
                        @Param("checkOutTo") LocalDate checkOutTo);

        @Query("SELECT DISTINCT b FROM Booking b " +
                        "JOIN FETCH b.bookingRooms br " +
                        "JOIN FETCH br.room r " +
                        "JOIN FETCH r.room_type rt " +
                        "WHERE (:checkInFrom IS NULL OR b.check_in_date >= :checkInFrom) " +
                        "AND (:checkInTo IS NULL OR b.check_in_date <= :checkInTo) " +
                        "AND (:checkOutFrom IS NULL OR b.check_out_date >= :checkOutFrom) " +
                        "AND (:checkOutTo IS NULL OR b.check_out_date <= :checkOutTo)")
        List<Booking> findByDateRange(
                        @Param("checkInFrom") LocalDate checkInFrom,
                        @Param("checkInTo") LocalDate checkInTo,
                        @Param("checkOutFrom") LocalDate checkOutFrom,
                        @Param("checkOutTo") LocalDate checkOutTo);

        @Query("SELECT DISTINCT b FROM Booking b WHERE b.status = :status")
        List<Booking> findByStatus(@Param("status") String status);

        @Query("SELECT DISTINCT b FROM Booking b " +
                        "JOIN FETCH b.bookingRooms br " +
                        "JOIN FETCH br.room r " +
                        "WHERE b.status = :status AND b.check_in_date BETWEEN :from AND :to")
        List<Booking> findByStatusAndCheckInDateBetween(
                        @Param("status") String status,
                        @Param("from") LocalDate from,
                        @Param("to") LocalDate to);

        @Query("SELECT DISTINCT b FROM Booking b " +
                        "JOIN FETCH b.bookingRooms br " +
                        "JOIN FETCH br.room r " +
                        "WHERE b.status = :status AND b.check_out_date BETWEEN :from AND :to")
        List<Booking> findByStatusAndCheckOutDateBetween(
                        @Param("status") String status,
                        @Param("from") LocalDate from,
                        @Param("to") LocalDate to);

        @Query("SELECT DISTINCT b FROM Booking b " +
                        "JOIN FETCH b.bookingRooms br " +
                        "JOIN FETCH br.room r " +
                        "WHERE b.check_in_date BETWEEN :from AND :to")
        List<Booking> findByCheckInDateBetween(
                        @Param("from") LocalDate from,
                        @Param("to") LocalDate to);

        @Query("SELECT DISTINCT b FROM Booking b " +
                        "JOIN FETCH b.bookingRooms br " +
                        "JOIN FETCH br.room r " +
                        "WHERE b.check_out_date BETWEEN :from AND :to")
        List<Booking> findByCheckOutDateBetween(
                        @Param("from") LocalDate from,
                        @Param("to") LocalDate to);

        @Query("SELECT b FROM Booking b WHERE b.id_booking_temp = :tempId")
        Booking findByIdBookingTempId(@Param("tempId") Long tempId);

        @Query("SELECT b FROM Booking b ORDER BY b.id_booking DESC")
        List<Booking> findRecentBookings(Pageable pageable);
}

package com.tourism.booking.repository;

import com.tourism.booking.model.BookingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IBookingRoomRepository extends JpaRepository<BookingRoom, Long> {

    @Query("SELECT br FROM BookingRoom br WHERE br.room.room_type.room_type_id = :roomTypeId")
    List<BookingRoom> findByRoomRoomTypeRoomTypeId(@Param("roomTypeId") Long roomTypeId);

    @Query("SELECT br FROM BookingRoom br WHERE br.room.id_room = :roomId")
    List<BookingRoom> findByRoomIdRoom(@Param("roomId") Long roomId);

    @Query("SELECT br FROM BookingRoom br WHERE br.room.id_room = :roomId AND br.booking.status NOT IN ('CANCELLED')")
    List<BookingRoom> findActiveBookingsByRoomId(@Param("roomId") Long roomId);

    @Query("SELECT br FROM BookingRoom br WHERE br.room.room_type.room_type_id = :roomTypeId AND br.booking.status NOT IN ('CANCELLED')")
    List<BookingRoom> findActiveBookingsByRoomTypeId(@Param("roomTypeId") Long roomTypeId);

    @Query("SELECT br FROM BookingRoom br WHERE br.room.room_type.hotel.hotel_id = :hotelId AND br.booking.status NOT IN ('CANCELLED')")
    List<BookingRoom> findActiveBookingsByHotelId(@Param("hotelId") Long hotelId);

    @Query("SELECT SUM(br.numberOfRooms) FROM BookingRoom br WHERE br.room.room_type.room_type_id = :roomTypeId AND br.booking.status NOT IN ('CANCELLED')")
    Integer countBookedRoomsByRoomTypeId(@Param("roomTypeId") Long roomTypeId);

    @Query("SELECT SUM(br.numberOfRooms) FROM BookingRoom br WHERE br.room.room_type.hotel.hotel_id = :hotelId AND br.booking.status NOT IN ('CANCELLED')")
    Integer countBookedRoomsByHotelId(@Param("hotelId") Long hotelId);

    @Query("SELECT br FROM BookingRoom br WHERE br.room.room_type.hotel.hotel_id = :hotelId " +
            "AND br.booking.status NOT IN ('CANCELLED') " +
            "AND ((br.booking.check_in_date <= :checkOutDate AND br.booking.check_out_date >= :checkInDate))")
    List<BookingRoom> findBookedRoomsByHotelIdAndDateRange(
            @Param("hotelId") Long hotelId,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate);
}
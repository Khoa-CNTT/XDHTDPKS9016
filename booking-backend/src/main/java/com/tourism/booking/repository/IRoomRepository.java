package com.tourism.booking.repository;

import com.tourism.booking.model.Room;
import com.tourism.booking.model.RoomType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IRoomRepository extends JpaRepository<Room, Long> {

        @Query("SELECT r FROM Room r JOIN r.room_type rt JOIN rt.hotel h WHERE h.hotel_id = :hotelId")
        List<Room> findByRoomTypeHotelHotelId(@Param("hotelId") Long hotelId);

        @Query("SELECT r FROM Room r WHERE r.room_type.room_type_id = :roomTypeId")
        List<Room> findByRoomTypeRoomTypeId(@Param("roomTypeId") Long roomTypeId);

        @Query("SELECT r FROM Room r WHERE r.room_type.room_type_id = :roomTypeId " +
                        "AND r.id_room NOT IN " +
                        "(SELECT b.room.id_room FROM Booking b " +
                        "WHERE ((b.check_in_date <= :checkOutDate AND b.check_out_date >= :checkInDate) " +
                        "OR (b.check_in_date >= :checkInDate AND b.check_in_date <= :checkOutDate) " +
                        "OR (b.check_out_date >= :checkInDate AND b.check_out_date <= :checkOutDate)) " +
                        "AND b.status NOT IN ('CANCELLED'))")
        List<Room> findAvailableRoomsByDateRange(
                        @Param("roomTypeId") Long roomTypeId,
                        @Param("checkInDate") LocalDate checkInDate,
                        @Param("checkOutDate") LocalDate checkOutDate);

        @Query("SELECT r FROM Room r WHERE r.room_type = :roomType")
        List<Room> findByRoomType(@Param("roomType") RoomType roomType);

        @Query("SELECT r FROM Room r ORDER BY r.id_room DESC")
        List<Room> findRecentBookings(Pageable pageable);
}
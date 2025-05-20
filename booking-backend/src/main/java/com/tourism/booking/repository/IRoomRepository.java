package com.tourism.booking.repository;

import com.tourism.booking.model.Room;
import com.tourism.booking.model.RoomType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IRoomRepository extends JpaRepository<Room, Long> {

        @Query("SELECT r FROM Room r JOIN r.room_type rt JOIN rt.hotel h WHERE h.hotel_id = :hotelId")
        List<Room> findByRoomTypeHotelHotelId(@Param("hotelId") Long hotelId);

        @Query("SELECT r FROM Room r WHERE r.room_type.room_type_id = :roomTypeId")
        List<Room> findByRoomTypeRoomTypeId(@Param("roomTypeId") Long roomTypeId);

        @Query("SELECT r FROM Room r " +
                        "WHERE r.room_type.room_type_id = :roomTypeId " +
                        "AND r.id_room NOT IN (" +
                        "    SELECT br.room.id_room FROM BookingRoom br " +
                        "    JOIN br.booking b " +
                        "    WHERE b.status NOT IN ('CANCELLED') " +
                        "    AND ((b.check_in_date <= :checkOutDate AND b.check_out_date >= :checkInDate) " +
                        "         OR (b.check_in_date >= :checkInDate AND b.check_in_date <= :checkOutDate) " +
                        "         OR (b.check_out_date >= :checkInDate AND b.check_out_date <= :checkOutDate)))")
        List<Room> findAvailableRoomsByDateRange(
                        @Param("roomTypeId") Long roomTypeId,
                        @Param("checkInDate") LocalDate checkInDate,
                        @Param("checkOutDate") LocalDate checkOutDate);

        // Method to find available rooms with limit
        default List<Room> findAvailableRoomsByTypeAndDateRange(
                        Long roomTypeId,
                        LocalDate checkInDate,
                        LocalDate checkOutDate,
                        Integer limit) {
                List<Room> rooms = findAvailableRoomsByDateRange(roomTypeId, checkInDate, checkOutDate);
                return rooms.size() <= limit ? rooms : rooms.subList(0, limit);
        }

        @Query("SELECT r FROM Room r WHERE r.room_type = :roomType")
        List<Room> findByRoomType(@Param("roomType") RoomType roomType);

        @Query("SELECT r FROM Room r ORDER BY r.id_room DESC")
        List<Room> findRecentBookings(Pageable pageable);

        @Query("SELECT r FROM Room r JOIN r.room_type rt WHERE rt.hotel.hotel_id = :hotelId")
        List<Room> findAllRoomsByHotelId(@Param("hotelId") Long hotelId);

        @Query("SELECT COUNT(r) FROM Room r JOIN r.room_type rt WHERE rt.hotel.hotel_id = :hotelId")
        int countRoomsByHotelId(@Param("hotelId") Long hotelId);

        @Query("SELECT COUNT(r) FROM Room r WHERE r.room_type.room_type_id = :roomTypeId")
        int countRoomsByRoomTypeId(@Param("roomTypeId") Long roomTypeId);

        @Query(nativeQuery = true, value = "SELECT r.* FROM room r " +
                        "WHERE r.room_type_id = :roomTypeId " +
                        "AND r.id_room NOT IN (" +
                        "    SELECT br.room_id FROM booking_room br " +
                        "    JOIN booking b ON br.booking_id = b.id_booking " +
                        "    WHERE b.status NOT IN ('CANCELLED') " +
                        "    AND ((b.check_in_date <= :checkOutDate AND b.check_out_date >= :checkInDate))" +
                        ")")
        List<Room> findAvailableRoomsByTypeAndDateRange(
                        @Param("roomTypeId") Long roomTypeId,
                        @Param("checkInDate") LocalDate checkInDate,
                        @Param("checkOutDate") LocalDate checkOutDate);

        @Query("""
        SELECT r FROM Room r
        WHERE r.room_type.hotel.hotel_id = :hotelId
          AND r.id_room NOT IN (
            SELECT br.room.id_room
            FROM BookingRoom br
            JOIN br.booking b
            WHERE b.status != 'CANCELLED'
              AND :checkIn < b.check_out_date
              AND :checkOut > b.check_in_date
          )
    """)
        List<Room> findAvailableRoomsByHotelAndDateRange(
                @Param("hotelId") Long hotelId,
                @Param("checkIn") LocalDate checkIn,
                @Param("checkOut") LocalDate checkOut
        );
}
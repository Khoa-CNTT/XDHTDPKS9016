package com.tourism.booking.repository;

import com.tourism.booking.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

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

        default List<Room> findAvailableRoomsByTypeAndDateRange(
                        Long roomTypeId,
                        LocalDate checkInDate,
                        LocalDate checkOutDate,
                        Integer limit) {
                List<Room> rooms = findAvailableRoomsByTypeAndDateRange(roomTypeId, checkInDate, checkOutDate);
                return rooms.size() <= limit ? rooms : rooms.subList(0, limit);
        }

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
}
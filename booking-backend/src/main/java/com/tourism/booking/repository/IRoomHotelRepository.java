package com.tourism.booking.repository;

import com.tourism.booking.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface IRoomHotelRepository extends JpaRepository<Room, Long> {
    @Query("""
                SELECT r FROM Room r
                JOIN r.room_type rt
                JOIN rt.hotel h
                JOIN h.account a
                WHERE a.account_id = :accountId
            """)
    List<Room> findRoomsByHotelOwner(@Param("accountId") Long accountId);

    @Query("SELECT COUNT(r) FROM Room r WHERE r.room_type.room_type_id = :roomTypeId")
    int countByRoomTypeId(@Param("roomTypeId") Long roomTypeId);
}

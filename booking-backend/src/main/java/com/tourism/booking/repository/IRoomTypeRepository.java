package com.tourism.booking.repository;

import com.tourism.booking.model.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRoomTypeRepository extends JpaRepository<RoomType, Long> {

    @Query(
            value = """
                    SELECT r.*
                    FROM room_type r
                    JOIN hotel h ON r.hotel_id = h.hotel_id
                    WHERE h.account_id = :id
                    """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM room_type r
                    JOIN hotel h ON r.hotel_id = h.hotel_id
                    WHERE h.account_id = :id
                    """,
            nativeQuery = true
    )
    Page<RoomType> findRoomTypesByAccountId(Long id, Pageable pageable);

    @Query(value = """
            SELECT r.room_type_id, r.type_name, r.number_room, r.description, r.room_image, r.hotel_id
            FROM room_type r
            WHERE r.hotel_id = :hotelId
            """, nativeQuery = true)
    List<RoomType> findByHotelId(Long hotelId);

    @Query(value = """
            SELECT r.room_type_id, r.type_name, r.number_room, r.description, r.room_image, r.hotel_id
            FROM room_type r
            WHERE r.hotel_id = :hotelId AND r.available_room > 0
            """, nativeQuery = true)
    List<RoomType> findAvailableRoomTypesByHotelId(Long hotelId);

    @Query("SELECT rt FROM RoomType rt WHERE rt.hotel.hotel_id = :hotelId")
    List<RoomType> findByHotelHotelId(Long hotelId);

    @Query("SELECT rt FROM RoomType rt WHERE rt.hotel.hotel_id = :hotelId")
    List<RoomType> findAllRoomTypesByHotelId(@Param("hotelId") Long hotelId);
}

package com.tourism.booking.repository;

import com.tourism.booking.model.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IRoomTypeRepository extends JpaRepository<RoomType, Long> {

    @Query(value = """
            SELECT r.*
            FROM room_type r
            JOIN hotel h ON r.hotel_id = h.hotel_id
            WHERE h.account_id = :id
            """, nativeQuery = true)
    Page<RoomType> findRoomTypesByAccountId(Long id, Pageable pageable);

    @Query(value = """
            SELECT r.room_type_id, r.type_name, r.number_bed, r.maximum_people, r.price,
                   r.description, r.room_image, r.available_room, r.status, r.hotel_id
            FROM room_type r
            WHERE r.hotel_id = :hotelId
            """, nativeQuery = true)
    List<RoomType> findByHotelId(Long hotelId);

    @Query(value = """
            SELECT r.room_type_id, r.type_name, r.number_bed, r.maximum_people, r.price,
                   r.description, r.room_image, r.available_room, r.status, r.hotel_id
            FROM room_type r
            WHERE r.hotel_id = :hotelId AND r.available_room > 0
            """, nativeQuery = true)
    List<RoomType> findAvailableRoomTypesByHotelId(Long hotelId);
}

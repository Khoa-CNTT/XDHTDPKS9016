package com.tourism.booking.repository;

import com.tourism.booking.model.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IRoomTypeRepository extends JpaRepository<RoomType, Long> {

    @Query(value = """
            SELECT r.*
            FROM room_type r
            JOIN hotel h ON r.hotel_id = h.hotel_id
            WHERE h.account_id = :id
            """, nativeQuery = true)
    Page<RoomType> findRoomTypesByAccountId(Long id, Pageable pageable);
}

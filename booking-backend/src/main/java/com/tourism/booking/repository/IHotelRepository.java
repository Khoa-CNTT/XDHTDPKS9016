package com.tourism.booking.repository;

import com.tourism.booking.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IHotelRepository extends JpaRepository<Hotel, Long> {
  @Query(value = """
      select * from hotel
      where account_id = :accountId
      """, nativeQuery = true)
  Optional<Hotel> findByAccountId(Long accountId);

  @Query("""
          SELECT CASE WHEN COUNT(rt) > 0 THEN TRUE ELSE FALSE END
          FROM Hotel h
          JOIN h.roomTypes rt
          WHERE h.account.account_id = :accountId
            AND rt.room_type_id = :roomTypeId
      """)
  boolean isOwnerOfRoomType(
      @Param("accountId") Long accountId,
      @Param("roomTypeId") Long roomTypeId);

}

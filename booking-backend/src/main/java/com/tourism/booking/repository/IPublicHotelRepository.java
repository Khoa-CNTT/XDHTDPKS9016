package com.tourism.booking.repository;

import com.tourism.booking.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IPublicHotelRepository extends JpaRepository<Hotel, Long> {
    @Query("""
                SELECT h FROM Hotel h
                LEFT JOIN FETCH h.services s
                LEFT JOIN FETCH h.roomTypes rt
                WHERE h.hotel_id = :hotelId
            """)
    Optional<Hotel> findDetailById(@Param("hotelId") Long hotelId);
}
package com.tourism.booking.repository;

import com.tourism.booking.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IHotelRepository extends JpaRepository<Hotel, Long> {
    @Query(value = """
            select * from hotel
            where account_id = :accountId
            """, nativeQuery = true)
    Optional<Hotel> findByAccountId(Long accountId);
}

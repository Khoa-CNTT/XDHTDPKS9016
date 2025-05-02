package com.tourism.booking.repository;

import com.tourism.booking.model.Services;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IServiceRepository extends JpaRepository<Services, Long> {
    @Query(value = """
            SELECT s.*
                FROM service s
                JOIN hotel h  ON s.hotel_id   = h.hotel_id
                    WHERE h.account_id = :id;
            """, nativeQuery = true)
    Page<Services> findServicesByHotelId(Long id, Pageable pageable);
}

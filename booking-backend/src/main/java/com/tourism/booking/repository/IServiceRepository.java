package com.tourism.booking.repository;

import com.tourism.booking.model.Services;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface IServiceRepository extends JpaRepository<Services, Long> {
    @Query(value = """
            SELECT s.*
                FROM service s
                JOIN hotel h  ON s.hotel_id   = h.hotel_id
                    WHERE h.account_id = :id;
            """, nativeQuery = true)
    Page<Services> findServicesByHotelId(Long id, Pageable pageable);

    @Query(value = """
            SELECT s.service_id, s.service_name, s.service_price, s.service_image,
                   s.description, s.hotel_id
            FROM service s
            WHERE s.hotel_id = :hotelId
            """, nativeQuery = true)
    List<Services> findByHotelId(Long hotelId);

    @Query(value = """
            SELECT s.service_id, s.service_name, s.service_price, s.service_image,
                   s.description, s.hotel_id
            FROM service s
            WHERE s.service_id IN (:serviceIds)
            """, nativeQuery = true)
    Set<Services> findAllByServiceIdIn(Set<Long> serviceIds);
}

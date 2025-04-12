package com.tourism.booking.repository;

import com.tourism.booking.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IHotelRepository extends JpaRepository<Hotel, Long> {
}

package com.tourism.booking.repository;

import com.tourism.booking.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRatingRepository extends JpaRepository<Rating, Integer> {
}

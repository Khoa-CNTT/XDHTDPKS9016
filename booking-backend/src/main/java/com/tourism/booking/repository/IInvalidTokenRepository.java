package com.tourism.booking.repository;

import com.tourism.booking.model.InvalidToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IInvalidTokenRepository extends JpaRepository<InvalidToken, String> {
}
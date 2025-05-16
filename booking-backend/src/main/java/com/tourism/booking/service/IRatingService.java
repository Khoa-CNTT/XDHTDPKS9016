package com.tourism.booking.service;

import com.tourism.booking.dto.rating.RatingRequest;
import com.tourism.booking.dto.rating.RatingResponse;

import java.util.List;
import java.util.Optional;

public interface IRatingService {
    List<RatingResponse> GetAllRatings();
    Optional<RatingResponse> GetRatingById(int id);
    RatingResponse save(RatingRequest ratingRequest);
    void deleteRatingById(int id);
}

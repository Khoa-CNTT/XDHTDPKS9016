package com.tourism.booking.service;

import com.tourism.booking.dto.rating.RatingRequest;
import com.tourism.booking.dto.rating.RatingResponse;

import java.security.Principal;
import java.util.List;

public interface IRatingService {
    List<RatingResponse> getRoomRatings(Long roomId);

    RatingResponse createRating(Long roomId, RatingRequest request, Principal principal);

}

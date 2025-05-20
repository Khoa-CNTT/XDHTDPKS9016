package com.tourism.booking.service.impl;

import com.tourism.booking.dto.rating.RatingRequest;
import com.tourism.booking.dto.rating.RatingResponse;
import com.tourism.booking.model.*;
import com.tourism.booking.repository.IRatingRepository;
import com.tourism.booking.repository.IRoomRepository;
import com.tourism.booking.service.IAccountService;
import com.tourism.booking.service.IUserProfileService;
import com.tourism.booking.service.IRatingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingService implements IRatingService {

    private final IRatingRepository ratingRepository;
    private final IRoomRepository roomRepository;
    private final IAccountService accountService;
    private final IUserProfileService userProfileService;

    @Override
    public List<RatingResponse> getRoomRatings(Long roomId) {
        return ratingRepository.getAllRatingByRoomId(roomId);
    }

    @Override
    @Transactional
    public RatingResponse createRating( Long roomId, RatingRequest request, Principal principal) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + roomId));

        // Lấy thông tin user
        Account acc = accountService.getAccountByUsername(principal.getName());
        UserProfile user = userProfileService.findUserProfileByAccoutId(acc.getAccount_id())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Rating rating = new Rating();
        rating.setScore(request.getScore());
        rating.setRating_date(LocalDate.now());
        rating.setRating_time(LocalTime.now());
        rating.setUser_profile(user);
        Rating saveRating = ratingRepository.save(rating);

        RoomRating roomRating = new RoomRating();
        roomRating.setRoom(room);
        roomRating.setRating(saveRating);

        rating.getRoomRatings().add(roomRating);
        saveRating.getRoomRatings().add(roomRating);
        return ratingRepository.findRatingById(saveRating.getRating_id());
    }

}

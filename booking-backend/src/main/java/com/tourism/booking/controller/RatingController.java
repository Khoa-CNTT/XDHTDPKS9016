package com.tourism.booking.controller;

import com.tourism.booking.dto.rating.RatingRequest;
import com.tourism.booking.dto.rating.RatingResponse;
import com.tourism.booking.service.IRatingService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ratings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingController {

    IRatingService ratingService;

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<RatingResponse>> getRoomRatings(@PathVariable Long roomId) {
        return ResponseEntity.ok(ratingService.getRoomRatings(roomId));
    }

    @PostMapping("/{roomId}")
    public ResponseEntity<RatingResponse> createRating(
            @PathVariable Long roomId,
            @RequestBody RatingRequest request,
            Principal principal) {
        RatingResponse savedRating = ratingService.createRating(roomId, request, principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRating);
    }
}

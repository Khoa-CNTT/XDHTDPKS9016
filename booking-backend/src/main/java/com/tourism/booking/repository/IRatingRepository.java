package com.tourism.booking.repository;

import com.tourism.booking.dto.rating.RatingResponse;
import com.tourism.booking.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IRatingRepository extends JpaRepository<Rating, Long> {

    @Query(value = """
                select r.rating_id AS id,
                       r.score AS rating,
                       up.full_name AS name
                from booking_management.rating r
                JOIN booking_management.room_rating rr on r.rating_id = rr.rating_id
                JOIN booking_management.user_profile up ON r.user_id = up.user_id
            WHERE rr.room_id = :id
            """, nativeQuery = true)
    List<RatingResponse> getAllRatingByRoomId(Long id);

    @Query(value = """
            SELECT
                     r.rating_id AS id,
                     r.score AS rating,
                    up.full_name AS name
                FROM booking_management.rating r
                JOIN booking_management.user_profile up ON r.user_id = up.user_id
                WHERE r.rating_id= :id
            """, nativeQuery = true)
    RatingResponse findRatingById(Long id);
}

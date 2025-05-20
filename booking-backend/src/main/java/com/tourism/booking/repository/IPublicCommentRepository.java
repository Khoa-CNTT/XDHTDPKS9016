package com.tourism.booking.repository;

import com.tourism.booking.dto.comment.CommentResponse;
import com.tourism.booking.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPublicCommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = """
            SELECT
                c.comment_id    AS comment_id,
                c.content       AS content,
                c.comment_date  AS comment_date,
                c.comment_time  AS comment_time,
                up.full_name    AS comment_author
            FROM comment c
            JOIN user_profile up
              ON c.user_id      = up.user_id
            JOIN room_comment rc
              ON c.comment_id   = rc.comment_id
            JOIN room r
              ON rc.room_id     = r.id_room
            JOIN room_type rt
              ON r.room_type_id = rt.room_type_id
            JOIN hotel h
              ON rt.hotel_id    = h.hotel_id
            WHERE h.hotel_id    = :hotelId
            """, nativeQuery = true)
    List<CommentResponse> findAllCommentsByHotelId(@Param("hotelId") Long hotelId);
}


package com.tourism.booking.repository;

import com.tourism.booking.dto.comment.CommentResponse;
import com.tourism.booking.dto.comment.RoomCommentDTO;
import com.tourism.booking.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = """
            SELECT
                c.comment_id as comment_id,
                c.content as content,
                c.comment_date as comment_date,
                c.comment_time as comment_time,
                up.full_name as comment_author
            FROM comment c
            JOIN room_comment rc ON c.comment_id = rc.comment_id
            JOIN user_profile up ON c.user_id = up.user_id
            WHERE rc.room_id = :id
            """, nativeQuery = true)
    List<CommentResponse> findAllComments(@Param("id") Long roomId);

    @Query(value = """
            SELECT
                c.comment_id as comment_id,
                c.content as content,
                c.comment_date as comment_date,
                c.comment_time as comment_time,
                up.full_name as comment_author
            FROM comment c
            JOIN user_profile up ON c.user_id = up.user_id
            WHERE c.comment_id = :id
            """, nativeQuery = true)
    CommentResponse findCommentById(@Param("id") Long id);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM room_comment WHERE room_id = :roomId AND comment_id = :commentId)", nativeQuery = true)
    boolean existsByRoomIdAndCommentId(@Param("roomId") Long roomId, @Param("commentId") Long commentId);

    @Query(value = """
        select br.id                 AS roomId,
               br.number_of_rooms    AS numberRoom,
               rt.type_name          AS nameTypeRoom
        from booking_management.booking b
                 join booking_management.booking_room br
                      on b.id_booking = br.booking_id
                 join booking_management.room_type rt
                      on br.room_type_id = rt.room_type_id
        where b.id_booking = :bookingId
        """,
            nativeQuery = true)
    RoomCommentDTO getRoomComment(@Param("bookingId") Long bookingId);
}

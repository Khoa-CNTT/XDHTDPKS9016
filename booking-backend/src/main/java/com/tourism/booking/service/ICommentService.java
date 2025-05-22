package com.tourism.booking.service;

import com.tourism.booking.dto.comment.CommentRequest;
import com.tourism.booking.dto.comment.CommentResponse;
import com.tourism.booking.dto.comment.RoomCommentDTO;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface ICommentService {
    List<CommentResponse> getAllComment(Long roomId);

    Optional<CommentResponse> getCommentById(Long id);

    CommentResponse createComment(Long roomId, CommentRequest commentRequest, Principal principal);

    CommentResponse updateComment(Long id, CommentRequest commentRequest, Principal principal);

    void deleteComment(Long id, Principal principal);

    RoomCommentDTO getRoomComment(Long bookingId);
}

package com.tourism.booking.service.impl;

import com.tourism.booking.dto.comment.CommentRequest;
import com.tourism.booking.dto.comment.CommentResponse;
import com.tourism.booking.dto.comment.RoomCommentDTO;
import com.tourism.booking.model.*;
import com.tourism.booking.repository.ICommentRepository;
import com.tourism.booking.repository.IRoomRepository;
import com.tourism.booking.service.IAccountService;
import com.tourism.booking.service.ICommentService;
import com.tourism.booking.service.IUserProfileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentService implements ICommentService {

    ICommentRepository commentRepository;
    IRoomRepository roomRepository;
    IAccountService accountService;
    IUserProfileService userProfileService;

    @Override
    public List<CommentResponse> getAllComment(Long roomId) {
        return commentRepository.findAllComments(roomId);
    }

    @Override
    public Optional<CommentResponse> getCommentById(Long id) {
        return Optional.ofNullable(commentRepository.findCommentById(id));
    }

    @Override
    @Transactional
    public CommentResponse createComment(Long roomId, CommentRequest commentRequest, Principal principal) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + roomId));

        Account acc = accountService.getAccountByUsername(principal.getName());
        UserProfile user = userProfileService.findUserProfileByAccoutId(acc.getAccount_id())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Comment comment = new Comment();
        comment.setContent(commentRequest.getComment());
        comment.setComment_date(LocalDate.now());
        comment.setComment_time(LocalTime.now());
        comment.setUser_profile(user);
        Comment savedComment = commentRepository.save(comment);
        RoomComment roomComment = new RoomComment();
        roomComment.setRoom(room);
        roomComment.setComment(savedComment);

        room.getRoomComments().add(roomComment);
        savedComment.getRoomComments().add(roomComment);

        return commentRepository.findCommentById(savedComment.getComment_id());
    }

    @Override
    @Transactional
    public CommentResponse updateComment(Long id, CommentRequest commentRequest, Principal principal) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + id));

        Account acc = accountService.getAccountByUsername(principal.getName());
        UserProfile user = userProfileService.findUserProfileByAccoutId(acc.getAccount_id())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!comment.getUser_profile().getUser_id().equals(user.getUser_id())) {
            throw new RuntimeException("You don't have permission to update this comment");
        }
        comment.setContent(commentRequest.getComment());
        comment.setComment_date(LocalDate.now());
        comment.setComment_time(LocalTime.now());

        Comment updatedComment = commentRepository.save(comment);
        return commentRepository.findCommentById(updatedComment.getComment_id());
    }

    @Override
    @Transactional
    public void deleteComment(Long id, Principal principal) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + id));
        Account acc = accountService.getAccountByUsername(principal.getName());
        UserProfile user = userProfileService.findUserProfileByAccoutId(acc.getAccount_id())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!comment.getUser_profile().getUser_id().equals(user.getUser_id())) {
            throw new RuntimeException("You don't have permission to delete this comment");
        }

        commentRepository.delete(comment);
    }

    @Override
    public RoomCommentDTO getRoomComment(Long bookingId) {
        return commentRepository.getRoomComment(bookingId);
    }
}

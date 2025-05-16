package com.tourism.booking.controller;

import com.tourism.booking.dto.comment.CommentRequest;
import com.tourism.booking.dto.comment.CommentResponse;
import com.tourism.booking.service.ICommentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@AllArgsConstructor
@CrossOrigin
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {

    ICommentService commentService;

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<CommentResponse>> getAllCommentsByRoomId(@PathVariable Long roomId) {
        try {
            System.out.println("Starting to fetch comments for roomId: " + roomId);
            List<CommentResponse> comments = commentService.getAllComment(roomId);
            System.out.println("Found " + (comments != null ? comments.size() : 0) + " comments");
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            System.err.println("Error fetching comments: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable Long id) {
        try {
            CommentResponse comment = commentService.getCommentById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + id));
            return ResponseEntity.ok(comment);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/room/{roomId}")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long roomId,
            @RequestBody CommentRequest commentRequest,
            Principal principal) {
        try {
            CommentResponse savedComment = commentService.createComment(roomId, commentRequest, principal);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long id,
            @RequestBody CommentRequest commentRequest,
            Principal principal) {
        try {
            CommentResponse updatedComment = commentService.updateComment(id, commentRequest, principal);
            return ResponseEntity.ok(updatedComment);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id, Principal principal) {
        try {
            commentService.deleteComment(id, principal);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

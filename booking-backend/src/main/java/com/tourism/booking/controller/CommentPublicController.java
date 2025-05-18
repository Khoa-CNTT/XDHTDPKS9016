package com.tourism.booking.controller;


import com.tourism.booking.dto.comment.CommentResponse;
import com.tourism.booking.service.impl.CommentPublicService;
import com.tourism.booking.service.impl.CommentService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/public-comments")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class CommentPublicController {

    CommentPublicService commentPublicService;

    @GetMapping("/{hotelId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByHotel(@PathVariable Long hotelId) {
        List<CommentResponse> comments = commentPublicService.getCommentsByHotelId(hotelId);
        return ResponseEntity.ok(comments);
    }

}

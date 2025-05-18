package com.tourism.booking.service;

import com.tourism.booking.dto.comment.CommentResponse;

import java.util.List;

public interface ICommentPublicService {
    List<CommentResponse> getCommentsByHotelId(Long hotelId);
}

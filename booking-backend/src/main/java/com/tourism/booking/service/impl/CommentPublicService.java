package com.tourism.booking.service.impl;

import com.tourism.booking.dto.comment.CommentResponse;
import com.tourism.booking.repository.IPublicCommentRepository;
import com.tourism.booking.service.ICommentPublicService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentPublicService implements ICommentPublicService {

    private final IPublicCommentRepository commentRepository;

    public CommentPublicService(IPublicCommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<CommentResponse> getCommentsByHotelId(Long hotelId) {
        return commentRepository.findAllCommentsByHotelId(hotelId);
    }
}

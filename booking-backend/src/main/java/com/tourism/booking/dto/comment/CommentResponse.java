package com.tourism.booking.dto.comment;

import java.time.LocalDate;
import java.time.LocalTime;

public interface CommentResponse {
    Long getComment_id();

    String getContent();

    LocalDate getComment_date();

    LocalTime getComment_time();

    String getComment_author();
}

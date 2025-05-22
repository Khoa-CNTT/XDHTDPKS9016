package com.tourism.booking.dto.user;

import java.time.LocalDate;

public interface UserProfileProjection {
    Long getUser_id();
    String getFull_name();
    String getGender();
    String getAddress();
    String getEmail();
    String getPhone();
    LocalDate getBirth_date();
    String getStatus();
    String getUsername();
    int getRole_id();
}
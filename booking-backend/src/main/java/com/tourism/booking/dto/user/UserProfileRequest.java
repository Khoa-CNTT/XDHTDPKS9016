package com.tourism.booking.dto.user;

import com.tourism.booking.constant.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileRequest {
    String full_name;
    Gender gender;
    String address;
    String email;
    String phone;
    LocalDateTime birth_date;
    String status;
    int account_id;
}

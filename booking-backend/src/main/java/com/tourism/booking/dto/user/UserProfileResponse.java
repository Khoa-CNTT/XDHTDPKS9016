package com.tourism.booking.dto.user;

import com.tourism.booking.constant.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
    Long user_id;
    String full_name;
    Gender gender;
    String address;
    String email;
    String phone;
    LocalDateTime birth_date;
    String status;
    String username;
}

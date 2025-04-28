package com.tourism.booking.dto.user;

import com.tourism.booking.constant.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
    Long user_id;
    String full_name;
    Gender gender;
    String address;
    String email;
    String phone;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate  birth_date;
    String status;
    String username;
    private String role;
}

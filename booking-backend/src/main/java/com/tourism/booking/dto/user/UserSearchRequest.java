package com.tourism.booking.dto.user;

import com.tourism.booking.constant.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserSearchRequest {
    String full_name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate dobFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate dobTo;

    Gender gender;

    String email;

    String phone;
}

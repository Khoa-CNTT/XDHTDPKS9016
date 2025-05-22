package com.tourism.booking.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tourism.booking.constant.Gender;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private Long user_id;
    private String full_name;
    private String gender; // Vì dữ liệu từ native query nên dùng String để tránh lỗi enum
    private String address;
    private String email;
    private String phone;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birth_date;

    private String status;
    private String username;
    private int role_id;
}

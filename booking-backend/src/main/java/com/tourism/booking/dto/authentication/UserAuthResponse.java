package com.tourism.booking.dto.authentication;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserAuthResponse {
    Long id;
    String username;
    String role;
}

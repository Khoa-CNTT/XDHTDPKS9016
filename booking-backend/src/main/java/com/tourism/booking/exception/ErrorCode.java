package com.tourism.booking.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    USER_NOT_EXIST(40401, "User is not found", HttpStatus.NOT_FOUND),
    HOTEL_NOT_EXIST(40402, "Hotel is not found", HttpStatus.NOT_FOUND),

    UNAUTHENTICATION(40102, "Username or password is incorrect !", HttpStatus.UNAUTHORIZED),

    USERNAME_EXISTS(40901, "Username already exists !", HttpStatus.CONFLICT),
    EMAIL_EXISTS(40901, "Email already exists !", HttpStatus.CONFLICT),
    ;

    Integer code;
    String message;
    HttpStatus status;
}

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
    STUDENTS_NOT_EXIST(40401, "Student is not found", HttpStatus.NOT_FOUND),
    UNAUTHENTICATION(40102, "Username or password is incorrect !", HttpStatus.UNAUTHORIZED),
    ;

    Integer code;
    String message;
    HttpStatus status;
}

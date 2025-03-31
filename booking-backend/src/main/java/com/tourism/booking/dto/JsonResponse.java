package com.tourism.booking.dto;

import com.tourism.booking.dto.page.PageReponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class JsonResponse {

    public static <T> ResponseEntity<ApiResponse<?>> ok(T data) {
        if (data instanceof Page<?> pageData) {
//            Page<?> pageData = (Page<?>) data;
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .data(new PageReponse<>(pageData))
                            .build()
            );
        } else {
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .data(data)
                            .build()
            );
        }
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T t) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<T>builder()
                        .data(t)
                        .build());
    }

    public static ResponseEntity<?> noContent() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

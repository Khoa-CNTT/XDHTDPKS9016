package com.tourism.booking.dto.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponse {
    private Long hotel_id;
    private String name;
    private String image;
    private String address;
    private String hotline;
    private String description;
    private AccountInfo account;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountInfo {
        private Integer account_id;
        private String username;
        private String email;
    }
}

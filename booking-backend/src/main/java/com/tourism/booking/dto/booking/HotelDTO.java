package com.tourism.booking.dto.booking;

import lombok.Data;

@Data
public class HotelDTO {
    private Long hotelId;
    private String hotelName;
    private String address;
    private String phone;
    private String email;
    private String imageUrl;
    private int starRating;
}
package com.tourism.booking.dto.booking;

import lombok.Data;

@Data
public class HotelDTO {
    private Long hotelId;
    private String name;
    private String address;
    private String email;
    private String phone;
    private String description;
}
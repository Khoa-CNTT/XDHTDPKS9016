package com.tourism.booking.dto.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateHotelRequest {
    private String name;

    private String email;

    private String address;

    private String hotline;

    private String image;

    private String description;

    private String username;

    private String password;

    private boolean sendEmail;
}
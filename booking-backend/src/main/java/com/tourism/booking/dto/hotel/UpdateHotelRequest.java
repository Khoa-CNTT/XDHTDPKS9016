package com.tourism.booking.dto.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateHotelRequest {
    private String name;

    private String address;

    private String hotline;

    private String image;

    private String description;
}
package com.tourism.booking.dto.hotel;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateHotelRequest {
    @NotBlank(message = "Hotel name is required")
    private String name;

    @NotBlank(message = "Hotel address is required")
    private String address;

    @NotBlank(message = "Hotel hotline is required")
    private String hotline;

    private String image;

    private String description;
}
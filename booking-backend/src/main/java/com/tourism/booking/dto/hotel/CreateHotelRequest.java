package com.tourism.booking.dto.hotel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateHotelRequest {
    @NotBlank(message = "Hotel name is required")
    private String name;

    @NotBlank(message = "Hotel address is required")
    private String address;

    @NotBlank(message = "Hotel hotline is required")
    private String hotline;

    private String image;

    private String description;

    @NotNull(message = "Account ID is required")
    private Integer account_id;
}
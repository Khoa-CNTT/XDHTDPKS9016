package com.tourism.booking.dto.room;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomRequest {

    @NotNull(message = "Number of beds is required")
    @Min(value = 1, message = "Number of beds must be at least 1")
    private Integer numberBeds;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must not be negative")
    private BigDecimal price;

    @NotNull(message = "Room type ID is required")
    private Long roomTypeId;

    private String status = "AVAILABLE";

    @NotNull(message = "User ID is required")
    private Long userId;
}
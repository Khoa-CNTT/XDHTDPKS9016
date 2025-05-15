package com.tourism.booking.dto.room;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomTypeRequest {

    @NotBlank(message = "Type name is required")
    private String typeName;

    @NotNull(message = "Number of rooms is required")
    @Min(value = 1, message = "Number of rooms must be at least 1")
    private Integer numberRoom;

    private String description;

    private String roomImage;

    @NotNull(message = "Hotel ID is required")
    private Long hotelId;
}
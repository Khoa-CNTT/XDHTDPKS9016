package com.tourism.booking.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomSelectionDTO {

    @NotNull(message = "Room type ID is required")
    private Long roomTypeId;

    @Min(value = 1, message = "Number of rooms must be at least 1")
    private Integer numberOfRooms;

    private Long roomId; // Optional, used when selecting specific rooms
}
package com.tourism.booking.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomSelectionDTO {
    @NotNull(message = "Room type ID is required")
    private Long roomTypeId;

    @NotNull(message = "Room ID is required")
    private Long roomId;
}
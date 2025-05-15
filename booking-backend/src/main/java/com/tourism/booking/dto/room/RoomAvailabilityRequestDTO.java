package com.tourism.booking.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomAvailabilityRequestDTO {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long roomTypeId;
}
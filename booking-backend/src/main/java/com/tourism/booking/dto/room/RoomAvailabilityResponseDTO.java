package com.tourism.booking.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomAvailabilityResponseDTO {
    private Long roomTypeId;
    private String roomTypeName;
    private int availableRooms;
    private BigDecimal price;
    private int numberOfBeds;
    private String description;
    private String imageUrl;
    private Long hotelId;
    private String hotelName;

    // For response
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
package com.tourism.booking.dto.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomInfoResponse {
    private Long roomId;
    private int numberBeds;
    private BigDecimal price;
    private String status;
    private boolean isBooked;
}
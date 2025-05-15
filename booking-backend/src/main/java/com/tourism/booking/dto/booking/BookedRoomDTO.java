package com.tourism.booking.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookedRoomDTO {
    private Long roomId;
    private Long roomTypeId;
    private String roomTypeName;
    private Integer numberOfRooms;
    private Integer numberBeds;
    private BigDecimal pricePerRoom;
    private BigDecimal totalPrice;
}
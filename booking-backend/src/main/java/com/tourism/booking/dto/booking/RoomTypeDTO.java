package com.tourism.booking.dto.booking;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class RoomTypeDTO {
    private Long roomTypeId;
    private String typeName;
    private int numberBed;
    private int maximumPeople;
    private BigDecimal price;
    private String description;
    private String roomImage;
    private int availableRoom;
    private String status;
    private Long hotelId;
}
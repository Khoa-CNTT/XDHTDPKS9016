package com.tourism.booking.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomDTO {
    private Long id;
    private int numberBed;
    private BigDecimal price;
    private Long roomTypeId;
    private String roomTypeName;
    private Long hotelId;
    private String hotelName;
    private String status;
    private Double averageRating;
    private int totalRatings;
}
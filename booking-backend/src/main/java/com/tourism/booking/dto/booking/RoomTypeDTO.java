package com.tourism.booking.dto.booking;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class RoomTypeDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int maxOccupancy;

    // Additional fields for UI
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Double averageRating;
    private int totalRoomCount;
    private int availableRoomCount;
    private List<RoomDTO> availableRooms;

    // Nested DTO for Room in RoomType
    @Data
    public static class RoomDTO {
        private Long id;
        private int numberBed;
        private BigDecimal price;
        private String status;
    }
}
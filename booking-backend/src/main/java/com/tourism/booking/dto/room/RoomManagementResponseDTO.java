package com.tourism.booking.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomManagementResponseDTO {
    private Long hotelId;
    private String hotelName;
    private String hotelAddress;
    private List<RoomTypeManagementDTO> roomTypes;
    private int totalRooms;
    private int totalBookedRooms;
    private int totalAvailableRooms;
}
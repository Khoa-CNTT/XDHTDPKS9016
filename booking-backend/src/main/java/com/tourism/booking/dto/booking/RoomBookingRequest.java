package com.tourism.booking.dto.booking;

import lombok.Data;

@Data
public class RoomBookingRequest {
    private Long roomTypeId;
    private Long roomId;
    private Integer numberOfRooms;
}
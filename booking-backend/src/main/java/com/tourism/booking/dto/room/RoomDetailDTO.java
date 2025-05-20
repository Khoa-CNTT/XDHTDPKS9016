package com.tourism.booking.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDetailDTO {
    private Long roomId;
    private int numberBeds;
    private BigDecimal price;
    private String status;
    private boolean isBooked;
    private List<BookingDetailDTO> bookings; // Chi tiết các booking đã đặt phòng này
}
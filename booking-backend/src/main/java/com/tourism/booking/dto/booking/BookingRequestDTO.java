package com.tourism.booking.dto.booking;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
public class BookingRequestDTO {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private int numberPeople;
    private Long roomTypeId;
    // private Long userId;
    private Set<Long> serviceIds;
}

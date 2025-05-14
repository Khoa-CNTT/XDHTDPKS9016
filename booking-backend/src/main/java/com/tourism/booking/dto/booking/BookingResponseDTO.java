package com.tourism.booking.dto.booking;

import com.tourism.booking.dto.user.UserProfileResponse;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
public class BookingResponseDTO {
    private Long bookingId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private int numberPeople;
    private String status;
    private RoomTypeDTO roomType;
    private HotelDTO hotel;
    private Set<ServiceDTO> services;
    private BillDTO bill;
    private UserProfileResponse user;

    // Contact information
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private String contactAddress;
    private String specialRequests;

    // Thông tin UI quản lý
    private String statusDisplay;
}
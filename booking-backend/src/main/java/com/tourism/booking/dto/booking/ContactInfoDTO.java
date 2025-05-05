package com.tourism.booking.dto.booking;

import lombok.Data;

@Data
public class ContactInfoDTO {
    private Long bookingId; // To associate with a pending booking
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String specialRequests;
}
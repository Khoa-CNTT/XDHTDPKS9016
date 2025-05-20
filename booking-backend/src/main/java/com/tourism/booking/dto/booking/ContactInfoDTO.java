package com.tourism.booking.dto.booking;

import lombok.Data;

@Data
public class ContactInfoDTO {
    private Long bookingId;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private String contactAddress;
    private String specialRequests;
}
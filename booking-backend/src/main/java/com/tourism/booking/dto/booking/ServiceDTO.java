package com.tourism.booking.dto.booking;


import lombok.Data;
import java.math.BigDecimal;

@Data
public class ServiceDTO {
    private Long serviceId;
    private String serviceName;
    private BigDecimal servicePrice;
    private String serviceImage;
    private String description;
}
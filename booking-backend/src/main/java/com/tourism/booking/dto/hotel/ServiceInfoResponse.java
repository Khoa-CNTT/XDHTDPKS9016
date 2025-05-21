package com.tourism.booking.dto.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceInfoResponse {
    private Long serviceId;
    private String serviceName;
    private BigDecimal servicePrice;
    private String serviceImage;
    private String description;
}

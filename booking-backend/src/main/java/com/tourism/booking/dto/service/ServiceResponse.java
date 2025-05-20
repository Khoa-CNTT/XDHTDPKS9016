package com.tourism.booking.dto.service;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceResponse {
    Long service_id;
    String service_name;
    BigDecimal service_price;
    String service_image;
    String description;
}
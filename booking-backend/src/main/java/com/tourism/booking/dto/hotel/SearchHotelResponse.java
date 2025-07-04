package com.tourism.booking.dto.hotel;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchHotelResponse {
    Long hotel_id;
    String name;
    String image;
    String address;
    String hotline;
    String description;
}

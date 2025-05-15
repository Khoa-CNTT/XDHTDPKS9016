package com.tourism.booking.dto.publicHotel;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelPublicDTO {
    Long hotel_id;
    String name;
    String image;
    String address;
    String hotline;
    String description;
}

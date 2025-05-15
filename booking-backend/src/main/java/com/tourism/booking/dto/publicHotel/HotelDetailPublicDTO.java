package com.tourism.booking.dto.publicHotel;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelDetailPublicDTO {
    Long hotel_id;
    String name;
    String image;
    String address;
    String hotline;
    String description;
    List<ServicePublicDTO> services;
    List<RoomTypePublicDTO> roomTypes;
}

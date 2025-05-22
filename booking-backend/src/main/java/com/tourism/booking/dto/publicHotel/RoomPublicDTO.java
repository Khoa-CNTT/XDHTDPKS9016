package com.tourism.booking.dto.publicHotel;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomPublicDTO {
    Long id_room;
    Integer number_bed;
    Integer number_rooms;
    BigDecimal price;
}
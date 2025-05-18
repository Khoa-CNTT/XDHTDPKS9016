package com.tourism.booking.dto.room;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomRequest {
    int number_bed;
    Integer number_rooms;
    BigDecimal price;
    Long room_type_id;
    String status;
}

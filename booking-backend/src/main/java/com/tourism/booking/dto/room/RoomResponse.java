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
public class RoomResponse {
    Long id_room;
    Integer number_bed;
    BigDecimal price;
    String full_name;
    String type_name;
    String status;
}

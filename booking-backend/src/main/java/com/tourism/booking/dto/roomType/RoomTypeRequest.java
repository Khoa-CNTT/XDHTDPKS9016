package com.tourism.booking.dto.roomType;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomTypeRequest {
    String type_name;
    int number_room;
    String description;
    String room_image;
}

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
public class RoomTypePublicDTO {
    Long room_type_id;
    String type_name;
    Integer number_room;
    String description;
    String room_image;
    List<RoomPublicDTO> rooms;
    String status;
}

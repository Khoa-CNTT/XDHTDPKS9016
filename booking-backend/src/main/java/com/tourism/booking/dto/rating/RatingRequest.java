package com.tourism.booking.dto.rating;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingRequest {
    int score;
    int room_type_id;
    int id_room;
}

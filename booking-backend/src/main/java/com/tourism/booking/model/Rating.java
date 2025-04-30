package com.tourism.booking.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "rating")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long rating_id;

    @Column(name = "score")
    int score;

    @Column(name = "rating_date")
    LocalDate rating_date;

    @Column(name = "rating_time")
    LocalTime rating_time;

    // Quan hệ nhiều-1 với UserProfile
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    UserProfile user_profile;

    @ManyToOne
    @JoinColumn(name = "room_type_id", nullable = false)
    RoomType room_type;
}

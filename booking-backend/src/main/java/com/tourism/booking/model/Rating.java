package com.tourism.booking.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

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

    @Column(name = "score", nullable = false)
    int score;

//    @Column(name = "comment", columnDefinition = "TEXT")
//    String comment;

    @Column(name = "rating_date")
    LocalDate rating_date;

    @Column(name = "rating_time")
    LocalTime rating_time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    UserProfile user_profile;

    @OneToMany(mappedBy = "rating", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<RoomRating> roomRatings = new HashSet<>();
}

package com.tourism.booking.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long comment_id;

    @Column(name = "content")
    String content;

    @Column(name = "comment_date")
    LocalDate comment_date;

    @Column(name = "comment_time")
    LocalTime comment_time;

    // Quan hệ nhiều-1 với UserProfile
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    UserProfile user_profile;

    @JsonIgnore
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<RoomComment> roomComments = new HashSet<>();
}

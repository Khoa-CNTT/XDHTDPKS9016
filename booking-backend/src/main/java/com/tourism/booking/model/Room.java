package com.tourism.booking.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long room_id;

    @Column(name = "room_number")
    String room_number;

    @Column(name = "room_type")
    String room_type;

    @Column(name = "number_bed")
    int number_bed;

    @Column(name = "maximum_people")
    int maximum_people;

    @Column(name = "price")
    BigDecimal price;

    @Column(name = "room_image")
    String room_image;

    @Column(name = "description")
    String description;

    @Column(name = "status")
    String status;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    Hotel hotel;

    // Quan hệ 1-N với Rating
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Rating> ratings = new HashSet<>();

    // Quan hệ 1-N với Comment
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Comment> comments = new HashSet<>();

    // Quan hệ 1-N với Booking
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Booking> bookings = new HashSet<>();
}

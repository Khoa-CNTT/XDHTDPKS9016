package com.tourism.booking.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "room_type")
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_type_id")
    Long room_type_id;

    @Column(name = "type_name", length = 255, nullable = false)
    String type_name;

    @Column(name = "number_bed", nullable = false)
    int number_bed;

    @Column(name = "maximum_people", nullable = false)
    int maximum_people;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    BigDecimal price;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Column(name = "room_image", length = 255)
    String room_image;

    @Column(name = "available_room")
    int available_room = 0;

    @Column(name = "status", length = 50)
    String status ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    @JsonBackReference
    Hotel hotel;

    // Quan hệ 1-N với Rating
    @JsonIgnore
    @OneToMany(mappedBy = "room_type", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Rating> ratings = new HashSet<>();

    // Quan hệ 1-N với Comment
    @JsonIgnore
    @OneToMany(mappedBy = "room_type", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Comment> comments = new HashSet<>();

    // Quan hệ 1-N với Booking
    @JsonIgnore
    @OneToMany(mappedBy = "room_type", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Booking> bookings = new HashSet<>();
}

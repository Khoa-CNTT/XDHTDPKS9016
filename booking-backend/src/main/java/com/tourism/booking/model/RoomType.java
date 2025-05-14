package com.tourism.booking.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "room_type_id")
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_type_id")
    Long room_type_id;

    @Column(name = "type_name", length = 255, nullable = false)
    String type_name;

    @Column(name = "number_room", nullable = false)
    int number_room;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Column(name = "room_image", length = 255)
    String room_image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    Hotel hotel;
}

package com.tourism.booking.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "hotel")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "hotel_id")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long hotel_id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "image")
    String image;

    @Column(name = "address", nullable = false)
    String address;

    @Column(name = "hotline", nullable = false)
    String hotline;

    @Column(name = "description")
    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    Account account;

    @OneToMany(mappedBy = "hotel", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "service_id")
    @JsonIgnore
    Set<Services> services = new HashSet<>();

    @OneToMany(mappedBy = "hotel", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "room_type_id")
    @JsonIgnore
    Set<RoomType> roomTypes = new HashSet<>();
}

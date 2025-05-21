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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "service")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "service_id")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long service_id;

    @Column(name = "service_name", nullable = false)
    String service_name;

    @Column(name = "service_price", nullable = false)
    BigDecimal service_price;

    @Column(name = "service_image")
    String service_image;

    @Column(name = "description")
    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    Hotel hotel;

    @ManyToMany(mappedBy = "services")
    @JsonIgnore
    Set<Booking> bookings = new HashSet<>();
}

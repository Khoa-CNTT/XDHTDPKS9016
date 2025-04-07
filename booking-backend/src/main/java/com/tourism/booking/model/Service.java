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
@Table(name = "service")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long service_id;

    @Column(name = "service_name")
    String service_name;

    @Column(name = "service_price")
    BigDecimal service_price;

    @Column(name = "service_image")
    String service_image;

    @Column(name = "description")
    String description;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    Hotel hotel;

    @ManyToMany(mappedBy = "services")
    Set<Booking> bookings = new HashSet<>();
}

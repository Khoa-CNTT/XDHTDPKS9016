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
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_booking")
    Long id_booking;

    @Column(name = "check_in_date")
    LocalDate check_in_date;

    @Column(name = "check_out_date")
    LocalDate check_out_date;

    @Column(name = "check_in_time")
    LocalTime check_in_time;

    @Column(name = "check_out_time")
    LocalTime check_out_time;

    @Column(name = "number_people")
    int number_people;

    @Column(name = "status")
    String status;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    Room room;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    UserProfile user_profile;

    // Quan hệ 1-1 với Bill
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    Bill bill;

    @ManyToMany
    @JoinTable(
            name = "booking_service",                // Tên bảng trung gian
            joinColumns = @JoinColumn(name = "booking_id"),  // Khóa ngoại trỏ đến Booking
            inverseJoinColumns = @JoinColumn(name = "service_id") // Khóa ngoại trỏ đến Service
    )
    Set<Services> services = new HashSet<>();
}

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
    private Long id_booking;

    @Version
    private Long version;

    @Column(name = "id_booking_temp")
    private Long id_booking_temp;

    @Column(name = "check_in_date")
    private LocalDate check_in_date;

    @Column(name = "check_out_date")
    private LocalDate check_out_date;

    @Column(name = "check_in_time")
    private LocalTime check_in_time;

    @Column(name = "check_out_time")
    private LocalTime check_out_time;

    @Column(name = "number_people")
    private int number_people;

    @Column(name = "status")
    private String status;

    // Contact information fields
    @Column(name = "contact_name")
    private String contact_name;

    @Column(name = "contact_email")
    private String contact_email;

    @Column(name = "contact_phone")
    private String contact_phone;

    @Column(name = "contact_address")
    private String contact_address;

    @Column(name = "special_requests")
    private String special_requests;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookingRoom> bookingRooms = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserProfile user_profile;

    @OneToOne(mappedBy = "booking", fetch = FetchType.LAZY)
    private Bill bill;

    @ManyToMany
    @JoinTable(name = "booking_service", joinColumns = @JoinColumn(name = "booking_id"), inverseJoinColumns = @JoinColumn(name = "service_id"))
    private Set<Services> services = new HashSet<>();
}

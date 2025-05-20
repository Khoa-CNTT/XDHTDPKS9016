package com.tourism.booking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tourism.booking.constant.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Long user_id;

    @Column(name = "full_name", length = 50)
    String full_name;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", columnDefinition = "VARCHAR(20)")
    Gender gender;

    @Column(name = "address", length = 255)
    String address;

    @Column(name = "email")
    String email;

    @Column(name = "phone", length = 20)
    String phone;

    @Column(name = "birth_date")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    LocalDate birth_date;

    @Column(name = "status")
    String status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    @JsonIgnore
    Account account;

    // Quan hệ 1-N với Booking
    @OneToMany(mappedBy = "user_profile", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Booking> bookings = new HashSet<>();

    // Quan hệ 1-N với Comment
    @OneToMany(mappedBy = "user_profile", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Comment> comments = new HashSet<>();

    // Quan hệ 1-N với Rating
    @OneToMany(mappedBy = "user_profile", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Rating> ratings = new HashSet<>();
}

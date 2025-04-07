package com.tourism.booking.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long bill_id;

    @Column(name = "total_amount")
    BigDecimal total_amount;

    @Column(name = "deposit")
    BigDecimal deposit;

    @Column(name = "print_date")
    LocalDate print_date;

    @Column(name = "print_time")
    LocalTime print_time;

    @OneToOne
    @JoinColumn(name = "id_booking") // Tên cột khóa ngoại trong bảng Bill
    Booking booking;

    // Quan hệ 1-n với Payment
    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Payment> payments = new ArrayList<>();
}
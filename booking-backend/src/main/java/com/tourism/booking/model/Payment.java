package com.tourism.booking.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long payment_id;

    @Column(name = "amount")
    BigDecimal amount;

    @Column(name = "payment_date")
    LocalDate payment_date;

    @Column(name = "payment_time")
    LocalTime payment_time;

    @Column(name = "payment_method")
    String payment_method;

    @Column(name = "transaction_id")
    String transaction_id;

    @Column(name = "status")
    String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id")
    Bill bill;

    @Column(name = "booking_id", insertable = true, updatable = true)
    Long bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", insertable = false, updatable = false)
    Booking booking;
}

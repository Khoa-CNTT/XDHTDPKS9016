package com.tourism.booking.model;

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
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long account_id;

    @Column(name = "username")
    String username;

    @Column(name = "password")
    String password;

    @Column(name = "email")
    String email;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    ForgotPassword forgotPassword;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_account", // Tên bảng trung gian
            joinColumns = @JoinColumn(name = "account_id"), // Cột khóa ngoại tham chiếu đến User
            inverseJoinColumns = @JoinColumn(name = "role_id") // Cột khóa ngoại tham chiếu đến Role
    )
    Set<Role> roles = new HashSet<>();
}

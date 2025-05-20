package com.tourism.booking.model;

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
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    Long role_id;

    @Column(name = "role_name")
    String role_name;

    @ManyToMany(mappedBy = "roles") // Bên này sẽ không tạo cột mà chỉ ánh xạ theo bảng trung gian
    @JsonIgnore
    Set<Account> acc = new HashSet<>();
}

package com.tourism.booking.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class RoleAccount {

    @EmbeddedId
    RoleAccountId id;

    @ManyToOne
    @MapsId("account_id")
    @JoinColumn(name = "account_id")
    Account account;

    @ManyToOne
    @MapsId("role_id")
    @JoinColumn(name = "role_id")
    Role role;
}


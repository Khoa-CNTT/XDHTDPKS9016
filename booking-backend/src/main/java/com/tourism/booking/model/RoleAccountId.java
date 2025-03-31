package com.tourism.booking.model;

import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleAccountId implements Serializable {
    int account_id;
    int role_id;
}

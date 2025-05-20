package com.tourism.booking.config;

import com.tourism.booking.constant.Gender;
import com.tourism.booking.model.UserProfile;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class UserProfileSpecification {
    public static Specification<UserProfile> hasName(String name) {
        return (root, query, cb) ->
                (name == null || name.isEmpty()) ? null
                        : cb.like(cb.lower(root.get("full_name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<UserProfile> hasDobFrom(LocalDate dobFrom) {
        return (root, query, cb) ->
                (dobFrom == null) ? null
                        : cb.greaterThanOrEqualTo(root.get("birth_date"), dobFrom);
    }

    public static Specification<UserProfile> hasDobTo(LocalDate dobTo) {
        return (root, query, cb) ->
                (dobTo == null) ? null
                        : cb.lessThanOrEqualTo(root.get("birth_date"), dobTo);
    }

    public static Specification<UserProfile> hasGender(Gender gender) {
        return (root, query, cb) ->
                (gender == null) ? null
                        : cb.equal(root.get("gender"), gender);
    }

    public static Specification<UserProfile> hasEmail(String email) {
        return (root, query, cb) ->
                (email == null || email.isEmpty()) ? null
                        : cb.like(root.get("email"), "%" + email + "%");
    }

    public static Specification<UserProfile> hasPhone(String phone) {
        return (root, query, cb) ->
                (phone == null || phone.isEmpty()) ? null
                        : cb.like(root.get("phone"), "%" + phone + "%");
    }
}

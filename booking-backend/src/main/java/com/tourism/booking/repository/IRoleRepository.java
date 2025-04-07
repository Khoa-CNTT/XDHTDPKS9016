package com.tourism.booking.repository;

import com.tourism.booking.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Integer> {
    @Query(value = """
                select r.role_id,r.role_name from booking_management.role r
                where r.role_name = :role_name
            """, nativeQuery = true)
    Optional<Role> findByRoleName(String role_name);

}

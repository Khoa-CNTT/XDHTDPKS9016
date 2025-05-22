package com.tourism.booking.repository;

import com.tourism.booking.dto.user.UserProfileDTO;
import com.tourism.booking.dto.user.UserProfileProjection;
import com.tourism.booking.dto.user.UserProfileResponse;
import com.tourism.booking.model.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserProfileRepository extends JpaRepository<UserProfile, Long> {

    boolean existsByEmail(String email);

    @Query(value = """
            SELECT new com.tourism.booking.dto.user.UserProfileResponse(
                u.user_id,
                u.full_name,
                u.gender,
                u.address,
                u.email,
                u.phone,
                u.birth_date,
                u.status,
                a.username)
            FROM UserProfile u
            JOIN u.account a
            WHERE u.user_id = :id
            """, nativeQuery = false)
    Optional<UserProfileResponse> findUserProfileAndAccount(Long id);

    @Query(value = """
            SELECT new com.tourism.booking.dto.user.UserProfileResponse(
                u.user_id,
                u.full_name,
                u.gender,
                u.address,
                u.email,
                u.phone,
                u.birth_date,
                u.status,
                a.username)
            FROM UserProfile u
            JOIN u.account a
            WHERE a.account_id = :id
            """, nativeQuery = false)
    Optional<UserProfileResponse> findUserProfileByAccountId(Long id);

    @Query(value = """
            SELECT u FROM UserProfile u
            JOIN u.account a
            WHERE a.account_id = :id
            """, nativeQuery = false)
    Optional<UserProfile> findUserProfileByAccoutId(Long id);


    @Query(value = "SELECT u.user_id, u.full_name, u.gender, u.address, u.email, u.phone, " +
            "u.birth_date, u.status, a.username, r.role_id " +
            "FROM user_profile u " +
            "JOIN booking_management.account a ON a.account_id = u.account_id " +
            "JOIN role_account r ON a.account_id = r.account_id " +
            "WHERE r.role_id = 2 OR r.role_id = 3",
            nativeQuery = true)
    Page<UserProfileProjection> getUserProfileDTOs(Pageable pageable);
}

package com.tourism.booking.repository;

import com.tourism.booking.dto.user.UserProfileResponse;
import com.tourism.booking.model.UserProfile;
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

}

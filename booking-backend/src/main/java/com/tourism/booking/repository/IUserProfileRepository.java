package com.tourism.booking.repository;

import com.tourism.booking.dto.user.UserProfileResponse;
import com.tourism.booking.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserProfileRepository extends JpaRepository<UserProfile, Long> , JpaSpecificationExecutor<UserProfile> {
    boolean existsByEmail(String email);
    @Query(value = """
            select u.user_id,
                   u.full_name,
                   u.gender,
                   u.address,
                   u.email,
                   u.phone,
                   u.birth_date,
                   u.status,
                   a.username 
            from user_profile u
            join account a 
                on a.account_id = u.account_id
            where u.user_id = :id
            """,nativeQuery = true)
    Optional<UserProfileResponse> findUserProfileAndAccount(Long id);
}

package com.tourism.booking.service;

import com.tourism.booking.dto.user.UserProfileDTO;
import com.tourism.booking.dto.user.UserProfileResponse;
import com.tourism.booking.dto.user.UserSearchRequest;
import com.tourism.booking.model.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IUserProfileService {
    Page<UserProfileDTO> findAll(Pageable pageable);

    Optional<UserProfileResponse> findById(Long id);

    Optional<UserProfile> findUserProfileEntityById(Long id);

    Optional<UserProfileResponse> findByAccoutId(Long id);

    Optional<UserProfile> findUserProfileByAccoutId(Long id);

    UserProfile save(UserProfile userProfile);

    void deleteById(Long id);
}

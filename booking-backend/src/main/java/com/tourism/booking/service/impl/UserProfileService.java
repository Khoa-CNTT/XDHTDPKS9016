package com.tourism.booking.service.impl;

import com.tourism.booking.config.UserProfileSpecification;
import com.tourism.booking.dto.user.UserProfileResponse;
import com.tourism.booking.dto.user.UserSearchRequest;
import com.tourism.booking.model.UserProfile;
import com.tourism.booking.repository.IUserProfileRepository;
import com.tourism.booking.service.IUserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileService implements IUserProfileService {

    IUserProfileRepository userProfileRepository;

    @Override
    public Page<UserProfile> findAll(UserSearchRequest userSearchRequest, Pageable pageable) {
        Specification<UserProfile> specification = Specification
                .where(UserProfileSpecification.hasName(userSearchRequest.getFull_name())
                        .and(UserProfileSpecification.hasDobFrom(userSearchRequest.getDobFrom()))
                        .and(UserProfileSpecification.hasDobTo(userSearchRequest.getDobTo()))
                        .and(UserProfileSpecification.hasGender(userSearchRequest.getGender()))
                        .and(UserProfileSpecification.hasEmail(userSearchRequest.getEmail()))
                        .and(UserProfileSpecification.hasPhone(userSearchRequest.getPhone()))

                );
        return userProfileRepository.findAll(specification, pageable);
    }

    @Override
    public Optional<UserProfileResponse> findById(Long id) {
        return userProfileRepository.findUserProfileAndAccount(id);
    }

    @Override
    public Optional<UserProfile> findUserProfileEntityById(Long id) {
        return userProfileRepository.findById(id);
    }

    @Override
    public UserProfile save(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    @Override
    public void deleteById(Long id) {
        userProfileRepository.deleteById(id);
    }

    @Override
    public Optional<UserProfileResponse> findByAccoutId(Long id) {
        return userProfileRepository.findUserProfileByAccountId(id);
    }
}

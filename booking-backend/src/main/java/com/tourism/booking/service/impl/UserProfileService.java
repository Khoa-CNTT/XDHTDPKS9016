package com.tourism.booking.service.impl;

import com.tourism.booking.dto.user.UserProfileResponse;
import com.tourism.booking.model.UserProfile;
import com.tourism.booking.repository.IUserProfileRepository;
import com.tourism.booking.service.IUserProfileService;
import com.tourism.booking.mapper.IUserProfileMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileService implements IUserProfileService {

    IUserProfileRepository userProfileRepository;
    IUserProfileMapper userProfileMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<UserProfileResponse> findAll(Pageable pageable) {
        Page<UserProfile> userProfiles = userProfileRepository.findAll(pageable);
        return userProfiles.map(userProfileMapper::UserProfileToUserProfileResponse);
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

    @Override
    public Optional<UserProfile> findUserProfileByAccoutId(Long id) {
        return userProfileRepository.findUserProfileByAccoutId(id);
    }
}

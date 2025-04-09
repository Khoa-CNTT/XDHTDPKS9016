package com.tourism.booking.controller;

import com.tourism.booking.dto.page.PageReponse;
import com.tourism.booking.dto.user.UserProfileRequest;
import com.tourism.booking.dto.user.UserSearchRequest;
import com.tourism.booking.exception.ApiException;
import com.tourism.booking.exception.ErrorCode;
import com.tourism.booking.mapper.IUserProfileMapper;
import com.tourism.booking.model.UserProfile;
import com.tourism.booking.service.IUserProfileService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/management_user")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserProfileController {
    IUserProfileService userProfileService;
    IUserProfileMapper userProfileMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getUserProfile(UserSearchRequest userSearchRequest, Pageable pageable) {
        return ResponseEntity.ok(new PageReponse<>(userProfileService.findAll(userSearchRequest, pageable)));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable("id") Long id) {
        return userProfileService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_EXIST));
    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping
//    public ResponseEntity<?> createUserProfile(@RequestBody UserProfileRequest userProfileRequest) {
//        UserProfile userProfile = userProfileMapper.UserProfileRequestToUserProfile(userProfileRequest);
//    }
//
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserProfile(@PathVariable("id") Long id, UserSearchRequest userSearchRequest, @RequestBody UserProfileRequest userProfileRequest) {
        UserProfile userProfile = userProfileMapper.UserProfileRequestToUserProfile(userProfileRequest);
        userProfile.setUser_id(id);
        userProfileService.save(userProfile);
        return ResponseEntity.ok(userProfileMapper.UserProfileToUserProfileResponse(userProfile));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserProfile(@PathVariable("id") Long id) {
        userProfileService.findById(id).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_EXIST));
        userProfileService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

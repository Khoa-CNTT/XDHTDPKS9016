package com.tourism.booking.controller;

import com.tourism.booking.dto.user.UserProfileRequest;
import com.tourism.booking.dto.user.UserProfileResponse;
import com.tourism.booking.exception.ApiException;
import com.tourism.booking.exception.ErrorCode;
import com.tourism.booking.mapper.IUserProfileMapper;
import com.tourism.booking.model.Account;
import com.tourism.booking.model.UserProfile;
import com.tourism.booking.service.IAccountService;
import com.tourism.booking.service.IUserProfileService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@PreAuthorize("hasAnyRole('USER','SUPPLIER','ADMIN')")
@RequestMapping("${api.prefix}/user-profile")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@CrossOrigin("http://localhost:5173/")
public class UserProfileController {
    IUserProfileService userProfileService;
    IAccountService accountService;
    IUserProfileMapper userProfileMapper;

    @GetMapping
    public ResponseEntity<?> getUserProfile(Principal principal) {
        Account acc = accountService.getAccountByUsername(principal.getName());
        System.out.println(acc.getAccount_id());
        return userProfileService.findByAccoutId(acc.getAccount_id())
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_EXIST));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable Long id,
                                           @RequestBody UserProfileRequest userProfileRequest) {
        UserProfile existingProfile = userProfileService.findUserProfileEntityById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_EXIST));

        UserProfile updatedProfile = userProfileMapper.UserProfileRequestToUserProfile(userProfileRequest);

        updatedProfile.setUser_id(id);
        updatedProfile.setAccount(existingProfile.getAccount());

        UserProfile savedProfile = userProfileService.save(updatedProfile);
        UserProfileResponse response = userProfileMapper.UserProfileToUserProfileResponse(savedProfile);
        response.setUsername(existingProfile.getAccount().getUsername());

        return ResponseEntity.ok(response);
    }
}

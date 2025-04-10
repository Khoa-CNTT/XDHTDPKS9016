package com.tourism.booking.controller;

import com.tourism.booking.dto.page.PageReponse;
import com.tourism.booking.dto.user.UserProfileRequest;
import com.tourism.booking.dto.user.UserProfileResponse;
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
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("${api.prefix}/management-user")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ManagementUserController {
    IUserProfileService userProfileService;
    IUserProfileMapper userProfileMapper;

    @GetMapping
    public ResponseEntity<?> getUserProfile(UserSearchRequest userSearchRequest, Pageable pageable) {
        return ResponseEntity.ok(new PageReponse<>(userProfileService.findAll(userSearchRequest, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable("id") Long id) {
        return userProfileService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_EXIST));
    }

    // @PreAuthorize("hasRole('ADMIN')")
    // @PostMapping
    // public ResponseEntity<?> createUserProfile(@RequestBody UserProfileRequest
    // userProfileRequest) {
    // UserProfile userProfile =
    // userProfileMapper.UserProfileRequestToUserProfile(userProfileRequest);
    // }
    //
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable Long id,
                                           @RequestBody UserProfileRequest userProfileRequest) {
        // Lấy thông tin UserProfile hiện tại từ database
        UserProfile existingProfile = userProfileService.findUserProfileEntityById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_EXIST));

        // Tạo đối tượng từ request
        UserProfile updatedProfile = userProfileMapper.UserProfileRequestToUserProfile(userProfileRequest);

        // Giữ nguyên ID và account
        updatedProfile.setUser_id(id);
        updatedProfile.setAccount(existingProfile.getAccount());

        // Lưu thông tin đã cập nhật
        UserProfile savedProfile = userProfileService.save(updatedProfile);
        UserProfileResponse response = userProfileMapper.UserProfileToUserProfileResponse(savedProfile);
        response.setUsername(existingProfile.getAccount().getUsername());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserProfile(@PathVariable("id") Long id) {
        userProfileService.findById(id).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_EXIST));
        userProfileService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

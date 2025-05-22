package com.tourism.booking.controller;

import com.tourism.booking.dto.page.PageReponse;
import com.tourism.booking.dto.user.UserProfileDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/management-user")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@CrossOrigin("http://localhost:5173/")
public class ManagementUserController {
    IUserProfileService userProfileService;
    IUserProfileMapper userProfileMapper;

    @GetMapping
    public ResponseEntity<?> getUserProfile(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserProfileDTO> userProfiles = userProfileService.findAll(pageable);
        return ResponseEntity.ok(new PageReponse<>(userProfiles));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable("id") Long id) {
        return userProfileService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_EXIST));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable Long id,
            @RequestBody UserProfileRequest userProfileRequest) {
        // Lấy thông tin UserProfile hiện tại từ database
        UserProfile existingProfile = userProfileService.findUserProfileEntityById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_EXIST));

        // Cập nhật thông tin từ request vào đối tượng hiện có
        existingProfile.setFull_name(userProfileRequest.getFull_name());
        existingProfile.setGender(userProfileRequest.getGender());
        existingProfile.setAddress(userProfileRequest.getAddress());
        existingProfile.setEmail(userProfileRequest.getEmail());
        existingProfile.setPhone(userProfileRequest.getPhone());
        existingProfile.setBirth_date(userProfileRequest.getBirth_date());
        existingProfile.setStatus(userProfileRequest.getStatus());

        // Lưu thông tin đã cập nhật
        UserProfile savedProfile = userProfileService.save(existingProfile);
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

package com.tourism.booking.mapper;

import com.tourism.booking.dto.user.UserProfileRequest;
import com.tourism.booking.dto.user.UserProfileResponse;
import com.tourism.booking.model.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserProfileMapper {
    UserProfile UserProfileRequestToUserProfile(UserProfileRequest userProfileRequest);
    UserProfileResponse UserProfileToUserProfileResponse(UserProfile userProfile);
}

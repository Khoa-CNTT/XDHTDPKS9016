package com.tourism.booking.mapper;

import com.tourism.booking.dto.hotel.HotelResponse;
import com.tourism.booking.model.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HotelMapper {
    @Mapping(target = "account.account_id", source = "account.account_id")
    @Mapping(target = "account.username", source = "account.username")
    @Mapping(target = "account.email", source = "account.email")
    HotelResponse toResponse(Hotel hotel);
}
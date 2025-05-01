package com.tourism.booking.dto.hotel;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelInfoResponse {
    String name;
    String image;
    String address;
    String hotline;
    String description;
    List<ServiceInfoResponse> services;
    List<RoomTypeInfoResponse> roomTypes;

}

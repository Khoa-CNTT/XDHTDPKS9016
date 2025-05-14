package com.tourism.booking.service;

import com.tourism.booking.dto.hotel.CreateHotelRequest;
import com.tourism.booking.dto.hotel.HotelInfoResponse;
import com.tourism.booking.dto.hotel.HotelResponse;
import com.tourism.booking.dto.hotel.UpdateHotelRequest;
import com.tourism.booking.model.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IHotelService {
    HotelResponse createHotel(CreateHotelRequest request);

    Page<HotelInfoResponse> getHotels(Pageable pageable);

    Optional<HotelInfoResponse> getHotelById(Long hotelId);

    Hotel save(Hotel hotel);

    void deleteHotel(Long hotelId);

    Optional<Hotel> getHotelByAccountId(Long accId);

    boolean isOwnerOfRoomType(Long accountId, Long roomTypeId);

    HotelInfoResponse updateHotelInfo(Long accountId, Hotel hotel);

    HotelResponse updateBasicHotelInfo(Long hotelId, UpdateHotelRequest request);
}

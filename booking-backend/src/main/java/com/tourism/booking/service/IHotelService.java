package com.tourism.booking.service;

import com.tourism.booking.model.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IHotelService {
    Page<Hotel> getHotels(Pageable pageable);
    Optional<Hotel> getHotelById(Long hotelId);
    Hotel save(Hotel hotel);
    void deleteHotel(Long hotelId);
    Optional<Hotel> getHotelByAccountId(Long hotelId);
}

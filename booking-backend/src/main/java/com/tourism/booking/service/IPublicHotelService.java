package com.tourism.booking.service;


import com.tourism.booking.dto.publicHotel.HotelDetailPublicDTO;
import com.tourism.booking.dto.publicHotel.HotelPublicDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface IPublicHotelService {
    Optional<HotelDetailPublicDTO> getHotelDetail(Long hotelId, LocalDate checkIn, LocalDate checkOut);
    Page<HotelPublicDTO> getHotels(Pageable pageable);
}

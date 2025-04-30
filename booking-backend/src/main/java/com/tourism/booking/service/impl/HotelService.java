package com.tourism.booking.service.impl;

import com.tourism.booking.model.Hotel;
import com.tourism.booking.repository.IHotelRepository;
import com.tourism.booking.service.IHotelService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HotelService implements IHotelService {
    IHotelRepository hotelRepository;

    @Override
    public Page<Hotel> getHotels(Pageable pageable) {
        return hotelRepository.findAll(pageable);
    }

    @Override
    public Optional<Hotel> getHotelById(Long hotelId) {
        return hotelRepository.findById(hotelId);
    }

    @Override
    public Hotel save(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @Override
    public void deleteHotel(Long hotelId) {
        hotelRepository.deleteById(hotelId);
    }

    @Override
    public Optional<Hotel> getHotelByAccountId(Long accId) {
        return hotelRepository.findByAccountId(accId);
    }


    @Override
    public boolean isOwnerOfRoomType(Long accountId, Long roomTypeId) {
        return hotelRepository.isOwnerOfRoomType(accountId, roomTypeId);
    }
}

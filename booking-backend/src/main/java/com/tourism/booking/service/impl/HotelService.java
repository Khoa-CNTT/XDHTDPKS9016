package com.tourism.booking.service.impl;

import com.tourism.booking.dto.hotel.HotelInfoResponse;
import com.tourism.booking.exception.ApiException;
import com.tourism.booking.exception.ErrorCode;
import com.tourism.booking.mapper.IHotelInfoMapper;
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
    IHotelInfoMapper mapper;

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

    @Override
    public HotelInfoResponse updateHotelInfo(Long accountId, Hotel hotel) {
        Hotel existing = hotelRepository.findByAccountId(accountId)
                .orElseThrow(() -> new ApiException(ErrorCode.HOTEL_NOT_EXIST));

        existing.setName(hotel.getName());
        existing.setImage(hotel.getImage());
        existing.setAddress(hotel.getAddress());
        existing.setHotline(hotel.getHotline());
        existing.setDescription(hotel.getDescription());

        Hotel saved = hotelRepository.save(existing);

        return mapper.toDto(saved);
    }
}

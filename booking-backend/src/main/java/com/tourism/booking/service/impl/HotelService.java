package com.tourism.booking.service.impl;

import com.tourism.booking.dto.hotel.CreateHotelRequest;
import com.tourism.booking.dto.hotel.HotelInfoResponse;
import com.tourism.booking.dto.hotel.HotelResponse;
import com.tourism.booking.dto.hotel.UpdateHotelRequest;
import com.tourism.booking.exception.ApiException;
import com.tourism.booking.exception.ErrorCode;
import com.tourism.booking.mapper.HotelMapper;
import com.tourism.booking.mapper.IHotelInfoMapper;
import com.tourism.booking.model.Account;
import com.tourism.booking.model.Hotel;
import com.tourism.booking.model.RoomType;
import com.tourism.booking.model.Services;
import com.tourism.booking.repository.IAccountRepository;
import com.tourism.booking.repository.IHotelRepository;
import com.tourism.booking.service.IHotelService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HotelService implements IHotelService {
    IHotelRepository hotelRepository;
    IAccountRepository accountRepository;
    IHotelInfoMapper hotelInfoMapper;
    HotelMapper hotelMapper;

    @Override
    @Transactional
    public HotelResponse createHotel(CreateHotelRequest request) {
        // Validate and get account
        Account account = accountRepository.findById(request.getAccount_id())
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_EXIST));

        // Create new hotel
        Hotel hotel = new Hotel();
        hotel.setName(request.getName());
        hotel.setAddress(request.getAddress());
        hotel.setHotline(request.getHotline());
        hotel.setImage(request.getImage());
        hotel.setDescription(request.getDescription());
        hotel.setAccount(account);

        Hotel savedHotel = hotelRepository.save(hotel);
        return hotelMapper.toResponse(savedHotel);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HotelInfoResponse> getHotels(Pageable pageable) {
        return hotelRepository.findAll(pageable)
                .map(hotelInfoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HotelInfoResponse> getHotelById(Long hotelId) {
        return hotelRepository.findById(hotelId)
                .map(hotelInfoMapper::toDto);
    }

    @Override
    @Transactional
    public Hotel save(Hotel hotel) {
        // Validate required fields
        if (hotel.getName() == null || hotel.getName().trim().isEmpty()) {
            throw new ApiException(ErrorCode.USER_NOT_EXIST);
        }
        if (hotel.getAddress() == null || hotel.getAddress().trim().isEmpty()) {
            throw new ApiException(ErrorCode.USER_NOT_EXIST);
        }
        if (hotel.getHotline() == null || hotel.getHotline().trim().isEmpty()) {
            throw new ApiException(ErrorCode.USER_NOT_EXIST);
        }

        // Validate and set account
        Account account = accountRepository.findById(Math.toIntExact(hotel.getAccount().getAccount_id()))
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_EXIST));
        hotel.setAccount(account);

        // Handle services
        if (hotel.getServices() != null) {
            Set<Services> services = new HashSet<>();
            for (Services service : hotel.getServices()) {
                // Validate required fields for service
                if (service.getService_name() == null || service.getService_name().trim().isEmpty()) {
                    throw new ApiException(ErrorCode.USER_NOT_EXIST);
                }
                if (service.getService_price() == null) {
                    throw new ApiException(ErrorCode.USER_NOT_EXIST);
                }

                service.setHotel(hotel);
                service.setBookings(new HashSet<>()); // Initialize empty bookings set
                services.add(service);
            }
            hotel.setServices(services);
        }

        // Handle room types
        if (hotel.getRoomTypes() != null) {
            Set<RoomType> roomTypes = new HashSet<>();
            for (RoomType roomType : hotel.getRoomTypes()) {
                // Validate required fields for room type
                if (roomType.getType_name() == null || roomType.getType_name().trim().isEmpty()) {
                    throw new ApiException(ErrorCode.USER_NOT_EXIST);
                }
                if (roomType.getNumber_room() <= 0) {
                    throw new ApiException(ErrorCode.USER_NOT_EXIST);
                }

                roomType.setHotel(hotel);
                roomTypes.add(roomType);
            }
            hotel.setRoomTypes(roomTypes);
        }

        return hotelRepository.save(hotel);
    }

    @Override
    @Transactional
    public void deleteHotel(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ApiException(ErrorCode.HOTEL_NOT_EXIST));

        hotelRepository.deleteById(hotelId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Hotel> getHotelByAccountId(Long accId) {
        return hotelRepository.findByAccountId(accId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isOwnerOfRoomType(Long accountId, Long roomTypeId) {
        return hotelRepository.isOwnerOfRoomType(accountId, roomTypeId);
    }

    @Override
    @Transactional
    public HotelInfoResponse updateHotelInfo(Long accountId, Hotel hotel) {
        Hotel existing = hotelRepository.findByAccountId(accountId)
                .orElseThrow(() -> new ApiException(ErrorCode.HOTEL_NOT_EXIST));

        existing.setName(hotel.getName());
        existing.setImage(hotel.getImage());
        existing.setAddress(hotel.getAddress());
        existing.setHotline(hotel.getHotline());
        existing.setDescription(hotel.getDescription());

        Hotel saved = hotelRepository.save(existing);
        return hotelInfoMapper.toDto(saved);
    }

    @Override
    @Transactional
    public HotelResponse updateBasicHotelInfo(Long hotelId, UpdateHotelRequest request) {
        // Find existing hotel
        Hotel existingHotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ApiException(ErrorCode.HOTEL_NOT_EXIST));

        // Update only the basic fields, preserve all relationships and other data
        existingHotel.setName(request.getName());
        existingHotel.setAddress(request.getAddress());
        existingHotel.setHotline(request.getHotline());
        existingHotel.setImage(request.getImage());
        existingHotel.setDescription(request.getDescription());

        // Save and return updated hotel
        Hotel savedHotel = hotelRepository.save(existingHotel);
        return hotelMapper.toResponse(savedHotel);
    }
}

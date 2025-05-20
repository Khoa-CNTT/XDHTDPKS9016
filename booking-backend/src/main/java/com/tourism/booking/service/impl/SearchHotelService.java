package com.tourism.booking.service.impl;

import com.tourism.booking.dto.hotel.SearchHotelResponse;
import com.tourism.booking.model.Hotel;
import com.tourism.booking.repository.ISearchHotelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchHotelService {
    ISearchHotelRepository searchHotelRepository;

    public SearchHotelService(ISearchHotelRepository findHotelRepository) {
        this.searchHotelRepository = findHotelRepository;
    }
    public List<SearchHotelResponse> searchHotels(String name) {
        List<Hotel> hotels = searchHotelRepository.searchByNameAndAddress(name);
        return hotels.stream().map(hotel -> new SearchHotelResponse(
                hotel.getHotel_id(),
                hotel.getName(),
                hotel.getImage(),
                hotel.getAddress(),
                hotel.getHotline(),
                hotel.getDescription()
        )).collect(Collectors.toList());
    }
}

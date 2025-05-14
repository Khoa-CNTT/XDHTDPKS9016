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
    public List<SearchHotelResponse> searchHotels(String name, String address) {
        List<Hotel> hotels = searchHotelRepository.searchByNameAndAddress(name, address);
        return hotels.stream().map(hotel -> new SearchHotelResponse(
                hotel.getName(),
                hotel.getImage(),
                hotel.getAddress(),
                hotel.getHotline()
        )).collect(Collectors.toList());
    }
}

package com.tourism.booking.controller;
import com.tourism.booking.dto.hotel.SearchHotelResponse;
import com.tourism.booking.service.impl.SearchHotelService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/hotels")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchHotelController {

    SearchHotelService searchHotelService;

    public SearchHotelController(SearchHotelService searchHotelService) {
        this.searchHotelService = searchHotelService;
    }

    @GetMapping("/search")
    public List<SearchHotelResponse> searchHotels(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String address) {
        return searchHotelService.searchHotels(name, address);
    }
}

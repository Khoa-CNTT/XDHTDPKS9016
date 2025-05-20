package com.tourism.booking.service.impl;

import com.tourism.booking.dto.statistical.QuarterlyStatDTO;
import com.tourism.booking.dto.statistical.StatisticalDTO;
import com.tourism.booking.repository.BookingRepository;
import com.tourism.booking.service.IStatisticalService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticalService implements IStatisticalService {
    BookingRepository bookingRepository;


    @Override
    public StatisticalDTO getByQuarter(int year, int quarter) {
        return bookingRepository.getStatisticalByQuarter(year, quarter);
    }

    @Override
    public QuarterlyStatDTO quarterlyStat(Long accId, int year, int quarter) {
        return bookingRepository.getHotelStatsByQuarter(accId, year, quarter);
    }
}

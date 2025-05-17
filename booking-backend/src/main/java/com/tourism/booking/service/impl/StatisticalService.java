package com.tourism.booking.service.impl;

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
    public StatisticalDTO getAll() {
        return bookingRepository.getStatistical();
    }
}

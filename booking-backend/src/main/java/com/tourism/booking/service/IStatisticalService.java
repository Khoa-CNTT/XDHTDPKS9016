package com.tourism.booking.service;

import com.tourism.booking.dto.statistical.QuarterlyStatDTO;
import com.tourism.booking.dto.statistical.StatisticalDTO;

public interface IStatisticalService {
    StatisticalDTO getByQuarter(int year, int quarter);
    QuarterlyStatDTO quarterlyStat(Long accId, int year, int quarter);
}

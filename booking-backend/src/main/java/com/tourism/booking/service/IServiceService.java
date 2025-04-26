package com.tourism.booking.service;

import com.tourism.booking.model.Services;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IServiceService {
    Page<Services> getServicesByIdHotel(Long id, Pageable pageable);

    Optional<Services> getServiceById(Long id);

    Services saveService(Services service);

    void deleteServiceById(Long id);
}

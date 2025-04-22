package com.tourism.booking.service.impl;

import com.tourism.booking.model.Services;
import com.tourism.booking.repository.IServiceRepository;
import com.tourism.booking.service.IServiceService;
import jakarta.persistence.Id;
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
public class ServiceService implements IServiceService {

    IServiceRepository serviceRepository;

    @Override
    public Page<Services> getServicesByIdHotel(Long id, Pageable pageable) {
        return serviceRepository.findServicesByHotelId(id, pageable);
    }

    @Override
    public Optional<Services> getServiceById(Long id) {
        return serviceRepository.findById(id);
    }

    @Override
    public Services saveService(Services service) {
        return serviceRepository.save(service);
    }

    @Override
    public void deleteServiceById(Long id) {
        serviceRepository.deleteById(id);
    }
}

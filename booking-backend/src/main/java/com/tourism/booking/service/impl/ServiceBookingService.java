package com.tourism.booking.service.impl;

import com.tourism.booking.dto.booking.ServiceDTO;
import com.tourism.booking.model.Services;
import com.tourism.booking.repository.IServiceRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor

public class ServiceBookingService {

    IServiceRepository serviceRepository;

    /**
     * Lấy danh sách dịch vụ của một khách sạn
     * Nghiệp vụ: Hiển thị các dịch vụ có thể chọn khi đặt phòng
     */
    public List<ServiceDTO> getServicesByHotelId(Long hotelId) {
        List<Services> services = serviceRepository.findByHotelId(hotelId);
        return services.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Lấy thông tin các dịch vụ đã chọn theo danh sách ID
     * Nghiệp vụ: Lấy chi tiết các dịch vụ người dùng đã chọn khi đặt phòng
     */
    public Set<Services> getServicesByIds(Set<Long> serviceIds) {
        return serviceRepository.findAllByServiceIdIn(serviceIds);
    }

    private ServiceDTO convertToDTO(Services service) {
        ServiceDTO dto = new ServiceDTO();
        dto.setId(service.getService_id());
        dto.setName(service.getService_name());
        dto.setPrice(service.getService_price());
        dto.setDescription(service.getDescription());
        return dto;
    }
}

package com.tourism.booking.mapper;

import com.tourism.booking.dto.service.ServiceResponse;
import com.tourism.booking.model.Services;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IServiceMapper {
    ServiceResponse ServiceToServiceResponse(Services service);
}
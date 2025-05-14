package com.tourism.booking.mapper;
import com.tourism.booking.dto.hotel.HotelInfoResponse;
//import com.tourism.booking.dto.hotel.RoomTypeInfoResponse;
import com.tourism.booking.dto.hotel.ServiceInfoResponse;
import com.tourism.booking.model.Hotel;
import com.tourism.booking.model.RoomType;
import com.tourism.booking.model.Services;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IHotelInfoMapper {

    @Mapping(target = "services", source = "services")
//    @Mapping(target = "roomTypes", source = "roomTypes")
    HotelInfoResponse toDto(Hotel hotel);

    @Mapping(source = "service_name", target = "serviceName")
    @Mapping(source = "service_price", target = "servicePrice")
    ServiceInfoResponse serviceToDto(Services s);

//    @Mapping(source = "type_name", target = "typeName")
////    @Mapping(source = "price", target = "price")
//    RoomTypeInfoResponse roomTypeToDto(RoomType rt);
}

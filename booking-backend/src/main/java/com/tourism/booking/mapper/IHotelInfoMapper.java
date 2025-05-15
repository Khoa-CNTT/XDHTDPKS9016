package com.tourism.booking.mapper;

import com.tourism.booking.dto.hotel.HotelInfoResponse;
import com.tourism.booking.dto.hotel.RoomInfoResponse;
import com.tourism.booking.dto.hotel.RoomTypeInfoResponse;
import com.tourism.booking.dto.hotel.ServiceInfoResponse;
import com.tourism.booking.model.Hotel;
import com.tourism.booking.model.Room;
import com.tourism.booking.model.RoomType;
import com.tourism.booking.model.Services;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface IHotelInfoMapper {

    @Mapping(target = "services", source = "services")
    @Mapping(target = "roomTypes", source = "roomTypes")
    @Mapping(target = "idHotel", source = "hotel_id")
    HotelInfoResponse toDto(Hotel hotel);

    @Mapping(source = "service_id", target = "serviceId")
    @Mapping(source = "service_name", target = "serviceName")
    @Mapping(source = "service_price", target = "servicePrice")
    @Mapping(source = "service_image", target = "serviceImage")
    @Mapping(source = "description", target = "description")
    ServiceInfoResponse serviceToDto(Services s);

    @Mapping(source = "type_name", target = "typeName")
    @Mapping(source = "room_type_id", target = "roomTypeId")
    @Mapping(source = "number_room", target = "numberRoom")
    @Mapping(source = "room_image", target = "roomImage")
    @Mapping(target = "averagePrice", expression = "java(calculateAveragePrice(rt.getRooms()))")
    RoomTypeInfoResponse roomTypeToDto(RoomType rt);

    @Mapping(source = "id_room", target = "roomId")
    @Mapping(source = "number_bed", target = "numberBeds")
    RoomInfoResponse roomToDto(Room room);

    @Named("calculateAveragePrice")
    default BigDecimal calculateAveragePrice(Set<Room> rooms) {
        if (rooms == null || rooms.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;
        int count = 0;

        for (Room room : rooms) {
            if (room.getPrice() != null) {
                total = total.add(room.getPrice());
                count++;
            }
        }

        if (count == 0) {
            return BigDecimal.ZERO;
        }

        return total.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
    }
}

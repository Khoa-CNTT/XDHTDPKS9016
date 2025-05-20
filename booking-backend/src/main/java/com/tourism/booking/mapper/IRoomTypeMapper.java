package com.tourism.booking.mapper;

import com.tourism.booking.dto.roomType.RoomTypeResponse;
import com.tourism.booking.model.RoomType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IRoomTypeMapper {
    IRoomTypeMapper INSTANCE = Mappers.getMapper(IRoomTypeMapper.class);

    @Mapping(source = "room_type_id", target = "room_type_id")
    @Mapping(source = "type_name",    target = "type_name")
    @Mapping(source = "number_room",  target = "number_room")
    @Mapping(source = "description",  target = "description")
    @Mapping(source = "room_image",   target = "room_image")
    RoomTypeResponse toResponse(RoomType entity);

    @Mapping(target = "room_type_id", ignore = true)
    @Mapping(target = "hotel",          ignore = true)
    RoomType toEntity(RoomTypeResponse dto);
}

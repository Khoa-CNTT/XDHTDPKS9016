package com.tourism.booking.mapper;


import com.tourism.booking.dto.room.RoomRequest;
import com.tourism.booking.dto.room.RoomResponse;
import com.tourism.booking.model.Room;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IRoomMapper {
    @Mapping(source="room_type_id", target="room_type.room_type_id")
    Room toEntity(RoomRequest req);

    @Mapping(source="id_room",       target="id_room")
    @Mapping(source = "number_rooms", target = "number_rooms")
    @Mapping(source="number_bed",    target="number_bed")
    @Mapping(source="price",         target="price")
    @Mapping(source="room_type.type_name", target="type_name")
    @Mapping(source="status",        target="status")
    @Mapping(target="full_name",     ignore=true)
    RoomResponse toResponse(Room room);
}


package com.tourism.booking.service.impl;


import com.tourism.booking.dto.room.RoomRequest;
import com.tourism.booking.dto.room.RoomResponse;
import com.tourism.booking.exception.ApiException;
import com.tourism.booking.exception.ErrorCode;
import com.tourism.booking.mapper.IRoomMapper;
import com.tourism.booking.model.Booking;
import com.tourism.booking.model.BookingRoom;
import com.tourism.booking.model.Room;
import com.tourism.booking.model.RoomType;
import com.tourism.booking.repository.IBookingRoomRepository;
import com.tourism.booking.repository.IRoomHotelRepository;
import com.tourism.booking.repository.IRoomTypeRepository;
import com.tourism.booking.service.IRoomHotelService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoomHotelService implements IRoomHotelService {
    IRoomHotelRepository roomHotelRepository;
    IRoomTypeRepository typeRepository;
    IBookingRoomRepository bookingRoomRepository;
    IRoomMapper mapper;

    @Override
    @Transactional
    public RoomResponse create(RoomRequest request) {
        RoomType type = typeRepository.findById(request.getRoom_type_id())
                .orElseThrow(() -> new ApiException(ErrorCode.ROOM_TYPE_NOT_EXIST));

        Room room = mapper.toEntity(request);
        room.setRoom_type(type);
        room.setStatus("AVAILABLE");

        Room saved = roomHotelRepository.save(room);

        updateRoomCount(type);

        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public RoomResponse update(Long id, RoomRequest request) {
        Room room = roomHotelRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.ROOM_NOT_EXIST));
        RoomType oldType = room.getRoom_type();
        RoomType newType = typeRepository.findById(request.getRoom_type_id())
                .orElseThrow(() -> new ApiException(ErrorCode.ROOM_TYPE_NOT_EXIST));

        if (!oldType.getRoom_type_id().equals(newType.getRoom_type_id())) {
            oldType.setNumber_room(oldType.getNumber_room() - 1);
            newType.setNumber_room(newType.getNumber_room() + 1);
            typeRepository.save(oldType);
            typeRepository.save(newType);
            room.setRoom_type(newType);
        }

        room.setNumber_bed(request.getNumber_bed());
        room.setPrice(request.getPrice());
        room.setStatus(request.getStatus());
        Room updated = roomHotelRepository.save(room);

        return mapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Room room = roomHotelRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.ROOM_NOT_EXIST));

        RoomType type = room.getRoom_type();

        roomHotelRepository.delete(room);
        updateRoomCount(type);
    }
    @Override
    public RoomResponse getById(Long id) {
        Room room = roomHotelRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.ROOM_NOT_EXIST));

        RoomResponse roomResponse = mapper.toResponse(room);

        LocalDate today = LocalDate.now();
        Optional<BookingRoom> current = bookingRoomRepository
                .findActiveBookingsByRoomId(id)
                .stream()
                .filter(br -> {
                    LocalDate in  = br.getBooking().getCheck_in_date();
                    LocalDate out = br.getBooking().getCheck_out_date();
                    return !today.isBefore(in) && !today.isAfter(out);
                })
                .findFirst();

        if (current.isPresent()) {
            Booking booking = current.get().getBooking();
            roomResponse.setFull_name(booking.getContact_name());
            roomResponse.setStatus("OCCUPIED");
        }

        return roomResponse;
    }

    @Override
    public Page<RoomResponse> getRoomsByAccountId(Long accountId, Pageable pageable) {
        Page<Room> rooms = roomHotelRepository.findRoomsByHotelOwner(accountId, pageable);

        return rooms.map(room -> {
            RoomResponse roomResponse = mapper.toResponse(room);

            Optional<BookingRoom> current = bookingRoomRepository
                    .findActiveBookingsByRoomId(room.getId_room())
                    .stream()
                    .filter(br -> {
                        LocalDate in  = br.getBooking().getCheck_in_date();
                        LocalDate out = br.getBooking().getCheck_out_date();
                        LocalDate today = LocalDate.now();
                        return !today.isBefore(in) && !today.isAfter(out);
                    })
                    .findFirst();

            if (current.isPresent()) {
                Booking booking = current.get().getBooking();
                roomResponse.setFull_name(booking.getContact_name());
                roomResponse.setStatus("OCCUPIED");
            }

            return roomResponse;
        });
    }




    private void updateRoomCount(RoomType type) {
        Long roomTypeId = type.getRoom_type_id();
        int count = roomHotelRepository.countByRoomTypeId(roomTypeId);
        typeRepository.updateNumberRoom(roomTypeId, count);
    }

}

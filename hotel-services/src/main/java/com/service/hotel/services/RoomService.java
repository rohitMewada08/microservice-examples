package com.service.hotel.services;

import com.service.hotel.consumer.ReservationRestConsumer;
import com.service.hotel.dto.RoomsResponseDto;
import com.service.hotel.entity.Hotel;
import com.service.hotel.entity.Room;
import com.service.hotel.exception.NotFoundException;
import com.service.hotel.repository.HotelRepository;
import com.service.hotel.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    ReservationRestConsumer reservationRestConsumer;

    public Room update(Room room, Long hotelId, Long roomId) {

        if (!hotelRepository.existsById(hotelId))
            throw new NotFoundException(String.format("Hotel with id %d not found.", hotelId));

        return roomRepository.findById(roomId).map(roomRecord -> {
            roomRecord.setBedSize(room.getBedSize());
            roomRecord.setBalcony(room.isBalcony());
            roomRecord.setAirConditioner(room.isAirConditioner());
            roomRecord.setFloor(room.getFloor());
            roomRecord.setFare(room.getFare());
            roomRecord.setNoOfGuestAllowed(room.getNoOfGuestAllowed());
            return roomRepository.save(roomRecord);
        }).orElseThrow(() ->
                new NotFoundException(String.format("Room with id %d does not exist.", roomId)));
    }

    public List<RoomsResponseDto> findAllRoomByHotel(Long hotelId, String inquiryStartAt, String inquiryEndAt) {

        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() ->
                new NotFoundException(String.format("Hotel with id %d not found.", hotelId)));
        List<Room> rooms = hotel.getRoom();
        hotel.setRoom(new ArrayList<>());
        List<RoomsResponseDto> roomsResponseDtos = new ArrayList<>();
        rooms.stream().forEach(room -> {
            roomsResponseDtos.add(new RoomsResponseDto(hotel, room, reservationRestConsumer
                    .checkAvailability(room.getRoomId(), inquiryStartAt, inquiryEndAt)));
        });

        return roomsResponseDtos;
    }
}

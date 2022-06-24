package com.service.hotel.services;

import com.service.hotel.controller.HotelRestController;
import com.service.hotel.entity.Hotel;
import com.service.hotel.exception.NotFoundException;
import com.service.hotel.repository.HotelRepository;
import com.service.hotel.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {
    private static final Logger logger = LoggerFactory.getLogger(HotelService.class);
    @Autowired
    HotelRepository hotelRepository;

    public List<Hotel> findAll() {
        var hotels = hotelRepository.findAll();
        if(hotels.isEmpty())
            throw new NotFoundException("No data found.");

        return hotels;
    }

    public Hotel save(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public Hotel update(Long hotelId, Hotel hotel) {
        return hotelRepository.findById(hotelId).map( hotelRecord -> {
            hotelRecord.setHotelName(hotel.getHotelName());
            hotelRecord.setServicesProvided(hotel.getServicesProvided());
            hotelRecord.setAddress(hotel.getAddress());
            hotelRecord.setDescription(hotel.getDescription());
            return hotelRepository.save(hotelRecord);
        }).orElseThrow(() ->
                new NotFoundException("Hotel with id " + hotelId +" does not exist."));
    }

    public List<Hotel> findByAddressCity(String city) {
        var hotels = hotelRepository.findByAddressCity(city);
        if(hotels.isEmpty())
            throw new NotFoundException(String.format("No hotel found in %s.", city));
        return hotels;
    }

    public Hotel findById(Long hotelId) {
        return hotelRepository.findById(hotelId).orElseThrow(() ->
                new NotFoundException("Hotel with id " + hotelId +" does not exist."));
    }
}

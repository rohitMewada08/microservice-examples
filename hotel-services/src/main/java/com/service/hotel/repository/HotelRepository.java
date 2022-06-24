package com.service.hotel.repository;

import com.service.hotel.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel,Long> {

    List<Hotel> findByAddressCity(String city);

    /*List<Room> findAllRoomByHotel(Long id);*/
}

package com.service.hotel.dto;

import com.service.hotel.entity.Hotel;
import com.service.hotel.entity.Room;
import com.service.hotel.enums.AvailabilityResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RoomsResponseDto {
    private Hotel hotel;
    private Room room;
    private AvailabilityResponse availabilityResponse;
}

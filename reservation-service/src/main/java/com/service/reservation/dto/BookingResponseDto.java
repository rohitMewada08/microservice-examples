package com.service.reservation.dto;

import com.service.reservation.entity.Booking;
import lombok.Data;

import java.util.List;

@Data
public class BookingResponseDto {
    Booking booking;
    Hotel hotel;
    Payment payment;
}

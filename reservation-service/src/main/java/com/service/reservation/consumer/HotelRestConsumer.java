package com.service.reservation.consumer;

import com.service.reservation.dto.Hotel;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name="hotel-service")
public interface HotelRestConsumer {

    @GetMapping("/hotel/get/{hotelId}")
    Hotel getHotel(@PathVariable("hotelId") Long hotelId);


}

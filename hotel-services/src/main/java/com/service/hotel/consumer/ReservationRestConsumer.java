package com.service.hotel.consumer;

import com.service.hotel.enums.AvailabilityResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="reservation-service")
public interface ReservationRestConsumer {

    @GetMapping("/reservation/check-availability/{roomId}")
    AvailabilityResponse checkAvailability(@PathVariable("roomId") Long roomId,
                                     @RequestParam("inquiryStartAt") String inquiryStartAt,
                                     @RequestParam("inquiryEndAt") String inquiryEndAt);
}

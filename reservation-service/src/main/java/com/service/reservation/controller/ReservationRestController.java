package com.service.reservation.controller;

import com.service.reservation.consumer.HotelRestConsumer;
import com.service.reservation.dto.BookingResponseDto;
import com.service.reservation.entity.Booking;
import com.service.reservation.enums.AvailabilityResponse;
import com.service.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationRestController.class);

    @Autowired
    ReservationService reservationService;

    @GetMapping("/get-all")
   public List<Booking> getAllBooking(){
    return reservationService.getAll();
   }

   @GetMapping("/get-booking/{status}")
   public List<Booking> getBookingByDateRange(@PathVariable(value="status") String roomStatus,
                                              @RequestParam("startAt")  @DateTimeFormat(pattern = "yyyy-MM-dd") Calendar startAt,
                                              @RequestParam("endAt")  @DateTimeFormat(pattern = "yyyy-MM-dd") Calendar endAt){
       LOGGER.info(startAt.toString()+endAt.toString()+"" +roomStatus);
        return reservationService.findBookingByEntityDateBetweenAndStatus(startAt,endAt,roomStatus);
   }

    @Operation(summary = "Used to book the room in a hotel for specific date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Booking.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    @PostMapping("/book")
    public BookingResponseDto save(@RequestBody Booking booking){
        LOGGER.info("Hotel add: {}", booking);
        return reservationService.save(booking);
    }

    @Operation(summary = "Get Hotel along room and address details by city.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
                    array =@ArraySchema(schema = @Schema(implementation = Booking.class)))}),
            @ApiResponse(responseCode = "404", description = "No hotel found in {city}. ", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })

    @GetMapping("/get-by-guest-id/{guestId}")
    public List<Booking> getBookingByGuest(@PathVariable(value="guestId") Long guestId){
        LOGGER.info("Booking find: guestId={}", guestId);
        return reservationService.findAllByGuestId(guestId);
    }

    @GetMapping("/get-by-hotel-id/{hotelId}")
    public List<Booking> getBookingByHotel(@PathVariable(value="hotelId") Long hotelId){
        LOGGER.info("Booking find: guestId={}", hotelId);
        return reservationService.findAllByHotelId(hotelId);
    }

    @GetMapping("/check-availability/{roomId}")
    public AvailabilityResponse checkIsAvailability(@PathVariable(value="roomId") Long roomId,
                                                    @RequestParam("inquiryStartAt")  @DateTimeFormat(pattern = "yyyy-MM-dd") Calendar inquiryStartAt,
                                                    @RequestParam("inquiryEndAt")  @DateTimeFormat(pattern = "yyyy-MM-dd") Calendar inquiryEndAt){
        return reservationService.checkAvailability(inquiryStartAt,inquiryEndAt,roomId);
    }

    @GetMapping("/get/{roomId}")
    public List<Booking> getBookingByRoom(@PathVariable(value="roomId") Long roomId){

        LOGGER.info("Booking find: roomId={}", roomId);
        return reservationService.findAllByRoomId(roomId);
    }

    @PostMapping("/cancel-booking/{bookingId}")
    public String getBookingByBooking(@PathVariable(value="bookingId") String bookingId){
        LOGGER.info("Booking find: guestId={}", bookingId);
        return reservationService.updateBookingStatus(bookingId);
    }


    /*@GetMapping("/get-hotel/{roomId}")
    public List<Hotel> getHotel(){
        LOGGER.info("Booking find: getHotel={}");
        return hotelRestConsumer.getAllHotel();
    }*/


}

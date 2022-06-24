package com.service.hotel.controller;

import com.service.hotel.dto.RoomsResponseDto;
import com.service.hotel.entity.Hotel;
import com.service.hotel.entity.Room;
import com.service.hotel.services.HotelService;
import com.service.hotel.services.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/hotel")
public class HotelRestController {

    @Autowired
    HotelService hotelService;

    @Autowired
    RoomService roomService;

    @Operation(summary = "Get all the registered hotel.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
                    array =@ArraySchema(schema = @Schema(implementation = Hotel.class)))}),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content) })
    @GetMapping("/get-all")
    public List<Hotel> getAllHotel(){
        log.info("Hotel find");
        return hotelService.findAll();
    }

    @Operation(summary = "Used to save the hotel details along with room and address.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Hotel.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    @PostMapping("/save")
    public Hotel save(@RequestBody Hotel hotel){
        log.info("Hotel add: {}", hotel);
        return hotelService.save(hotel);
    }

    @Operation(summary = "Get Hotel along room and address details by hotel id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the Hotel", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Hotel.class)) }),
            @ApiResponse(responseCode = "404", description = "Hotel with id {hotelId} does not exist.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    @GetMapping("/get/{hotelId}")
    public Hotel getHotelById(@PathVariable(value="hotelId") Long hotelId){
        log.info("Hotel find: id={}", hotelId);
        return hotelService.findById(hotelId);
    }

    @Operation(summary = "Get Hotel along room and address details by city.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
                    array =@ArraySchema(schema = @Schema(implementation = Hotel.class)))}),
            @ApiResponse(responseCode = "404", description = "No hotel found in {city}. ", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    @GetMapping("/get-by-city/{city}")
    public List<Hotel> getHotelByCity(@PathVariable(value="city") String city){
        log.info("Hotel find: city={}", city);
        return hotelService.findByAddressCity(city);
    }

    @GetMapping("/get/{hotelId}/room")
    public List<RoomsResponseDto> getRoomsByHotelId(@PathVariable(value="hotelId") Long hotelId,
                                                    @RequestParam("inquiryStartAt") String inquiryStartAt,
                                                    @RequestParam("inquiryEndAt") String inquiryEndAt ){
        log.info("Hotel find:hotelId ={}", hotelId);
        return roomService.findAllRoomByHotel(hotelId,inquiryStartAt,inquiryEndAt);
    }

    @Operation(summary = "Used to update the hotel details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the Hotel", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Hotel.class)) }),
            @ApiResponse(responseCode = "404", description = "Hotel with id {hotelId}  does not exist.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Hotel with id {hotelId}  does not exist.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    @PutMapping("/update/{hotelId}")
    public Hotel update(@RequestBody Hotel hotel, @PathVariable Long hotelId){
        log.info("Hotel Update: hotelId={}", hotelId);
        return hotelService.update(hotelId,hotel);
    }

    @Operation(summary = "Used to update the room details for a hotel.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Room.class)) }),
            @ApiResponse(responseCode = "404", description = "Hotel with id {hotelId} not found." , content = @Content),
            @ApiResponse(responseCode = "404", description = "Room with id {roomId} does not exist." , content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    @PutMapping("/update/{hotelId}/room/{roomId}")
    public Room updateRoom(@RequestBody Room room,
                           @PathVariable("hotelId") Long hotelId,
                           @PathVariable("roomId") Long roomId) {
        log.info("Room Update: hotelId={}, roomId={}", hotelId,roomId);
        return roomService.update(room, hotelId, roomId);
    }

}

package com.service.hotel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.hotel.entity.Address;
import com.service.hotel.entity.Hotel;
import com.service.hotel.entity.Room;
import com.service.hotel.enums.HotelEnums;
import com.service.hotel.exception.NotFoundException;
import com.service.hotel.repository.HotelRepository;
import com.service.hotel.repository.RoomRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HotelControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    HotelRepository mockHotelRepository;

    @MockBean
    RoomRepository roomRepository;

    ObjectMapper om = new ObjectMapper();

    private Room room;

    private Hotel hotel;
    List<Hotel> hotels;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        room = Room.builder()
                .bedSize(HotelEnums.RoomType.KING)
                .balcony(true).floor(1).noOfGuestAllowed(2)
                .airConditioner(false).fare(1500.00).build();

        hotel = Hotel.builder().hotelName("City Hotel")
                .address(Address.builder()
                        .street(" 001 Main Road old bhopal")
                        .city("Bhopal")
                        .state("Madhya Pradesh").pinCode("466030").build())
                .room(List.of(new Room[]{room}))
                .servicesProvided(List.of(new HotelEnums.Services[]{HotelEnums.Services.ROOM_SERVICE, HotelEnums.Services.BREAKFAST}))
                .description("Hotel 1 KM from market ").build();

        hotels = new ArrayList<>(Arrays.asList(hotel));

    }

    @Test
    public void getHotelTest() throws Exception {

        when(mockHotelRepository.findAll()).thenReturn(hotels);
        mockMvc.perform(get("/hotel/get-all").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(content().json(om.writeValueAsString(hotels)));

    }

    @Test
    public void addHotelTest() throws Exception {

        when(mockHotelRepository.save(any(Hotel.class))).thenReturn(hotel);
        String jsonRequest = om.writeValueAsString(hotel);
        mockMvc.perform(post("/hotel/save").content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andExpect(status().isOk())
                .andExpect(content().json(jsonRequest));

    }

    @Test
    public void getHotelByIdTest() throws Exception {

        when(mockHotelRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(hotel));
        String jsonRequest = om.writeValueAsString(hotel);
        mockMvc.perform(get("/hotel/get/01")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(status().isOk()).andExpect(content().json(jsonRequest));

    }

    @Test
    public void getHotelByCityTest() throws Exception {

        when(mockHotelRepository.findByAddressCity("Bhopal")).thenReturn(hotels);
        mockMvc.perform(get("/hotel/get-by-city/Bhopal")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(content().json(om.writeValueAsString(hotels)));

    }

    @Test
    public void updateHotelTest() throws Exception {

        when(mockHotelRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(hotel));
        when(mockHotelRepository.save(any(Hotel.class))).thenReturn(hotel);
        String jsonRequest = om.writeValueAsString(hotel);
        mockMvc.perform(put("/hotel/update/2")
                        .content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andExpect(content().json(jsonRequest));

    }

    @Test
    public void updateRoomTest() throws Exception {

        when(mockHotelRepository.existsById(any(Long.class))).thenReturn(true);
        when(roomRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(hotel.getRoom().get(0)));
        when(roomRepository.save(any(Room.class))).thenReturn(room);
        String jsonRequest = om.writeValueAsString(room);
        mockMvc.perform(put("/hotel/update/2/room/3")
                        .content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andExpect(content().json(jsonRequest));

    }

    @Test
    public void getHotelByIdExceptionTest() throws Exception {

        when(mockHotelRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(null));
        mockMvc.perform(get("/hotel/get/01").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError()).andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals("Hotel with id 1 does not exist.", result.getResolvedException().getMessage()));

    }

    @Test
    public void getHotelAllNoDataExceptionTest() throws Exception {

        when(mockHotelRepository.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/hotel/get-all").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals("No data found.", result.getResolvedException().getMessage()));

    }

    @Test
    public void getHotelByCityNoDataExceptionTest() throws Exception {

        when(mockHotelRepository.findByAddressCity("Bhopal")).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/hotel/get-by-city/Bhopal").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals("No hotel found in Bhopal.", result.getResolvedException().getMessage()));

    }

    @Test
    public void updateRoomExceptionHotelNotFoundTest() throws Exception {

        when(mockHotelRepository.existsById(2L)).thenReturn(false);
        String jsonRequest = om.writeValueAsString(room);
        mockMvc.perform(put("/hotel/update/2/room/3")
                        .content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals("Hotel with id 2 not found.", result.getResolvedException().getMessage()));

    }

    @Test
    public void updateRoomExceptionRoomNotFoundTest() throws Exception {

        when(mockHotelRepository.existsById(any(Long.class))).thenReturn(true);
        when(roomRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(null));
        String jsonRequest = om.writeValueAsString(room);
        mockMvc.perform(put("/hotel/update/2/room/3")
                        .content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals("Room with id 3 does not exist.", result.getResolvedException().getMessage()));

    }

    @Test
    public void updateHotelExceptionNoFoundTest() throws Exception {

        when(mockHotelRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(null));
        String jsonRequest = om.writeValueAsString(hotel);
        mockMvc.perform(put("/hotel/update/2")
                        .content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals("Hotel with id 2 does not exist.", result.getResolvedException().getMessage()));

    }

}
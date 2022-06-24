package com.service.reservation.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class Hotel {

    private Long hotelId;

    private String hotelName;

    private Address address;

    private List<Room> room;

    private List<String> servicesProvided;

    private String description;

    }

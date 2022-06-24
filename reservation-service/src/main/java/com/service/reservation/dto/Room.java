package com.service.reservation.dto;

import lombok.*;
@Getter
@Setter
public class Room extends AuditModel {

    private Long roomId;

    private String bedSize;
    private boolean balcony;
    private int floor;
    private Double fare;
    private boolean airConditioner;
    private int noOfGuestAllowed;

}

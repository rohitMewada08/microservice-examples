package com.service.reservation.enums;

public enum RoomStatus {
    BOOKED("Booked"),CANCELLED("Cancelled"),AVAILABLE("Available"),PENDING("Pending");
    private String label;
    private RoomStatus(String label){
        this.label = label;
    }
}

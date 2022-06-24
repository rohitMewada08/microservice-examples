package com.service.reservation.dto;

import lombok.*;

@Getter
@Setter
public class Address extends AuditModel {

    private Long addressId;
    private String street;
    private String city;
    private String state;
    private String pinCode;

}

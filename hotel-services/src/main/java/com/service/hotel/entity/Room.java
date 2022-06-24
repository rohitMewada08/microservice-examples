package com.service.hotel.entity;

import com.service.hotel.enums.HotelEnums;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Room extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @Enumerated(EnumType.STRING)
    private HotelEnums.RoomType bedSize;
    private boolean balcony;
    private int floor;
    private Double fare;
    private boolean airConditioner;
    private int noOfGuestAllowed;
}

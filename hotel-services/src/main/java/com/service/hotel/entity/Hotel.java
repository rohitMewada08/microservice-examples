package com.service.hotel.entity;

import com.service.hotel.enums.HotelEnums;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Hotel extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hotelId;

    private String hotelName;

    @OneToOne(targetEntity = Address.class,cascade = CascadeType.PERSIST)
    private Address address;

    @OneToMany(targetEntity = Room.class,cascade = CascadeType.PERSIST)
    private List<Room> room;

    @ElementCollection(targetClass = HotelEnums.Services.class)
    @JoinTable(name = "tblServices", joinColumns = @JoinColumn(name = "hotelId"))
    @Enumerated(EnumType.STRING)
    private List<HotelEnums.Services> servicesProvided;

    private String description;

    }

package com.service.reservation.entity;

import com.service.reservation.enums.RoomStatus;
import com.service.reservation.util.StringPrefixedSequenceIdGenerator;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class Booking extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "book_seq")
    @GenericGenerator(
            name = "book_seq",
            strategy = "com.service.reservation.util.StringPrefixedSequenceIdGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "50"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "B_"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d") })
    private String bookingId;

    @ElementCollection(targetClass=Long.class)
    private List<Long> roomId;
    private Long hotelId;
    private Long guestId;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;
    @Temporal(TemporalType.DATE)
    private Calendar startAt;
    @Temporal(TemporalType.DATE)
    private Calendar endAt;

    private Double totalPrice;

}

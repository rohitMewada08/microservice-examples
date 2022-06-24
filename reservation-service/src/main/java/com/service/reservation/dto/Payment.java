package com.service.reservation.dto;

import com.service.reservation.enums.PaymentStatus;
import lombok.*;
import java.util.Date;

@Data
@Builder
public class Payment {

    private Long paymentId;
    private String bookingId;
    private Long guestId;
    private double amount;
    private PaymentStatus paymentStatus;
    private Date createdAt;
    private Date updatedAt;

}

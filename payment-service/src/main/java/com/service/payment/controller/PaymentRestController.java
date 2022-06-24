package com.service.payment.controller;

import com.service.payment.entity.Payment;
import com.service.payment.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.PathParam;

@RestController
@RequestMapping("/payment")
public class PaymentRestController {

    @Autowired
    PaymentService paymentService;

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentRestController.class);

    @PostMapping("/pay")
    public Payment doPayment(@RequestBody Payment payment) {
       return paymentService.save(payment);
    }

    @GetMapping("/get/{paymentId}")
    public Payment getPayment(@PathParam("paymentId") Long paymentId) {
        return paymentService.findById(paymentId);
    }

}

package com.service.payment.service;

import com.service.payment.entity.Payment;
import com.service.payment.enums.PaymentStatus;
import com.service.payment.exception.NotFoundException;
import com.service.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    public Payment save(Payment payment) {
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        return paymentRepository.save(payment);
    }

    public Payment findById(Long paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow(() ->
                new NotFoundException(String.format("No payment details found by id %s", paymentId)));
    }

    public Payment updatePaymentStatus(Long paymentId, PaymentStatus paymentStatus) {
        return paymentRepository.findById(paymentId).map( payment -> {
            payment.setPaymentStatus(paymentStatus);
             return paymentRepository.save(payment);
        }).orElseThrow(() -> new NotFoundException(String.format("No payment details found by id %s", paymentId)));
    }
}

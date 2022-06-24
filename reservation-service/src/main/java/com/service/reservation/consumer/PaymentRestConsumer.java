package com.service.reservation.consumer;

import com.service.reservation.dto.Payment;
import com.service.reservation.enums.PaymentStatus;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "payment-service")
public interface PaymentRestConsumer {


    @PostMapping("/payment/pay")
    @CircuitBreaker(name = "paymentCircuitBreaker", fallbackMethod = "doPaymentFallbackMethod")
    Payment doPayment(@RequestBody Payment payment);

    default Payment doPaymentFallbackMethod(Payment payment,Exception exc) {
        Logger logger = LoggerFactory.getLogger(PaymentRestConsumer.class);
        logger.info("Got an error, executing doPaymentFallbackMethod and returning empty");
        payment.setPaymentStatus(PaymentStatus.FAILED);
        return payment;
    };
}

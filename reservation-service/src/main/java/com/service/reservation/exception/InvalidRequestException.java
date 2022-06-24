package com.service.reservation.exception;

public class InvalidRequestException extends RuntimeException{
    public InvalidRequestException(String s) {
        super(s);
    }
}

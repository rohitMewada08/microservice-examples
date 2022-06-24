package com.service.reservation.exception;

public class AlreadyBookedException extends RuntimeException{
    public AlreadyBookedException(String s) {
        super(s);
    }
}

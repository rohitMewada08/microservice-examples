package com.service.hotel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InvalidRequestException extends RuntimeException{
    public InvalidRequestException(String s) {
        super(s);
    }
}

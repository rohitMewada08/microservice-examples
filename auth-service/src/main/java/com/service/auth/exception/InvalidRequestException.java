package com.service.auth.exception;

public class InvalidRequestException extends RuntimeException{
    public InvalidRequestException(String s) {
        super(s);
    }
}

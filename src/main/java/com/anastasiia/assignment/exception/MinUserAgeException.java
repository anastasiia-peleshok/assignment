package com.anastasiia.assignment.exception;

public class MinUserAgeException extends RuntimeException {
    public MinUserAgeException(String errorMessage) {
        super(errorMessage);
    }
}
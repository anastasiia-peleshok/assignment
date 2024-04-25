package com.anastasiia.assignment.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class ErrorResponse {
    private HttpStatus status;
    private LocalDateTime timeStamp;
    private String message;
    private Map<String, String> errors;

    public ErrorResponse() {
        timeStamp = LocalDateTime.now();
    }
    public ErrorResponse(HttpStatus status, String message) {
        this();
        this.status = status;
        this.message = message;
    }

    public ErrorResponse(HttpStatus status, String message,Map<String, String> errors) {
        this();
        this.status = status;
        this.message = message;
        this.errors=errors;
    }
}
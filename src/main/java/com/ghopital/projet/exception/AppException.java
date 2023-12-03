package com.ghopital.projet.exception;

import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public AppException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public AppException(HttpStatus status, Throwable cause) {
        super(cause);
        this.status = status;
        this.message = cause.getMessage();
    }

    public AppException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

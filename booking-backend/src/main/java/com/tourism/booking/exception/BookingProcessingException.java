package com.tourism.booking.exception;

public class BookingProcessingException extends RuntimeException {
    public BookingProcessingException(String message) {
        super(message);
    }

    public BookingProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
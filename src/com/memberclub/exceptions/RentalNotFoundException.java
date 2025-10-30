package com.memberclub.exceptions;

/**
 * Exception thrown when a rental is not found.
 */
public class RentalNotFoundException extends Exception {

    /**
     * Creates a new RentalNotFoundException with a message.
     * @param message the error message
     */
    public RentalNotFoundException(String message) {
        super(message);
    }

    /**
     * Creates a new RentalNotFoundException with a message and cause.
     * @param message the error message
     * @param cause the underlying cause
     */
    public RentalNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
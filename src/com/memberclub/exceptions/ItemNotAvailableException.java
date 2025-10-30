package com.memberclub.exceptions;

/**
 * Exception thrown when an item is not available for rental.
 */
public class ItemNotAvailableException extends Exception {

    /**
     * Creates a new ItemNotAvailableException with a message.
     * @param message the error message
     */
    public ItemNotAvailableException(String message) {
        super(message);
    }

    /**
     * Creates a new ItemNotAvailableException with a message and cause.
     * @param message the error message
     * @param cause the underlying cause
     */
    public ItemNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
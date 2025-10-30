package com.memberclub.exceptions;

/**
 * Exception thrown when an item is not found in the inventory.
 */
public class ItemNotFoundException extends Exception {

    /**
     * Creates a new ItemNotFoundException with a message.
     * @param message the error message
     */
    public ItemNotFoundException(String message) {
        super(message);
    }

    /**
     * Creates a new ItemNotFoundException with a message and cause.
     * @param message the error message
     * @param cause the underlying cause
     */
    public ItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
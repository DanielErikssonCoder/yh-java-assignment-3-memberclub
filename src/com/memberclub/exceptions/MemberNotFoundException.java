package com.memberclub.exceptions;

/**
 * Exception thrown when a member is not found in the registry.
 */
public class MemberNotFoundException extends Exception {

    /**
     * Creates a new MemberNotFoundException with a message.
     * @param message the error message
     */
    public MemberNotFoundException(String message) {
        super(message);
    }

    /**
     * Creates a new MemberNotFoundException with a message and cause.
     * @param message the error message
     * @param cause the underlying cause
     */
    public MemberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
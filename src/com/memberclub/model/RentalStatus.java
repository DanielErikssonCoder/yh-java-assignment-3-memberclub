package com.memberclub.model;

/**
 * Represents the status of a rental transaction.
 */
public enum RentalStatus {

    // rental is currently ongoing
    ACTIVE,

    // rental has been returned and completed
    COMPLETED,

    // rental was cancelled before completion
    CANCELLED
}
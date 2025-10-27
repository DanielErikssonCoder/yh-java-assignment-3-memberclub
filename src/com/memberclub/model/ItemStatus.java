package com.memberclub.model;

/**
 * This represents the current status of an item in the inventory.
 * Both for tracking availability and condition.
 */
public enum ItemStatus {

    // Ready to rent
    AVAILABLE,

    // Currently out on rental
    RENTED,

    // Needs repair before next rental
    BROKEN

}

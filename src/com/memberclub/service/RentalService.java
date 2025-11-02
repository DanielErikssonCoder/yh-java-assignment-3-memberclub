package com.memberclub.service;

import com.memberclub.model.*;
import com.memberclub.model.enums.ItemStatus;
import com.memberclub.model.enums.RentalPeriod;
import com.memberclub.pricing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages rental operations with creating and completing rentals.
 * Coordinates between Inventory, MemberRegistry, and PricingFactory.
 */
public class RentalService {

    private final Inventory inventory;
    private final MemberRegistry memberRegistry;
    private final List<Rental> rentals;
    private int rentalCounter;

    /**
     * Creates a RentalService with access to inventory and the member registry.
     * @param inventory the inventory to check items from
     * @param memberRegistry the registry to check members from
     */
    public RentalService(Inventory inventory, MemberRegistry memberRegistry) {
        this.inventory = inventory;
        this.memberRegistry = memberRegistry;
        this.rentals = new ArrayList<>();
        this.rentalCounter = 1;
    }

    /**
     * Creates a new rental for a member.
     * @param memberId the ID of the member renting
     * @param itemId the ID of the item to rent
     * @param duration rental duration
     * @param period billing period
     * @return the created Rental object, or null if rental failed
     */
    public Rental rentItem(int memberId, String itemId, int duration, RentalPeriod period) {

        // Find member
        Member member = memberRegistry.getMember(memberId);

        // If member is null then we return null
        if (member == null) {
            return null;
        }

        // Find item
        Item item = inventory.getItem(itemId);

        // If item is null then we return null
        if (item == null) {
            return null;
        }

        // If item is not available then we return null
        if (!item.isAvailable()) {
            return null;
        }

        // Get pricing based on member level
        PricePolicy pricing = PricingFactory.getPricing(member.getMembershipLevel());

        // Calculate total cost
        double totalCost = pricing.calculatePrice(item, member, duration, period);

        // Generate unique ID for rental
        String rentalId = "RENT-" + String.format("%03d", rentalCounter);
        rentalCounter++;

        // Set end date -> same day for hourly, add duration for daily
        LocalDate expectedReturnDate;
        if (period == RentalPeriod.HOURLY) {
            expectedReturnDate = LocalDate.now();
        } else {
            expectedReturnDate = LocalDate.now().plusDays(duration);
        }

        // Create a rental object
        Rental rental = new Rental(rentalId, memberId, itemId, LocalDate.now(), expectedReturnDate, null, totalCost);

        // Mark item as rented
        item.setStatus(ItemStatus.RENTED);

        // Add rental to member history
        member.addRental(rentalId);

        // Save rental in a list
        rentals.add(rental);

        // Return the created rental
        return rental;
    }

    /**
     * Completes a rental and marks the item as available again.
     * @param rentalId the ID of the rental to complete
     * @return true if successful, false if rental not found
     */
    public boolean returnItem(String rentalId) {

        // Loop through all rentals to find matching ID
        for (Rental rental : rentals) {

            // Check if this rental matches the ID we are looking for
            if (rental.getRentalId().equals(rentalId)) {

                // Mark rental as completed
                rental.complete();

                // Get the item that was rented
                Item item = inventory.getItem(rental.getItemId());

                // Mark item as available
                item.setStatus(ItemStatus.AVAILABLE);

                // Return success
                return true;
            }

        }
        // If rental is not found, return failure
        return false;
    }

    /**
     * Returns all currently active rentals.
     * @return list of active rentals
     */
    public List<Rental> getActiveRentals() {

        // Create an empty list for results
        List<Rental> activeRentals = new ArrayList<>();

        // Loop through all rentals
        for (Rental rental : rentals) {

            // Check if this rental is active and add to results
            if (rental.isActive()) {
                activeRentals.add(rental);
            }
        }

        // Return the filtered list
        return activeRentals;
    }

    /**
     * Returns all rentals (active and completed).
     * @return list of all rentals
     */
    public List<Rental> getAllRentals() {

        // Return the complete rentals list
        return rentals;
    }

    /**
     * Finds and returns a specific rental by ID.
     * @param rentalId the ID of the rental to find
     * @return the Rental object, or null if not found
     */
    public Rental getRental(String rentalId) {

        // Loop through all rentals to find matching ID
        for (Rental rental : rentals) {

            // Check if current rental ID matches the search ID
            if (rental.getRentalId().equals(rentalId)) {
                return rental;
            }
        }

        // Return null if not found
        return null;
    }
}

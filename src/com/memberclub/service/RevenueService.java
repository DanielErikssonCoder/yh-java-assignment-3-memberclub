package com.memberclub.service;

/**
 * Service for tracking revenue and financial statistics.
 * Manages all revenue from rentals and late fees.
 */
public class RevenueService {

    private double totalRevenue;

    /**
     * Creates a new revenue service.
     */
    public RevenueService() {
        this.totalRevenue = 0.0;
    }

    /**
     * Adds revenue to the total.
     * @param amount the amount to add
     */
    public void addRevenue(double amount) {

        if (amount > 0) {
            this.totalRevenue += amount;
        }
    }

    /**
     * Gets the total revenue.
     * @return total revenue amount
     */
    public double getTotalRevenue() {
        return totalRevenue;
    }

    /**
     * Resets the revenue counter to zero.
     * This clears all revenue history.
     */
    public void resetRevenue() {
        this.totalRevenue = 0.0;
    }
}
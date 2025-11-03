package com.memberclub.model;

import com.memberclub.model.enums.RentalStatus;
import java.time.LocalDate;

/**
 * Represents a rental transaction between a member and an item.
 * Acts as a receipt tracking who rented what, when, and for how much.
 * Status can transition between ACTIVE, COMPLETED, and CANCELLED.
 */
public class Rental {

    private final String rentalId;
    private final int memberId;
    private final String itemId;
    private final LocalDate startDate;
    private final LocalDate expectedReturnDate;
    private LocalDate endDate;
    private final double totalCost;
    private RentalStatus status;

    /**
     * Constructor that creates a new rental transaction.
     * @param rentalId unique rental identifier
     * @param memberId ID of the member renting
     * @param itemId ID of the item being rented
     * @param startDate start date of rental period
     * @param expectedReturnDate expected return date
     * @param endDate end date of rental period
     * @param totalCost total cost calculated by pricing policy
     */
    public Rental(String rentalId, int memberId, String itemId, LocalDate startDate, LocalDate expectedReturnDate, LocalDate endDate, double totalCost) {

        // Initialize own fields
        this.rentalId = rentalId;
        this.memberId = memberId;
        this.itemId = itemId;
        this.startDate = startDate;
        this.expectedReturnDate = expectedReturnDate;
        this.endDate = endDate;
        this.totalCost = totalCost;
        this.status = RentalStatus.ACTIVE;
    }

    // Getters
    public String getRentalId() {
        return rentalId;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getItemId() {
        return itemId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public RentalStatus getStatus() {
        return status;
    }

    // Setter
    public void setStatus(RentalStatus status) {
        this.status = status;
    }

    /**
     * Calculates the duration of the rental in days.
     * @return number of days between start and end date
     */
    public long getDurationInDays() {
        return startDate.until(endDate).getDays();
    }

    /**
     * Checks if the rental is currently active.
     * @return true if status is ACTIVE
     */
    public boolean isActive() {
        return status == RentalStatus.ACTIVE;
    }

    /**
     * Checks if the rental has been completed.
     * @return true if status is COMPLETED
     */
    public boolean isCompleted() {
        return status == RentalStatus.COMPLETED;
    }

    /**
     * Marks the rental as completed.
     */
    public void complete() {
        this.endDate = LocalDate.now();
        this.status = RentalStatus.COMPLETED;
    }

    /**
     * Marks the rental as cancelled.
     */
    public void cancel() {
        this.status = RentalStatus.CANCELLED;
    }

    // Returns string representation for easy printing or debugging
    @Override
    public String toString() {
        return "Rental{" + "id=" + rentalId + ", medlem=" + memberId + ", item=" + itemId + ", start=" + startDate
                + ", slut=" + endDate + ", kostnad=" + totalCost + " kr, status=" + status + "}";
    }
}

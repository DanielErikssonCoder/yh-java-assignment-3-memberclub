package com.memberclub.ui.components;

import com.memberclub.model.*;
import com.memberclub.model.enums.RentalPeriod;
import com.memberclub.ui.UIHelper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Generates and displays receipts for rental transactions.
 * Handles order confirmation, pricing breakdowns, and return receipts.
 */
public class ReceiptGenerator {

    private UIHelper helper;
    private Scanner scanner;

    /**
     * Creates a new receipt generator.
     * @param helper the UI helper for display operations
     * @param scanner the scanner for user input
     */
    public ReceiptGenerator(UIHelper helper, Scanner scanner) {
        this.helper = helper;
        this.scanner = scanner;
    }

    /**
     * Displays a receipt after successful rental checkout.
     * @param cart the shopping cart items
     * @param member the member
     * @param totalBeforeDiscount total price before discount
     */
    public void displayRentalReceipt(List<CartItem> cart, Member member, double totalBeforeDiscount) {
        helper.clearScreen();
        helper.printHeader("         ORDERBEKRÄFTELSE");
        System.out.println("Medlem: " + member.getName() + " (" + member.getMembershipLevel() + ")");
        System.out.println();
        helper.printDivider();
        System.out.println();

        // Create formatter for date and time display
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm");

        // Display each rented item with return date
        for (int i = 0; i < cart.size(); i++) {

            // Get current cart item
            CartItem cartItem = cart.get(i);

            // Display item information
            System.out.println("Artikel " + (i + 1) + ": " + cartItem.getItem().getId() + " - " + cartItem.getItem().getName() + " (" + ItemSelector.getItemTypeDescription(cartItem.getItem()) + ")");

            // Calculate when item should be returned
            LocalDateTime returnDateTime = calculateReturnDateTime(cartItem);

            // Display duration and return time
            if (cartItem.getPeriod() == RentalPeriod.HOURLY) {

                // Hourly duration
                System.out.println("Längd: " + cartItem.getDuration() + " " + (cartItem.getDuration() == 1 ? "timme" : "timmar") + " (Retur: " + returnDateTime.format(formatter) + ")");

            } else {

                // Daily duration
                System.out.println("Längd: " + cartItem.getDuration() + " " + (cartItem.getDuration() == 1 ? "dag" : "dagar") + " (Retur: " + returnDateTime.format(formatter) + ")");
            }

            // Display item price
            System.out.printf("Pris: %.2f kr%n", cartItem.getPrice());
            System.out.println();
        }

        // Calculate and display pricing breakdown
        double discount = getDiscount(member);
        double discountAmount = totalBeforeDiscount * discount;
        double totalAfterDiscount = totalBeforeDiscount - discountAmount;

        // Display pricing summary
        helper.printDivider();
        System.out.println();
        System.out.printf("Totalt (före rabatt): %.2f kr%n", totalBeforeDiscount);
        System.out.printf("Rabatt (%d%%): -%.2f kr%n", (int)(discount * 100), discountAmount);
        System.out.println();
        System.out.printf("TOTALT ATT BETALA: %.2f kr%n", totalAfterDiscount);
        System.out.println();
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        helper.pressEnterToContinue();
    }

    /**
     * Calculates expected return date/time for a cart item.
     * Adjusts for business hours (8-20) for hourly rentals.
     * @param cartItem the cart item
     * @return calculated return date/time
     */
    private LocalDateTime calculateReturnDateTime(CartItem cartItem) {
        LocalDateTime returnDateTime;

        // Check if rental is hourly
        if (cartItem.getPeriod() == RentalPeriod.HOURLY) {

            // Add hours to current time
            returnDateTime = LocalDateTime.now().plusHours(cartItem.getDuration());

            // Get the hour when item should be returned
            int returnHour = returnDateTime.getHour();

            // Adjust if return falls outside business hours (8-20)
            if (returnHour >= 20 || returnHour < 8) {

                int hoursOutside;

                // Check if return is after closing time
                if (returnHour >= 20) {

                    // After 20:00, push to next day at 8:00 + overflow
                    hoursOutside = returnHour - 20;
                    returnDateTime = returnDateTime.plusDays(1).withHour(8).withMinute(returnDateTime.getMinute()).plusHours(hoursOutside);

                } else {

                    // Before 8:00, set to 8:00 same day
                    returnDateTime = returnDateTime.withHour(8).withMinute(returnDateTime.getMinute());
                }

                // Round minutes to nearest 15 min interval
                int minutes = returnDateTime.getMinute();
                int roundedMinutes = ((minutes + 7) / 15) * 15;

                // Check if rounding caused minute overflow
                if (roundedMinutes == 60) {

                    // Add hour and reset minutes
                    returnDateTime = returnDateTime.plusHours(1).withMinute(0);

                } else {

                    // Set rounded minutes
                    returnDateTime = returnDateTime.withMinute(roundedMinutes);
                }
            }
        } else {

            // Daily rental - return at 20:00 on final day
            returnDateTime = LocalDateTime.now().plusDays(cartItem.getDuration()).withHour(20).withMinute(0);
        }
        return returnDateTime;
    }

    /**
     * Displays a return receipt for a single item.
     * @param rental the rental being returned
     * @param item the item being returned
     * @param member the member returning the item
     * @return the late fee amount (0 if on time)
     */
    public double displaySingleReturnReceipt(Rental rental, Item item, Member member) {
        helper.clearScreen();
        helper.printHeader("        ARTIKEL RETURNERAD");
        System.out.println("Artikel: " + item.getName() + " [" + item.getId() + "] (" + ItemSelector.getItemTypeDescription(item) + ")");
        System.out.println("Medlem: " + member.getName());
        System.out.println("Hyrd från: " + rental.getStartDate());

        // Get current time as actual return date
        LocalDateTime actualReturnDate = LocalDateTime.now();

        // Format date and time for better readability
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm");

        System.out.println("Returnerad: " + actualReturnDate.format(dateTimeFormatter));

        helper.printDivider();

        // Display original rental cost
        double originalCost = rental.getTotalCost();
        System.out.printf("Betalt pris: %.2f kr%n", originalCost);

        // Calculate late fee
        double lateFee = calculateLateFee(rental, item, member, actualReturnDate);

        // Display late fee if applicable
        if (lateFee > 0) {
            System.out.println();
            System.out.printf("TOTALT ATT BETALA: %.2f kr%n", lateFee);
        }

        System.out.println();
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        helper.pressEnterToContinue();

        return lateFee;
    }

    /**
     * Displays a bulk return receipt for multiple items.
     * @param returnedItems list of item descriptions
     * @param totalLateFees total late fees across all returns
     * @param successCount number of successfully returned items
     */
    public void displayBulkReturnReceipt(List<String> returnedItems, double totalLateFees, int successCount) {
        helper.clearScreen();
        helper.printHeader("    RETURSAMMANFATTNING");
        System.out.println("RETURNERADE ARTIKLAR (" + successCount + " st):");
        System.out.println();

        // Display each returned item
        for (String itemInfo : returnedItems) {
            System.out.println(itemInfo);
        }

        System.out.println();
        helper.printDivider();

        // Display late fees or success message
        if (totalLateFees > 0) {
            System.out.println();
            System.out.printf("TOTALA FÖRSENINGSAVGIFTER: %.2f kr%n", totalLateFees);
        } else {
            System.out.println();
            System.out.println("Alla artiklar returnerade i tid!");
        }

        System.out.println();
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        helper.pressEnterToContinue();
    }

    /**
     * Calculates late fee for a returned rental.
     * @param rental the rental
     * @param item the item
     * @param member the member
     * @param actualReturnDate the actual return date
     * @return late fee amount (0 if on time)
     */
    private double calculateLateFee(Rental rental, Item item, Member member, LocalDateTime actualReturnDate) {

        // Get original rental cost and member discount
        double originalCost = rental.getTotalCost();
        double discount = getDiscount(member);

        // Calculate days rented
        LocalDateTime rentalStartDateTime = rental.getStartDate().atStartOfDay();
        long daysRented = java.time.temporal.ChronoUnit.DAYS.between(rentalStartDateTime.toLocalDate(), actualReturnDate.toLocalDate());

        // Estimate expected rental period
        double pricePerDay = item.getPricePerDay() * (1 - discount);
        long expectedDays = Math.round(originalCost / pricePerDay);

        //Check if returnal date is overdue
        if (daysRented > expectedDays) {

            // Calculate late fee
            long overdueDays = daysRented - expectedDays;
            double lateFee = overdueDays * pricePerDay;

            System.out.println();
            System.out.println("FÖRSENAD RETUR");
            System.out.println("Förväntad retur: Efter " + expectedDays + " dagar");
            System.out.println("Faktisk retur: Efter " + daysRented + " dagar");
            System.out.println("Försenad: " + overdueDays + " dagar");
            System.out.printf("Senavgift: %.2f kr%n", lateFee);

            return lateFee;
        }

        // No late fee if returned on time
        return 0;
    }

    /**
     * Calculates late fee info for bulk returns.
     * @param rental the rental
     * @param item the item
     * @param member the member
     * @return formatted string with late fee info, or empty string if on time
     */
    public String calculateBulkLateFeeInfo(Rental rental, Item item, Member member) {

        // Get current time as return date
        LocalDateTime actualReturnDate = LocalDateTime.now();

        // Get original cost and member discount
        double originalCost = rental.getTotalCost();
        double discount = getDiscount(member);

        // Calculate days rented
        LocalDateTime rentalStartDateTime = rental.getStartDate().atStartOfDay();
        long daysRented = java.time.temporal.ChronoUnit.DAYS.between(rentalStartDateTime.toLocalDate(), actualReturnDate.toLocalDate());

        // Calculate expected days
        double pricePerDay = item.getPricePerDay() * (1 - discount);
        long expectedDays = Math.round(originalCost / pricePerDay);

        // Return late fee info if overdue
        if (daysRented > expectedDays) {
            long overdueDays = daysRented - expectedDays;
            double lateFee = overdueDays * pricePerDay;
            return String.format("%n    Försenad %d dagar - Avgift: %.2f kr", overdueDays, lateFee);
        }
        return "";
    }

    /**
     * Calculates late fee amount for bulk returns.
     * @param rental the rental
     * @param item the item
     * @param member the member
     * @return late fee amount
     */
    public double calculateBulkLateFeeAmount(Rental rental, Item item, Member member) {

        // Get current time as return date
        LocalDateTime actualReturnDate = LocalDateTime.now();

        // Get original cost and member discount
        double originalCost = rental.getTotalCost();
        double discount = getDiscount(member);

        // Calculate days rented
        LocalDateTime rentalStartDateTime = rental.getStartDate().atStartOfDay();
        long daysRented = java.time.temporal.ChronoUnit.DAYS.between(rentalStartDateTime.toLocalDate(), actualReturnDate.toLocalDate());

        // Calculate expected rental period
        double pricePerDay = item.getPricePerDay() * (1 - discount);
        long expectedDays = Math.round(originalCost / pricePerDay);

        // Calculate and return late fee if overdue
        if (daysRented > expectedDays) {
            long overdueDays = daysRented - expectedDays;
            return overdueDays * pricePerDay;
        }

        // No late fee if returned on time
        return 0;
    }

    /**
     * Get discount multiplier based on membership level.
     * @param member the member
     * @return discount as decimal (0.0 = 0%, 0.20 = 20%, 0.30 = 30%)
     */
    private double getDiscount(Member member) {

        // Return discount based on membership level
        return switch (member.getMembershipLevel()) {
            case STANDARD -> 0.0;
            case STUDENT -> 0.20;
            case PREMIUM -> 0.30;
        };
    }
}
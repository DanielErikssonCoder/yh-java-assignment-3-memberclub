package com.memberclub.ui.components;

import com.memberclub.model.*;
import com.memberclub.service.RevenueService;
import com.memberclub.system.ClubSystem;
import com.memberclub.ui.UIHelper;
import com.memberclub.ui.validation.InputValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles all return-related operations.
 * Manages single, multiple, and bulk returns with late fee calculations.
 */
public class ReturnHandler {
    private Scanner scanner;
    private ClubSystem system;
    private UIHelper helper;
    private ReceiptGenerator receiptGenerator;
    private RevenueService revenueService;

    /**
     * Creates a new return handler.
     * @param scanner the scanner for user input
     * @param system the club system
     * @param helper the UI helper
     * @param receiptGenerator the receipt generator
     * @param revenueService the revenue service
     */
    public ReturnHandler(Scanner scanner, ClubSystem system, UIHelper helper, ReceiptGenerator receiptGenerator, RevenueService revenueService) {
        this.scanner = scanner;
        this.system = system;
        this.helper = helper;
        this.receiptGenerator = receiptGenerator;
        this.revenueService = revenueService;
    }

    /**
     * Main return menu with options for single, multiple, or all returns.
     */
    public void showReturnMenu() {
        helper.clearScreen();
        helper.printHeader("        RETURNERA ARTIKEL");

        // Get all active rentals
        List<Rental> activeRentals = system.getRentalService().getActiveRentals();

        // Check if any active rentals exist
        if (activeRentals.isEmpty()) {
            System.out.println("Inga aktiva uthyrningar finns!");
            helper.pressEnterToContinue();
            return;
        }

        System.out.println("[1] Returnera enskild artikel");
        System.out.println("[2] Returnera flera artiklar");
        System.out.println("[3] Returnera allt");
        System.out.println();
        System.out.println("[0] Avbryt");
        System.out.println();
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        System.out.println();

        // Get user choice
        int choice = InputValidator.getIntInRange(scanner, 0, 3, "Välj alternativ: ");

        // Handle menu selection
        switch (choice) {
            case 0 -> {
                // Cancel (do nothing)
            }
            case 1 -> returnSingleItem(activeRentals);
            case 2 -> returnMultipleItems(activeRentals);
            case 3 -> returnAllItems(activeRentals);
        }
    }

    /**
     * Return a single item.
     * @param activeRentals list of active rentals
     */
    private void returnSingleItem(List<Rental> activeRentals) {
        helper.clearScreen();
        helper.printHeader("    RETURNERA ENSKILD ARTIKEL");
        displayActiveRentals(activeRentals);
        System.out.println();
        System.out.println("[0] Avbryt");
        System.out.println();
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        System.out.println();

        // Get user selection
        int choice = InputValidator.getIntInRange(scanner, 0, activeRentals.size(), "Välj artikel: ");

        // Check if user cancelled
        if (choice == 0) {
            return;
        }

        // Get selected rental
        Rental rental = activeRentals.get(choice - 1);

        // Process the return
        processReturn(rental);
    }

    /**
     * Return multiple items.
     * @param activeRentals list of active rentals
     */
    private void returnMultipleItems(List<Rental> activeRentals) {
        helper.clearScreen();
        helper.printHeader("    RETURNERA FLERA ARTIKLAR");
        displayActiveRentals(activeRentals);
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        System.out.println();
        System.out.println("Ange artikelnummer separerade med komma (ex: 1,3,5)");
        System.out.println();
        System.out.print("Artiklar: ");

        String input = scanner.nextLine().trim();

        // Check if input is empty
        if (input.isEmpty()) {
            System.out.println();
            System.out.println("Du måste ange minst en artikel!");
            helper.pressEnterToContinue();
            return;
        }

        // Parse selections
        String[] selections = input.split(",");

        // Initialize list for selected rentals
        List<Rental> toReturn = new ArrayList<>();

        // Process each selection
        for (String sel : selections) {

            // Try to parse integer
            try {
                int num = Integer.parseInt(sel.trim());

                // Validate and add to return list
                if (num > 0 && num <= activeRentals.size()) {
                    toReturn.add(activeRentals.get(num - 1));
                }
            } catch (NumberFormatException e) {
                // Skip invalid
            }
        }

        // Check if any valid selections were made
        if (toReturn.isEmpty()) {
            System.out.println();
            System.out.println("Inga giltiga val gjordes.");
            helper.pressEnterToContinue();
            return;
        }

        // Process bulk return
        processReturnBulk(toReturn);
    }

    /**
     * Return all items after confirmation.
     * @param activeRentals list of all active rentals
     */
    private void returnAllItems(List<Rental> activeRentals) {
        helper.clearScreen();
        helper.printHeader("       RETURNERA ALLT");
        System.out.println("Returnera " + activeRentals.size() + " artiklar?");
        System.out.println();

        // Get user confirmation
        boolean confirmed = InputValidator.getYesNoConfirmation(scanner, "Bekräfta (Ja/Nej): ");

        // Check if user cancelled
        if (!confirmed) {
            return;
        }

        // Process bulk return
        processReturnBulk(activeRentals);
    }

    /**
     * Process a single return with receipt.
     * @param rental the rental to return
     */
    private void processReturn(Rental rental) {

        // Get item and member from rental
        Item item = system.getInventory().getItem(rental.getItemId());
        Member member = system.getMemberRegistry().getMember(rental.getMemberId());

        // Process return in rental service
        boolean returned = system.getRentalService().returnItem(rental.getRentalId());

        // Check if return was successful
        if (returned) {

            // Display receipt and get late fee
            double lateFee = receiptGenerator.displaySingleReturnReceipt(rental, item, member);

            // Add late fee to revenue if applicable
            if (lateFee > 0) {
                revenueService.addRevenue(lateFee);
            }
        } else {
            System.out.println();
            System.out.println("Returneringen misslyckades.");
            helper.pressEnterToContinue();
        }
    }

    /**
     * Process multiple returns with bulk receipt.
     * @param rentals list of rentals to return
     */
    private void processReturnBulk(List<Rental> rentals) {

        // Initialize tracking variables
        List<String> returnedItems = new ArrayList<>();
        double totalLateFees = 0.0;
        int successCount = 0;

        // Process each rental
        for (Rental rental : rentals) {

            // Get item and member from rental
            Item item = system.getInventory().getItem(rental.getItemId());
            Member member = system.getMemberRegistry().getMember(rental.getMemberId());

            // Process return in rental service
            boolean returned = system.getRentalService().returnItem(rental.getRentalId());

            // Check if return was successful
            if (returned) {

                // Increment success counter
                successCount++;

                // Build item info string
                String itemInfo = item.getName() + " [" + item.getId() + "] (" + ItemSelector.getItemTypeDescription(item) + ")";

                // Add late fee info if applicable
                String lateFeeInfo = receiptGenerator.calculateBulkLateFeeInfo(rental, item, member);
                double lateFee = receiptGenerator.calculateBulkLateFeeAmount(rental, item, member);

                // Check if late fee exists
                if (lateFee > 0) {

                    // Add to total late fees
                    totalLateFees += lateFee;

                    // Append late fee info to item string
                    itemInfo += lateFeeInfo;
                }

                // Add to returned items list
                returnedItems.add(itemInfo);
            }
        }

        // Add late fees to revenue
        if (totalLateFees > 0) {
            revenueService.addRevenue(totalLateFees);
        }

        // Display bulk receipt
        receiptGenerator.displayBulkReturnReceipt(returnedItems, totalLateFees, successCount);
    }

    /**
     * Display list of active rentals.
     * @param activeRentals list of active rentals
     */
    private void displayActiveRentals(List<Rental> activeRentals) {
        System.out.println("Aktiva uthyrningar:");
        System.out.println();

        // Loop through and display each rental
        for (int i = 0; i < activeRentals.size(); i++) {

            // Get rental and related data
            Rental rental = activeRentals.get(i);
            Item item = system.getInventory().getItem(rental.getItemId());
            Member member = system.getMemberRegistry().getMember(rental.getMemberId());

            System.out.println("[" + (i + 1) + "] " + item.getName());
            System.out.println("Medlem: " + member.getName());
            System.out.println("Hyrd: " + rental.getStartDate());
            System.out.printf("Pris: %.2f kr%n", rental.getTotalCost());
            System.out.println();
        }
    }
}
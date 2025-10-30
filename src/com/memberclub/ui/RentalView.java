package com.memberclub.ui;

import com.memberclub.model.*;
import com.memberclub.model.enums.RentalPeriod;
import com.memberclub.system.ClubSystem;
import com.memberclub.ui.components.*;
import com.memberclub.ui.validation.InputValidator;
import com.memberclub.service.RevenueService;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * View class for rental-related operations.
 * Refactored to use component classes for better separation of concerns.
 */
public class RentalView {

    private Scanner scanner;
    private ClubSystem system;
    private UIHelper helper;
    private RevenueService revenueService;

    // Component classes
    private ItemSelector itemSelector;
    private MemberSelector memberSelector;
    private ReceiptGenerator receiptGenerator;
    private ReturnHandler returnHandler;

    /**
     * Creates a new rental view.
     * @param scanner the scanner to use for input
     * @param system the club system
     * @param helper the UI helper for common operations
     */
    public RentalView(Scanner scanner, ClubSystem system, UIHelper helper) {
        this.scanner = scanner;
        this.system = system;
        this.helper = helper;
        this.revenueService = system.getRevenueService();

        // Initialize component classes
        this.itemSelector = new ItemSelector(scanner, system, helper);
        this.memberSelector = new MemberSelector(scanner, system, helper);
        this.receiptGenerator = new ReceiptGenerator(helper, scanner);
        this.returnHandler = new ReturnHandler(scanner, system, helper, receiptGenerator, revenueService);
    }

    /**
     * Menu option 1: Rent an item.
     * Main entry point for rental flow with shopping cart functionality.
     */
    public void rentItem() {
        // Select member first
        Member selectedMember = memberSelector.selectMember();

        // Check if member selection was cancelled
        if (selectedMember == null) {
            return;
        }

        // Create shopping cart
        ShoppingCart cart = new ShoppingCart(scanner, helper);

        // Shopping loop, continues until checkout or cancel
        boolean continueShopping = true;
        while (continueShopping) {
            helper.clearScreen();
            helper.printHeader("           HYRA ARTIKEL");
            System.out.println("Medlem: " + selectedMember.getName() + " (" + selectedMember.getMembershipLevel() + ")");
            System.out.println("Artiklar i kundvagn: " + cart.size() + " st");
            System.out.println();
            helper.printDivider();
            System.out.println();
            System.out.println("[1] Lägg till artikel (enskild)");
            System.out.println("[2] Lägg till flera artiklar");
            System.out.println("[3] Visa kundvagn");
            System.out.println("[4] Registrera order");
            System.out.println();
            System.out.println("[0] Avbryt");
            System.out.println();
            System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
            System.out.println();

            // Get user choice
            int mainChoice = InputValidator.getIntInRange(scanner, 0, 4, "Välj alternativ: ");

            // Handle menu selection
            switch (mainChoice) {
                case 0 -> {

                    // Cancel and exit
                    return;
                }
                case 1 -> {

                    // Add single item
                    CartItem cartItem = addSingleItem(selectedMember);

                    // Check if item was added successfully
                    if (cartItem != null) {
                        cart.addCartItem(cartItem);
                        System.out.println();
                        System.out.println("Artikel tillagd i kundvagnen!");
                        helper.pressEnterToContinue();
                    }
                }
                case 2 -> {

                    // Add multiple items
                    List<CartItem> items = addMultipleItems(selectedMember);
                    cart.addAll(items);

                    // Display confirmation if items were added
                    if (!items.isEmpty()) {
                        helper.clearScreen();
                        System.out.println(items.size() + " artiklar tillagda i kundvagnen!");
                        helper.pressEnterToContinue();
                    }
                }
                case 3 -> {

                    // Show cart management menu
                    cart.showManagementMenu(selectedMember);
                }
                case 4 -> {

                    // Check if cart has items
                    if (cart.isEmpty()) {
                        System.out.println();
                        System.out.println("Kundvagnen är tom! Lägg till artiklar först.");
                        helper.pressEnterToContinue();
                    } else {

                        // Proceed to checkout
                        checkout(cart, selectedMember);
                        continueShopping = false;
                    }
                }
            }
        }
    }

    /**
     * Add a single item to cart.
     * @param member the member renting
     * @return CartItem if successful, null if cancelled
     */
    private CartItem addSingleItem(Member member) {

        // Let user select category
        int category = itemSelector.selectCategory();

        // Check if cancelled
        if (category == 0) {
            return null;
        }

        // Let user select item from category
        Item selectedItem = itemSelector.selectItem(category);

        // Check if cancelled
        if (selectedItem == null) {
            return null;
        }

        // Let user select rental period
        RentalPeriod period = itemSelector.selectPeriod();

        // Let user select rental duration
        int duration = itemSelector.selectDuration(period);

        // Calculate base price
        double basePrice = (period == RentalPeriod.HOURLY) ? selectedItem.getPricePerHour() * duration : selectedItem.getPricePerDay() * duration;

        // Return new cart item
        return new CartItem(selectedItem, duration, period, basePrice);
    }

    /**
     * Add multiple items to cart.
     * @param member the member renting
     * @return list of CartItems
     */
    private List<CartItem> addMultipleItems(Member member) {

        // Initialize list for cart items
        List<CartItem> items = new ArrayList<>();

        // Let user select category
        int category = itemSelector.selectCategory();

        // Check if cancelled
        if (category == 0) {
            return items;
        }

        // Let user select multiple items
        List<Item> selectedItems = itemSelector.selectMultipleItems(category);

        // Check if any items were selected
        if (selectedItems.isEmpty()) {
            return items;
        }

        // Show selected items
        helper.clearScreen();
        System.out.println("Valda artiklar:");
        for (int i = 0; i < selectedItems.size(); i++) {
            System.out.println((i + 1) + ". " + selectedItems.get(i).getName() + " (" + ItemSelector.getItemTypeDescription(selectedItems.get(i)) + ")");
        }

        // Ask for same or different periods
        System.out.println();
        System.out.println("[1] Samma period för alla artiklar");
        System.out.println("[2] Olika period för varje artikel");
        System.out.println();

        // Get user choice
        int periodChoice = InputValidator.getIntInRange(scanner, 1, 2, "Välj: ");

        // Check if same period for all items
        if (periodChoice == 1) {

            // Same period for all
            helper.clearScreen();
            System.out.println();
            System.out.println("Välj periodtyp för samtliga artiklar");

            // Get rental period
            RentalPeriod period = itemSelector.selectPeriod();

            // Get rental duration
            int duration = itemSelector.selectDuration(period);

            // Create cart items for all selected items with same period
            for (Item selectedItem : selectedItems) {

                // Calculate base price
                double basePrice = (period == RentalPeriod.HOURLY) ? selectedItem.getPricePerHour() * duration : selectedItem.getPricePerDay() * duration;

                // Add to items list
                items.add(new CartItem(selectedItem, duration, period, basePrice));
            }
        } else {

            // Different period for each item
            for (int i = 0; i < selectedItems.size(); i++) {

                // Get current item
                Item selectedItem = selectedItems.get(i);

                System.out.println();
                System.out.println("--- Artikel " + (i + 1) + "/" + selectedItems.size() + ": " + selectedItem.getName() + " (" + ItemSelector.getItemTypeDescription(selectedItem) + ") ---");

                // Get rental period for this item
                RentalPeriod period = itemSelector.selectPeriod();

                // Get rental duration for this item
                int duration = itemSelector.selectDuration(period);

                // Calculate base price
                double basePrice = (period == RentalPeriod.HOURLY) ? selectedItem.getPricePerHour() * duration : selectedItem.getPricePerDay() * duration;

                // Add to items list
                items.add(new CartItem(selectedItem, duration, period, basePrice));
            }
        }
        return items;
    }

    /**
     * Checkout and create rentals for all items in cart.
     * @param cart the shopping cart
     * @param member the member
     */
    private void checkout(ShoppingCart cart, Member member) {
        helper.clearScreen();
        helper.printHeader("        BEKRÄFTA BESTÄLLNING");
        System.out.println("Medlem: " + member.getName() + " (" + member.getMembershipLevel() + ")");
        System.out.println();
        helper.printDivider();
        System.out.println();
        System.out.println("Du är på väg att hyra ut följande artiklar:");
        System.out.println();

        // Get total before discount
        double totalBeforeDiscount = cart.getTotalBeforeDiscount();

        // Display each cart item
        for (CartItem cartItem : cart.getItems()) {
            System.out.print(cartItem.getItem().getId() + " - " + cartItem.getItem().getName());
            System.out.print(" (" + ItemSelector.getItemTypeDescription(cartItem.getItem()) + ")");
            System.out.print(" - " + cartItem.getDuration() + " ");
            System.out.print(cartItem.getPeriod() == RentalPeriod.HOURLY ? (cartItem.getDuration() == 1 ? "timme" : "timmar") : (cartItem.getDuration() == 1 ? "dag" : "dagar"));
            System.out.printf(" (%.2f kr)%n", cartItem.getPrice());
        }

        System.out.println();

        // Calculate pricing breakdown
        double discount = getDiscount(member);
        double discountAmount = totalBeforeDiscount * discount;
        double totalAfterDiscount = totalBeforeDiscount - discountAmount;

        helper.printDivider();
        System.out.println();
        System.out.printf("Summa: %.2f kr%n", totalBeforeDiscount);

        // Display discount if applicable
        if (discount > 0) {
            System.out.printf("Din rabatt (%d%%): -%.2f kr%n", (int)(discount * 100), discountAmount);
        }

        System.out.println();
        System.out.printf("ATT BETALA: %.2f kr%n", totalAfterDiscount);
        System.out.println();
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        System.out.println();

        // Get user confirmation
        boolean confirmed = InputValidator.getYesNoConfirmation(scanner, "Vill du genomföra beställningen? (Ja/Nej): ");

        // Check if user cancelled
        if (!confirmed) {
            helper.clearScreen();
            System.out.println("Beställning avbruten.");
            helper.pressEnterToContinue();
            return;
        }

        // Create rentals
        List<Rental> createdRentals = new ArrayList<>();

        // Process each cart item
        for (CartItem cartItem : cart.getItems()) {

            // Create rental in system
            Rental rental = system.getRentalService().rentItem(member.getId(), cartItem.getItem().getId(), cartItem.getDuration(), cartItem.getPeriod());

            // Add to created rentals if successful
            if (rental != null) {
                createdRentals.add(rental);
            }
        }

        // Check if any rentals were created successfully
        if (!createdRentals.isEmpty()) {

            // Add revenue
            revenueService.addRevenue(totalAfterDiscount);

            // Display receipt
            receiptGenerator.displayRentalReceipt(cart.getItems(), member, totalBeforeDiscount);
        } else {
            System.out.println();
            System.out.println("Något gick fel vid skapandet av uthyrningar.");
            helper.pressEnterToContinue();
        }
    }

    /**
     * Menu option 2: Return an item.
     * Delegates to ReturnHandler component.
     */
    public void returnItem() {
        returnHandler.showReturnMenu();
    }

    /**
     * Menu option 3: View all rentals.
     */
    public void viewMyRentals() {
        helper.clearScreen();
        helper.printHeader("       ALLA UTHYRNINGAR");

        // Get all rentals from system
        List<Rental> allRentals = system.getRentalService().getAllRentals();

        // Check if any rentals exist
        if (allRentals.isEmpty()) {
            System.out.println("Inga aktiva uthyrningar finns!");
            helper.pressEnterToContinue();
            return;
        }

        // Separate active and completed
        List<Rental> activeRentals = new ArrayList<>();
        List<Rental> completedRentals = new ArrayList<>();

        // Sort rentals into active and completed lists
        for (Rental rental : allRentals) {
            if (rental.getEndDate() == null) {

                // Active rental
                activeRentals.add(rental);
            } else {

                // Completed rental
                completedRentals.add(rental);
            }
        }

        // Display active rentals
        helper.printDivider();
        System.out.println("AKTIVA UTHYRNINGAR (" + activeRentals.size() + " st)");
        helper.printDivider();

        // Check if any active rentals exist
        if (activeRentals.isEmpty()) {
            System.out.println("Inga aktiva uthyrningar");
        } else {

            // Display each active rental
            for (Rental rental : activeRentals) {

                // Get item and member from rental
                Item item = system.getInventory().getItem(rental.getItemId());
                Member member = system.getMemberRegistry().getMember(rental.getMemberId());

                // Display rental information
                System.out.println();
                System.out.println("Artikel: " + item.getId() + " - " + item.getName() + " (" + ItemSelector.getItemTypeDescription(item) + ")");
                System.out.println("Medlem: " + member.getName() + " (" + member.getMembershipLevel() + ")");
                System.out.println("Hyrd från: " + rental.getStartDate());
                System.out.printf("Pris: %.2f kr%n", rental.getTotalCost());
            }
        }

        System.out.println();
        System.out.println();

        // Display completed rentals
        helper.printDivider();
        System.out.println("AVSLUTADE UTHYRNINGAR (" + completedRentals.size() + " st)");
        helper.printDivider();

        // Check if any completed rentals exist
        if (completedRentals.isEmpty()) {
            System.out.println();
            System.out.println("Inga avslutade uthyrningar");
        } else {

            // Display each completed rental
            for (Rental rental : completedRentals) {

                // Get item and member from rental
                Item item = system.getInventory().getItem(rental.getItemId());
                Member member = system.getMemberRegistry().getMember(rental.getMemberId());

                // Display rental information
                System.out.println();
                System.out.println("Artikel: " + item.getId() + " - " + item.getName() + " (" + ItemSelector.getItemTypeDescription(item) + ")");
                System.out.println("Medlem: " + member.getName() + " (" + member.getMembershipLevel() + ")");
                System.out.println("Period: " + rental.getStartDate() + " - " + rental.getEndDate());
                System.out.printf("Betalt: %.2f kr%n", rental.getTotalCost());
            }
        }
        System.out.println();
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        helper.pressEnterToContinue();
    }

    /**
     * Menu option 6: Show revenue (cashier view).
     */
    public void showRevenue() {
        helper.clearScreen();
        helper.printHeader("              KASSAVY");

        // Get all rentals
        List<Rental> allRentals = system.getRentalService().getAllRentals();
        int totalRentals = allRentals.size();

        // Count active and completed rentals
        long activeCount = allRentals.stream().filter(r -> r.getEndDate() == null).count();
        long completedCount = allRentals.stream().filter(r -> r.getEndDate() != null).count();

        // Display statistics
        System.out.println("Statistik:");
        System.out.println("Totalt antal uthyrningar: " + totalRentals + " st");
        System.out.println("Aktiva uthyrningar: " + activeCount + " st");
        System.out.println("Avslutade uthyrningar: " + completedCount + " st");
        System.out.println();
        helper.printDivider();
        System.out.println();

        // Display total revenue
        System.out.printf("TOTALA INTÄKTER: %.2f kr%n", revenueService.getTotalRevenue());
        System.out.println();
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        helper.pressEnterToContinue();
    }

    /**
     * Get discount based on membership level.
     * @param member the member
     * @return discount as decimal
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
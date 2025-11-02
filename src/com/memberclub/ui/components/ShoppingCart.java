package com.memberclub.ui.components;

import com.memberclub.model.Item;
import com.memberclub.model.Member;
import com.memberclub.model.enums.RentalPeriod;
import com.memberclub.ui.ItemView;
import com.memberclub.ui.UIHelper;
import com.memberclub.ui.validation.InputValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Manages shopping cart functionality for rental items
 * Handles adding, removing, viewing cart items
 */
public class ShoppingCart {

    private List<CartItem> items;
    private Scanner scanner;
    private UIHelper helper;

    /**
     * Creates a new shopping cart
     * @param scanner the scanner for user input
     * @param helper the UI helper for display operations
     */
    public ShoppingCart(Scanner scanner, UIHelper helper) {
        this.items = new ArrayList<>();
        this.scanner = scanner;
        this.helper = helper;
    }

    /**
     * Adds an item to the cart
     * @param item the item to add
     * @param duration rental duration
     * @param period rental period (HOURLY or DAILY)
     * @param price calculated price
     */
    public void addItem(Item item, int duration, RentalPeriod period, double price) {
        items.add(new CartItem(item, duration, period, price));
    }

    /**
     * Adds a pre created cart item.
     * @param cartItem the cart item to add
     */
    public void addCartItem(CartItem cartItem) {
        items.add(cartItem);
    }

    /**
     * Adds multiple cart items at once
     * @param cartItems list of cart items to add
     */
    public void addAll(List<CartItem> cartItems) {
        items.addAll(cartItems);
    }

    /**
     * Removes an item from the cart
     * @param index the index of the item to remove
     * @return the removed cart item, or null if index is invalid
     */
    public CartItem removeItem(int index) {

        // Validate index and remove item
        if (index >= 0 && index < items.size()) {
            return items.remove(index);
        }

        // Return null for invalid index
        return null;
    }

    /**
     * Clears all items from the cart
     */
    public void clear() {
        items.clear();
    }

    /**
     * Gets the number of items in the cart
     * @return the cart size
     */
    public int size() {
        return items.size();
    }

    /**
     * Checks if the cart is empty
     * @return true if cart has no items
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Gets all items in the cart
     * @return unmodifiable view of cart items
     */
    public List<CartItem> getItems() {
        return new ArrayList<>(items);
    }

    /**
     * Calculates total price before discount
     * @return sum of all item prices
     */
    public double getTotalBeforeDiscount() {

        // Initialize total
        double total = 0;

        // Sum up all item prices
        for (CartItem cartItem : items) {
            total += cartItem.getPrice();
        }
        return total;
    }

    /**
     * Calculates discount amount based on member level
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

    /**
     * Displays the cart contents with pricing breakdown
     * @param member the member, for discount calculation
     */
    public void display(Member member) {
        helper.clearScreen();
        helper.printHeader("              KUNDVAGN");
        System.out.println("Medlem: " + member.getName() + " (" + member.getMembershipLevel() + ")");
        System.out.println();
        helper.printDivider();
        System.out.println();

        // Check if cart is empty
        if (items.isEmpty()) {
            System.out.println("Kundvagnen är tom!");
            System.out.println();
            System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
            helper.pressEnterToContinue();
            return;
        }

        // Display each item
        for (int i = 0; i < items.size(); i++) {

            // Get current cart item and display item name, rental duration and price
            CartItem cartItem = items.get(i);
            System.out.println(ItemView.formatItemForList(i + 1, cartItem.getItem()));
            System.out.println("    Längd: " + cartItem.getDuration() + " " + (cartItem.getPeriod() == RentalPeriod.HOURLY ? (cartItem.getDuration() == 1 ? "timme" : "timmar") : (cartItem.getDuration() == 1 ? "dag" : "dagar")));
            System.out.printf("    Pris: %.2f kr%n", cartItem.getPrice());
            System.out.println();
        }

        // Calculate totals
        double totalBeforeDiscount = getTotalBeforeDiscount();
        double discount = getDiscount(member);
        double totalAfterDiscount = totalBeforeDiscount * (1 - discount);

        // Display price breakdown
        helper.printDivider();
        System.out.println();
        System.out.printf("Pris (före rabatt): %.2f kr%n", totalBeforeDiscount);
        System.out.println("Rabatt (" + (int)(discount * 100) + "%): -" +
                String.format("%.2f", totalBeforeDiscount - totalAfterDiscount) + " kr");
        System.out.printf("Totalt: %.2f kr%n", totalAfterDiscount);
        System.out.println();
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
    }

    /**
     * Shows cart management menu with options to remove items or clear cart
     * @param member the member (for display)
     */
    public void showManagementMenu(Member member) {

        // Control variable for menu loop
        boolean inCart = true;

        // Continue loop until user exits
        while (inCart) {

            // Display current cart
            display(member);

            // Exit if cart is empty
            if (isEmpty()) {
                return;
            }

            System.out.println();
            System.out.println("[1] Ta bort artikel");
            System.out.println("[2] Rensa kundvagn");
            System.out.println("[0] Gå tillbaka");
            System.out.println();
            System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
            System.out.println();

            // Get user choice
            int choice = InputValidator.getIntInRange(scanner, 0, 2, "Välj alternativ: ");

            // Handle menu selection
            switch (choice) {
                case 0 -> inCart = false;
                case 1 -> removeItemInteractive();
                case 2 -> {

                    // Clear cart and exit menu
                    clearInteractive();
                    inCart = false;
                }
            }
        }
    }

    /**
     * Interactively removes a single item from cart
     */
    private void removeItemInteractive() {

        System.out.println();

        // Get item number to remove
        int itemChoice = InputValidator.getIntInRange(scanner, 1, items.size(), "Ange artikelnummer att ta bort: ");

        // Remove selected item
        CartItem removed = removeItem(itemChoice - 1);

        // Display confirmation if successful
        if (removed != null) {
            helper.clearScreen();
            System.out.println(ItemView.formatItemShort(removed.getItem()) + " borttagen från kundvagnen!");
            helper.pressEnterToContinue();
        }
    }

    /**
     * Interactively clears the entire cart after confirmation
     */
    private void clearInteractive() {
        System.out.println();

        // Get user confirmation
        boolean confirmed = InputValidator.getYesNoConfirmation(scanner, "Är du säker på att du vill rensa hela kundvagnen? (Ja/Nej): ");

        // Check if user confirmed
        if (confirmed) {

            // Store count before clearing
            int itemCount = items.size();

            // Clear cart
            clear();

            System.out.println();
            System.out.println("Kundvagnen rensad! (" + itemCount + " artiklar borttagna)");
        } else {
            System.out.println();
            System.out.println("Rensning avbruten.");
        }
        helper.pressEnterToContinue();
    }
}

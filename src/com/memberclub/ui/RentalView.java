package com.memberclub.ui;

import com.memberclub.model.*;
import com.memberclub.system.ClubSystem;
import com.memberclub.model.camping.*;
import com.memberclub.model.fishing.*;
import com.memberclub.model.vehicles.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * View class for rental-related operations.
 * Handles renting items, returning items, and viewing rentals.
 */
public class RentalView {

    private Scanner scanner;
    private ClubSystem system;
    private UIHelper helper;
    private static double totalRevenue = 0.0; // Track total revenue

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
    }

    /**
     * Inner class to represent a cart item before checkout.
     * Stores item, rental duration, period type, and calculated price.
     */
    private static class CartItem {

        // The item to rent
        Item item;

        // Number of hours or days
        int duration;

        // HOURLY or DAILY
        RentalPeriod period;

        // Base price before member discount
        double price;

        /**
         * Creates a new cart item.
         * @param item the item to rent
         * @param duration number of hours or days
         * @param period HOURLY or DAILY
         * @param price calculated base price before discount
         */
        CartItem(Item item, int duration, RentalPeriod period, double price) {
            this.item = item;
            this.duration = duration;
            this.period = period;
            this.price = price;
        }
    }

    /**
     * Menu option 1: Rent an item.
     * Main entry point for rental flow with shopping cart functionality.
     */
    public void rentItem() {

        // Choose member first
        Member selectedMember = selectMember();

        // If the user cancelled member selection, return to main menu
        if (selectedMember == null) {
            return;
        }

        // Create shopping cart to hold items before checkout
        List<CartItem> cart = new ArrayList<>();

        // Shopping loop that continues until user checks out or cancels
        boolean continueShopping = true;
        while (continueShopping) {

            // Display cart menu
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
            System.out.print("Välj alternativ: ");

            // Read user choice with validation
            int mainChoice = -1;

            // Continue until valid choice
            while (mainChoice < 0 || mainChoice > 4) {
                String input = scanner.nextLine().trim();

                // Check if input is empty
                if (input.isEmpty()) {
                    System.out.println();
                    System.out.println("Du måste ange ett val!");
                    System.out.println();
                    System.out.print("Välj alternativ: ");
                    continue;
                }

                // Try to parse as integer
                try {
                    mainChoice = Integer.parseInt(input);

                    // Check if choice is within valid range
                    if (mainChoice < 0 || mainChoice > 4) {
                        System.out.println();
                        System.out.println("Ogiltigt val! Ange ett nummer mellan 0-4.");
                        System.out.println();
                        System.out.print("Välj alternativ: ");
                    }
                } catch (NumberFormatException e) {
                    System.out.println();
                    System.out.println("Ogiltigt val! Ange ett nummer.");
                    System.out.println();
                    System.out.print("Välj alternativ: ");
                }
            }

            // Handle menu choice from the user
            switch (mainChoice) {
                case 0 -> {

                    // Cancel and return to main menu
                    return;
                }
                case 1 -> {

                    // Add a single item to cart
                    CartItem cartItem = addSingleItem(selectedMember);

                    // Only add if user didn't cancel during selection
                    if (cartItem != null) {
                        cart.add(cartItem);
                        System.out.println();
                        System.out.println("Artikel tillagd i kundvagnen!");
                        helper.pressEnterToContinue();
                    }
                }
                case 2 -> {

                    // Add multiple items to cart
                    List<CartItem> items = addMultipleItems(selectedMember);
                    cart.addAll(items);

                    // Show confirmation if any items were added
                    if (!items.isEmpty()) {
                        helper.clearScreen();
                        System.out.println(items.size() + " artiklar tillagda i kundvagnen!");
                        helper.pressEnterToContinue();
                    }
                }
                case 3 -> {

                    // View and manage shopping cart
                    showCart(cart, selectedMember);
                }
                case 4 -> {

                    // Check that cart is not empty before checkout
                    if (cart.isEmpty()) {
                        System.out.println();
                        System.out.println("Kundvagnen är tom! Lägg till artiklar först.");
                        helper.pressEnterToContinue();
                    } else {

                        // Proceed to checkout and exit shopping loop
                        checkout(cart, selectedMember);
                        continueShopping = false;
                    }
                }
            }
        }
    }

    /**
     * Add a single item to cart.
     * Guides user through category selection, item selection, and rental details.
     * @param member the member renting the item
     * @return CartItem if successful, null if user cancels
     */
    private CartItem addSingleItem(Member member) {

        // Let user select category first
        int category = selectCategory();

        // If user cancelled category selection
        if (category == 0) {
            return null;
        }

        // Add item to cart based on selected category
        return addItemToCart(category, member);
    }

    /**
     * Add multiple items to cart at once.
     * Allows batch selection with same or different rental periods.
     * @param member the member renting the items
     * @return list of CartItems added (empty list if user cancels)
     */
    private List<CartItem> addMultipleItems(Member member) {

        // Create empty list to hold selected items
        List<CartItem> items = new ArrayList<>();

        // Let user select category first
        int category = selectCategory();

        // Return empty list if cancelled
        if (category == 0) {
            return items;
        }

        // Get all available items in selected category
        List<Item> availableItems = getAvailableItemsByCategory(category);

        // Check if any items are available
        if (availableItems.isEmpty()) {
            System.out.println();
            System.out.println("Inga tillgängliga artiklar i denna kategori.");
            helper.pressEnterToContinue();
            return items;
        }

        // Display available items for selection
        helper.clearScreen();
        helper.printHeader("    VÄLJ FLERA ARTIKLAR");
        displayItemList(availableItems);
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        System.out.println();
        System.out.println("Ange artikelnummer separerade med komma (ex: 1,3,5)");
        System.out.println();
        System.out.println("Eller tryck 0 för att avbryta");
        System.out.println();
        System.out.print("Artiklar: ");

        String input = scanner.nextLine().trim();

        // Validate user provided input
        if (input.isEmpty()) {
            System.out.println();
            System.out.println("Du måste ange minst en artikel eller 0 för att avbryta!");
            helper.pressEnterToContinue();
            return items;
        }

        // Check if user wants to cancel
        if (input.equals("0")) {
            return items;
        }

        // Parse comma separated selections
        String[] selections = input.split(",");

        List<Item> selectedItems = new ArrayList<>();

        // Loop through each selection and validate
        for (String sel : selections) {

            // Try to parse as integer
            try {
                int itemNum = Integer.parseInt(sel.trim());

                // Check if number is within valid range
                if (itemNum > 0 && itemNum <= availableItems.size()) {
                    selectedItems.add(availableItems.get(itemNum - 1));
                }
            } catch (NumberFormatException e) {
                // Skip invalid input silently
            }
        }

        // Check if any valid items were selected
        if (selectedItems.isEmpty()) {
            System.out.println();
            System.out.println("Inga giltiga artiklar valda.");
            helper.pressEnterToContinue();
            return items;
        }

        // Show what was selected
        helper.clearScreen();
        System.out.println("Valda artiklar:");
        for (int i = 0; i < selectedItems.size(); i++) {
            System.out.println((i + 1) + ". " + selectedItems.get(i).getName() + " (" + getItemTypeDescription(selectedItems.get(i)) + ")");
        }

        // Ask if user wants same period for all or different per item
        System.out.println();
        System.out.println("[1] Samma period för alla artiklar");
        System.out.println("[2] Olika period för varje artikel");
        System.out.println();
        System.out.print("Välj: ");

        int periodChoice = -1;

        // Read period choice with validation
        while (periodChoice != 1 && periodChoice != 2) {
            String periodInput = scanner.nextLine().trim();

            // Check if input is empty
            if (periodInput.isEmpty()) {
                System.out.print("Välj: ");
                continue;
            }
            try {
                periodChoice = Integer.parseInt(periodInput);
                if (periodChoice != 1 && periodChoice != 2) {
                    System.out.print("Välj 1 eller 2: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Ogiltigt val: ");
            }
        }

        // Handle same period for all items
        if (periodChoice == 1) {

            // Same period for all, ask once
            helper.clearScreen();
            System.out.println();
            System.out.println("Välj period för samtliga artiklar");

            // Ask for period type (HOURLY or DAILY)
            RentalPeriod period = selectPeriod();
            if (period == null) {
                return items;
            }

            // Ask for duration (number of hours/days)
            int duration = selectDuration(period);

            // If the user cancelled, return empty
            if (duration == -1) {
                return items;
            }

            // Apply same period and duration to all selected items
            for (Item selectedItem : selectedItems) {

                // Calculate base price based on period
                double basePrice = (period == RentalPeriod.HOURLY) ? selectedItem.getPricePerHour() * duration : selectedItem.getPricePerDay() * duration;
                items.add(new CartItem(selectedItem, duration, period, basePrice));
            }
        } else {

            // Different period for each, ask for each item individually
            for (int i = 0; i < selectedItems.size(); i++) {

                Item selectedItem = selectedItems.get(i);

                System.out.println();
                System.out.println("--- Artikel " + (i + 1) + "/" + selectedItems.size() + ": " + selectedItem.getName() + " (" + getItemTypeDescription(selectedItem) + ") ---");

                // Ask for period for this specific item
                RentalPeriod period = selectPeriod();

                // Skip this item if the user cancels
                if (period == null) {
                    continue;
                }

                // Ask for duration for this specific item and skip if user cancels
                int duration = selectDuration(period);
                if (duration == -1) {
                    continue;
                }

                // Calculate price for this item
                double basePrice = (period == RentalPeriod.HOURLY) ? selectedItem.getPricePerHour() * duration : selectedItem.getPricePerDay() * duration;

                // Add cart item with individual settings
                items.add(new CartItem(selectedItem, duration, period, basePrice));
            }
        }

        // Return list of configured cart items
        return items;
    }

    /**
     * Let user select rental category.
     * @return category number (1=Camping equipment, 2=Fishing equipment, 3=Boats), or 0 if cancelled
     */
    private int selectCategory() {
        helper.clearScreen();
        helper.printHeader("         VÄLJ KATEGORI");
        System.out.println("[1] Campingutrustning");
        System.out.println("[2] Fiskeutrustning");
        System.out.println("[3] Båtar");
        System.out.println();
        System.out.println("[0] Avbryt");
        System.out.println();
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        System.out.println();
        System.out.print("Välj kategori: ");

        // Initialize a choice variable for the validation loop
        int choice = -1;

        // Read choice with validation
        while (choice < 0 || choice > 3) {

            String input = scanner.nextLine().trim();

            // Check if input is empty
            if (input.isEmpty()) {
                System.out.println();
                System.out.println("Du måste ange ett val!");
                System.out.println();
                System.out.print("Välj kategori: ");
                continue;
            }

            // Try to parse as integer
            try {
                choice = Integer.parseInt(input);
                if (choice < 0 || choice > 3) {
                    System.out.println();
                    System.out.print("Ogiltigt val! Välj 0-3: ");
                }
            } catch (NumberFormatException e) {
                System.out.println();
                System.out.print("Ogiltigt val! Ange ett nummer: ");
            }
        }

        // Return validated choice
        return choice;
    }

    /**
     * Let user select rental period (hourly or daily).
     * @return RentalPeriod.HOURLY or RentalPeriod.DAILY, or null if error
     */
    private RentalPeriod selectPeriod() {
        System.out.println();
        System.out.println("[1] Hyra per timme");
        System.out.println("[2] Hyra per dag");
        System.out.println();
        System.out.print("Period: ");

        int choice = -1;

        // Read choice with validation
        while (choice != 1 && choice != 2) {

            String input = scanner.nextLine().trim();

            // Check if input is empty
            if (input.isEmpty()) {
                System.out.println("Du måste ange ett val!");
                System.out.println();
                System.out.print("Period: ");
                continue;
            }

            // Try to parse as integer
            try {
                choice = Integer.parseInt(input);
                if (choice != 1 && choice != 2) {
                    System.out.print("Välj 1 eller 2: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Ogiltigt val: ");
            }
        }

        // Convert choice to enum
        return (choice == 1) ? RentalPeriod.HOURLY : RentalPeriod.DAILY;
    }

    /**
     * Let user select rental duration.
     * @param period the rental period (HOURLY or DAILY) to display correct prompt
     * @return duration (1-365), or -1 if error
     */
    private int selectDuration(RentalPeriod period) {

        System.out.println();

        // Dynamic prompt based on period
        System.out.print("Antal " + (period == RentalPeriod.HOURLY ? "timmar" : "dagar") + ": ");

        // Initialize choice variable for validation loop
        int duration = -1;

        // Read duration with validation (1-365)
        while (duration < 1 || duration > 365) {
            String input = scanner.nextLine().trim();

            // Check if input is empty
            if (input.isEmpty()) {
                System.out.println("Du måste ange ett värde!");
                System.out.println();
                System.out.print("Antal " + (period == RentalPeriod.HOURLY ? "timmar" : "dagar") + ": ");
                continue;
            }

            // Try to parse as integer
            try {

                duration = Integer.parseInt(input);

                // Check if duration is within reasonable limits
                if (duration < 1 || duration > 365) {
                    System.out.print("Ogiltigt! Ange 1-365: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Ogiltigt! Ange ett nummer: ");
            }
        }

        // Return the validated duration
        return duration;
    }

    /**
     * Display item list with full details including type-specific attributes.
     * Shows price per hour and per day for each item.
     * @param items list of items to display
     */
    private void displayItemList(List<Item> items) {

        // Loop through each item and display with type-specific details
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            System.out.println("[" + (i + 1) + "] " + item.getName());

            // Display water vehicle specific details
            if (item instanceof WaterVehicle) {
                WaterVehicle vehicle = (WaterVehicle) item;
                System.out.print("Typ: ");

                // Check specific water vehicle type
                if (vehicle instanceof Kayak) {
                    Kayak kayak = (Kayak) vehicle;
                    System.out.println("Kajak (" + kayak.getKayakType() + ")");
                    System.out.println("Kapacitet: " + kayak.getCapacity() + " personer | Längd: " + kayak.getLength() + "m");

                } else if (vehicle instanceof MotorBoat) {
                    MotorBoat boat = (MotorBoat) vehicle;
                    System.out.println("Motorbåt (" + boat.getEnginePower() + " hk)");
                    System.out.println("Kapacitet: " + boat.getCapacity() + " personer | Längd: " + boat.getLength() + "m");

                } else if (vehicle instanceof ElectricBoat) {
                    ElectricBoat boat = (ElectricBoat) vehicle;
                    System.out.println("Elbåt (Max: " + boat.getMaxSpeed() + " km/h)");
                    System.out.println("Kapacitet: " + boat.getCapacity() + " personer | Batteri: " + boat.getBatteryCapacity() + " kWh");

                } else if (vehicle instanceof RowBoat) {
                    RowBoat boat = (RowBoat) vehicle;
                    System.out.println("Roddbåt (" + boat.getOars() + " åror)");
                    System.out.println("Kapacitet: " + boat.getCapacity() + " personer | Längd: " + boat.getLength() + "m");
                }
            }

            // Display camping equipment specific details
            else if (item instanceof CampingEquipment) {
                CampingEquipment camping = (CampingEquipment) item;

                // Check specific camping equipment type
                if (camping instanceof Tent) {
                    Tent tent = (Tent) camping;
                    System.out.println("Typ: Tält (" + tent.getTentType() + ")");
                    System.out.println("Kapacitet: " + tent.getCapacity() + " personer | Säsong: " + tent.getSeasonRating());

                } else if (camping instanceof Backpack) {
                    Backpack backpack = (Backpack) camping;
                    System.out.println("Typ: Ryggsäck (" + backpack.getBackpackType() + ")");
                    System.out.println("Volym: " + backpack.getVolume() + "L | Tillverkare: " + backpack.getBrand());

                } else if (camping instanceof SleepingBag) {
                    SleepingBag bag = (SleepingBag) camping;
                    System.out.println("Typ: Sovsäck");
                    System.out.println("Temperatur: " + bag.getTemperatureRating() + "°C | Säsong: " + bag.getSeasonRating());

                } else if (camping instanceof Lantern) {
                    Lantern lantern = (Lantern) camping;
                    System.out.println("Typ: Lykta");
                    System.out.println("Ljusstyrka: " + lantern.getBrightness() + " lumens | Ström: " + lantern.getPowerSource());

                } else if (camping instanceof TrangiaKitchen) {
                    TrangiaKitchen trangia = (TrangiaKitchen) camping;
                    System.out.println("Typ: Trangiakök");
                    System.out.println("Brännare: " + trangia.getBurners() + " st | Bränsle: " + trangia.getFuelType());
                }
            }

            // Display fishing equipment specific details
            else if (item instanceof FishingEquipment) {
                FishingEquipment fishing = (FishingEquipment) item;

                // Check specific fishing equipment type
                if (fishing instanceof FishingRod) {
                    FishingRod rod = (FishingRod) fishing;
                    System.out.println("Typ: Fiskespö (" + rod.getRodType() + ")");
                    System.out.println("Längd: " + rod.getRodLength() + "m | Tillverkare: " + rod.getBrand());
                } else if (fishing instanceof FishingNet) {
                    FishingNet net = (FishingNet) fishing;
                    System.out.println("Typ: Fiskenät (" + net.getNetSize() + ")");
                    System.out.println("Maskstorlek: " + net.getMeshSize() + "mm | Tillverkare: " + net.getBrand());
                } else if (fishing instanceof FishingBait) {
                    FishingBait bait = (FishingBait) fishing;
                    System.out.println("Typ: Bete (" + bait.getBaitType() + ")");
                    System.out.println("Antal: " + bait.getQuantity() + " st | Tillverkare: " + bait.getBrand());
                }
            }

            // Display pricing for all items
            System.out.printf("Pris: %.2f kr/timme | %.2f kr/dag%n", item.getPricePerHour(), item.getPricePerDay());
            System.out.println();
        }
    }

    /**
     * Let user select a member from the registry.
     * @return selected Member, or null if user cancels
     */
    private Member selectMember() {
        helper.clearScreen();
        helper.printHeader("         VÄLJ MEDLEM");

        // Get all members from the member registry
        List<Member> allMembers = system.getMemberRegistry().getAllMembers();

        // Check if any members exist
        if (allMembers.isEmpty()) {
            System.out.println("Inga medlemmar finns i systemet!");
            helper.pressEnterToContinue();
            return null;
        }

        // Display all members with their membership level
        for (int i = 0; i < allMembers.size(); i++) {
            Member member = allMembers.get(i);
            System.out.println("[" + (i + 1) + "] " + member.getName() + " (" + member.getMembershipLevel() + ")");
        }

        System.out.println();
        System.out.println("[0] Avbryt");
        System.out.println();
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        System.out.println();
        System.out.print("Välj medlem: ");

        int memberChoice = -1;

        // Read choice with validation
        while (memberChoice < 0 || memberChoice > allMembers.size()) {

            String input = scanner.nextLine().trim();

            // Check if input is empty
            if (input.isEmpty()) {
                System.out.println();
                System.out.println("Du måste ange ett val!");
                System.out.println();
                System.out.print("Välj medlem: ");
                continue;
            }

            // Try to parse as integer
            try {

                memberChoice = Integer.parseInt(input);

                // Check if choice is within valid range
                if (memberChoice < 0 || memberChoice > allMembers.size()) {
                    System.out.print("Ogiltigt val! Välj 0-" + allMembers.size() + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Ogiltigt val! Ange ett nummer: ");
            }
        }

        // Check if user chose to cancel
        if (memberChoice == 0) {
            return null;
        }

        // Return selected member
        return allMembers.get(memberChoice - 1);
    }

    /**
     * Add an item to cart with period and duration selection.
     * @param categoryChoice the category (1=Camping, 2=Fishing, 3=Boats)
     * @param member the member renting
     * @return CartItem if successful, null if user cancels
     */
    private CartItem addItemToCart(int categoryChoice, Member member) {

        // Get available items in selected category
        List<Item> availableItems = getAvailableItemsByCategory(categoryChoice);

        // Check if any items are available
        if (availableItems.isEmpty()) {
            System.out.println();
            System.out.println("Inga tillgängliga artiklar i denna kategori.");
            helper.pressEnterToContinue();
            return null;
        }

        // Display available items
        helper.clearScreen();
        helper.printHeader("       TILLGÄNGLIGA ARTIKLAR");
        displayItemList(availableItems);
        System.out.println("[0] Gå tillbaka");
        System.out.println();
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        System.out.print("Välj artikel: ");

        int itemChoice = -1;

        // Read item choice with validation
        while (itemChoice < 0 || itemChoice > availableItems.size()) {

            String input = scanner.nextLine().trim();

            // Check if input is empty
            if (input.isEmpty()) {
                System.out.println();
                System.out.println("Du måste ange ett val!");
                System.out.println();
                System.out.print("Välj artikel: ");
                continue;
            }

            // Try to parse as integer
            try {
                itemChoice = Integer.parseInt(input);
                if (itemChoice < 0 || itemChoice > availableItems.size()) {
                    System.out.print("Ogiltigt val! Välj 0-" + availableItems.size() + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Ogiltigt val! Ange ett nummer: ");
            }
        }

        // Return null if user cancelled
        if (itemChoice == 0) {
            return null;
        }

        // Get selected item
        Item selectedItem = availableItems.get(itemChoice - 1);

        // Ask for rental period
        RentalPeriod period = selectPeriod();

        if (period == null) {
            return null;
        }

        // Ask for rental duration, if user cancelled, return null
        int duration = selectDuration(period);
        if (duration == -1) {
            return null;
        }

        // Calculate base price based on period
        double basePrice = (period == RentalPeriod.HOURLY) ? selectedItem.getPricePerHour() * duration : selectedItem.getPricePerDay() * duration;

        // Return new cart item with all details
        return new CartItem(selectedItem, duration, period, basePrice);
    }

    /**
     * Show shopping cart with options to manage items.
     * Allows viewing total, removing items, or clear entire cart.
     * @param cart the current shopping cart
     * @param member the member (needed for discount calculation)
     */
    private void showCart(List<CartItem> cart, Member member) {

        // Control variable for cart viewing loop
        boolean inCart = true;

        // Cart viewing loop
        while (inCart) {
            helper.clearScreen();
            helper.printHeader("              KUNDVAGN");
            System.out.println("Medlem: " + member.getName() + " (" + member.getMembershipLevel() + ")");
            System.out.println();
            helper.printDivider();
            System.out.println();

            // Check if cart is empty
            if (cart.isEmpty()) {
                System.out.println("Kundvagnen är tom!");
                System.out.println();
                System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
                helper.pressEnterToContinue();
                return;
            }

            // Variable to accumulate total price before discount
            double totalBeforeDiscount = 0;

            // Display each item in cart
            for (int i = 0; i < cart.size(); i++) {
                CartItem cartItem = cart.get(i);
                System.out.println("[" + (i + 1) + "] " + cartItem.item.getName());
                System.out.println("    Längd: " + cartItem.duration + " " + (cartItem.period == RentalPeriod.HOURLY ? (cartItem.duration == 1 ? "timme" : "timmar") : (cartItem.duration == 1 ? "dag" : "dagar")));
                System.out.printf("    Pris: %.2f kr%n", cartItem.price);
                System.out.println();
                totalBeforeDiscount += cartItem.price;
            }

            // Calculate discount based on membership level
            double discount = getDiscount(member);

            // Calculate total after discount
            double totalAfterDiscount = totalBeforeDiscount * (1 - discount);

            // Display price breakdown
            helper.printDivider();
            System.out.println();
            System.out.printf("Pris (före rabatt): %.2f kr%n", totalBeforeDiscount);
            System.out.println("Rabatt (" + (int)(discount * 100) + "%): -" + String.format("%.2f", totalBeforeDiscount - totalAfterDiscount) + " kr");
            System.out.printf("Totalt: %.2f kr%n", totalAfterDiscount);
            System.out.println();
            System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
            System.out.println();
            System.out.println("[1] Ta bort artikel");
            System.out.println("[2] Rensa kundvagn");
            System.out.println("[0] Gå tillbaka");
            System.out.println();
            System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
            System.out.println();
            System.out.print("Välj alternativ: ");

            // Initialize a choice variable for the validation loop
            int choice = -1;

            // Read choice with validation
            while (choice < 0 || choice > 2) {
                String input = scanner.nextLine().trim();

                // Check if input is empty
                if (input.isEmpty()) {
                    System.out.println();
                    System.out.println("Du måste ange ett val!");
                    System.out.println();
                    System.out.print("Välj alternativ: ");
                    continue;
                }

                // Try to parse as integer
                try {
                    choice = Integer.parseInt(input);
                    if (choice < 0 || choice > 2) {
                        System.out.print("Ogiltigt val! Välj 0-2: ");
                    }
                } catch (NumberFormatException e) {
                    System.out.print("Ogiltigt val! Ange ett nummer: ");
                }
            }

            // Handle user choice
            switch (choice) {
                case 0 -> {

                    // Go back to rental menu
                    inCart = false;
                }
                case 1 -> {

                    // Remove single item from cart
                    removeItemFromCart(cart);
                }
                case 2 -> {

                    // Clear entire cart
                    clearCart(cart);
                    inCart = false;
                }
            }
        }
    }

    /**
     * Remove a single item from the cart.
     * @param cart the shopping cart to remove from
     */
    private void removeItemFromCart(List<CartItem> cart) {

        System.out.println();
        System.out.print("Ange artikelnummer att ta bort: ");

        int itemChoice = -1;

        // Read item choice with validation
        while (itemChoice < 1 || itemChoice > cart.size()) {

            String input = scanner.nextLine().trim();

            // Check if input is empty
            if (input.isEmpty()) {
                System.out.println();
                System.out.println("Du måste ange ett val!");
                System.out.println();
                System.out.print("Ange artikelnummer: ");
                continue;
            }

            // Try to parse as integer
            try {
                itemChoice = Integer.parseInt(input);

                if (itemChoice < 1 || itemChoice > cart.size()) {
                    System.out.print("Ogiltigt val! Välj 1-" + cart.size() + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Ogiltigt val! Ange ett nummer: ");
            }
        }

        // Remove item and show confirmation
        CartItem removed = cart.remove(itemChoice - 1);
        helper.clearScreen();
        System.out.println(removed.item.getName() + " borttagen från kundvagnen!");
        helper.pressEnterToContinue();
    }

    /**
     * Clear the entire cart after confirmation.
     * @param cart the shopping cart to clear
     */
    private void clearCart(List<CartItem> cart) {
        System.out.println();
        System.out.print("Är du säker på att du vill rensa hela kundvagnen? (Ja/Nej): ");

        // Get confirmation from user
        if (getYesNoConfirmation()) {
            int itemCount = cart.size();
            cart.clear();
            System.out.println();
            System.out.println("Kundvagnen rensad! (" + itemCount + " artiklar borttagna)");
        } else {
            System.out.println();
            System.out.println("Rensning avbruten.");
        }

        helper.pressEnterToContinue();
    }

    /**
     * Checkout and create rentals for all items in the cart.
     * Shows order summary, applies discount, and processes payment.
     * @param cart the shopping cart to checkout
     * @param member the member checking out
     */
    private void checkout(List<CartItem> cart, Member member) {

        helper.clearScreen();
        helper.printHeader("        BEKRÄFTA BESTÄLLNING");
        System.out.println("Medlem: " + member.getName() + " (" + member.getMembershipLevel() + ")");
        System.out.println();
        helper.printDivider();
        System.out.println();

        // Display order summary
        System.out.println("Du är på väg att hyra ut följande artiklar:");
        System.out.println();

        double totalBeforeDiscount = 0;

        // Display each item in order
        for (int i = 0; i < cart.size(); i++) {
            CartItem cartItem = cart.get(i);
            System.out.print(cartItem.item.getId() + " - " + cartItem.item.getName());
            System.out.print(" (" + getItemTypeDescription(cartItem.item) + ")");
            System.out.print(" - " + cartItem.duration + " ");
            System.out.print(cartItem.period == RentalPeriod.HOURLY ? (cartItem.duration == 1 ? "timme" : "timmar") : (cartItem.duration == 1 ? "dag" : "dagar"));
            System.out.printf(" (%.2f kr)%n", cartItem.price);
            totalBeforeDiscount += cartItem.price;
        }

        System.out.println();

        // Calculate discount and final price
        double discount = getDiscount(member);
        double discountAmount = totalBeforeDiscount * discount;
        double totalAfterDiscount = totalBeforeDiscount - discountAmount;

        // Display price breakdown
        helper.printDivider();
        System.out.println();
        System.out.printf("Summa: %.2f kr%n", totalBeforeDiscount);

        // Only show discount if member has a discount
        if (discount > 0) {
            System.out.printf("Din rabatt (%d%%): -%.2f kr%n", (int)(discount * 100), discountAmount);
        }

        System.out.println();
        System.out.printf("ATT BETALA: %.2f kr%n", totalAfterDiscount);
        System.out.println();
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        System.out.println();
        System.out.print("Vill du genomföra beställningen? (Ja/Nej): ");

        // Get final confirmation
        if (!getYesNoConfirmation()) {
            helper.clearScreen();
            System.out.println("Beställning avbruten.");
            helper.pressEnterToContinue();
            return;
        }

        // User confirmed, create actual Rental objects
        List<Rental> createdRentals = new ArrayList<>();

        // Create rental for each cart item
        for (CartItem cartItem : cart) {

            // Call rental service to create rental
            Rental rental = system.getRentalService().rentItem(member.getId(), cartItem.item.getId(), cartItem.duration, cartItem.period);

            // Check if rentals were created successfully
            if (rental != null) {
                createdRentals.add(rental);
            }
        }

        // Check if any rentals were successfully created
        if (!createdRentals.isEmpty()) {

            // Add total revenue (after discount)
            double revenue = totalAfterDiscount;
            totalRevenue += revenue;

            // Display receipt
            displayReceipt(createdRentals, cart, member, totalBeforeDiscount);
        } else {
            System.out.println();
            System.out.println("Något gick fel vid skapandet av uthyrningar.");
            helper.pressEnterToContinue();
        }
    }

    /**
     * Display receipt after checkout.
     * Shows all rented items with expected return dates and final pricing.
     * @param rentals the created rentals
     * @param cart the shopping cart (for duration info)
     * @param member the member
     * @param totalBeforeDiscount total price before discount
     */
    private void displayReceipt(List<Rental> rentals, List<CartItem> cart, Member member, double totalBeforeDiscount) {

        helper.clearScreen();
        helper.printHeader("         ORDERBEKRÄFTELSE");
        System.out.println("Medlem: " + member.getName() + " (" + member.getMembershipLevel() + ")");
        System.out.println();
        helper.printDivider();
        System.out.println();

        // Date and time formatter for display
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm");

        // Display each rented item with return date
        for (int i = 0; i < cart.size(); i++) {

            CartItem cartItem = cart.get(i);
            System.out.println("Artikel " + (i + 1) + ": " + cartItem.item.getId() + " - " + cartItem.item.getName() + " (" + getItemTypeDescription(cartItem.item) + ")");

            LocalDateTime returnDateTime;

            // Calculate expected return date and time based on period
            if (cartItem.period == RentalPeriod.HOURLY) {

                // Add hours to current time
                returnDateTime = LocalDateTime.now().plusHours(cartItem.duration);

                // Get the hour when item should be returned
                int returnHour = returnDateTime.getHour();

                // Adjust if return falls outside business hours (8-20)
                if (returnHour >= 20 || returnHour < 8) {

                    int hoursOutside;

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

                    if (roundedMinutes == 60) {
                        returnDateTime = returnDateTime.plusHours(1).withMinute(0);
                    } else {
                        returnDateTime = returnDateTime.withMinute(roundedMinutes);
                    }
                }

                // Display with duration and the calculated return time
                System.out.println("Längd: " + cartItem.duration + " " + (cartItem.duration == 1 ? "timme" : "timmar") + " (Retur: " + returnDateTime.format(formatter) + ")");

            } else {

                // Daily rental - return at 20:00 on final day
                returnDateTime = LocalDateTime.now().plusDays(cartItem.duration).withHour(20).withMinute(0);
                System.out.println("Längd: " + cartItem.duration + " " + (cartItem.duration == 1 ? "dag" : "dagar") + " (Retur: " + returnDateTime.format(formatter) + ")");
            }

            System.out.printf("Pris: %.2f kr%n", cartItem.price);
            System.out.println();
        }

        // Calculate and display discount
        double discount = getDiscount(member);
        double discountAmount = totalBeforeDiscount * discount;
        double totalAfterDiscount = totalBeforeDiscount - discountAmount;

        // Display price breakdown
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
     * Get discount multiplier based on membership level.
     * @param member the member
     * @return discount as decimal (0.0 = 0%, 0.20 = 20%, 0.30 = 30%)
     */
    private double getDiscount(Member member) {

        // Return discount based on the membership level
        return switch (member.getMembershipLevel()) {
            case STANDARD -> 0.0;
            case STUDENT -> 0.20;
            case PREMIUM -> 0.30;
        };
    }

    /**
     * Menu option 2: Return an item.
     * Main entry point for return flow with single/multiple/all options.
     */
    public void returnItem() {
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

        // Display return options
        System.out.println("[1] Returnera enskild artikel");
        System.out.println("[2] Returnera flera artiklar");
        System.out.println("[3] Returnera allt");
        System.out.println();
        System.out.println("[0] Avbryt");
        System.out.println();
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        System.out.println();
        System.out.print("Välj alternativ: ");

        // Initialize a choice variable for the validation loop
        int choice = -1;

        // Read user choice with validation
        while (choice < 0 || choice > 3) {

            String input = scanner.nextLine().trim();

            // Check if input is empty
            if (input.isEmpty()) {
                System.out.println();
                System.out.println("Du måste ange ett val!");
                System.out.println();
                System.out.print("Välj alternativ: ");
                continue;
            }

            // Try to parse as integer
            try {
                choice = Integer.parseInt(input);
                if (choice < 0 || choice > 3) {
                    System.out.print("Ogiltigt val! Välj 0-3: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Ogiltigt val! Ange ett nummer: ");
            }
        }

        // Handle user's choice
        switch (choice) {
            case 0 -> {
                // Cancel and return to main menu
                return;
            }
            case 1 -> returnSingleItem(activeRentals);
            case 2 -> returnMultipleItems(activeRentals);
            case 3 -> returnAllItems(activeRentals);
        }
    }

    /**
     * Return a single item.
     * Shows a list of active rentals and processes selected return.
     * @param activeRentals list of active rentals to choose from
     */
    private void returnSingleItem(List<Rental> activeRentals) {

        helper.clearScreen();
        helper.printHeader("    RETURNERA ENSKILD ARTIKEL");
        displayActiveRentals(activeRentals);
        System.out.println();
        System.out.println("[0] Avbryt");
        System.out.println();
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        System.out.print("Välj artikel: ");

        // Initialize a choice variable for the validation loop
        int choice = -1;

        // Read choice with validation
        while (choice < 0 || choice > activeRentals.size()) {

            String input = scanner.nextLine().trim();

            // Check if input is empty
            if (input.isEmpty()) {
                System.out.println();
                System.out.println("Du måste ange ett val!");
                System.out.println();
                System.out.print("Välj artikel: ");
                continue;
            }

            // Try to parse as integer
            try {
                choice = Integer.parseInt(input);
                if (choice < 0 || choice > activeRentals.size()) {
                    System.out.print("Ogiltigt val! Välj 0-" + activeRentals.size() + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Ogiltigt val! Ange ett nummer: ");
            }
        }

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
     * Allows comma separated selection and bulk return processing.
     * @param activeRentals list of active rentals to choose from
     */
    private void returnMultipleItems(List<Rental> activeRentals) {

        // Display list of active rentals
        helper.clearScreen();
        helper.printHeader("    RETURNERA FLERA ARTIKLAR");
        displayActiveRentals(activeRentals);
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        System.out.println();
        System.out.println("Ange artikelnummer separerade med komma (ex: 1,3,5)");
        System.out.println();
        System.out.print("Artiklar: ");

        String input = scanner.nextLine().trim();

        // Validate user provided input
        if (input.isEmpty()) {
            System.out.println();
            System.out.println("Du måste ange minst en artikel!");
            helper.pressEnterToContinue();
            return;
        }

        // Parse comma separated selections
        String[] selections = input.split(",");

        List<Rental> toReturn = new ArrayList<>();

        // Loop through and validate each selection
        for (String sel : selections) {
            try {

                // Try to parse as integer
                int num = Integer.parseInt(sel.trim());

                // Check if number is within valid range
                if (num > 0 && num <= activeRentals.size()) {
                    toReturn.add(activeRentals.get(num - 1));
                }
            } catch (NumberFormatException e) {
                // Skip invalid selections silently
            }
        }

        // Check if any valid selections were made
        if (toReturn.isEmpty()) {
            System.out.println();
            System.out.println("Inga giltiga val gjordes.");
            helper.pressEnterToContinue();
            return;
        }

        // Use bulk return for multiple items
        processReturnBulk(toReturn);
    }

    /**
     * Return all items.
     * Confirms action and processes all active rentals at once.
     * @param activeRentals list of all active rentals
     */
    private void returnAllItems(List<Rental> activeRentals) {

        helper.clearScreen();
        helper.printHeader("       RETURNERA ALLT");
        System.out.println("Returnera " + activeRentals.size() + " artiklar?");
        System.out.println();
        System.out.print("Bekräfta (Ja/Nej): ");

        // Get confirmation before returning everything
        if (!getYesNoConfirmation()) {
            return;
        }

        // Use bulk return for all items
        processReturnBulk(activeRentals);
    }

    /**
     * Process a single return and calculate late fees if applicable.
     * Displays return summary with late fee calculation.
     * @param rental the rental to return
     */
    private void processReturn(Rental rental) {

        // Get item and member from rental
        Item item = system.getInventory().getItem(rental.getItemId());
        Member member = system.getMemberRegistry().getMember(rental.getMemberId());

        // Get original cost and member discount
        double originalCost = rental.getTotalCost();
        double discount = getDiscount(member);

        // Process the return via service
        boolean returned = system.getRentalService().returnItem(rental.getRentalId());

        if (returned) {
            helper.clearScreen();
            helper.printHeader("        ARTIKEL RETURNERAD");
            System.out.println("Artikel: " + item.getName() + " [" + item.getId() + "] (" + getItemTypeDescription(item) + ")");
            System.out.println("Medlem: " + member.getName());
            System.out.println("Hyrd från: " + rental.getStartDate());

            // Get actual return date (now)
            LocalDateTime actualReturnDate = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm");

            System.out.println("Returnerad: " + actualReturnDate.format(dateTimeFormatter));

            helper.printDivider();

            System.out.printf("Betalt pris: %.2f kr%n", originalCost);

            // Calculate days rented (from start to now)
            LocalDateTime rentalStartDateTime = rental.getStartDate().atStartOfDay();
            long daysRented = java.time.temporal.ChronoUnit.DAYS.between(rentalStartDateTime.toLocalDate(), actualReturnDate.toLocalDate());

            // Estimate expected rental period based on paid amount
            double pricePerDay = item.getPricePerDay() * (1 - discount);
            long expectedDays = Math.round(originalCost / pricePerDay);

            // Calculate late fee if overdue
            if (daysRented > expectedDays) {

                long overdueDays = daysRented - expectedDays;
                double lateFee = overdueDays * pricePerDay;

                System.out.println();
                System.out.println("FÖRSENAD RETUR");
                System.out.println("Förväntad retur: Efter " + expectedDays + " dagar");
                System.out.println("Faktisk retur: Efter " + daysRented + " dagar");
                System.out.println("Försenad: " + overdueDays + " dagar");
                System.out.printf("Senavgift: %.2f kr%n", lateFee);
                System.out.println();
                System.out.printf("TOTALT ATT BETALA: %.2f kr%n", lateFee);

                // Add late fee to revenue
                totalRevenue += lateFee;
            }

            System.out.println();
            System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
            helper.pressEnterToContinue();

        } else {
            System.out.println();
            System.out.println("Returneringen misslyckades.");
            helper.pressEnterToContinue();
        }
    }

    /**
     * Process multiple returns and show summary with total late fees.
     * @param rentals list of rentals to return
     */
    private void processReturnBulk(List<Rental> rentals) {
        helper.clearScreen();
        helper.printHeader("    RETURSAMMANFATTNING");

        // Lists to track results
        List<String> returnedItems = new ArrayList<>();
        double totalLateFees = 0.0;
        int successCount = 0;

        // Process each rental
        for (Rental rental : rentals) {
            Item item = system.getInventory().getItem(rental.getItemId());
            Member member = system.getMemberRegistry().getMember(rental.getMemberId());

            double originalCost = rental.getTotalCost();
            double discount = getDiscount(member);

            // Process return via service
            boolean returned = system.getRentalService().returnItem(rental.getRentalId());

            if (returned) {
                successCount++;

                // Calculate late fee if applicable
                LocalDateTime actualReturnDate = LocalDateTime.now();
                LocalDateTime rentalStartDateTime = rental.getStartDate().atStartOfDay();

                // Calculate actual days rented
                long daysRented = java.time.temporal.ChronoUnit.DAYS.between(rentalStartDateTime.toLocalDate(), actualReturnDate.toLocalDate());

                // Calculate expected days based on paid amount
                double pricePerDay = item.getPricePerDay() * (1 - discount);
                long expectedDays = Math.round(originalCost / pricePerDay);

                // Build item info string
                String itemInfo = item.getName() + " [" + item.getId() + "] (" + getItemTypeDescription(item) + ")";

                // Add late fee info if overdue
                if (daysRented > expectedDays) {
                    long overdueDays = daysRented - expectedDays;
                    double lateFee = overdueDays * pricePerDay;
                    totalLateFees += lateFee;
                    itemInfo += String.format("%n    Försenad %d dagar - Avgift: %.2f kr", overdueDays, lateFee);
                }

                // Add item info to returned items list
                returnedItems.add(itemInfo);
            }
        }

        // Show summary of all returns
        System.out.println("RETURNERADE ARTIKLAR (" + successCount + " st):");
        System.out.println();

        // Display each returned item
        for (int i = 0; i < returnedItems.size(); i++) {
            System.out.println(returnedItems.get(i));
        }

        System.out.println();
        helper.printDivider();

        // Show total late fees or success message
        if (totalLateFees > 0) {
            System.out.println();
            System.out.printf("TOTALA FÖRSENINGSAVGIFTER: %.2f kr%n", totalLateFees);

            // Add to total revenue
            totalRevenue += totalLateFees;
        } else {
            System.out.println();
            System.out.println("Alla artiklar returnerade i tid!");
        }

        System.out.println();
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        helper.pressEnterToContinue();
    }

    /**
     * Display list of active rentals with basic info.
     * @param activeRentals list of active rentals to display
     */
    private void displayActiveRentals(List<Rental> activeRentals) {
        System.out.println("Aktiva uthyrningar:");
        System.out.println();

        // Display each active rental with index numbers
        for (int i = 0; i < activeRentals.size(); i++) {

            Rental rental = activeRentals.get(i);

            // Get item and member for this rental
            Item item = system.getInventory().getItem(rental.getItemId());
            Member member = system.getMemberRegistry().getMember(rental.getMemberId());

            // Display as numbered list
            System.out.println("[" + (i + 1) + "] " + item.getName());
            System.out.println("Medlem: " + member.getName());
            System.out.println("Hyrd: " + rental.getStartDate());
            System.out.printf("Pris: %.2f kr%n", rental.getTotalCost());
            System.out.println();
        }
    }

    /**
     * Menu option 3: View rentals.
     * Shows all rentals separated into active and completed.
     */
    public void viewMyRentals() {
        helper.clearScreen();
        helper.printHeader("       ALLA UTHYRNINGAR");

        // Get all rentals from service, both active and completed
        List<Rental> allRentals = system.getRentalService().getAllRentals();

        // Check if any rentals exist
        if (allRentals.isEmpty()) {
            System.out.println("Inga aktiva uthyrningar finns!");
            helper.pressEnterToContinue();
            return;
        }

        // Create separate lists for active and completed rentals
        List<Rental> activeRentals = new ArrayList<>();
        List<Rental> completedRentals = new ArrayList<>();

        // Separate rentals into active and completed
        for (Rental rental : allRentals) {

            // Active if endDate is null, otherwise completed
            if (rental.getEndDate() == null) {
                activeRentals.add(rental);
            } else {
                completedRentals.add(rental);
            }
        }

        // Display active rentals section
        helper.printDivider();
        System.out.println("AKTIVA UTHYRNINGAR (" + activeRentals.size() + " st)");
        helper.printDivider();

        if (activeRentals.isEmpty()) {
            System.out.println("Inga aktiva uthyrningar");
        } else {

            // Display each active rental with details
            for (Rental rental : activeRentals) {

                Item item = system.getInventory().getItem(rental.getItemId());
                Member member = system.getMemberRegistry().getMember(rental.getMemberId());

                System.out.println();
                System.out.println("Artikel: " + item.getId() + " - " + item.getName() + " (" + getItemTypeDescription(item) + ")");
                System.out.println("Medlem: " + member.getName() + " (" + member.getMembershipLevel() + ")");
                System.out.println("Hyrd från: " + rental.getStartDate());
                System.out.printf("Pris: %.2f kr%n", rental.getTotalCost());
            }
        }

        System.out.println();
        System.out.println();

        // Display completed rentals section
        helper.printDivider();
        System.out.println("AVSLUTADE UTHYRNINGAR (" + completedRentals.size() + " st)");
        helper.printDivider();

        if (completedRentals.isEmpty()) {
            System.out.println();
            System.out.println("Inga avslutade uthyrningar");
        } else {

            // Display each completed rental with details
            for (Rental rental : completedRentals) {

                Item item = system.getInventory().getItem(rental.getItemId());
                Member member = system.getMemberRegistry().getMember(rental.getMemberId());

                System.out.println();
                System.out.println("Artikel: " + item.getId() + " - " + item.getName() + " (" + getItemTypeDescription(item) + ")");
                System.out.println("Medlem: " + member.getName() + " (" + member.getMembershipLevel() + ")");
                System.out.println("Period: " + rental.getStartDate() + " → " + rental.getEndDate());
                System.out.printf("Betalt: %.2f kr%n", rental.getTotalCost());
            }
        }

        System.out.println();
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        helper.pressEnterToContinue();
    }

    /**
     * Show the total revenue (cashier view).
     * Displays statistics and total earnings including late fees.
     */
    public void showRevenue() {
        helper.clearScreen();
        helper.printHeader("              KASSAVY");

        // Get all rentals for statistics
        List<Rental> allRentals = system.getRentalService().getAllRentals();
        int totalRentals = allRentals.size();

        // Count active rentals
        long activeCount = allRentals.stream().filter(r -> r.getEndDate() == null).count();

        // Count completed rentals
        long completedCount = allRentals.stream().filter(r -> r.getEndDate() != null).count();

        // Display statistics
        System.out.println("Statistik:");
        System.out.println("Totalt antal uthyrningar: " + totalRentals + " st");
        System.out.println("Aktiva uthyrningar: " + activeCount + " st");
        System.out.println("Avslutade uthyrningar: " + completedCount + " st");
        System.out.println();
        helper.printDivider();
        System.out.println();
        System.out.printf("TOTALA INTÄKTER: %.2f kr%n", totalRevenue);
        System.out.println();
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        helper.pressEnterToContinue();
    }

    /**
     * Helper method to get available items by category.
     * Filters items by type and availability status.
     * @param categoryChoice the category (1=Camping, 2=Fishing, 3=Boats)
     * @return list of available items in the category
     */
    private List<Item> getAvailableItemsByCategory(int categoryChoice) {

        // Get all items from inventory
        List<Item> allItems = system.getInventory().getAllItems();
        List<Item> availableItems = new ArrayList<>();

        // Filter items by category and availability
        for (Item item : allItems) {

            // Skip if item is not available
            if (item.getStatus() != ItemStatus.AVAILABLE) {
                continue;
            }

            // Add item if it matches selected category
            switch (categoryChoice) {
                case 1 -> {
                    if (item instanceof CampingEquipment) {
                        availableItems.add(item);
                    }
                }
                case 2 -> {
                    if (item instanceof FishingEquipment) {
                        availableItems.add(item);
                    }
                }
                case 3 -> {
                    if (item instanceof WaterVehicle) {
                        availableItems.add(item);
                    }
                }
            }
        }

        // Return filtered list
        return availableItems;
    }

    /**
     * Helper method to get the item type description in Swedish.
     * Translates item class to user friendly Swedish.
     * @param item the item to describe
     * @return Swedish description of item type
     */
    private String getItemTypeDescription(Item item) {
        if (item instanceof FishingRod) {
            return "Fiskespö";
        } else if (item instanceof FishingNet) {
            return "Fiskenät";
        } else if (item instanceof FishingBait) {
            return "Bete";
        } else if (item instanceof Tent) {
            return "Tält";
        } else if (item instanceof Backpack) {
            return "Ryggsäck";
        } else if (item instanceof SleepingBag) {
            return "Sovsäck";
        } else if (item instanceof Lantern) {
            return "Lykta";
        } else if (item instanceof TrangiaKitchen) {
            return "Trangiakök";
        } else if (item instanceof Kayak) {
            return "Kajak";
        } else if (item instanceof MotorBoat) {
            return "Motorbåt";
        } else if (item instanceof ElectricBoat) {
            return "Elbåt";
        } else if (item instanceof RowBoat) {
            return "Roddbåt";
        }
        return "Artikel";
    }

    /**
     * Get yes/no confirmation with robust error handling.
     * Loops until user provides valid "Ja" or "Nej" response.
     * @return true if user confirmed (Ja), false if declined (Nej)
     */
    private boolean getYesNoConfirmation() {

        // Loop until valid response
        while (true) {
            String input = scanner.nextLine().trim();

            // Check for "Ja"
            if (input.equalsIgnoreCase("Ja")) {
                return true;

                // Check for "Nej"
            } else if (input.equalsIgnoreCase("Nej")) {
                return false;

                // Invalid response, ask again
            } else {
                System.out.println();
                System.out.println("Ogiltigt svar! Svara 'Ja' eller 'Nej'.");
                System.out.println();
                System.out.print("Svar: ");
            }
        }
    }
}
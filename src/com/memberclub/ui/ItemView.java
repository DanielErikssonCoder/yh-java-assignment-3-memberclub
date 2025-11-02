package com.memberclub.ui;

import com.memberclub.model.Item;
import com.memberclub.model.enums.ItemStatus;
import com.memberclub.model.camping.CampingEquipment;
import com.memberclub.model.fishing.FishingEquipment;
import com.memberclub.model.vehicles.WaterVehicle;
import com.memberclub.system.ClubSystem;
import com.memberclub.ui.components.ItemSelector;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * View class for item-related operations.
 * Handles displaying and managing items in the inventory.
 */
public class ItemView {

    private Scanner scanner;
    private ClubSystem system;
    private UIHelper helper;

    /**
     * Creates a new item view.
     * @param scanner the scanner to use for input
     * @param system the club system
     * @param helper the UI helper for common operations
     */
    public ItemView(Scanner scanner, ClubSystem system, UIHelper helper) {
        this.scanner = scanner;
        this.system = system;
        this.helper = helper;
    }

    /**
     * Menu option 5: View all items
     */
    public void viewAllItems() {

        helper.clearScreen();
        helper.printHeader("       ALLA TILLGÄNGLIGA ARTIKLAR");

        // Get all items from inventory
        List<Item> allItems = system.getInventory().getAllItems();

        // Separate items by category
        List<Item> campingItems = new ArrayList<>();
        List<Item> fishingItems = new ArrayList<>();
        List<Item> vehicleItems = new ArrayList<>();

        // Loop through all items and categorize them
        for (Item item : allItems) {
            if (item instanceof CampingEquipment) {
                campingItems.add(item);
            } else if (item instanceof FishingEquipment) {
                fishingItems.add(item);
            } else if (item instanceof WaterVehicle) {
                vehicleItems.add(item);
            }
        }

        // Display Camping Equipment section
        helper.printDivider();
        System.out.println("CAMPINGUTRUSTNING (" + campingItems.size() + " artiklar)");
        helper.printDivider();
        System.out.println();

        // Loop through and display each camping item
        for (Item item : campingItems) {
            displayItemInfo(item);
        }

        // Display Fishing Equipment section
        helper.printDivider();
        System.out.println("FISKEUTRUSTNING (" + fishingItems.size() + " artiklar)");
        helper.printDivider();
        System.out.println();

        // Loop through and display each fishing item
        for (Item item : fishingItems) {
            displayItemInfo(item);
        }

        // Display Water Vehicles section
        helper.printDivider();
        System.out.println("VATTENFORDON (" + vehicleItems.size() + " artiklar)");
        helper.printDivider();
        System.out.println();

        // Loop through and display each water vehicle
        for (Item item : vehicleItems) {
            displayItemInfo(item);
        }

        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        helper.pressEnterToContinue();
    }

    /**
     * Format item for selection lists with number prefix
     */
    public static String formatItemForList(int number, Item item) {
        return "[" + number + "] " + item.getName() + " [" + item.getId() + "] (" +
                ItemSelector.getItemTypeDescription(item) + ")";
    }

    /**
     * Format item as short summary
     */
    public static String formatItemShort(Item item) {
        return item.getName() + " [" + item.getId() + "] (" +
                ItemSelector.getItemTypeDescription(item) + ")";
    }

    /**
     * Format item for receipts and details with label
     */
    public static String formatItemFull(Item item) {
        return "Artikel: " + item.getName() + " [" + item.getId() + "] (" +
                ItemSelector.getItemTypeDescription(item) + ")";
    }

    /**
     * Helper method to display individual item information
     */
    private void displayItemInfo(Item item) {
        System.out.println(formatItemShort(item));
        System.out.printf("Pris: %.2f kr/timme | %.2f kr/dag%n", item.getPricePerHour(), item.getPricePerDay());
        System.out.println("Status: " + translateStatus(item.getStatus()));
        System.out.println();
    }

    /**
     * Translates ItemStatus enum to Swedish
     */
    private String translateStatus(ItemStatus status) {

        return switch (status) {
            case AVAILABLE -> "TILLGÄNGLIG";
            case RENTED -> "UTHYRD";
            case BROKEN -> "TRASIG";
        };
    }
}
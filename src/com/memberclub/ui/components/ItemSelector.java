package com.memberclub.ui.components;

import com.memberclub.model.*;
import com.memberclub.model.camping.*;
import com.memberclub.model.enums.ItemStatus;
import com.memberclub.model.enums.RentalPeriod;
import com.memberclub.model.fishing.*;
import com.memberclub.model.vehicles.*;
import com.memberclub.system.ClubSystem;
import com.memberclub.ui.ItemView;
import com.memberclub.ui.UIHelper;
import com.memberclub.ui.validation.InputValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles item selection, category browsing, and item display logic.
 * Separates item selection logic from rental processing.
 */
public class ItemSelector {
    private final Scanner scanner;
    private final ClubSystem system;
    private final UIHelper helper;

    /**
     * Creates a new item selector.
     * @param scanner the scanner for user input
     * @param system the club system
     * @param helper the UI helper
     */
    public ItemSelector(Scanner scanner, ClubSystem system, UIHelper helper) {
        this.scanner = scanner;
        this.system = system;
        this.helper = helper;
    }

    /**
     * Let user select rental category.
     * @return category number (1=Camping, 2=Fishing, 3=Boats), or 0 if cancelled
     */
    public int selectCategory() {
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

        // Get and return the chosen category
        return InputValidator.getIntInRange(scanner, 0, 3, "Välj kategori: ");
    }

    /**
     * Let user select an item from a category.
     * @param category the category (1=Camping, 2=Fishing, 3=Boats)
     * @return the selected item, or null if cancelled
     */
    public Item selectItem(int category) {

        // Get all available items in the selected category
        List<Item> availableItems = getAvailableItemsByCategory(category);

        // Check if category has any available items
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
        System.out.println();

        // Get item selection from user
        int itemChoice = InputValidator.getIntInRange(scanner, 0, availableItems.size(), "Välj artikel: ");

        // Check if user cancelled
        if (itemChoice == 0) {
            return null;
        }

        // Return selected item
        return availableItems.get(itemChoice - 1);
    }

    /**
     * Let user select multiple items from a category.
     * @param category the category (1=Camping, 2=Fishing, 3=Boats)
     * @return list of selected items (empty if cancelled)
     */
    public List<Item> selectMultipleItems(int category) {

        // Get all available items in selected category
        List<Item> availableItems = getAvailableItemsByCategory(category);

        // Initialize list to store selected items
        List<Item> selectedItems = new ArrayList<>();

        // Check if category has any available items
        if (availableItems.isEmpty()) {
            System.out.println();
            System.out.println("Inga tillgängliga artiklar i denna kategori.");
            helper.pressEnterToContinue();
            return selectedItems;
        }

        // Display available items and instructions
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

        // Check if input is empty
        if (input.isEmpty()) {
            System.out.println();
            System.out.println("Du måste ange minst en artikel eller 0 för att avbryta!");
            helper.pressEnterToContinue();
            return selectedItems;
        }

        // Check if user cancelled
        if (input.equals("0")) {
            return selectedItems;
        }

        // Parse comma separated selections
        String[] selections = input.split(",");

        // Process each selection
        for (String sel : selections) {

            // Try to parse integer
            try {
                int itemNum = Integer.parseInt(sel.trim());

                // Validate item number and add to selection
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
        }
        return selectedItems;
    }

    /**
     * Let the user select rental period (hourly or daily).
     * @return RentalPeriod.HOURLY or RentalPeriod.DAILY
     */
    public RentalPeriod selectPeriod() {
        System.out.println();
        System.out.println("[1] Hyra per timme");
        System.out.println("[2] Hyra per dag");
        System.out.println();

        // Get period choice from user
        int choice = InputValidator.getIntInRange(scanner, 1, 2, "Periodtyp: ");

        // Return corresponding period type
        return (choice == 1) ? RentalPeriod.HOURLY : RentalPeriod.DAILY;
    }

    /**
     * Let user select rental duration.
     * @param period the rental period (HOURLY or DAILY) to display correct prompt
     * @return duration (1-365)
     */
    public int selectDuration(RentalPeriod period) {
        System.out.println();

        // Create appropriate prompt based on period type
        String prompt = "Antal " + (period == RentalPeriod.HOURLY ? "timmar" : "dagar") + ": ";

        // Get and return duration from user
        return InputValidator.getIntInRange(scanner, 1, 365, prompt);
    }

    /**
     * Gets available items filtered by category.
     * @param categoryChoice the category (1=Camping, 2=Fishing, 3=Boats)
     * @return list of available items in the category
     */
    public List<Item> getAvailableItemsByCategory(int categoryChoice) {

        // Get all items from inventory
        List<Item> allItems = system.getInventory().getAllItems();

        // Initialize list for filtered items
        List<Item> availableItems = new ArrayList<>();

        // Filter items by availability and category
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
        return availableItems;
    }

    /**
     * Display item list with full details including type-specific attributes.
     * @param items list of items to display
     */
    public void displayItemList(List<Item> items) {

        // Loop through and display each item
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);

            // Display item number and name
            System.out.println(ItemView.formatItemForList(i + 1, item));

            // Display type-specific details
            displayItemDetails(item);

            // Display pricing
            System.out.printf("Pris: %.2f kr/timme | %.2f kr/dag%n", item.getPricePerHour(), item.getPricePerDay());
            System.out.println();
        }
    }

    /**
     * Display type-specific details for an item.
     * @param item the item to display details for
     */
    private void displayItemDetails(Item item) {

        // Water vehicles
        if (item instanceof WaterVehicle) {
            displayWaterVehicleDetails((WaterVehicle) item);
        }

        // Camping equipment
        else if (item instanceof CampingEquipment) {
            displayCampingDetails((CampingEquipment) item);
        }

        // Fishing equipment
        else if (item instanceof FishingEquipment) {
            displayFishingDetails((FishingEquipment) item);
        }
    }

    /**
     * Display water vehicle specific details.
     * @param vehicle the water vehicle
     */
    private void displayWaterVehicleDetails(WaterVehicle vehicle) {

        // Display kayak details
        if (vehicle instanceof Kayak) {
            Kayak kayak = (Kayak) vehicle;
            System.out.println("Kapacitet: " + kayak.getCapacity() + " personer | Längd: " + kayak.getLength() + "m");

            // Display motor boat details
        } else if (vehicle instanceof MotorBoat) {
            MotorBoat boat = (MotorBoat) vehicle;
            System.out.println("Kapacitet: " + boat.getCapacity() + " personer | Längd: " + boat.getLength() + "m");

            // Display electric boat details
        } else if (vehicle instanceof ElectricBoat) {
            ElectricBoat boat = (ElectricBoat) vehicle;
            System.out.println("Kapacitet: " + boat.getCapacity() + " personer | Batteri: " + boat.getBatteryCapacity() + " kWh");

            // Display row boat details
        } else if (vehicle instanceof RowBoat) {
            RowBoat boat = (RowBoat) vehicle;
            System.out.println("Kapacitet: " + boat.getCapacity() + " personer | Längd: " + boat.getLength() + "m");
        }
    }

    /**
     * Display camping equipment specific details.
     * @param camping the camping equipment
     */
    private void displayCampingDetails(CampingEquipment camping) {

        // Display tent details
        if (camping instanceof Tent) {
            Tent tent = (Tent) camping;
            System.out.println("Kapacitet: " + tent.getCapacity() + " personer | Säsong: " + tent.getSeasonRating());

            // Display backpack details
        } else if (camping instanceof Backpack) {
            Backpack backpack = (Backpack) camping;
            System.out.println("Volym: " + backpack.getVolume() + "L | Tillverkare: " + backpack.getBrand());

            // Display sleeping bag details
        } else if (camping instanceof SleepingBag) {
            SleepingBag bag = (SleepingBag) camping;
            System.out.println("Temperatur: " + bag.getTemperatureRating() + "°C | Säsong: " + bag.getSeasonRating());

            // Display lantern details
        } else if (camping instanceof Lantern) {
            Lantern lantern = (Lantern) camping;
            System.out.println("Ljusstyrka: " + lantern.getBrightness() + " lumens | Ström: " + lantern.getPowerSource());

            // Display trangia kitchen details
        } else if (camping instanceof TrangiaKitchen) {
            TrangiaKitchen trangia = (TrangiaKitchen) camping;
            System.out.println("Brännare: " + trangia.getBurners() + " st | Bränsle: " + trangia.getFuelType());
        }
    }

    /**
     * Display fishing equipment specific details.
     * @param fishing the fishing equipment
     */
    private void displayFishingDetails(FishingEquipment fishing) {

        // Display fishing rod details
        if (fishing instanceof FishingRod) {
            FishingRod rod = (FishingRod) fishing;
            System.out.println("Längd: " + rod.getRodLength() + "m | Tillverkare: " + rod.getBrand());

            // Display fishing net details
        } else if (fishing instanceof FishingNet) {
            FishingNet net = (FishingNet) fishing;
            System.out.println("Maskstorlek: " + net.getMeshSize() + "mm | Tillverkare: " + net.getBrand());

            // Display fishing bait details
        } else if (fishing instanceof FishingBait) {
            FishingBait bait = (FishingBait) fishing;
            System.out.println("Antal: " + bait.getQuantity() + " st | Tillverkare: " + bait.getBrand());
        }
    }

    /**
     * Gets a human-readable description of an item type in Swedish.
     * @param item the item to describe
     * @return Swedish description of item type
     */
    public static String getItemTypeDescription(Item item) {

        // Check item type and return description in Swedish
        if (item instanceof FishingRod) {
            return "Fiskespö";
        }
        else if (item instanceof FishingNet) {
            return "Fiskenät";
        }
        else if (item instanceof FishingBait) {
            return "Bete";
        }
        else if (item instanceof Tent) {
            return "Tält";
        }
        else if (item instanceof Backpack) {
            return "Ryggsäck";
        }
        else if (item instanceof SleepingBag) {
            return "Sovsäck";
        }
        else if (item instanceof Lantern) {
            return "Lykta";
        }
        else if (item instanceof TrangiaKitchen) {
            return "Trangiakök";
        }
        else if (item instanceof Kayak) {
            return "Kajak";
        }
        else if (item instanceof MotorBoat) {
            return "Motorbåt";
        }
        else if (item instanceof ElectricBoat) {
            return "Elbåt";
        }
        else if (item instanceof RowBoat) {
            return "Roddbåt";
        }
        return "Artikel";
    }
}
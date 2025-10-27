package com.memberclub.model.fishing;

import com.memberclub.model.ItemType;

/**
 * Concrete class representing fishing bait.
 * Extends FishingEquipment with specific attributes for fishing bait.
 */
public class FishingBait extends FishingEquipment {

    private BaitType baitType;
    private int quantity;

    /**
     * Constructor that creates new fishing bait with given specifications.
     * @param id unique identifier
     * @param name name of the bait
     * @param pricePerDay rental price per day
     * @param pricePerHour rental price per hour
     * @param material material type
     * @param weight weight in kilograms
     * @param brand brand name
     * @param baitType type of bait
     * @param quantity number of pieces in package
     */
    public FishingBait(String id, String name, double pricePerDay, double pricePerHour, String material, double weight, String brand, BaitType baitType, int quantity) {
        super(id, name, pricePerDay, pricePerHour, material, weight, brand);
        this.baitType = baitType;
        this.quantity = quantity;
    }

    // Getters
    public BaitType getBaitType() {
        return baitType;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters
    public void setBaitType(BaitType baitType) {
        this.baitType = baitType;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Returns the specific item type for this fishing bait
    @Override
    public ItemType getItemType() {
        return ItemType.FISHING_BAIT;
    }

    // Returns string representation for easy printing or debugging
    @Override
    public String toString() {
        return "FishingBait{" + "typ=" + baitType + ", antal=" + quantity + "st} " + super.toString();
    }


}

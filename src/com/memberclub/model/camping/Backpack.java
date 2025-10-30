package com.memberclub.model.camping;

import com.memberclub.model.enums.BackpackType;
import com.memberclub.model.enums.Color;
import com.memberclub.model.enums.ItemType;
import com.memberclub.model.enums.Material;

/**
 * Concrete class representing a backpack.
 * Extends CampingEquipment with specific attributes for backpacks.
 */
public class Backpack extends CampingEquipment {

    private int volume;
    private BackpackType backpackType;

    /**
     * Constructor that creates a new backpack with given specifications.
     * @param id unique identifier
     * @param name name of the backpack
     * @param pricePerDay rental price per day
     * @param pricePerHour rental price per hour
     * @param year model year
     * @param color item color
     * @param material material type
     * @param weight weight in kilograms
     * @param brand brand name
     * @param volume volume in liters
     * @param backpackType type of backpack
     */
    public Backpack(String id, String name, double pricePerDay, double pricePerHour, int year, Color color, Material material,
                    double weight, String brand, int volume, BackpackType backpackType) {

        // Call parent constructors (CampingEquipment, which calls Item)
        super(id, name, pricePerDay, pricePerHour, year, color, material, weight, brand);

        // Initialize own fields
        this.volume = volume;
        this.backpackType = backpackType;
    }

    // Getters
    public int getVolume() {
        return volume;
    }

    public BackpackType getBackpackType() {
        return backpackType;
    }

    // Setters
    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setBackpackType(BackpackType backpackType) {
        this.backpackType = backpackType;
    }

    // Returns the specific item type for this backpack
    @Override
    public ItemType getItemType() {
        return ItemType.BACKPACK;
    }

    // Returns string representation for easy printing or debugging
    @Override
    public String toString() {
        return "Backpack{" + "volym=" + volume + ", typ=" + backpackType + "} " + super.toString();
    }
}

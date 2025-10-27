package com.memberclub.model.fishing;

import com.memberclub.model.ItemType;
import com.memberclub.model.Color;

/**
 * Concrete class that represents a fishing rod.
 * Extends FishingEquipment with specific attributes for rods.
 */
public class FishingRod extends FishingEquipment {

    private double rodLength;
    private RodType rodType;

    /**
     * Constructor that creates a new fishing rod with given specifications.
     * @param id unique identifier
     * @param name name of the rod
     * @param pricePerDay rental price per day
     * @param pricePerHour rental price per hour
     * @param year model year
     * @param color item color
     * @param material material type
     * @param weight weight in kilograms
     * @param brand brand name
     * @param rodLength length in meters
     * @param rodType type of fishing rod
     */
    public FishingRod(String id, String name, double pricePerDay, double pricePerHour,  int year, Color color,
                      String material, double weight, String brand, double rodLength, RodType rodType) {
        super(id, name, pricePerDay, pricePerHour, year, color, material, weight, brand);
        this.rodLength = rodLength;
        this.rodType = rodType;
    }

    // Getters
    public double getRodLength() {
        return rodLength;
    }

    public RodType getRodType() {
        return rodType;
    }

    // Setters
    public void setRodLength(double rodLength) {
        this.rodLength = rodLength;
    }

    public void setRodType(RodType rodType) {
        this.rodType = rodType;
    }

    // Returns the specific item type for this fishing rod
    @Override
    public ItemType getItemType() {
        return ItemType.FISHING_ROD;
    }

    // Returns string representation for easy printing or debugging
    @Override
    public String toString() {
        return "FishingRod{" + "längd=" + rodLength + "m, typ=" + rodType + "} " + super.toString();
    }
}

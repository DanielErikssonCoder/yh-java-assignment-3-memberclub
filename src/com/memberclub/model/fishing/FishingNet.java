package com.memberclub.model.fishing;

import com.memberclub.model.ItemType;
import com.memberclub.model.Color;
import com.memberclub.model.Material;

/**
 * Concrete class that represents a fishing net.
 * Extends FishingEquipment with specific attributes for nets.
 */
public class FishingNet extends FishingEquipment {

    private NetSize netSize;
    private double meshSize;

    /**
     * Constructor that creates a new fishing net with given specifications.
     * @param id unique identifier
     * @param name name of the net
     * @param pricePerDay rental price per day
     * @param pricePerHour rental price per hour
     * @param year model year
     * @param color item color
     * @param material material type
     * @param weight weight in kilograms
     * @param brand brand name
     * @param netSize size of the net (SMALL, MEDIUM, LARGE)
     * @param meshSize mesh size in millimeters
     */
    public FishingNet(String id, String name, double pricePerDay, double pricePerHour, int year, Color color,
                      Material material, double weight, String brand, NetSize netSize, double meshSize) {
        super(id, name, pricePerDay, pricePerHour, year, color, material, weight, brand);
        this.netSize = netSize;
        this.meshSize = meshSize;
    }

    // Getters
    public NetSize getNetSize() {
        return netSize;
    }

    public double getMeshSize() {
        return meshSize;
    }

    // Setters
    public void setNetSize(NetSize netSize) {
        this.netSize = netSize;
    }

    public void setMeshSize(double meshSize) {
        this.meshSize = meshSize;
    }

    // Returns the specific item type for this fishing net
    @Override
    public ItemType getItemType() {
        return ItemType.FISHING_NET;
    }

    // Returns string representation for easy printing or debugging
    @Override
    public String toString() {
        return "FishingNet{" + "storlek=" + netSize + ", maskstorlek=" + meshSize + "mm} " + super.toString();
    }
}

package com.memberclub.model.camping;

import com.memberclub.model.enums.Color;
import com.memberclub.model.enums.FuelType;
import com.memberclub.model.enums.ItemType;
import com.memberclub.model.enums.Material;

/**
 * Concrete class representing a Trangia camping kitchen.
 * Extends CampingEquipment with specific attributes for camping stoves.
 */
public class TrangiaKitchen extends CampingEquipment {

    private int burners;
    private FuelType fuelType;

    /**
     * Constructor that creates a new Trangia kitchen with given specifications.
     * @param id unique identifier
     * @param name name of the kitchen
     * @param pricePerDay rental price per day
     * @param pricePerHour rental price per hour
     * @param year model year
     * @param color item color
     * @param material material type
     * @param weight weight in kilograms
     * @param brand brand name
     * @param burners number of burners
     * @param fuelType fuel type
     */
    public TrangiaKitchen(String id, String name, double pricePerDay, double pricePerHour, int year, Color color, Material material,
                          double weight, String brand, int burners, FuelType fuelType) {

        // Call parent constructors (CampingEquipment, which calls Item)
        super(id, name, pricePerDay, pricePerHour, year, color, material, weight, brand);

        // Initialize own fields
        this.burners = burners;
        this.fuelType = fuelType;
    }

    // Getters
    public int getBurners() {
        return burners;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    // Setters
    public void setBurners(int burners) {
        this.burners = burners;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    // Returns the specific item type for this trangia kitchen
    @Override
    public ItemType getItemType() {
        return ItemType.TRANGIA_KITCHEN;
    }

    // Returns string representation for easy printing or debugging
    @Override
    public String toString() {
        return "TrangiaKitchen{" + "brännare=" + burners + ", bränsletyp=" + fuelType + "} " + super.toString();
    }
}

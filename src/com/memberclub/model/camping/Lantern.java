package com.memberclub.model.camping;

import com.memberclub.model.enums.Color;
import com.memberclub.model.enums.ItemType;
import com.memberclub.model.enums.Material;
import com.memberclub.model.enums.PowerSource;

/**
 * Concrete class representing a camping lantern.
 * Extends CampingEquipment with specific attributes for lanterns.
 */
public class Lantern extends CampingEquipment {

    private int brightness;
    private PowerSource powerSource;

    /**
     * Constructor that creates a new lantern with given specifications.
     * @param id unique identifier
     * @param name name of the lantern
     * @param pricePerDay rental price per day
     * @param pricePerHour rental price per hour
     * @param year model year
     * @param color item color
     * @param material material type
     * @param weight weight in kilograms
     * @param brand brand name
     * @param brightness brightness in lumens
     * @param powerSource power source type
     */
    public Lantern(String id, String name, double pricePerDay, double pricePerHour, int year, Color color, Material material,
                   double weight, String brand, int brightness, PowerSource powerSource) {

        // Call parent constructors (CampingEquipment, which calls Item)
        super(id, name, pricePerDay, pricePerHour, year, color, material, weight, brand);

        // Initialize own fields
        this.brightness = brightness;
        this.powerSource = powerSource;
    }

    // Getters
    public int getBrightness() {
        return brightness;
    }

    public PowerSource getPowerSource() {
        return powerSource;
    }

    // Setters
    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public void setPowerSource(PowerSource powerSource) {
        this.powerSource = powerSource;
    }

    // Returns the specific item type for this lantern
    @Override
    public ItemType getItemType() {
        return ItemType.LANTERN;
    }

    // Returns string representation for easy printing or debugging
    @Override
    public String toString() {
        return "Lantern{" + "ljusstyrka=" + brightness + ", strömkälla=" + powerSource + "} " + super.toString();
    }
}

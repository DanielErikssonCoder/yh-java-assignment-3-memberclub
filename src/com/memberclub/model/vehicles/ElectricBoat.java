package com.memberclub.model.vehicles;

import com.memberclub.model.enums.Color;
import com.memberclub.model.enums.ItemType;
import com.memberclub.model.enums.Material;

/**
 * Concrete class representing an electric boat.
 * Extends Boat with specific attributes for electric-powered boats.
 */
public class ElectricBoat extends Boat {

    private double batteryCapacity;
    private int chargeTime;

    /**
     * Constructor that creates a new electric boat with given specifications.
     * @param id unique identifier
     * @param name name of the boat
     * @param pricePerDay rental price per day
     * @param pricePerHour rental price per hour
     * @param year model year
     * @param color item color
     * @param material material type
     * @param weight weight in kilograms
     * @param brand brand name
     * @param capacity number of persons
     * @param length length in meters
     * @param hasFishFinder whether boat has fish finder equipment
     * @param maxSpeed maximum speed in knots
     * @param batteryCapacity battery capacity in kilowatt hours
     * @param chargeTime charging time in hours
     */
    public ElectricBoat(String id, String name, double pricePerDay, double pricePerHour, int year, Color color,
                        Material material, double weight, String brand, int capacity, double length, boolean hasFishFinder,
                        double maxSpeed, double batteryCapacity, int chargeTime) {

        // Call parent constructors (Boat, which calls WaterVehicle, which calls Item)
        super(id, name, pricePerDay, pricePerHour, year, color, material, weight, brand, capacity, length, hasFishFinder, maxSpeed);

        // Initialize own fields
        this.batteryCapacity = batteryCapacity;
        this.chargeTime = chargeTime;
    }

    // Getters
    public double getBatteryCapacity() {
        return batteryCapacity;
    }

    public int getChargeTime() {
        return chargeTime;
    }

    // Setters
    public void setBatteryCapacity(double batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public void setChargeTime(int chargeTime) {
        this.chargeTime = chargeTime;
    }

    // Returns the specific item type for this electric boat
    @Override
    public ItemType getItemType() {
        return ItemType.ELECTRIC_BOAT;
    }

    // Returns string representation for easy printing or debugging
    @Override
    public String toString() {
        return "ElectricBoat{" + "batterikapacitet=" + batteryCapacity + " kWh, laddningstid=" + chargeTime + " timmar} " + super.toString();
    }
}

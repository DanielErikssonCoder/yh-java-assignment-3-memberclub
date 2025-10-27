package com.memberclub.model.vehicles;

import com.memberclub.model.Color;
import com.memberclub.model.Material;

/**
 * Abstract base class for all boats.
 * Extends WaterVehicle with specific attributes for Boat items.
 */
public abstract class Boat extends WaterVehicle {

    private boolean hasFishFinder;
    private double maxSpeed;

    /**
     * Constructor that creates a new boat with given information.
     * @param id unique identifier
     * @param name name of boat
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
     */
    public Boat(String id, String name, double pricePerDay, double pricePerHour, int year, Color color, Material material,
                double weight, String brand, int capacity, double length, boolean hasFishFinder, double maxSpeed) {

        // Call parent constructors (WaterVehicle, which calls Item)
        super(id, name, pricePerDay, pricePerHour, year, color, material, weight, brand, capacity, length);

        // Initialize own fields
        this.hasFishFinder = hasFishFinder;
        this.maxSpeed = maxSpeed;
    }

    // Getters
    public boolean isHasFishFinder() {
        return hasFishFinder;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    // Setters
    public void setHasFishFinder(boolean hasFishFinder) {
        this.hasFishFinder = hasFishFinder;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    // Returns string representation of boat for easy printing or debugging
    @Override
    public String toString() {
        return "Boat{" + "fiskfinnare=" + (hasFishFinder ? "ja" : "nej") + ", maxhastighet=" + maxSpeed + " knop} " + super.toString();
    }
}

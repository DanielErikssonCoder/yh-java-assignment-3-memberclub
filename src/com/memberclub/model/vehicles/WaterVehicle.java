package com.memberclub.model.vehicles;

import com.memberclub.model.Item;
import com.memberclub.model.enums.Material;
import com.memberclub.model.enums.Color;

/**
 * Abstract base class for all water vehicles.
 * Contains attributes common to all boats and kayaks.
 */
public abstract class WaterVehicle extends Item {

    private Material material;
    private double weight;
    private String brand;
    private int capacity;
    private double length;

    /**
     * Constructor that creates new water vehicle with given information.
     * @param id unique identifier
     * @param name name of vehicle
     * @param pricePerDay rental price per day
     * @param pricePerHour rental price per hour
     * @param year model year
     * @param color item color
     * @param material material type
     * @param weight weight in kilograms
     * @param brand brand name
     * @param capacity number of persons
     * @param length length in meters
     */
    public WaterVehicle(String id, String name, double pricePerDay, double pricePerHour, int year, Color color,
                        Material material, double weight, String brand, int capacity, double length) {

        // Call parent constructor (Item Class)
        super(id, name, pricePerDay, pricePerHour, year, color);

        // Initialize own fields
        this.material = material;
        this.weight = weight;
        this.brand = brand;
        this.capacity = capacity;
        this.length = length;
    }

    // Getters
    public Material getMaterial() {
        return material;
    }

    public double getWeight() {
        return weight;
    }

    public String getBrand() {
        return brand;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getLength() {
        return length;
    }

    // Setters
    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setLength(double length) {
        this.length = length;
    }

    // Returns string representation of water vehicle for easy printing or debugging
    @Override
    public String toString() {
        return "WaterVehicle{" + "material=" + material + ", vikt=" + weight + ", varumärke=" + brand + ", kapacitet="
                + capacity + " personer, längd=" + length + "m} " + super.toString();
    }

}

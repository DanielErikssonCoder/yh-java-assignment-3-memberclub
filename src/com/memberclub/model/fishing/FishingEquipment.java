package com.memberclub.model.fishing;

import com.memberclub.model.Item;

/**
 * Abstract base class for all fishing equipment.
 * Contains attributes common to all fishing items.
 */
public abstract class FishingEquipment extends Item {

    private String material;
    private double weight;
    private String brand;

    /**
     * Constructor that creates new fishing equipment with given information.
     * @param id unique identifier
     * @param name name of equipment
     * @param pricePerDay rental price per day
     * @param pricePerHour rental price per hour
     * @param material material type (carbon, fiberglass, nylon, etc)
     * @param weight weight in kilograms
     * @param brand brand name
     */
    public FishingEquipment(String id, String name, double pricePerDay, double pricePerHour, String material, double weight, String brand) {

        // Call parent constructor (Item Class)
        super(id, name, pricePerDay, pricePerHour);

        // Initialize own fields
        this.material = material;
        this.weight = weight;
        this.brand = brand;
    }

    // Getters
    public String getMaterial() {
        return material;
    }

    public double getWeight() {
        return weight;
    }

    public String getBrand() {
        return brand;
    }

    // Setters
    public void setMaterial(String material) {
        this.material = material;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    // Returns string representation of fishing equipment for easy printing or debugging
    @Override
    public String toString() {
        return "FishingEquipment{" + "material=" + material + ", vikt=" + weight + ", märke=" + brand + "} " + super.toString();
    }
}

package com.memberclub.model.camping;

import com.memberclub.model.Item;
import com.memberclub.model.enums.Color;
import com.memberclub.model.enums.Material;

/**
 * Abstract base class for all camping equipment.
 * Contains attributes common to all camping items.
 */
public abstract class CampingEquipment extends Item {

    private Material material;
    private double weight;
    private String brand;

    /**
     * Constructor that creates new camping equipment with given information.
     * @param id unique identifier
     * @param name name of equipment
     * @param pricePerDay rental price per day
     * @param pricePerHour rental price per hour
     * @param year model year
     * @param color item color
     * @param material material type
     * @param weight weight in kilograms
     * @param brand brand name
     */
    public CampingEquipment(String id, String name, double pricePerDay, double pricePerHour, int year, Color color,
                            Material material, double weight, String brand) {

        // Call parent constructor (Item Class)
        super(id, name, pricePerDay, pricePerHour, year, color);

        // Initialize own fields
        this.material = material;
        this.weight = weight;
        this.brand = brand;
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

    // Returns string representation of camping equipment for easy printing or debugging
    @Override
    public String toString() {
        return "CampingEquipment{" + "material=" + material + ", vikt=" + weight + ", varumärke=" + brand + "} " + super.toString();
    }


}

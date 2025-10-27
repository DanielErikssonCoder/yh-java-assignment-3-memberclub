package com.memberclub.model.vehicles;

import com.memberclub.model.Color;
import com.memberclub.model.ItemType;
import com.memberclub.model.Material;

/**
 * Concrete class representing a kayak.
 * Extends WaterVehicle with specific attributes for kayaks.
 */
public class Kayak extends WaterVehicle {

    private int seats;
    private KayakType kayakType;

    /**
     * Constructor that creates a new kayak with given specifications.
     * @param id unique identifier
     * @param name name of the kayak
     * @param pricePerDay rental price per day
     * @param pricePerHour rental price per hour
     * @param year model year
     * @param color item color
     * @param material material type
     * @param weight weight in kilograms
     * @param brand brand name
     * @param capacity number of persons
     * @param length length in meters
     * @param seats number of seat positions
     * @param kayakType type of kayak
     */
    public Kayak(String id, String name, double pricePerDay, double pricePerHour, int year, Color color, Material material,
                 double weight, String brand, int capacity, double length, int seats, KayakType kayakType) {

        // Call parent constructors (WaterVehicle, which calls Item)
        super(id, name, pricePerDay, pricePerHour, year, color, material, weight, brand, capacity, length);

        // Initialize own fields
        this.seats = seats;
        this.kayakType = kayakType;
    }

    // Getters
    public int getSeats() {
        return seats;
    }

    public KayakType getKayakType() {
        return kayakType;
    }

    // Setters
    public void setSeats(int seats) {
        this.seats = seats;
    }

    public void setKayakType(KayakType kayakType) {
        this.kayakType = kayakType;
    }

    // Returns the specific item type for this kayak
    @Override
    public ItemType getItemType() {
        return ItemType.KAYAK;
    }

    // Returns string representation for easy printing or debugging
    @Override
    public String toString() {
        return "Kayak{" + "säten=" + seats + ", typ=" + kayakType + "} " + super.toString();
    }
}

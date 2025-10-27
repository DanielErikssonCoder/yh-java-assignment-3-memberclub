package com.memberclub.model.camping;

import com.memberclub.model.Color;
import com.memberclub.model.ItemType;
import com.memberclub.model.Material;

/**
 * Concrete class representing a tent.
 * Extends CampingEquipment with specific attributes for tents.
 */
public class Tent extends CampingEquipment {

    private int capacity;
    private SeasonRating seasonRating;

    /**
     * Constructor that creates a new tent with given specifications.
     * @param id unique identifier
     * @param name name of the tent
     * @param pricePerDay rental price per day
     * @param pricePerHour rental price per hour
     * @param year model year
     * @param color item color
     * @param material material type
     * @param weight weight in kilograms
     * @param brand brand name
     * @param capacity number of persons
     * @param seasonRating season suitability
     */
    public Tent(String id, String name, double pricePerDay, double pricePerHour, int year, Color color, Material material,
                double weight, String brand, int capacity, SeasonRating seasonRating) {

        // Call parent constructors (Item Class and CampingEquipment)
        super(id, name, pricePerDay, pricePerHour, year, color, material, weight, brand);

        // Initialize own fields
        this.capacity = capacity;
        this.seasonRating = seasonRating;
    }

    // Getters
    public int getCapacity() {
        return capacity;
    }

    public SeasonRating getSeasonRating() {
        return seasonRating;
    }

    // Setters
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setSeasonRating(SeasonRating seasonRating) {
        this.seasonRating = seasonRating;
    }

    // Returns the specific item type for this tent
    @Override
    public ItemType getItemType() {
        return ItemType.TENT;
    }

    // Returns string representation for easy printing or debugging
    @Override
    public String toString() {
        return "Tent{" + "kapacitet=" + capacity + " personer, säsong=" + seasonRating + "} " + super.toString();
    }
}

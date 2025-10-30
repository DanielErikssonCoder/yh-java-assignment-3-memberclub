package com.memberclub.model.camping;

import com.memberclub.model.enums.*;

/**
 * Concrete class representing a tent.
 * Extends CampingEquipment with specific attributes for tents.
 */
public class Tent extends CampingEquipment {

    private int capacity;
    private SeasonRating seasonRating;
    private TentType tentType;

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
     * @param tentType type of tent structure
     */
    public Tent(String id, String name, double pricePerDay, double pricePerHour, int year, Color color, Material material,
                double weight, String brand, int capacity, SeasonRating seasonRating, TentType tentType) {

        // Call parent constructors (CampingEquipment, which calls Item)
        super(id, name, pricePerDay, pricePerHour, year, color, material, weight, brand);

        // Initialize own fields
        this.capacity = capacity;
        this.seasonRating = seasonRating;
        this.tentType = tentType;
    }

    // Getters
    public int getCapacity() {
        return capacity;
    }

    public SeasonRating getSeasonRating() {
        return seasonRating;
    }

    public TentType getTentType() {
        return tentType;
    }

    // Setters
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setSeasonRating(SeasonRating seasonRating) {
        this.seasonRating = seasonRating;
    }

    public void setTentType(TentType tentType) {
        this.tentType = tentType;
    }

    // Returns the specific item type for this tent
    @Override
    public ItemType getItemType() {
        return ItemType.TENT;
    }

    // Returns string representation for easy printing or debugging
    @Override
    public String toString() {
        return "Tent{" + "kapacitet=" + capacity + " personer, säsong=" + seasonRating + ", typ=" + tentType + "} " + super.toString();
    }
}

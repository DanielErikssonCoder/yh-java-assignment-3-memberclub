package com.memberclub.model.camping;

import com.memberclub.model.Color;
import com.memberclub.model.ItemType;
import com.memberclub.model.Material;

/**
 * Concrete class representing a sleeping bag.
 * Extends CampingEquipment with specific attributes for sleeping bags.
 */
public class SleepingBag extends CampingEquipment {

    private double temperatureRating;
    private SeasonRating seasonRating;

    /**
     * Constructor that creates a new sleeping bag with given specifications.
     * @param id unique identifier
     * @param name name of the sleeping bag
     * @param pricePerDay rental price per day
     * @param pricePerHour rental price per hour
     * @param year model year
     * @param color item color
     * @param material material type
     * @param weight weight in kilograms
     * @param brand brand name
     * @param temperatureRating lowest comfortable temperature in Celsius
     * @param seasonRating season suitability
     */
    public SleepingBag(String id, String name, double pricePerDay, double pricePerHour, int year, Color color,
                       Material material, double weight, String brand, double temperatureRating, SeasonRating seasonRating) {

        // Call parent constructors (CampingEquipment, which calls Item)
        super(id, name, pricePerDay, pricePerHour, year, color, material, weight, brand);

        // Initialize own fields
        this.temperatureRating = temperatureRating;
        this.seasonRating = seasonRating;
    }

    // Getters
    public double getTemperatureRating() {
        return temperatureRating;
    }

    public SeasonRating getSeasonRating() {
        return seasonRating;
    }

    // Setters
    public void setTemperatureRating(double temperatureRating) {
        this.temperatureRating = temperatureRating;
    }

    public void setSeasonRating(SeasonRating seasonRating) {
        this.seasonRating = seasonRating;
    }

    // Returns the specific item type for this sleeping bag
    @Override
    public ItemType getItemType() {
        return ItemType.SLEEPING_BAG;
    }

    // Returns string representation for easy printing or debugging
    @Override
    public String toString() {
        return "SleepingBag{" + "temperatur=" + temperatureRating + "°C, säsong=" + seasonRating + "} " + super.toString();
    }
}

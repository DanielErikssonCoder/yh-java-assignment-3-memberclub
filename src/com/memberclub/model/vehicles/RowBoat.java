package com.memberclub.model.vehicles;

import com.memberclub.model.Color;
import com.memberclub.model.ItemType;
import com.memberclub.model.Material;

/**
 * Concrete class representing a row boat.
 * Extends Boat with specific attributes for non-motor boats.
 */
public class RowBoat extends Boat {

    private int oars;

    /**
     * Constructor that creates a new row boat with given specifications.
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
     * @param oars number of oars
     */
    public RowBoat(String id, String name, double pricePerDay, double pricePerHour, int year, Color color,
                   Material material, double weight, String brand, int capacity, double length, boolean hasFishFinder,
                   double maxSpeed, int oars) {

        // Call parent constructors (Boat, which calls WaterVehicle, which calls Item)
        super(id, name, pricePerDay, pricePerHour, year, color, material, weight, brand, capacity, length, hasFishFinder, maxSpeed);

        // Initialize own fields
        this.oars = oars;
    }

    // Getter
    public int getOars() {
        return oars;
    }

    // Setter
    public void setOars(int oars) {
        this.oars = oars;
    }

    // Returns the specific item type for this row boat
    @Override
    public ItemType getItemType() {
        return ItemType.ROW_BOAT;
    }

    // Returns string representation for easy printing or debugging
    @Override
    public String toString() {
        return "RowBoat{" + "åror=" + oars + " st} " + super.toString();
    }
}

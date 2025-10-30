package com.memberclub.model.vehicles;
import com.memberclub.model.enums.Color;
import com.memberclub.model.enums.FuelType;
import com.memberclub.model.enums.ItemType;
import com.memberclub.model.enums.Material;

/**
 * Concrete class representing a motor boat.
 * Extends Boat with specific attributes for motor-powered boats.
 */
public class MotorBoat extends Boat {

    private int enginePower;
    private FuelType fuelType;

    /**
     * Constructor that creates a new motor boat with given specifications.
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
     * @param enginePower engine power in horsepower
     * @param fuelType fuel type
     */
    public MotorBoat(String id, String name, double pricePerDay, double pricePerHour, int year, Color color,
                     Material material, double weight, String brand, int capacity, double length, boolean hasFishFinder,
                     double maxSpeed, int enginePower, FuelType fuelType) {

        // Call parent constructors (Boat, which calls WaterVehicle, which calls Item)
        super(id, name, pricePerDay, pricePerHour, year, color, material, weight, brand, capacity, length, hasFishFinder, maxSpeed);

        // Initialize own fields
        this.enginePower = enginePower;
        this.fuelType = fuelType;
    }

    // Getters
    public int getEnginePower() {
        return enginePower;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    // Setters
    public void setEnginePower(int enginePower) {
        this.enginePower = enginePower;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    // Returns the specific item type for this motorboat
    @Override
    public ItemType getItemType() {
        return ItemType.MOTOR_BOAT;
    }

    // Returns string representation for easy printing or debugging
    @Override
    public String toString() {
        return "MotorBoat{" + "motorstyrka=" + enginePower + " HP, bränsletyp=" + fuelType + "} " + super.toString();
    }
}

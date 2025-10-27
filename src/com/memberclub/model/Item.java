package com.memberclub.model;

/**
 * Abstract super class for all rental items.
 * Contains common attributes and behavior for all items in the inventory.
 */
public abstract class Item {

    private String id;
    private String name;
    private double pricePerDay;
    private double pricePerHour;
    private ItemStatus status;

    /**
     * Creates a new item with the given information.
     * @param id is a unique identifier for the item
     * @param name is the name of the item
     * @param pricePerDay is the rental price per day
     * @param pricePerHour is the rental price per hour
     */
    public Item(String id, String name, double pricePerDay, double pricePerHour) {
        this.id = id;
        this.name = name;
        this.pricePerDay = pricePerDay;
        this.pricePerHour = pricePerHour;
        this.status = ItemStatus.AVAILABLE;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public ItemStatus getStatus() {
        return status;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    /**
     * This method checks if the item is available for rental.
     * @return true if status is AVAILABLE, false otherwise
     */
    public boolean isAvailable() {
        return status == ItemStatus.AVAILABLE;
    }

    /**
     * This method checks if the item is currently rented out.
     * @return true if status is RENTED, false otherwise
     */
    public boolean isRented() {
        return status == ItemStatus.RENTED;
    }

    /**
     * This method checks if the item is broken and needs repair.
     * @return true if status is BROKEN, false otherwise
     */
    public boolean isBroken() {
        return status == ItemStatus.BROKEN;
    }

    /**
     * This returns the specific type of this item.
     * Must be implemented by all subclasses to identify their item type.
     * @return the ItemType enum value for this item
     */
    public abstract ItemType getItemType();

    // Returns string representation of Item for easy printing or debugging
    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", namn=" + name + ", pris/dag=" + pricePerDay + ", pris/timme=" + pricePerHour + ", status=" + status + "}";
    }
}

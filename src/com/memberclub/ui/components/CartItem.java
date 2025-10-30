package com.memberclub.ui.components;

import com.memberclub.model.Item;
import com.memberclub.model.RentalPeriod;

/**
 * Represents a single item in the shopping cart before checkout
 * Contains item, rental duration, period type, and calculated price
 */
public class CartItem {
    private Item item;
    private int duration;
    private RentalPeriod period;
    private double price;

    /**
     * Creates a new cart item
     * @param item the item to rent
     * @param duration number of hours or days
     * @param period HOURLY or DAILY
     * @param price calculated base price before member discount
     */
    public CartItem(Item item, int duration, RentalPeriod period, double price) {
        this.item = item;
        this.duration = duration;
        this.period = period;
        this.price = price;
    }

    // Getters
    public Item getItem() {
        return item;
    }

    public int getDuration() {
        return duration;
    }

    public RentalPeriod getPeriod() {
        return period;
    }

    public double getPrice() {
        return price;
    }
}

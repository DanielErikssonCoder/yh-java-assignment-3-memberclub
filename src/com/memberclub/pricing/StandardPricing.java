package com.memberclub.pricing;

import com.memberclub.model.Item;
import com.memberclub.model.Member;
import com.memberclub.model.RentalPeriod;

/**
 * Standard pricing strategy with no discount.
 * Applies 1.0x multiplier.
 */
public class StandardPricing implements PricePolicy {

    // Implements the pricing calculation from PricePolicy interface
    @Override
    public double calculatePrice(Item item, Member member, int duration, RentalPeriod period) {

        // Calculate cost based on a rental period (hourly or daily)
        if (period == RentalPeriod.HOURLY) {
            return item.getPricePerHour() * duration;
        } else {
            return item.getPricePerDay() * duration;
        }
    }
}


package com.memberclub.pricing;

import com.memberclub.model.Item;
import com.memberclub.model.Member;
import com.memberclub.model.RentalPeriod;

/**
 * Premium pricing strategy with 30% discount.
 * Applies 0.7x multiplier.
 */
public class PremiumPricing implements PricePolicy {

    // 30% Discount
    private static final double PREMIUM_DISCOUNT = 0.7;

    // Implements the pricing calculation from PricePolicy interface
    @Override
    public double calculatePrice(Item item, Member member, int duration, RentalPeriod period) {

        // Calculate cost based on rental period (hourly or daily) with premium discount
        if (period == RentalPeriod.HOURLY) {
            return item.getPricePerHour() * duration * PREMIUM_DISCOUNT;
        } else {
            return item.getPricePerDay() * duration * PREMIUM_DISCOUNT;
        }

    }
}

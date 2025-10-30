package com.memberclub.pricing;

import com.memberclub.model.Item;
import com.memberclub.model.Member;
import com.memberclub.model.enums.RentalPeriod;

/**
 * Student pricing strategy with 20% discount.
 * Applies 0.8x multiplier.
 */
public class StudentPricing implements PricePolicy {

    // 20% Discount
    private static final double STUDENT_DISCOUNT = 0.8;

    // Implements the pricing calculation from PricePolicy interface
    @Override
    public double calculatePrice(Item item, Member member, int duration, RentalPeriod period) {

        // Calculate cost based on rental period (hourly or daily) with student discount
        if (period == RentalPeriod.HOURLY) {
            return item.getPricePerHour() * duration * STUDENT_DISCOUNT;
        } else {
            return item.getPricePerDay() * duration * STUDENT_DISCOUNT;
        }

    }
}

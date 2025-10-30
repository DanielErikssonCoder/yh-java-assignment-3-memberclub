package com.memberclub.pricing;

import com.memberclub.model.enums.MembershipLevel;

/**
 * Factory class that creates the appropriate pricing strategy
 * based on membership level.
 */
public class PricingFactory {

    /**
     * Returns the correct PricePolicy for a given membership level.
     * @param level the membership level
     * @return the appropriate pricing strategy
     */
    public static PricePolicy getPricing(MembershipLevel level) {

        // Match membership level to corresponding pricing strategy
        switch (level) {

            // No discount
            case STANDARD -> {
                return new StandardPricing();
            }

            // 20% discount
            case STUDENT -> {
                return new StudentPricing();
            }

            // 30% discount
            case PREMIUM -> {
                return new PremiumPricing();
            }

            // Fallback to standard pricing if level is null or unexpected
            default -> {
                return new StandardPricing();
            }
        }
    }
}

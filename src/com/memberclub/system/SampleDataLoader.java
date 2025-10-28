package com.memberclub.system;

import com.memberclub.model.*;
import com.memberclub.model.camping.*;
import com.memberclub.model.fishing.*;
import com.memberclub.model.vehicles.*;
import com.memberclub.service.*;

/**
 * Loads sample data for demonstration.
 * In production, this would be loaded from a database.
 * Includes variety to showcase different enum types and item categories.
 */
public class SampleDataLoader {

    /**
     * Loads sample items into the inventory.
     * @param inventory the inventory to populate
     * @param generator the ID generator to use
     */
    public static void loadSampleItems(Inventory inventory, ItemIdGenerator generator) {

        // CAMPING EQUIPMENT

        // Backpack - DAYPACK
        inventory.addItem(new Backpack(generator.generateBackpackId(), "Urban Daypack 25L", 150.0, 30.0,
                2023, Color.BLACK, Material.POLYESTER, 1.2, "Patagonia", 25, BackpackType.DAYPACK));

        // Backpack - EXPEDITION
        inventory.addItem(new Backpack(generator.generateBackpackId(), "Mountain Explorer 65L", 300.0, 60.0,
                2024, Color.GREEN, Material.NYLON, 2.5, "Fjällräven", 65, BackpackType.EXPEDITION));

        // Tent - SUMMER
        inventory.addItem(new Tent(generator.generateTentId(), "Summer Breeze 2P", 250.0, 50.0,
                2024, Color.YELLOW, Material.NYLON, 2.5, "MSR", 2, SeasonRating.SUMMER, TentType.DOME));

        // Tent - WINTER
        inventory.addItem(new Tent(generator.generateTentId(), "Arctic Expedition 4P", 600.0, 120.0,
                2023, Color.ORANGE, Material.RIPSTOP_NYLON, 8.0, "Hilleberg", 4, SeasonRating.WINTER, TentType.TUNNEL));

        // Lantern - BATTERY
        inventory.addItem(new Lantern(generator.generateLanternId(), "LED Battery Light Pro", 80.0, 15.0,
                2024, Color.YELLOW, Material.PLASTIC, 0.8, "Coleman", 500, PowerSource.BATTERY));

        // Sleeping Bag - THREE_SEASON
        inventory.addItem(new SleepingBag(generator.generateSleepingBagId(), "All Season Comfort", 180.0, 35.0,
                2023, Color.GREEN, Material.SYNTHETIC, 1.5, "Marmot", 0, SeasonRating.THREE_SEASON));

        // Trangia Kitchen - ALCOHOL
        inventory.addItem(new TrangiaKitchen(generator.generateTrangiaId(), "Trangia 25 Spirit", 150.0, 30.0,
                2023, Color.SILVER, Material.ALUMINUM, 1.2, "Trangia", 2, FuelType.ALCOHOL));


        //FISHING EQUIPMENT

        // Fishing Rod - SPINNING
        inventory.addItem(new FishingRod(generator.generateRodId(), "Shimano Spinning Pro", 200.0, 40.0,
                2023, Color.BLACK, Material.CARBON_FIBER, 0.4, "Shimano", 2.1, RodType.SPINNING));

        // Fishing Rod - FLY
        inventory.addItem(new FishingRod(generator.generateRodId(), "Orvis Fly Master", 280.0, 55.0,
                2024, Color.BROWN, Material.FIBERGLASS, 0.5, "Orvis", 2.7, RodType.FLY));

        // Fishing Rod - ICE
        inventory.addItem(new FishingRod(generator.generateRodId(), "Ice Fishing Special", 150.0, 30.0,
                2023, Color.RED, Material.FIBERGLASS, 0.3, "Rapala", 0.9, RodType.ICE));

        // Fishing Net - SMALL
        inventory.addItem(new FishingNet(generator.generateNetId(), "Compact Travel Net", 80.0, 15.0,
                2024, Color.BLUE, Material.NYLON, 0.8, "Frabill", NetSize.SMALL, 1.0));

        // Fishing Net - LARGE
        inventory.addItem(new FishingNet(generator.generateNetId(), "Trophy Catch Net", 180.0, 35.0,
                2024, Color.BLACK, Material.NYLON, 1.8, "Savage Gear", NetSize.LARGE, 2.0));

        // Fishing Bait - WOBBLER
        inventory.addItem(new FishingBait(generator.generateBaitId(), "Pike Wobbler Pro", 40.0, 10.0,
                2024, Color.MULTICOLOR, Material.PLASTIC, 0.15, "Rapala", BaitType.WOBBLER, 5));


        // WATER VEHICLES

        // Kayak - SIT_ON_TOP
        inventory.addItem(new Kayak(generator.generateKayakId(), "Ocean Pro Explorer", 850.0, 170.0,
                2024, Color.RED, Material.FIBERGLASS, 28.0, "Hobie", 2, 4.5, 2, KayakType.SIT_ON_TOP));

        // Kayak - FISHING
        inventory.addItem(new Kayak(generator.generateKayakId(), "Angler Pro", 950.0, 190.0,
                2023, Color.CAMOUFLAGE, Material.PLASTIC, 32.0, "Old Town", 1, 3.8, 1, KayakType.FISHING));

        // Motor Boat - GASOLINE
        inventory.addItem(new MotorBoat(generator.generateMotorBoatId(), "Speedster 2000", 2000.0, 400.0,
                2024, Color.RED, Material.FIBERGLASS, 800.0, "Yamaha", 6, 6.5, true, 25.0, 150, FuelType.GASOLINE));

        // Electric Boat
        inventory.addItem(new ElectricBoat(generator.generateElectricBoatId(), "Eco Cruiser Silent", 1200.0, 240.0,
                2024, Color.WHITE, Material.FIBERGLASS, 450.0, "Torqeedo", 4, 4.5, true, 8.0, 50.0, 6));

        // Row Boat
        inventory.addItem(new RowBoat(generator.generateRowBoatId(), "Classic Wooden Fisher", 350.0, 70.0,
                2023, Color.BROWN, Material.WOOD, 120.0, "Traditional Boats", 3, 4.0, true, 5.0, 2));
    }

    /**
     * Loads sample members into the registry.
     * @param registry the member registry to populate
     * @param generator the ID generator to use
     */
    public static void loadSampleMembers(MemberRegistry registry, MemberIdGenerator generator) {

        registry.addMember(new Member(generator.generateMemberId(), "Daniel Svensson", MembershipLevel.STANDARD));

        registry.addMember(new Member(generator.generateMemberId(), "Erik Johansson", MembershipLevel.STUDENT));

        registry.addMember(new Member(generator.generateMemberId(), "Anders Karlsson", MembershipLevel.PREMIUM));
    }
}
package com.memberclub.system;

import com.memberclub.model.*;
import com.memberclub.service.*;

/**
 * Central system class that initializes and coordinates all services.
 * Acts as the main entry point for the console application.
 */
public class ClubSystem {

    private Inventory inventory;
    private MemberRegistry memberRegistry;
    private RentalService rentalService;
    private MembershipService membershipService;
    private ItemIdGenerator itemIdGenerator;
    private MemberIdGenerator memberIdGenerator;

    /**
     * Creates and initializes the complete club system.
     * Sets up all services and their dependencies.
     */
    public ClubSystem() {
        // Initialize ID generators first
        this.itemIdGenerator = new ItemIdGenerator();
        this.memberIdGenerator = new MemberIdGenerator();

        // Initialize core services
        this.inventory = new Inventory();
        this.memberRegistry = new MemberRegistry();
        this.rentalService = new RentalService(inventory, memberRegistry);
        this.membershipService = new MembershipService(memberRegistry);

        // Load sample data using generators
        SampleDataLoader.loadSampleItems(inventory, itemIdGenerator);
        SampleDataLoader.loadSampleMembers(memberRegistry, memberIdGenerator);
    }

    // Getters
    public Inventory getInventory() {
        return inventory;
    }

    public MemberRegistry getMemberRegistry() {
        return memberRegistry;
    }

    public RentalService getRentalService() {
        return rentalService;
    }

    public MembershipService getMembershipService() {
        return membershipService;
    }

    public ItemIdGenerator getItemIdGenerator() {
        return itemIdGenerator;
    }

    public MemberIdGenerator getMemberIdGenerator() {
        return memberIdGenerator;
    }
}
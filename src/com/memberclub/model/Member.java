package com.memberclub.model;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a member in the rental club.
 * Each member has an id, name, membership level and rental history.
 */
public class Member {

    private int id;
    private String name;
    private MembershipLevel membershipLevel;
    private List<String> rentalHistory;

    /**
     * Creates a new member with the given information.
     * @param id is the unique member id
     * @param name is the member's name
     * @param membershipLevel is the membership level (STANDARD, STUDENT, or PREMIUM)
     */
    public Member(int id, String name, MembershipLevel membershipLevel) {
        this.id = id;
        this.name = name;
        this.membershipLevel = membershipLevel;
        this.rentalHistory = new ArrayList<>();
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MembershipLevel getMembershipLevel() {
        return membershipLevel;
    }

    public List<String> getRentalHistory() {
        return rentalHistory;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setMembershipLevel(MembershipLevel membershipLevel) {
        this.membershipLevel = membershipLevel;
    }

    /**
     * Adds a rental to the member's history.
     * @param rentalId is the id of the rental to add
     */
    public void addRental(String rentalId) {
        rentalHistory.add(rentalId);
    }

    // Returns string representation of Member for easy printing or debugging
    @Override
    public String toString() {
        return "Medlem{" + "id=" + id + ", namn=" + name + ", nivå=" + membershipLevel + ", historik=" + rentalHistory.size() + "}";
    }
}

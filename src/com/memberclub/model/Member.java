package com.memberclub.model;

import com.memberclub.model.enums.MembershipLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a member in the rental club.
 * Each member has an id, name, email, phone, membership level and rental history.
 */
public class Member {

    private final int id;
    private String name;
    private String email;
    private String phone;
    private MembershipLevel membershipLevel;
    private final List<String> rentalHistory;

    /**
     * Constructor that creates a new member with the given information.
     * @param id unique member id
     * @param name member's name
     * @param email member's email adress
     * @param phone member's phone number
     * @param membershipLevel membership level (STANDARD, STUDENT, or PREMIUM)
     */
    public Member(int id, String name, String email, String phone, MembershipLevel membershipLevel) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
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

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

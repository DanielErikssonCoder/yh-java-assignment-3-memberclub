package com.memberclub.service;

import com.memberclub.model.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages member related operations.
 * Provides business logic for member management.
 */
public class MembershipService {

    private MemberRegistry memberRegistry;

    /**
     * Creates a MembershipService with access to the member registry
     * @param memberRegistry the registry to manage members
     */
    public MembershipService(MemberRegistry memberRegistry) {
        this.memberRegistry = memberRegistry;
    }

    /**
     * Adds a new member to the system.
     * @param member the member to add
     */
    public void addMember(Member member) {
        memberRegistry.addMember(member);
    }

    /**
     * Finds a member by their ID.
     * @param memberId the ID to search for
     * @return the member if found, null otherwise
     */
    public Member getMember(int memberId) {

        // Get member from registry
        Member member = memberRegistry.getMember(memberId);

        return member;
    }

    /**
     * Updates a member's membership level.
     * @param memberId the ID of the member
     * @param newLevel the new membership level
     * @return true if successful, false if member not found
     */
    public boolean updateMemberLevel(int memberId, MembershipLevel newLevel) {

        // Find member by ID
        Member member = memberRegistry.getMember(memberId);

        // If member not found, return false
        if (member == null) {
            return false;
        }

        // Update membership level
        member.setMembershipLevel(newLevel);

        // Return success
        return true;
    }

    /**
     * Returns all members in the system.
     * @return list of all members
     */
    public List<Member> getAllMembers() {

        // Get all members from registry
        List<Member> members = memberRegistry.getAllMembers();

        return members;
    }

    /**
     * Searches for members by name (case-insensitive, partial match).
     * @param searchTerm the name to search for
     * @return list of matching members
     */
    public List<Member> searchMemberByName(String searchTerm) {

        // Get all members
        List<Member> allMembers = memberRegistry.getAllMembers();

        // Create an empty result list
        List<Member> results = new ArrayList<>();

        // Loop through all members
        for (Member member : allMembers) {

            // Check if name contains searchTerm (without any case sensitivity)
            if (member.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                results.add(member);
            }
        }

        // Return matching members
        return results;
    }
}

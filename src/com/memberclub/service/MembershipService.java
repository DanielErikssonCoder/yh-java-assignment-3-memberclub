package com.memberclub.service;

import com.memberclub.model.*;
import com.memberclub.model.enums.MembershipLevel;
import com.memberclub.system.MemberIdGenerator;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages member related operations.
 * Provides business logic for member management.
 */
public class MembershipService {

    private final MemberRegistry memberRegistry;
    private final MemberIdGenerator memberIdGenerator;

    /**
     * Creates a MembershipService with access to the member registry and ID generator
     * @param memberRegistry the registry to manage members
     * @param memberIdGenerator the ID generator for new members
     */
    public MembershipService(MemberRegistry memberRegistry, MemberIdGenerator memberIdGenerator) {
        this.memberRegistry = memberRegistry;
        this.memberIdGenerator = memberIdGenerator;
    }

    /**
     * Adds a new member to the system.
     * @param member the member to add
     */
    public void addMember(Member member) {
        memberRegistry.addMember(member);
    }

    /**
     * Creates and adds a new member to the system.
     * @param name member name
     * @param email member email
     * @param phone member phone
     * @param level membership level
     * @return the created member
     */
    public Member addMember(String name, String email, String phone, MembershipLevel level) {
        int memberId = memberIdGenerator.generateMemberId();
        Member newMember = new Member(memberId, name, email, phone, level);
        memberRegistry.addMember(newMember);
        return newMember;
    }

    /**
     * Removes a member from the system.
     * @param memberId the member ID
     * @return true if removed successfully
     */
    public boolean removeMember(int memberId) {
        return memberRegistry.removeMember(memberId);
    }

    /**
     * Finds a member by their ID.
     * @param memberId the ID to search for
     * @return the member if found, null otherwise
     */
    public Member getMember(int memberId) {
        return memberRegistry.getMember(memberId);
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
        return memberRegistry.getAllMembers();
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
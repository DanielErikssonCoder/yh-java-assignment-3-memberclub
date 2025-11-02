package com.memberclub.service;

import com.memberclub.model.Member;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages all members in the member club.
 * Uses a Map for fast ID lookup.
 */
public class MemberRegistry {

    // Map for ID-based lookup: memberId -> Member
    private final Map<Integer, Member> members;

    /**
     * Creates an empty member registry.
     */
    public MemberRegistry() {
        this.members = new HashMap<>();
    }

    /**
     * Adds a member to the registry.
     * @param member the member to add
     */
    public void addMember(Member member) {
        members.put(member.getId(), member);
    }

    /**
     * Finds a member by their ID.
     * @param memberId the ID to search for
     * @return the member if found, null if not found
     */
    public Member getMember(int memberId) {
        return members.get(memberId);
    }

    /**
     * Removes a member from registry.
     * @param memberId the ID of the member to remove
     * @return true if removed, false if not found
     */
    public boolean removeMember(int memberId) {
        if (members.containsKey(memberId)) {
            members.remove(memberId);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Convert map.values() to an ArrayList and return.
     * @return list of all members
     */
    public List<Member> getAllMembers() {
        List<Member> allMembers = new ArrayList<>(members.values());
        return allMembers;
    }

    /**
     * Returns the number of members in registry.
     * @return member count
     */
    public int getMemberCount() {
        return members.size();
    }

}

package com.memberclub.system;

/**
 * Generates unique IDs for members.
 */
public class MemberIdGenerator {

    private int counter = 1;

    /**
     * Generates next available member ID.
     * @return unique member ID
     */
    public int generateMemberId() {
        return counter++;
    }
}
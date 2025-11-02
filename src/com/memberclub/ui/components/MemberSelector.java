package com.memberclub.ui.components;

import com.memberclub.model.Member;
import com.memberclub.system.ClubSystem;
import com.memberclub.ui.UIHelper;
import com.memberclub.ui.validation.InputValidator;
import java.util.List;
import java.util.Scanner;

/**
 * Handles member selection from the registry.
 */
public class MemberSelector {

    private final Scanner scanner;
    private final ClubSystem system;
    private final UIHelper helper;

    /**
     * Creates a new member selector.
     * @param scanner the scanner for user input
     * @param system the club system
     * @param helper the UI helper
     */
    public MemberSelector(Scanner scanner, ClubSystem system, UIHelper helper) {
        this.scanner = scanner;
        this.system = system;
        this.helper = helper;
    }

    /**
     * Let user select a member from the registry.
     * @return selected Member, or null if user cancels
     */
    public Member selectMember() {
        helper.clearScreen();
        helper.printHeader("         VÄLJ MEDLEM");

        // Get all members from registry
        List<Member> allMembers = system.getMemberRegistry().getAllMembers();

        // Check if any members exist
        if (allMembers.isEmpty()) {
            System.out.println("Inga medlemmar finns i systemet!");
            helper.pressEnterToContinue();
            return null;
        }

        // Display all members with their membership level
        for (int i = 0; i < allMembers.size(); i++) {
            Member member = allMembers.get(i);
            System.out.println("[" + (i + 1) + "] " + member.getName() + " (" + member.getMembershipLevel() + ")");
        }

        System.out.println();
        System.out.println("[0] Avbryt");
        System.out.println();
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        System.out.println();

        // Get member selection from user
        int memberChoice = InputValidator.getIntInRange(scanner, 0, allMembers.size(), "Välj medlem: ");

        // Check if user cancelled
        if (memberChoice == 0) {
            return null;
        }

        // Return selected member
        return allMembers.get(memberChoice - 1);
    }
}
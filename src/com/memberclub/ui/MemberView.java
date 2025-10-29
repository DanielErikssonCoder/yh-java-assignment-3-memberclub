package com.memberclub.ui;

import com.memberclub.model.Member;
import com.memberclub.model.MembershipLevel;
import com.memberclub.system.ClubSystem;
import java.util.List;
import java.util.Scanner;

/**
 * View class for member-related operations.
 * Handles creating, updating, and managing members.
 */
public class MemberView {

    private Scanner scanner;
    private ClubSystem system;
    private UIHelper helper;

    /**
     * Creates a new member view.
     * @param scanner the scanner to use for input
     * @param system the club system
     * @param helper the UI helper for common operations
     */
    public MemberView(Scanner scanner, ClubSystem system, UIHelper helper) {
        this.scanner = scanner;
        this.system = system;
        this.helper = helper;
    }

    /**
     * Menu option 4: Manage members
     */
    public void manageMembers() {
        helper.clearScreen();
        helper.printHeader("        HANTERA MEDLEMMAR");
        System.out.println("[1] Visa alla medlemmar");
        System.out.println("[2] Lägg till medlem");
        System.out.println("[3] Ta bort medlem");
        System.out.println("[0] Gå tillbaka");
        System.out.println();
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        System.out.println();
        System.out.print("Välj alternativ: ");

        // Initialize choice for validation loop
        int choice = -1;

        // Continue loop until valid choice
        while (choice < 0 || choice > 3) {

            String input = scanner.nextLine().trim();

            // Check if input is empty
            if (input.isEmpty()) {
                System.out.println();
                System.out.println("Du måste ange ett val!");
                System.out.println();
                System.out.print("Välj alternativ: ");
                continue;
            }

            // Try to parse as integer
            try {
                choice = Integer.parseInt(input);

                // Validate range after parsing
                if (choice < 0 || choice > 3) {
                    System.out.println();
                    System.out.println("Ogiltigt val! Ange ett nummer mellan 0-3.");
                    System.out.println();
                    System.out.print("Välj alternativ: ");
                }
            } catch (NumberFormatException e) {
                System.out.println();
                System.out.println("Ogiltigt val! Ange ett nummer.");
                System.out.println();
                System.out.print("Välj alternativ: ");
            }
        }

        // Handle choice
        switch (choice) {
            case 1 -> viewAllMembers();
            case 2 -> addMember();
            case 3 -> removeMember();
            case 0 -> {} // Go back
        }
    }

    /**
     * Display all members
     */
    private void viewAllMembers() {
        helper.clearScreen();
        helper.printHeader("        ALLA MEDLEMMAR");

        // Get all members from registry
        List<Member> allMembers = system.getMemberRegistry().getAllMembers();

        // Check if member registry is empty
        if (allMembers.isEmpty()) {
            System.out.println("Inga medlemmar finns i systemet!");
            helper.pressEnterToContinue();
            return;
        }

        // Separate standard members
        List<Member> standardMembers = allMembers.stream().filter(m -> m.getMembershipLevel() == MembershipLevel.STANDARD).toList();

        // Filter student members
        List<Member> studentMembers = allMembers.stream().filter(m -> m.getMembershipLevel() == MembershipLevel.STUDENT).toList();

        // Filter premium members
        List<Member> premiumMembers = allMembers.stream().filter(m -> m.getMembershipLevel() == MembershipLevel.PREMIUM).toList();

        helper.printDivider();
        System.out.println("STANDARD MEDLEMMAR (" + standardMembers.size() + " st)");
        helper.printDivider();

        // Display each standard member
        for (Member member : standardMembers) {
            System.out.println();
            System.out.println("[" + member.getId() + "] " + member.getName());
            System.out.println("Email: " + member.getEmail());
            System.out.println("Telefon: " + member.getPhone());
        }

        System.out.println();
        System.out.println();
        helper.printDivider();
        System.out.println("STUDENT MEDLEMMAR (" + studentMembers.size() + " st)");
        helper.printDivider();

        // Display each student member
        for (Member member : studentMembers) {
            System.out.println();
            System.out.println("[" + member.getId() + "] " + member.getName());
            System.out.println("Email: " + member.getEmail());
            System.out.println("Telefon: " + member.getPhone());
        }

        System.out.println();
        System.out.println();
        helper.printDivider();
        System.out.println("PREMIUM MEDLEMMAR (" + premiumMembers.size() + " st)");
        helper.printDivider();

        // Display each premium member
        for (Member member : premiumMembers) {
            System.out.println();
            System.out.println("[" + member.getId() + "] " + member.getName());
            System.out.println("Email: " + member.getEmail());
            System.out.println("Telefon: " + member.getPhone());
        }

        System.out.println();
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        helper.pressEnterToContinue();
    }

    /**
     * Add a new member
     */
    private void addMember() {
        helper.clearScreen();
        helper.printHeader("        LÄGG TILL MEDLEM");

        // Get name
        System.out.print("Namn: ");
        String name = scanner.nextLine().trim();

        // Validation loop to check for empty name input
        while (name.isEmpty()) {
            System.out.println();
            System.out.println("Namnet kan inte vara tomt!");
            System.out.println();
            System.out.print("Namn: ");
            name = scanner.nextLine().trim();
        }

        // Get email
        System.out.println();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        // Validation loop to check for empty email input
        while (email.isEmpty()) {
            System.out.println();
            System.out.println("Email kan inte vara tom!");
            System.out.println();
            System.out.print("Email: ");
            email = scanner.nextLine().trim();
        }

        // Get phone
        System.out.println();
        System.out.print("Telefon: ");
        String phone = scanner.nextLine().trim();

        // Validation loop to check for empty phone input
        while (phone.isEmpty()) {
            System.out.println();
            System.out.println("Telefon kan inte vara tomt!");
            System.out.println();
            System.out.print("Telefon: ");
            phone = scanner.nextLine().trim();
        }

        // Get membership level
        System.out.println();
        System.out.println("Medlemskapsnivå:");
        System.out.println("[1] Standard (0% rabatt)");
        System.out.println("[2] Student (20% rabatt)");
        System.out.println("[3] Premium (30% rabatt)");
        System.out.println();
        System.out.print("Välj nivå: ");

        // Initialize choice for validation loop
        int levelChoice = -1;

        // Validation loop for membership level
        while (levelChoice < 1 || levelChoice > 3) {
            String input = scanner.nextLine().trim();

            // Check if input is empty
            if (input.isEmpty()) {
                System.out.println();
                System.out.println("Du måste ange ett val!");
                System.out.println();
                System.out.print("Välj nivå: ");
                continue;
            }

            // Try to parse as intege
            try {
                levelChoice = Integer.parseInt(input);

                // Validate range after parsing
                if (levelChoice < 1 || levelChoice > 3) {
                    System.out.println();
                    System.out.println("Ogiltigt val! Ange ett nummer mellan 1-3.");
                    System.out.println();
                    System.out.print("Välj nivå: ");
                }
            } catch (NumberFormatException e) {
                System.out.println();
                System.out.println("Ogiltigt val! Ange ett nummer.");
                System.out.println();
                System.out.print("Välj nivå: ");
            }
        }

        // Convert to MembershipLevel
        MembershipLevel level = switch (levelChoice) {
            case 1 -> MembershipLevel.STANDARD;
            case 2 -> MembershipLevel.STUDENT;
            case 3 -> MembershipLevel.PREMIUM;
            default -> MembershipLevel.STANDARD;
        };

        // Create member
        Member newMember = system.getMembershipService().addMember(name, email, phone, level);

        if (newMember != null) {
            helper.clearScreen();
            System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
            System.out.println("         MEDLEM SKAPAD!");
            System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
            System.out.println();
            System.out.println("Medlems-ID: " + newMember.getId());
            System.out.println("Namn: " + newMember.getName());
            System.out.println("Email: " + newMember.getEmail());
            System.out.println("Telefon: " + newMember.getPhone());
            System.out.println("Nivå: " + newMember.getMembershipLevel());
            System.out.println();
            System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        } else {
            helper.clearScreen();
            System.out.println("Något gick fel vid skapande av medlem. Försök igen.");
        }

        helper.pressEnterToContinue();
    }

    /**
     * Remove a member
     */
    private void removeMember() {
        helper.clearScreen();
        helper.printHeader("        TA BORT MEDLEM");

        // Get all members from registry
        List<Member> allMembers = system.getMemberRegistry().getAllMembers();

        // Check if member registry is empty
        if (allMembers.isEmpty()) {
            System.out.println("Inga medlemmar finns i systemet!");
            helper.pressEnterToContinue();
            return;
        }

        // Display all members
        System.out.println("Medlemmar:");
        System.out.println();

        // Display each member with index number
        for (int i = 0; i < allMembers.size(); i++) {
            Member member = allMembers.get(i);
            System.out.println(" [" + (i + 1) + "] " + member.getName() + " (" + member.getMembershipLevel() + ")");
        }

        System.out.println();
        System.out.println(" [0] Avbryt");
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        System.out.print("Välj medlem att ta bort: ");

        // Initialize choice for validation loop
        int choice = -1;

        // Continue loop until valid choice
        while (choice < 0 || choice > allMembers.size()) {
            String input = scanner.nextLine().trim();

            // Check if input is empty
            if (input.isEmpty()) {
                System.out.println();
                System.out.println("Du måste ange ett val!");
                System.out.println();
                System.out.print("Välj medlem att ta bort: ");
                continue;
            }

            // Try to parse input as integer
            try {
                choice = Integer.parseInt(input);

                // Validate that choice is within valid range
                if (choice < 0 || choice > allMembers.size()) {
                    System.out.println();
                    System.out.println("Ogiltigt val! Ange ett nummer mellan 0-" + allMembers.size() + ".");
                    System.out.println();
                    System.out.print("Välj medlem att ta bort: ");
                }
            } catch (NumberFormatException e) {
                System.out.println();
                System.out.println("Ogiltigt val! Ange ett nummer.");
                System.out.println();
                System.out.print("Välj medlem att ta bort: ");
            }
        }

        // Check if the user wants to cancel
        if (choice == 0) {
            return;
        }

        // Get selected member
        Member selectedMember = allMembers.get(choice - 1);

        // Confirm deletion
        System.out.println();
        System.out.println("Är du säker på att du vill ta bort " + selectedMember.getName() + "?");
        System.out.println();
        System.out.print("Skriv 'JA' för att bekräfta: ");
        String confirmation = scanner.nextLine().trim();

        // Check if user confirmed with "JA"
        if (confirmation.equalsIgnoreCase("JA")) {

            // Attempt to remove member from system
            boolean removed = system.getMembershipService().removeMember(selectedMember.getId());

            // Display success message if removal succeeded
            if (removed) {
                helper.clearScreen();
                System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
                System.out.println("Medlem: '" + selectedMember.getName() + "' har tagits bort!");
                System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
            } else {
                helper.clearScreen();
                System.out.println("Borttagningen misslyckades. Försök igen.");
            }
        } else {
            helper.clearScreen();
            System.out.println("Borttagning avbruten.");
        }
        helper.pressEnterToContinue();
    }
}
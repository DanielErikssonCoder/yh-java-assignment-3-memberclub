package com.memberclub.ui;

import com.memberclub.system.ClubSystem;
import com.memberclub.model.User;
import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * Console-based UI for the Member Club Rental System.
 * Handles all user interactions and menu navigation.
 */
public class ConsoleMenu {

    private Scanner scanner;
    private ClubSystem system;
    private User currentUser;

    // ANSI color codes - Outdoor/Camping theme
    private static final String GREEN = "\033[38;5;28m";
    private static final String RESET = "\033[0m";

    /**
     * Creates a new console menu.
     * @param system the club system to interact with
     */
    public ConsoleMenu(ClubSystem system) {
        this.scanner = new Scanner(System.in);
        this.system = system;
        this.currentUser = null;
    }

    /**
     * Clears the console screen
     */
    private void clearScreen() {
        try {

            // Check if system is Windows
            if (System.getProperty("os.name").contains("Windows")) {

                // Execute Windows command to clear the console
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                System.out.println();

            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
                System.out.println();
            }
        } catch (Exception e) {

            // If clear fails, then we just print some empty lines
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    /**
     * Waits for user to press 'Enter' before continuing
     */
    private void pressEnterToContinue() {
        System.out.println();
        System.out.print("Tryck 'Enter' för att fortsätta...");
        scanner.nextLine();
    }

    /**
     * Waits for user to press 'Enter' before quitting
     */
    private void pressEnterToQuit() {
        System.out.println();
        System.out.print("Tryck 'Enter' för att avsluta...");
        scanner.nextLine();
    }

    /**
     * Displays start/logout menu with login or exit options.
     * @param afterLogout if true, shows the logout message first
     * @return true if user wants to login, false if user wants to exit program
     */
    private boolean showStartOrLogoutMenu(boolean afterLogout) {

        // If user just logged out, show message first
        if (afterLogout) {
            clearScreen();
            System.out.println("Du har loggats ut.");
            pressEnterToContinue();
        }

        clearScreen();
        System.out.println(GREEN + "=====================================" + RESET);
        System.out.println("    MEDLEMSKLUBB UTHYRNINGSSYSTEM");
        System.out.println(GREEN + "=====================================" + RESET);
        System.out.println();
        System.out.println(" [1] Logga in");
        System.out.println(" [0] Avsluta program");
        System.out.println();
        System.out.println(GREEN + "=====================================" + RESET);
        System.out.print("Välj alternativ: ");

        // Read choice with validation
        int choice = -1;

        while (choice != 0 && choice != 1) {

            try {
                choice = scanner.nextInt();
                scanner.nextLine();

                if (choice != 0 && choice != 1) {
                    System.out.println();
                    System.out.println("Ogiltigt val! Välj 'Logga in' eller 'Avsluta program'");
                    System.out.print("Välj alternativ: ");
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println();
                System.out.println("Ogiltigt val! Ange ett nummer.");
                System.out.print("Välj alternativ: ");
            }
        }

        if (choice == 1) {
            // Login
            return true;

        } else {
            clearScreen();
            System.out.println("Tack för att du använder uthyrningssystemet!");
            pressEnterToQuit();

            // Exit
            return false;
        }
    }

    /**
     * Displays login screen and authenticates user.
     * Loops until successful login.
     * @return the authenticated user
     */
    private User showLogin() {

        User authenticatedUser = null;

        // Outer loop: Until successful authentication
        while (authenticatedUser == null) {

            clearScreen();
            System.out.println(GREEN + "=====================================" + RESET);
            System.out.println("    MEDLEMSKLUBB UTHYRNINGSSYSTEM");
            System.out.println(GREEN + "=====================================" + RESET);
            System.out.println("              LOGGA IN");
            System.out.println(GREEN + "-------------------------------------" + RESET);
            System.out.println();

            // Inner loop 1: Validate username exists
            User user = null;
            String username = "";

            while (user == null) {
                System.out.print("Användarnamn: ");
                username = scanner.nextLine().trim();

                // Check if the user exists
                user = system.getUser(username);

                if (user == null) {
                    System.out.println();
                    System.out.println("Användaren finns inte! Försök igen.");
                    System.out.println();
                }
            }

            // Inner loop 2: Validate password is correct
            boolean passwordCorrect = false;

            while (!passwordCorrect) {
                System.out.println();
                System.out.print("Lösenord: ");
                String password = scanner.nextLine().trim();

                // Validate password
                if (user.validatePassword(password)) {
                    authenticatedUser = user;
                    passwordCorrect = true;
                } else {
                    System.out.println();
                    System.out.println("Fel lösenord! Försök igen.");
                    System.out.println();
                }
            }
        }
        return authenticatedUser;
    }

    /**
     * Displays the main menu.
     */
    private void printMainMenu() {

        clearScreen();
        System.out.println(GREEN + "=====================================" + RESET);
        System.out.println("    MEDLEMSKLUBB UTHYRNINGSSYSTEM");
        System.out.println(GREEN + "=====================================" + RESET);

        // Display logged in user
        System.out.println("    Inloggad som: " + currentUser.getFullName());
        System.out.println(GREEN + "-------------------------------------" + RESET);

        System.out.println();
        System.out.println(" [1] Hyr föremål");
        System.out.println(" [2] Returnera föremål");
        System.out.println(" [3] Visa mina uthyrningar");
        System.out.println(" [4] Hantera medlemmar");
        System.out.println(" [5] Visa alla föremål");
        System.out.println(" [0] Logga ut");
        System.out.println();
        System.out.println(GREEN + "=====================================" + RESET);
        System.out.println();
        System.out.print("Välj alternativ: ");
    }

    /**
     * Starts the menu system.
     * Handles login and main menu loop.
     */
    public void start() {

        // Show start menu
        boolean wantsToLogin = showStartOrLogoutMenu(false);

        if (!wantsToLogin) {

            // User chose to exit immediately
            return;
        }

        boolean running = true;

        while (running) {

            // Login
            currentUser = showLogin();

            // Welcome message
            clearScreen();
            System.out.println("Välkommen " + currentUser.getFullName() + "!");
            pressEnterToContinue();

            // Main menu loop
            boolean loggedIn = true;

            while (loggedIn) {
                printMainMenu();

                // Read user choice
                int choice = -1;

                try {
                    choice = scanner.nextInt();
                    scanner.nextLine();

                } catch (InputMismatchException e) {
                    scanner.nextLine();
                }

                System.out.println();

                // Handle menu navigation
                switch (choice) {
                    case 1 -> rentItem();
                    case 2 -> returnItem();
                    case 3 -> viewMyRentals();
                    case 4 -> manageMembers();
                    case 5 -> viewAllItems();

                    // Trigger logout
                    case 0 -> loggedIn = false;

                    default -> {
                        System.out.println("Ogiltigt val! Försök igen.");
                        pressEnterToContinue();
                    }
                }
            }

            // After logout - show menu with logout message
            boolean loginAgain = showStartOrLogoutMenu(true);

            if (!loginAgain) {

                // Exit program
                running = false;
            }
        }
    }

    /**
     * Menu option 1: Rent an item
     */
    private void rentItem() {
        clearScreen();
        System.out.println("Hyra");
        pressEnterToContinue();
    }

    /**
     * Menu option 2: Return an item
     */
    private void returnItem() {
        clearScreen();
        System.out.println("Returnera");
        pressEnterToContinue();
    }

    /**
     * Menu option 3: View my rentals
     */
    private void viewMyRentals() {
        clearScreen();
        System.out.println("Visa uthyrningar");
        pressEnterToContinue();
    }

    /**
     * Menu option 4: Manage members
     */
    private void manageMembers() {
        clearScreen();
        System.out.println("Hantera medlemmar");
        pressEnterToContinue();
    }

    /**
     * Menu option 5: View all items
     */
    private void viewAllItems() {
        clearScreen();
        System.out.println("Visa alla föremål");
        pressEnterToContinue();
    }
}
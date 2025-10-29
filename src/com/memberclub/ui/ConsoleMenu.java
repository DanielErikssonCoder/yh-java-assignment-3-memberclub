package com.memberclub.ui;

import com.memberclub.system.ClubSystem;
import com.memberclub.model.User;
import java.util.Scanner;

/**
 * Console-based UI for the Member Club Rental System.
 * Handles all user interactions and menu navigation.
 */
public class ConsoleMenu {

    private Scanner scanner;
    private ClubSystem system;
    private User currentUser;
    private UIHelper helper;

    // View classes
    private RentalView rentalView;
    private ItemView itemView;
    private MemberView memberView;

    /**
     * Creates a new console menu.
     * @param system the club system to interact with
     */
    public ConsoleMenu(ClubSystem system) {
        this.scanner = new Scanner(System.in);
        this.system = system;
        this.currentUser = null;
        this.helper = new UIHelper(scanner);

        // Initialize view classes
        this.rentalView = new RentalView(scanner, system, helper);
        this.itemView = new ItemView(scanner, system, helper);
        this.memberView = new MemberView(scanner, system, helper);
    }

    /**
     * Displays start/logout menu with login, register, or exit options.
     * @param afterLogout if true, shows the logout message first
     * @return true if user wants to continue, false if user wants to exit program
     */
    private boolean showStartOrLogoutMenu(boolean afterLogout) {

        // If user just logged out, show message first
        if (afterLogout) {
            helper.clearScreen();
            System.out.println("Du har loggats ut.");
            helper.pressEnterToContinue();
        }

        // Control variable for menu loop
        boolean shouldContinue = true;

        // Continue loop until user logs in or exits
        while (shouldContinue) {
            // Clear screen once when menu opens
            helper.clearScreen();
            System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
            System.out.println("    MEDLEMSKLUBB UTHYRNINGSSYSTEM");
            System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
            System.out.println();
            System.out.println("[1] Logga in");
            System.out.println("[2] Skapa användare");
            System.out.println("[0] Avsluta program");
            System.out.println();
            System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
            System.out.println();
            System.out.print("Välj alternativ: ");

            // Read choice with validation
            int choice = -1;

            // Continue loop until user choose either 0,1,2
            while (choice != 0 && choice != 1 && choice != 2) {

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

                    // Check if user choice is one of the valid options
                    if (choice != 0 && choice != 1 && choice != 2) {
                        System.out.println();
                        System.out.println("Ogiltigt val! Välj 'Logga in', 'Skapa användare' eller 'Avsluta program'");
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
            if (choice == 1) {
                // Login - exit loop and return true
                return true;

            } else if (choice == 2) {
                // Register new user
                registerNewUser();
                // Loop continues - show menu again

            } else {
                // Exit program
                helper.clearScreen();
                System.out.println("Tack för att du använder uthyrningssystemet!");
                helper.pressEnterToQuit();
                return false;
            }
        }
        return false;
    }

    /**
     * Registers a new user in the system.
     */
    private void registerNewUser() {
        helper.clearScreen();
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        System.out.println("    MEDLEMSKLUBB UTHYRNINGSSYSTEM");
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        System.out.println("          SKAPA ANVÄNDARE");
        System.out.println(UIHelper.GREEN + "-------------------------------------" + UIHelper.RESET);
        System.out.println();

        // Get username
        String username = "";
        boolean usernameValid = false;

        // Continue loop until valid username
        while (!usernameValid) {
            System.out.print("Användarnamn: ");
            username = scanner.nextLine().trim();

            // Check if username is empty
            if (username.isEmpty()) {
                System.out.println();
                System.out.println("Användarnamn kan inte vara tomt!");
                System.out.println();
                continue;
            }

            // Check if username already exists
            if (system.getUser(username) != null) {
                System.out.println();
                System.out.println("Användarnamn är redan taget! Välj ett annat.");
                System.out.println();
                continue;
            }

            usernameValid = true;
        }

        // Get password
        String password = "";
        boolean passwordValid = false;

        // Continue loop until valid password
        while (!passwordValid) {
            System.out.println();
            System.out.print("Lösenord (minst 4 tecken): ");
            password = scanner.nextLine().trim();

            // Validate password length
            if (password.length() < 4) {
                System.out.println();
                System.out.println("Lösenordet måste vara minst 4 tecken!");
                continue;
            }

            // Confirm password
            System.out.println();
            System.out.print("Bekräfta lösenord: ");
            String confirmPassword = scanner.nextLine().trim();

            // Check if passwords match
            if (!password.equals(confirmPassword)) {
                System.out.println();
                System.out.println("Lösenorden matchar inte! Försök igen.");
                continue;
            }

            passwordValid = true;
        }

        // Get full name
        System.out.println();
        System.out.print("Fullständigt namn: ");
        String fullName = scanner.nextLine().trim();

        // Validate full name
        while (fullName.isEmpty()) {
            System.out.println();
            System.out.println("Namnet kan inte vara tomt!");
            System.out.println();
            System.out.print("Fullständigt namn: ");
            fullName = scanner.nextLine().trim();
        }

        // Create the user
        boolean userCreated = system.createUser(username, password, fullName);

        // Display success or error message
        if (userCreated) {
            helper.clearScreen();
            System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
            System.out.println("         ANVÄNDARE SKAPAD!");
            System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
            System.out.println();
            System.out.println("Användarnamn: " + username);
            System.out.println("Lösenord: " + password);
            System.out.println("Fullständigt namn: " + fullName);
            System.out.println();
            System.out.println("Du kan nu logga in med ditt användarnamn och lösenord.");
            System.out.println();
            System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        } else {
            helper.clearScreen();
            System.out.println("Något gick fel vid skapande av användare. Försök igen.");
            System.out.println();
        }
        helper.pressEnterToContinue();
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

            helper.clearScreen();
            System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
            System.out.println("    MEDLEMSKLUBB UTHYRNINGSSYSTEM");
            System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
            System.out.println("              LOGGA IN");
            System.out.println(UIHelper.GREEN + "-------------------------------------" + UIHelper.RESET);
            System.out.println();

            // Inner loop that validates that username exists
            User user = null;
            String username = "";

            // Continue loop until valid username found
            while (user == null) {
                System.out.print("Användarnamn: ");
                username = scanner.nextLine().trim();

                // Check if username is empty
                if (username.isEmpty()) {
                    System.out.println();
                    System.out.println("Användarnamn kan inte vara tomt!");
                    System.out.println();
                    continue;
                }

                // Check if the user exists, and display error message if not found
                user = system.getUser(username);
                if (user == null) {
                    System.out.println();
                    System.out.println("Användaren finns inte! Försök igen.");
                    System.out.println();
                }
            }

            // Inner loop that validates that password is correct
            boolean passwordCorrect = false;
            while (!passwordCorrect) {
                System.out.println();
                System.out.print("Lösenord: ");
                String password = scanner.nextLine().trim();

                // Check if password is empty
                if (password.isEmpty()) {
                    System.out.println();
                    System.out.println("Lösenord kan inte vara tomt!");
                    continue;
                }

                // Validate password
                if (user.validatePassword(password)) {
                    authenticatedUser = user;
                    passwordCorrect = true;
                } else {
                    System.out.println();
                    System.out.println("Fel lösenord! Försök igen.");
                }
            }
        }
        return authenticatedUser;
    }

    /**
     * Displays the main menu.
     */
    private void printMainMenu() {

        helper.clearScreen();
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
        System.out.println("    MEDLEMSKLUBB UTHYRNINGSSYSTEM");
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);

        // Display logged in user
        System.out.println("       Konto: " + currentUser.getFullName());
        System.out.println(UIHelper.GREEN + "-------------------------------------" + UIHelper.RESET);

        System.out.println();
        System.out.println("[1] Uthyrning");
        System.out.println("[2] Returnera");
        System.out.println("[3] Visa uthyrningar");
        System.out.println("[4] Hantera medlemmar");
        System.out.println("[5] Visa alla artiklar");
        System.out.println("[6] Kassavy");
        System.out.println();
        System.out.println("[0] Logga ut");
        System.out.println();
        System.out.println(UIHelper.GREEN + "=====================================" + UIHelper.RESET);
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

        // Check if user chose to exit
        if (!wantsToLogin) {
            return;
        }

        // Main application loop
        boolean running = true;
        while (running) {

            // Login
            currentUser = showLogin();

            // Welcome message
            helper.clearScreen();
            System.out.println("Välkommen " + currentUser.getFullName() + "!");
            helper.pressEnterToContinue();

            boolean loggedIn = true;

            // Continue loop until user logs out
            while (loggedIn) {
                printMainMenu();

                // Initialize a choice variable for the validation loop
                int choice = -1;

                // Continue loop until valid choice
                while (choice < 0 || choice > 6) {
                    String input = scanner.nextLine().trim();

                    // Check if input is empty
                    if (input.isEmpty()) {
                        System.out.println();
                        System.out.println("Du måste ange ett val!");
                        System.out.println();
                        System.out.print("Välj alternativ: ");
                        continue;
                    }

                    // Try to parse input as integer
                    try {
                        choice = Integer.parseInt(input);

                        // Validate that choice is within valid range
                        if (choice < 0 || choice > 6) {
                            System.out.println();
                            System.out.println("Ogiltigt val! Ange ett nummer mellan 0-6.");
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

                // Handle menu navigation - delegates to view classes
                switch (choice) {
                    case 1 -> rentalView.rentItem();
                    case 2 -> rentalView.returnItem();
                    case 3 -> rentalView.viewMyRentals();
                    case 4 -> memberView.manageMembers();
                    case 5 -> itemView.viewAllItems();
                    case 6 -> rentalView.showRevenue();
                    case 0 -> loggedIn = false;
                }
            }

            // After logout - show menu with logout message
            boolean loginAgain = showStartOrLogoutMenu(true);

            // Check if user wants to exit or login again
            if (!loginAgain) {

                // Exit program
                running = false;
            }
        }
    }
}
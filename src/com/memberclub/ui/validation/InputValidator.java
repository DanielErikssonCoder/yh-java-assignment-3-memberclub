package com.memberclub.ui.validation;

import java.util.Scanner;

/**Utility class for validating user input from the console
 * Minimizes code duplication across the UI classes
 */
public class InputValidator {

    /**
     * Reads an integer within a specified range from user
     * Keeps prompting until valid input is received.
     * @param scanner the scanner to read from
     * @param min minimum valid value
     * @param max maximum valid value
     * @param prompt the initial prompt message
     * @return the validated integer
     */
    public static int getIntInRange(Scanner scanner, int min, int max, String prompt) {

        System.out.print(prompt);

        // Initialize return value
        int value = -1;

        // Control variable for validation loop
        boolean valid = false;

        // Continue loop until valid input
        while (!valid) {

            String input = scanner.nextLine().trim();

            // Check if input is empty
            if (input.isEmpty()) {
                System.out.println();
                System.out.println("Du måste ange ett val!");
                System.out.println();
                System.out.println(prompt);
                continue;
            }

            // Try to parse as integer
            try {
                value = Integer.parseInt(input);

                // Validate range
                if (value < min || value > max) {
                    System.out.println();
                    System.out.println("Ogiltigt val! Välj " + min + "-" + max + ": ");

                } else {

                    // Valid input -> exit loop
                    valid = true;
                }
            } catch (NumberFormatException exception) {
                System.out.println();
                System.out.println("Ogiltigt val! Ange ett nummer: ");
            }
        }
        return value;
    }

    /**
     * Reads a non empty string from user
     * Keeps prompting until a valid input is received
     * @param scanner the scanner to read from
     * @param prompt the initial prompt message
     * @param errorMessage the error message to display if input is empty
     * @return the validated non empty string
     */
    public static String getNonEmptyString(Scanner scanner, String prompt, String errorMessage) {

        System.out.println(prompt);

        // Initialize return value
        String value = "";

        // Control variable for validation loop
        boolean valid = false;

        // Continue loop until valid input
        while (!valid) {

            value = scanner.nextLine().trim();

            // Check if input is empty
            if (value.isEmpty()) {
                System.out.println();
                System.out.println(errorMessage);
                System.out.println();
                System.out.print(prompt);
            } else {

                // Not empty -> exit loop
                valid = true;
            }
        }
        return value;
    }

    /**
     * Reads a yes/no confirmation from the user
     * Accepts "Ja" or "Nej"
     * @param scanner the scanner to read from
     * @param prompt the confirmation prompt
     * @return true if user answered "Ja", false if "Nej"
     */
    public static boolean getYesNoConfirmation(Scanner scanner, String prompt) {

        System.out.println(prompt);

        // Continue loop until valid response
        while (true) {

            String input = scanner.nextLine().trim();

            // Check if user answered yes
            if (input.equalsIgnoreCase("Ja")) {
                return true;

              // Check if user answered no
            } else if (input.equalsIgnoreCase("Nej")) {
                return false;

            } else {
                System.out.println();
                System.out.println("Ogiltigt svar! Svara 'Ja' eller 'Nej'.");
                System.out.println();
                System.out.print("Svar: ");
            }
        }
    }

    /**
     * Reads a password with minimum length validation
     * @param scanner the scanner to read from
     * @param prompt the password prompt
     * @param minLength minimum required length
     * @return the validated password
     */
    public static String getPassword(Scanner scanner, String prompt, int minLength) {

        System.out.println(prompt);

        // Initialize return value
        String password = "";

        // Control variable for validation loop
        boolean valid = false;

        // Continue loop until valid input
        while (!valid) {

            password = scanner.nextLine().trim();

            // Check if password meets minimum length
            if (password.length() < minLength) {
                System.out.println();
                System.out.println("Lösenordet måste vara minst " + minLength + " tecken!");
                System.out.println();
                System.out.print(prompt);

            } else {

                // Valid password -> exit loop
                valid = true;
            }
        }
        return password;
    }

    /**
     * Reads and validates a password confirmation
     * @param scanner the scanner to read from
     * @param originalPassword the original password to match
     * @return true if passwords match, false otherwise
     */
    public static boolean confirmPassword(Scanner scanner, String originalPassword) {
        System.out.println();
        System.out.print("Bekräfta lösenord: ");
        String confirmPassword = scanner.nextLine().trim();

        // Check if passwords match
        if (!originalPassword.equals(confirmPassword)) {
            System.out.println();
            System.out.println("Lösenorden matchar inte!");
            return false;
        }
        return true;
    }

    /**
     * Validates email format with a simple validation
     * @param email the email to validate
     * @return true if email format is valid
     */
    public static boolean isValidEmail(String email) {

        // Check if email is null or empty
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        // Simple email validation
        return email.contains("@") && email.contains(".") && email.indexOf("@") < email.lastIndexOf(".");
    }

    /**
     * Reads a valid email from user
     * @param scanner the scanner to read from
     * @param prompt the email prompt
     * @return the validated email
     */
    public static String getValidEmail (Scanner scanner, String prompt) {

        System.out.println(prompt);

        // Initialize return value
        String email = "";

        // Control variable for validation loop
        boolean valid = false;

        // Continue loop until valid input
        while (!valid) {

            email = scanner.nextLine().trim();

            // Check if email is empty
            if (email.isEmpty()) {
                System.out.println();
                System.out.println("Email kan inte vara tom!");
                System.out.println();
                System.out.print(prompt);

              // Check if email format is valid
            } else if (!isValidEmail(email)) {
                System.out.println();
                System.out.println("Ogiltig email! Använd formatet: exempel@domän.se");
                System.out.println();
                System.out.print(prompt);
            } else {

                // Valid email -> exit loop
                valid = true;
            }
        }
        return email;
    }

    /**
     * Reads a valid phone number from user
     * Allows only digits and common separators
     * @param scanner the scanner to read from
     * @param prompt the phone prompt
     * @return the validated phone number
     */
    public static String getValidPhone(Scanner scanner, String prompt) {

        System.out.println(prompt);

        // Initialize return value
        String phone = "";

        // Control variable for validation loop
        boolean valid = false;

        // Continue loop until valid input
        while (!valid) {

            phone = scanner.nextLine().trim();

            // Check if phone is empty
            if (phone.isEmpty()) {
                System.out.println();
                System.out.println("Telefon kan inte vara tomt!");
                System.out.println();
                System.out.print(prompt);

              // Validate phone format
            } else if (!phone.matches("^[0-9\\s\\-+()]+$")) {
                System.out.println();
                System.out.println("Ogiltigt nummer! Använd endast siffror, mellanslag och +()-");
                System.out.println();
                System.out.print(prompt);
            } else {

                // Valid phone -> exit loop
                valid = true;
            }
        }
        return phone;
    }
}

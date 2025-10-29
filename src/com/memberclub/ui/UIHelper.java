package com.memberclub.ui;

import java.util.Scanner;

/**
 * Helper class containing common UI utility methods.
 * Provides screen clearing, user prompts, and input handling.
 */
public class UIHelper {

    private Scanner scanner;

    // ANSI color codes
    public static final String GREEN = "\033[38;5;35m";
    public static final String RESET = "\033[0m";

    /**
     * Creates a new UI helper.
     * @param scanner the scanner to use for input
     */
    public UIHelper(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Clears the console screen
     */
    public void clearScreen() {
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
    public void pressEnterToContinue() {
        System.out.println();
        System.out.print("Tryck 'Enter' för att fortsätta...");
        scanner.nextLine();
    }

    /**
     * Waits for user to press 'Enter' before quitting
     */
    public void pressEnterToQuit() {
        System.out.println();
        System.out.print("Tryck 'Enter' för att avsluta...");
        scanner.nextLine();
    }

    /**
     * Prints a header with title
     */
    public void printHeader(String title) {
        System.out.println(GREEN + "=====================================" + RESET);
        System.out.println(title);
        System.out.println(GREEN + "=====================================" + RESET);
        System.out.println();
    }

    /**
     * Prints a divider line
     */
    public void printDivider() {
        System.out.println(GREEN + "-------------------------------------" + RESET);
    }
}
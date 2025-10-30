package com.memberclub.ui;

import java.io.IOException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Helper class containing common UI utility methods.
 * Provides screen clearing, user prompts, and input handling.
 */
public class UIHelper {

    private Scanner scanner;
    private BufferedReader reader;

    // ANSI color codes
    public static final String GREEN = "\033[38;5;35m";
    public static final String RESET = "\033[0m";

    /**
     * Creates a new UI helper.
     * @param scanner the scanner to use for input
     */
    public UIHelper(Scanner scanner) {
        this.scanner = scanner;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
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
        flushAndWaitForEnter();
    }

    /**
     * Waits for user to press 'Enter' before quitting
     */
    public void pressEnterToQuit() {
        System.out.println();
        System.out.print("Tryck 'Enter' för att avsluta...");
        flushAndWaitForEnter();
    }

    /**
     *Waits for Enter while ignoring buffered scroll artifacts
     */
    private void flushAndWaitForEnter() {
        try {

            // Continue loop until valid Enter detected
            while (true) {

                // "Record" start time
                long startTime = System.currentTimeMillis();

                String line = reader.readLine();

                // Calculate time elapsed
                long duration = System.currentTimeMillis() - startTime;

                // Ignore non empty input
                if (!line.isEmpty()) {
                    continue;
                }

                // Ignore input that was too fast
                if (duration < 200) {
                    continue;
                }
                break;
            }
        } catch (IOException e) {
            scanner.nextLine();
        }
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
package com.memberclub.model;

/**
 * Represents a system user with authentication credentials.
 * Users can log in to the rental system using username and password.
 */
public class User {

    private final String username;
    private final String password;
    private final String fullName;

    /**
     * Creates a new user with the specified credentials.
     * @param username the unique username for login
     * @param password the password for authentication
     * @param fullName the user's full name for display purpose
     */
    public User(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    /**
     * Validates if the provided password matches this user's password.
     * @param inputPassword the password to validate
     * @return true if the password is a match
     */
    public boolean validatePassword(String inputPassword) {
        return inputPassword.equals(this.password);
    }
}

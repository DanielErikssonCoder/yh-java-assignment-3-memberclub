package com.memberclub.system;

import com.memberclub.model.*;
import com.memberclub.service.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Central system class that initializes and coordinates all services.
 * Acts as the main entry point for the console application.
 */
public class ClubSystem {

    private Inventory inventory;
    private MemberRegistry memberRegistry;
    private RentalService rentalService;
    private MembershipService membershipService;
    private ItemIdGenerator itemIdGenerator;
    private MemberIdGenerator memberIdGenerator;
    private HashMap<String, User> users;

    /**
     * Creates and initializes the complete club system.
     * Sets up all services and their dependencies.
     */
    public ClubSystem() {

        // Initialize ID generators first
        this.itemIdGenerator = new ItemIdGenerator();
        this.memberIdGenerator = new MemberIdGenerator();

        // Initialize core services
        this.inventory = new Inventory();
        this.memberRegistry = new MemberRegistry();
        this.rentalService = new RentalService(inventory, memberRegistry);
        this.membershipService = new MembershipService(memberRegistry, memberIdGenerator);

        this.users = new HashMap<>();

        loadUsers();

        // Load sample data using generators
        SampleDataLoader.loadSampleItems(inventory, itemIdGenerator);
        SampleDataLoader.loadSampleMembers(memberRegistry, memberIdGenerator);
    }

    // Getters
    public Inventory getInventory() {
        return inventory;
    }

    public MemberRegistry getMemberRegistry() {
        return memberRegistry;
    }

    public RentalService getRentalService() {
        return rentalService;
    }

    public MembershipService getMembershipService() {
        return membershipService;
    }

    public ItemIdGenerator getItemIdGenerator() {
        return itemIdGenerator;
    }

    public MemberIdGenerator getMemberIdGenerator() {
        return memberIdGenerator;
    }

    /**
     * Loads system users (Daniel Eriksson and Tomas Wigell).
     * Called during initialization of the system.
     * This is just a sample for demo purposes.
     */
    private void loadUsers() {

        // Add sample data for Daniel Eriksson
        User daniel = new User("danieleriksson", "0000", "Daniel Eriksson");
        addUser(daniel);

        // Add sample data for Tomas Wigell
        User tomas = new User("tomaswigell", "5555", "Tomas Wigell");
        addUser(tomas);
    }

    /**
     * Adds a user to the system.
     * @param user the user to add
     */
    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    /**
     * Finds a user by username.
     * @param username the username to search for
     * @return the user if found, null if not found
     */
    public User getUser(String username) {
        return users.get(username);
    }

    /**
     * Creates a new user in the system.
     * @param username the username
     * @param password the password
     * @param fullName the user's full name
     * @return true if user was created successfully, false if username already exists
     */
    public boolean createUser(String username, String password, String fullName) {

        // Check if username already exists
        if (getUser(username) != null) {
            return false;
        }

        // Create new user
        User newUser = new User(username, password, fullName);

        // Add to users map
        users.put(username, newUser);

        return true;
    }

    /**
     * Removes a user from the system.
     * @param username the username of the user to remove
     * @return true if removed, false if not found
     */
    public boolean removeUser(String username) {
        if (users.containsKey(username)) {
            users.remove(username);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Convert map.values() to an ArrayList and return.
     * @return list of all users
     */
    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>(users.values());
        return allUsers;
    }

    /**
     * Returns the number of users in the system.
     * @return user count
     */
    public int getUserCount() {
        return users.size();
    }

    /**
     * Authenticate user with username and password.
     * @param username the username to authenticate
     * @param password the password to validate
     * @return the authenticated User object, or null if the authentication fails
     */
    public User authenticateUser(String username, String password) {

        // Get user from HashMap by username
        User user = users.get(username);

        // If user doesn't exist, return null
        if (user == null) {
            return null;
        }

        // Validate password and return user if correct
        if (user.validatePassword(password)) {
            return user;
        }

        // Password was incorrect, return null
        return null;
    }
}
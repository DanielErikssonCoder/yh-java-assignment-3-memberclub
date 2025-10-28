package com.memberclub.service;

import com.memberclub.model.Item;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages all items available for rental.
 * Uses a Map for fast ID lookup.
 */
public class Inventory {

    // Map for ID-based lookup: itemId -> Item
    private Map<String, Item> items;

    /**
     * Creates an empty inventory.
     */
    public Inventory() {
        this.items = new HashMap<>();
    }

    /**
     * Adds an item to the inventory.
     * @param item the item to add
     */
    public void addItem(Item item) {
        items.put(item.getId(), item);
    }

    /**
     * Finds an item by its ID.
     * @param itemId the ID to search for
     * @return the item if found, null if not found
     */
    public Item getItem(String itemId) {
        return items.get(itemId);
    }

    /**
     * Removes an item from inventory.
     * @param itemId the ID of the item to remove
     * @return true if removed, false if not found
     */
    public boolean removeItem(String itemId) {
        if (items.containsKey(itemId)) {
            items.remove(itemId);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Convert map.values() to an ArrayList and return.
     * @return list of all items
     */
    public List<Item> getAllItems() {
        List<Item> allItems = new ArrayList<>(items.values());
        return allItems;
    }

    /**
     * Returns the number of items in inventory.
     * @return item count
     */
    public int getItemCount() {
        return items.size();
    }
}

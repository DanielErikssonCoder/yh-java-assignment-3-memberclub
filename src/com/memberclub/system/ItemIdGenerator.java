package com.memberclub.system;

/**
 * Generates unique IDs for different item types.
 * Maintains separate counters for each category.
 */
public class ItemIdGenerator {

    // Camping equipment counters
    private int backpackCounter = 1;
    private int lanternCounter = 1;
    private int sleepingBagCounter = 1;
    private int tentCounter = 1;
    private int trangiaCounter = 1;

    // Fishing equipment counters
    private int baitCounter = 1;
    private int netCounter = 1;
    private int rodCounter = 1;

    // Water vehicles counters
    private int kayakCounter = 1;
    private int electricBoatCounter = 1;
    private int motorBoatCounter = 1;
    private int rowBoatCounter = 1;

    // Camping
    public String generateBackpackId() {
        return "BACK-" + String.format("%03d", backpackCounter++);
    }

    public String generateLanternId() {
        return "LANT-" + String.format("%03d", lanternCounter++);
    }

    public String generateSleepingBagId() {
        return "SLEEP-" + String.format("%03d", sleepingBagCounter++);
    }

    public String generateTentId() {
        return "TENT-" + String.format("%03d", tentCounter++);
    }

    public String generateTrangiaId() {
        return "TRANG-" + String.format("%03d", trangiaCounter++);
    }

    // Fishing
    public String generateBaitId() {
        return "BAIT-" + String.format("%03d", baitCounter++);
    }

    public String generateNetId() {
        return "NET-" + String.format("%03d", netCounter++);
    }

    public String generateRodId() {
        return "ROD-" + String.format("%03d", rodCounter++);
    }

    // Water vehicles
    public String generateKayakId() {
        return "KAY-" + String.format("%03d", kayakCounter++);
    }

    public String generateElectricBoatId() {
        return "EBOAT-" + String.format("%03d", electricBoatCounter++);
    }

    public String generateMotorBoatId() {
        return "MBOAT-" + String.format("%03d", motorBoatCounter++);
    }

    public String generateRowBoatId() {
        return "RBOAT-" + String.format("%03d", rowBoatCounter++);
    }
}
package com.example.instalearnenglish.feature.station23.model;

import com.example.instalearnenglish.feature.station23.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScannerItemProvider {

    private static final List<ScannerItem> SAFE_ITEMS = new ArrayList<>();
    private static final List<ScannerItem> FORBIDDEN_ITEMS = new ArrayList<>();

    static {
        // Safe Items
        SAFE_ITEMS.add(new ScannerItem(R.drawable.ic_laptop, "Laptop", false));
        SAFE_ITEMS.add(new ScannerItem(R.drawable.ic_passport, "Passport", false));
        SAFE_ITEMS.add(new ScannerItem(R.drawable.ic_phone, "Phone", false));
        SAFE_ITEMS.add(new ScannerItem(R.drawable.ic_book, "Book", false));
        SAFE_ITEMS.add(new ScannerItem(R.drawable.ic_headphones, "Headphones", false));

        // Forbidden Items
        FORBIDDEN_ITEMS.add(new ScannerItem(R.drawable.ic_knife, "Knife", true));
        FORBIDDEN_ITEMS.add(new ScannerItem(R.drawable.ic_gun, "Gun", true));
        FORBIDDEN_ITEMS.add(new ScannerItem(R.drawable.ic_scissors, "Scissors", true));
        FORBIDDEN_ITEMS.add(new ScannerItem(R.drawable.ic_bottle_water, "Water Bottle", true));
        FORBIDDEN_ITEMS.add(new ScannerItem(R.drawable.ic_lighter, "Lighter", true));
    }

    /**
     * Generates a list of items for a single round, ensuring exactly one forbidden item.
     * @param totalItems The total number of items to display in the suitcase (e.g., 6).
     * @return A shuffled list of items for the round.
     */
    public static List<ScannerItem> generateRoundItems(int totalItems) {
        if (totalItems < 2) {
            throw new IllegalArgumentException("Total items must be at least 2.");
        }

        List<ScannerItem> roundItems = new ArrayList<>();

        // 1. Shuffle both lists to ensure randomness
        Collections.shuffle(SAFE_ITEMS);
        Collections.shuffle(FORBIDDEN_ITEMS);

        // 2. Add one forbidden item
        roundItems.add(FORBIDDEN_ITEMS.get(0));

        // 3. Add the rest as safe items
        int safeItemsToAdd = totalItems - 1;
        for (int i = 0; i < safeItemsToAdd; i++) {
            roundItems.add(SAFE_ITEMS.get(i % SAFE_ITEMS.size()));
        }

        // 4. Shuffle the final list so the forbidden item isn't always in the same place
        Collections.shuffle(roundItems);

        return roundItems;
    }
}

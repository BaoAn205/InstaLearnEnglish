package com.example.instalearnenglish.feature.station23.model;

public class ScannerItem {
    private final int drawableResId;
    private final String itemName;
    private final boolean isForbidden;

    public ScannerItem(int drawableResId, String itemName, boolean isForbidden) {
        this.drawableResId = drawableResId;
        this.itemName = itemName;
        this.isForbidden = isForbidden;
    }

    public int getDrawableResId() {
        return drawableResId;
    }

    public String getItemName() {
        return itemName;
    }

    public boolean isForbidden() {
        return isForbidden;
    }
}

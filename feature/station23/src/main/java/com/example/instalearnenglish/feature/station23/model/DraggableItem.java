package com.example.instalearnenglish.feature.station23.model;

import androidx.annotation.DrawableRes;

public class DraggableItem {
    private final String name;
    @DrawableRes
    private final int imageResId;
    private final boolean isAllowed;
    private final String reason;

    public DraggableItem(String name, @DrawableRes int imageResId, boolean isAllowed, String reason) {
        this.name = name;
        this.imageResId = imageResId;
        this.isAllowed = isAllowed;
        this.reason = reason;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public boolean isAllowed() {
        return isAllowed;
    }

    public String getReason() {
        return reason;
    }
}

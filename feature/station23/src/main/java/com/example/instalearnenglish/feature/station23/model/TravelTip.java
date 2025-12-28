package com.example.instalearnenglish.feature.station23.model;

public class TravelTip {
    private final int iconResId;
    private final String title;
    private final String content;

    public TravelTip(int iconResId, String title, String content) {
        this.iconResId = iconResId;
        this.title = title;
        this.content = content;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}

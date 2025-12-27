package com.example.instalearnenglish.feature.station4.model;

public class Tip {
    private String title;
    private String content;
    private int imageResId; // Will be set locally

    // Required empty constructor for Firestore
    public Tip() {}

    public Tip(String title, String content, int imageResId) {
        this.title = title;
        this.content = content;
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getImageResId() {
        return imageResId;
    }

    // Setter for local image assignment
    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}

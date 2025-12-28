package com.example.instalearnenglish.feature.station1.model;

public class Tip {
    private String title;
    private String content;
    private String vietnameseMeaning;
    private int imageResId; // Will be set locally

    // Required empty constructor for Firestore
    public Tip() {}

    public Tip(String title, String content, String vietnameseMeaning, int imageResId) {
        this.title = title;
        this.content = content;
        this.vietnameseMeaning = vietnameseMeaning;
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getVietnameseMeaning() {
        return vietnameseMeaning;
    }

    public int getImageResId() {
        return imageResId;
    }

    // Setter for local image assignment
    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}

package com.example.instalearnenglish;

public class ST1_Game {
    private String title;
    private String type;
    private Long level;

    // Required for Firestore
    public ST1_Game() {}

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public Long getLevel() {
        return level;
    }

    // Setters to allow manual mapping
    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLevel(Long level) {
        this.level = level;
    }
}

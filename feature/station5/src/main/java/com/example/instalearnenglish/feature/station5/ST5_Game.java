package com.example.instalearnenglish.feature.station1;

public class ST5_Game {
    private String title;
    private String type;
    private Long level;

    // Required for Firestore
    public ST5_Game() {}

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

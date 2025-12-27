package com.example.instalearnenglish.feature.home.model;

public class Tip {
    private String title;
    private String content;

    // No-argument constructor required for Firestore
    public Tip() {}

    public Tip(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}

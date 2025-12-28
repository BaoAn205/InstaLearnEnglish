package com.example.instalearnenglish.feature.station1.model;

public class Tip {
    private String title;
    private String content;
    private String vietnameseContent;
    private int imageResId;

    public Tip(String title, String content, String vietnameseContent, int imageResId) {
        this.title = title;
        this.content = content;
        this.vietnameseContent = vietnameseContent;
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getVietnameseContent() {
        return vietnameseContent;
    }

    public int getImageResId() {
        return imageResId;
    }
}

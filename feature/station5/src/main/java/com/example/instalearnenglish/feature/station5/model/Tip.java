package com.example.instalearnenglish.feature.station5.model;

public class Tip {
    private String title;
    private String content;
    private int imageResId;

    public Tip() {}

    public Tip(String title, String content, int imageResId) {
        this.title = title;
        this.content = content;
        this.imageResId = imageResId;
    }

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public int getImageResId() { return imageResId; }
}

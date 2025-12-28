package com.example.instalearnenglish.feature.station23.model;

public class St23VocabItem {
    private final String name;
    private final String phonetic;
    private final int imageResId;
    private final String vietnameseMeaning;
    private final String exampleSentence;

    public St23VocabItem(String name, String phonetic, int imageResId, String vietnameseMeaning, String exampleSentence) {
        this.name = name;
        this.phonetic = phonetic;
        this.imageResId = imageResId;
        this.vietnameseMeaning = vietnameseMeaning;
        this.exampleSentence = exampleSentence;
    }

    public String getName() {
        return name;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getVietnameseMeaning() {
        return vietnameseMeaning;
    }

    public String getExampleSentence() {
        return exampleSentence;
    }
}

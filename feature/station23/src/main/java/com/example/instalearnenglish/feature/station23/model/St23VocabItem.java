package com.example.instalearnenglish.feature.station23.model;

public class St23VocabItem {
    private String englishWord;
    private String vietnameseMeaning;

    public St23VocabItem(String englishWord, String vietnameseMeaning) {
        this.englishWord = englishWord;
        this.vietnameseMeaning = vietnameseMeaning;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public String getVietnameseMeaning() {
        return vietnameseMeaning;
    }
}

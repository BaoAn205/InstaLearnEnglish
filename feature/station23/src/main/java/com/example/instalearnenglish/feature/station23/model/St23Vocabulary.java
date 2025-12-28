package com.example.instalearnenglish.feature.station23.model;

public class St23Vocabulary {
    private final String word;
    private final String pronunciation;
    private final String meaning;
    private final String imageUrl;

    public St23Vocabulary(String word, String pronunciation, String meaning, String imageUrl) {
        this.word = word;
        this.pronunciation = pronunciation;
        this.meaning = meaning;
        this.imageUrl = imageUrl;
    }

    public String getWord() {
        return word;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

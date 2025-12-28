package com.example.instalearnenglish.feature.station23.model;

public class ScrambleQuestion {
    private final String correctSentence;
    private final String vietnameseHint;

    public ScrambleQuestion(String correctSentence, String vietnameseHint) {
        this.correctSentence = correctSentence;
        this.vietnameseHint = vietnameseHint;
    }

    public String getCorrectSentence() {
        return correctSentence;
    }

    public String getVietnameseHint() {
        return vietnameseHint;
    }
}

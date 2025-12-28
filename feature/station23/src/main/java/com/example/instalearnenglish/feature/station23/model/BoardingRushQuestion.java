package com.example.instalearnenglish.feature.station23.model;

import java.util.List;

public class BoardingRushQuestion {
    private final String question;
    private final List<String> answers;
    private final int correctAnswerIndex;

    public BoardingRushQuestion(String question, List<String> answers, int correctAnswerIndex) {
        this.question = question;
        this.answers = answers;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
}

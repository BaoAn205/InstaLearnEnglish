package com.example.instalearnenglish.feature.station23.model;

public class QuizQuestion {
    private String questionText;
    private String optionA;
    private String optionB;
    private boolean isOptionBCorrect;

    public QuizQuestion(String questionText, String optionA, String optionB, boolean isOptionBCorrect) {
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.isOptionBCorrect = isOptionBCorrect;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public boolean isOptionBCorrect() {
        return isOptionBCorrect;
    }
}

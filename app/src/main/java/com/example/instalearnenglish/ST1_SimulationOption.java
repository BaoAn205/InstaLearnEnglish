package com.example.instalearnenglish;

public class ST1_SimulationOption {
    private String text;
    private boolean isCorrect;
    private String response;
    private String nextStep;

    // Required for Firestore
    public ST1_SimulationOption() {}

    public String getText() {
        return text;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public String getResponse() {
        return response;
    }

    public String getNextStep() {
        return nextStep;
    }
}

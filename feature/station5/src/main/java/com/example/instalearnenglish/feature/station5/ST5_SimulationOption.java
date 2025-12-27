package com.example.instalearnenglish.feature.station5;

public class ST5_SimulationOption {
    private String text;
    private boolean isCorrect;
    private String response;
    private String nextStep;

    // Required for Firestore
    public ST5_SimulationOption() {}

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

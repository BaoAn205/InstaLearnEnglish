package com.example.instalearnenglish.feature.station4;

public class ST4_SimulationOption {
    private String text;
    private boolean isCorrect;
    private String response;
    private String nextStep;

    // Required for Firestore
    public ST4_SimulationOption() {}

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

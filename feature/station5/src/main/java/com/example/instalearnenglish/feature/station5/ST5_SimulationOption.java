package com.example.instalearnenglish.feature.station5;

public class ST5_SimulationOption {
    private String text;
    private boolean isCorrect;
    private String response;
    private String nextStep;

    public ST5_SimulationOption() {}

    public ST5_SimulationOption(String text, boolean isCorrect, String response, String nextStep) {
        this.text = text;
        this.isCorrect = isCorrect;
        this.response = response;
        this.nextStep = nextStep;
    }

    public String getText() { return text; }
    public boolean isCorrect() { return isCorrect; }
    public String getResponse() { return response; }
    public String getNextStep() { return nextStep; }
}

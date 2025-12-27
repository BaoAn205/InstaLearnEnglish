package com.example.instalearnenglish.feature.station5;

import java.util.List;

public class ST5_SimulationStep {
    private String dialogue;
    private List<ST5_SimulationOption> options;

    // Required for Firestore
    public ST5_SimulationStep() {}

    public String getDialogue() {
        return dialogue;
    }

    public List<ST5_SimulationOption> getOptions() {
        return options;
    }
}

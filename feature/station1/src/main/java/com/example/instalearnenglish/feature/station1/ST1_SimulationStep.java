package com.example.instalearnenglish.feature.station1;

import java.util.List;

public class ST1_SimulationStep {
    private String dialogue;
    private List<ST1_SimulationOption> options;

    // Required for Firestore
    public ST1_SimulationStep() {}

    public String getDialogue() {
        return dialogue;
    }

    public List<ST1_SimulationOption> getOptions() {
        return options;
    }
}

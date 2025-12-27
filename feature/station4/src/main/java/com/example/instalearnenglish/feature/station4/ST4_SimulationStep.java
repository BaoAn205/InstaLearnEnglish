package com.example.instalearnenglish.feature.station4;

import java.util.List;

public class ST4_SimulationStep {
    private String dialogue;
    private List<ST4_SimulationOption> options;

    // Required for Firestore
    public ST4_SimulationStep() {}

    public String getDialogue() {
        return dialogue;
    }

    public List<ST4_SimulationOption> getOptions() {
        return options;
    }
}

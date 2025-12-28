package com.example.instalearnenglish.feature.station5;

import java.util.List;

public class ST5_SimulationStep {
    private String dialogue;
    private List<ST5_SimulationOption> options;

    public ST5_SimulationStep() {}

    public ST5_SimulationStep(String dialogue, List<ST5_SimulationOption> options) {
        this.dialogue = dialogue;
        this.options = options;
    }

    public String getDialogue() { return dialogue; }
    public List<ST5_SimulationOption> getOptions() { return options; }
}

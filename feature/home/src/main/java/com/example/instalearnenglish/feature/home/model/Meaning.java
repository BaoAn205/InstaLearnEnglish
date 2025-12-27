package com.example.instalearnenglish.feature.home.model;

import java.util.List;

public class Meaning {
    private String partOfSpeech;
    private List<Definition> definitions;

    // Getters
    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public List<Definition> getDefinitions() {
        return definitions;
    }
}

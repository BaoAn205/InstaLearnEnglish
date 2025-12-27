package com.example.instalearnenglish.feature.home.model;

import java.util.List;

public class Definition {
    private String definition;
    private String example;
    private List<String> synonyms;
    private List<String> antonyms;

    // Getters
    public String getDefinition() {
        return definition;
    }

    public String getExample() {
        return example;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public List<String> getAntonyms() {
        return antonyms;
    }
}

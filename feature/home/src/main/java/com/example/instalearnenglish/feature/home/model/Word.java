package com.example.instalearnenglish.feature.home.model;

import java.util.List;

public class Word {
    private String word;
    private String origin;
    private List<Phonetic> phonetics;
    private List<Meaning> meanings;

    // Getters
    public String getWord() {
        return word;
    }

    public String getOrigin() {
        return origin;
    }

    public List<Phonetic> getPhonetics() {
        return phonetics;
    }

    public List<Meaning> getMeanings() {
        return meanings;
    }
}

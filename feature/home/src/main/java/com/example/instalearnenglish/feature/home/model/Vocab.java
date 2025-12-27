package com.example.instalearnenglish.feature.home.model;

public class Vocab {
    private String name;
    private String phonetic;
    private String vietnamese;

    // No-argument constructor required for Firestore
    public Vocab() {}

    public Vocab(String name, String phonetic, String vietnamese) {
        this.name = name;
        this.phonetic = phonetic;
        this.vietnamese = vietnamese;
    }

    public String getName() {
        return name;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public String getVietnamese() {
        return vietnamese;
    }
}

package com.example.instalearnenglish.model;

import java.io.Serializable;
import java.util.List;

public class Word implements Serializable {
    private String word;
    private List<Phonetic> phonetics;
    private List<Meaning> meanings;

    // Getters and Setters
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<Phonetic> getPhonetics() {
        return phonetics;
    }

    public void setPhonetics(List<Phonetic> phonetics) {
        this.phonetics = phonetics;
    }

    public List<Meaning> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<Meaning> meanings) {
        this.meanings = meanings;
    }
}

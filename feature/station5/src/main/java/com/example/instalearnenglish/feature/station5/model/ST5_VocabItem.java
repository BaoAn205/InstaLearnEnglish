package com.example.instalearnenglish.feature.station5.model;

public class ST5_VocabItem {
    private String name;
    private String phonetic;
    private int imageResId;
    private String vietnameseMeaning;
    private String exampleSentence;

    public ST5_VocabItem(String name, String phonetic, int imageResId, String vietnameseMeaning, String exampleSentence) {
        this.name = name;
        this.phonetic = phonetic;
        this.imageResId = imageResId;
        this.vietnameseMeaning = vietnameseMeaning;
        this.exampleSentence = exampleSentence;
    }

    public String getName() { return name; }
    public String getPhonetic() { return phonetic; }
    public int getImageResId() { return imageResId; }
    public String getVietnameseMeaning() { return vietnameseMeaning; }
    public String getExampleSentence() { return exampleSentence; }
}

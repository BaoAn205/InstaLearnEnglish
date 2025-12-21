package com.example.instalearnenglish;

public class ST1_Vocabulary {
    private String word;
    private String pronunciation;
    private String meaning;
    private String imageUrl; // Optional

    // Required empty constructor for Firestore
    public ST1_Vocabulary() {}

    public ST1_Vocabulary(String word, String pronunciation, String meaning, String imageUrl) {
        this.word = word;
        this.pronunciation = pronunciation;
        this.meaning = meaning;
        this.imageUrl = imageUrl;
    }

    public String getWord() {
        return word;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

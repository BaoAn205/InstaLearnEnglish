package com.example.instalearnenglish.feature.station1;

public class ST1_Tip {
    private String title;
    private String content;

    // Required empty constructor for Firestore
    public ST1_Tip() {}

    public ST1_Tip(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}

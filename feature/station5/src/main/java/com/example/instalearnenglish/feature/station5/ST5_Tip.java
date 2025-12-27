package com.example.instalearnenglish.feature.station1;

public class ST5_Tip {
    private String title;
    private String content;

    // Required empty constructor for Firestore
    public ST5_Tip() {}

    public ST5_Tip(String title, String content) {
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

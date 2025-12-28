package com.example.instalearnenglish.feature.station5.model;

import java.util.List;

public class ST5_VocabCategory {
    private String title;
    private List<ST5_VocabItem> items;

    public ST5_VocabCategory(String title, List<ST5_VocabItem> items) {
        this.title = title;
        this.items = items;
    }

    public String getTitle() { return title; }
    public List<ST5_VocabItem> getItems() { return items; }
}
